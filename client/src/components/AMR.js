import React, {Component} from "react";
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import axios from 'axios';
import {css, StyleSheet} from "aphrodite";
import ReactTable from 'react-table'
import "react-table/react-table.css";
import {Option} from "../utils/Option";
import Loader from "react-loader";
import 'react-datepicker/dist/react-datepicker.css';
import {advaya_attendance, fetchAanganwadis, fetchAMRLog, fetchBeneficiaryImage} from "../utils/WebApi";
import {reportTableColumns} from "./schema/reportTableColumns";
import DatePicker from "react-datepicker";
import moment from "moment";

class AMR extends Component {

    constructor() {
        super();
        this.state = {
            selectedDate: moment(),
            selectedOption: '',
            options: ['27511010507','27511010508'],
            reportData: [],
            loaded: false
        }
    }

    handleChange = date => {

        this.setState({
            selectedDate: date
        });
    };


    addImageLink = record => {
        return Object.assign({}, record, {
            image: <a style={{cursor: "pointer"}} onClick={_ => this.addImage(record)}>View Image</a>
        })
    };

    isSameCode = (r, record) => r.studentcode === record.studentcode

    addImage = record => {
        this.setState({loaded: false});
        const MIME = "data:image/jpeg;base64,";
        fetchBeneficiaryImage(`${advaya_attendance}/${record.imageuri}`).then(res => {
            const image = <img src={MIME + res.data} style={{"height": "40px", "width": "40px"}}/>;
            const newRecord = Object.assign({}, record, {"image": image})

            this.setState(prevState => ({
                reportData: prevState.reportData.map(r => this.isSameCode(r, record) ? newRecord : r),
                loaded: true
            }))
        }).catch(err => {
            this.setState({loaded: true})
        })
    }


    onHandleChange = selectedOption => {
        this.setState({loaded: false});
        Option(selectedOption).fold(
            _ => this.setState({selectedOption: '', reportData: [], loaded: true}),
            _ => {
                fetchAMRLog(selectedOption.value).then(res => {
                    this.setState({
                        selectedOption,
                        reportData: res.data.map(this.addImageLink),
                        loaded: true
                    })
                }).catch(err => {
                    this.setState({loaded: true})
                })

            })

    }

    render() {
        const {options} = this.state;
        let selectedOption = this.state.selectedOption;
        const value = selectedOption && selectedOption.value;
        return (
            <section className="wrapper state-overview">
                <Loader loaded={this.state.loaded} top="50%" left="55%">
                    <div>
                        <DatePicker
                            selected={this.state.selectedDate}
                            onChange={this.handleChange}
                            dateFormat="DD-MM-YYYY"
                            name = 'Select Date'
                        />
                    </div>
                    <Select
                        style={{width: "95%"}}
                        value={value}
                        onChange={this.onHandleChange}
                        options={options}
                    />
                    <ReactTable
                        style={{width: "95%", marginTop: "2%"}}
                        data={this.state.reportData}
                        columns={reportTableColumns}
                        filterable
                        defaultPageSize={5}
                        className="-striped -highlight"
                    />
                </Loader>
            </section>
        )
    }

    componentDidMount = () => {
        axios.get(`/pcms/v1/attendance/report/27511010507/${this.state.selectedDate.format("DD-MM-YYYY")}`)
            .then(({data}) => {
                this.setState({
                    loaded: true,
                    reportData : data
                })
            })
            .catch(err => {
            })
    }

}

export default AMR;
