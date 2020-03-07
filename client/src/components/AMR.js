import React, {Component} from "react";
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import axios from 'axios';
import ReactTable from 'react-table'
import "react-table/react-table.css";
import {Option} from "../utils/Option";
import Loader from "react-loader";
import 'react-datepicker/dist/react-datepicker.css';
import {advaya_attendance, fetchAanganwadis, fetchAMRLog, fetchBeneficiaryImage} from "../utils/WebApi";
import {reportTableColumns} from "./schema/reportTableColumns";
import DatePicker from "react-datepicker";
import moment from "moment";
import {imageGenartion} from "../utils/ImageUtil"
class AMR extends Component {

    constructor() {
        super();
        this.state = {
            selectedDate: moment(),
            selectedOption: '',
            options: [
              {label: "VISHNUPURI 6", code: "27511150606"},
            ],
            reportData: [],
            loaded: false
        }
    }


    fetchData = (selectedOption,date) => {
      if(selectedOption !== null && selectedOption !== '') {
        axios.get(`/pcms/v1/attendance/report/${selectedOption.code}/${date.format("DD-MM-YYYY")}`)
            .then(({data}) => {
                this.setState({
                    loaded: true,
                    reportData : data.map(a => imageGenartion(a))
                  })
                })
                .catch(err => {
                  console.log("something went wrong!!!");
                })
      } else {
        this.setState({selectedOption: '', reportData: [], loaded: true});
      }
    }

    componentDidUpdate(prevProps,prevState) {
      if (this.state.selectedDate !== prevState.selectedDate ||  this.state.selectedOption !== prevState.selectedOption ) {
        const {selectedDate,selectedOption} = this.state
          this.fetchData(selectedOption,selectedDate )
      }
    }

    componentDidMount = () => {
      const {selectedDate,selectedOption} = this.state
        this.fetchData(selectedOption,selectedDate )
    }

    // addImageLink = record => {
    //     return Object.assign({}, record, {
    //         image: <a style={{cursor: "pointer"}} onClick={_ => this.addImage(record)}>View Image</a>
    //     })
    // };

    // isSameCode = (r, record) => r.studentcode === record.studentcode

    // addImage = record => {
    //     this.setState({loaded: false});
    //     const MIME = "data:image/jpeg;base64,";
    //     fetchBeneficiaryImage(`${advaya_attendance}/${record.imageuri}`).then(res => {
    //         const image = <img src={MIME + res.data} alt="test" style={{"height": "40px", "width": "40px"}}/>;
    //         const newRecord = Object.assign({}, record, {"image": image})
    //
    //         this.setState(prevState => ({
    //             reportData: prevState.reportData.map(r => this.isSameCode(r, record) ? newRecord : r),
    //             loaded: true
    //         }))
    //     }).catch(err => {
    //         this.setState({loaded: true})
    //     })
    // }

    handleAnganwadiCodeChange = selectedOption => {
          Option(selectedOption).fold(
              _ => this.setState({selectedOption: '', reportData: [], loaded: true}),
              _ => {
                      this.setState({
                          selectedOption,
                          loaded: true
                      })
                  })
    };
    handleDateChange = date => {
      this.setState({
        selectedDate: date
      });
    };

    render() {

        const { options } = this.state;

        let selectedOption = this.state.selectedOption;

        // const value = selectedOption && selectedOption.value;
        return (
            <section className="wrapper state-overview report">
              <Loader loaded={this.state.loaded} top="50%" left="55%">
                <div className="dashbord-top-panel">
                  <div className="heading" >
                    <label> Anganwadi </label>
                    <div className="anganwadi-drop-down">
                      <Select
                        value={selectedOption}
                        onChange={this.handleAnganwadiCodeChange}
                        options={options}
                      />
                    </div>
                  </div>

                  <div className="dashbord-calender">
                    <span className="date-title"> Date:</span>
                    <DatePicker
                      selected={this.state.selectedDate}
                      onChange={this.handleDateChange}
                      dateFormat="DD-MM-YYYY"
                    />
                  </div>
                </div>

                <ReactTable

                  data={this.state.reportData}
                  columns={reportTableColumns}
                  filterable
                  defaultPageSize={5}
                  className="-striped -highlight data-table"
                />
              </Loader>
            </section>
        )
    }

}

export default AMR;
