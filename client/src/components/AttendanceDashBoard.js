import React, {Component} from "react";
import GenderWise from "./graphs/GenderWise";
import MonthWise from "./graphs/MonthWise";
import AgeWise from "./graphs/AgeWise";
import MetricsDashboard from './MetricsDashboard';
import DatePicker from "react-datepicker";
import 'react-select/dist/react-select.css';
import Loader from "react-loader";
import axios from 'axios';
import moment from "moment";
import {Router} from "react-router";

class AttendanceDashboard extends Component {
    constructor() {
        super();
        this.state = {
            data: {},
            selectedDate: moment(),
            loaded: true
        };
    }

    handleChange = date => {
        console.log("handle change called, ${date}", date)
        this.setState({
            selectedDate: date
        });
    };

    componentDidMount() {
        axios.get(`/pcms/v1/attendance/dashboard/27/${this.state.selectedDate.format("DD-MM-YYYY")}`)
            .then(({data}) => {
                this.setState({
                    loaded: true,
                    data
                })
            })
            .catch(err => {

            })
    }


    render() {
        const {
            percentage, presentcount, totalcount, malecount, femalecount,
            zerotoonecount, onetotwocount, twotothreecount, threetofourcount, fourtofivecount, fivetosixcount,
            januarycount, februarycount, marchcount, aprilcount, maycount, junecount, julycount, augustcount,
            septembercount, octobercount, novembercount, decembercount, currentdate
        } = this.state.data;

        const metrics_data = {
            percentage: percentage,
            presentcount: presentcount,
            totalcount: totalcount
        };
        const gender_data = [
            {value: malecount, color: "#6ccac9"},
            {value: femalecount, color: "#ff6c60"}
        ];
        const age_data = [
            {name: '0m-1Y', value: Number(zerotoonecount)},
            {name: '1-2Y', value: Number(onetotwocount)},
            {name: '2-3Y', value: Number(twotothreecount)},
            {name: '3-4Y', value: Number(threetofourcount)},
            {name: '4-5Y', value: Number(fourtofivecount)},
            {name: '5-6Y', value: Number(fivetosixcount)}
        ];
        const month_data = [
            {name: 'Jan', value: januarycount},
            {name: 'Feb', value: februarycount},
            {name: 'Mar', value: marchcount},
            {name: 'Apr', value: aprilcount},
            {name: 'May', value: maycount},
            {name: 'Jun', value: junecount},
            {name: 'Jul', value: julycount},
            {name: 'Aug', value: augustcount},
            {name: 'Sep', value: septembercount},
            {name: 'Oct', value: octobercount},
            {name: 'Nov', value: novembercount},
            {name: 'Dec', value: decembercount}];

        return (
            <section className="wrapper">
                <Loader loaded={this.state.loaded} top="50%" left="55%">
                    <div>
                        <DatePicker
                            selected={this.state.selectedDate}
                            onChange={this.handleChange}
                            dateFormat="DD-MM-YYYY"
                        />
                    </div>
                    <div style={{height: '20px', fontSize: '20px', paddingBottom: '30px'}}>
                        <label> Attendance Dashboard</label>
                    </div>
                    <div>
                        <MetricsDashboard data={metrics_data}/>
                        <MonthWise title='Month wise' data={month_data}/>
                        <GenderWise title='Gender Wise' data={gender_data}/>
                        <AgeWise title="Age wise" data={age_data}/>
                    </div>
                </Loader>
            </section>

        )
    }
}


export default AttendanceDashboard;