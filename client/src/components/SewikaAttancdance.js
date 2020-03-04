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
import {Link} from "react-router-dom";

class SewikaAttendance extends Component {
    constructor() {
        super();
        this.state = {
            data: {},
            selectedDate: moment(),
            loaded: true
        };
    }

    handleChange = date => {
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
            septembercount, octobercount, novembercount, decembercount
        } = this.state.data;

         const metrics_data = [
            {label:"Total Sewikas", totalcount: totalcount},
            {label:"Total Packets Distributed", presentcount: presentcount},
            {label:"Percentage of Sewikas Present", percentage: percentage},
         ];
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
                <div className="dashbord-top-panel">
                  <div className="heading" >
                    <label> Sewika Attendace </label>
                  </div>
                  <div className="detail-report-link">
                    <Link to={"/samr"}>
                      <span>View Detail Report</span>
                    </Link>
                  </div>
                  <div className="dashbord-calender">
                    <span className="date-title"> Date: </span>
                    <DatePicker
                      selected={this.state.selectedDate}
                      onChange={this.handleChange}
                      dateFormat="DD-MM-YYYY"
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="col-lg-8 col-md-12 col-sm-12" >
                    <div className="bar-chart-wrapper">
                      <MetricsDashboard data={metrics_data}/>
                      <MonthWise title='Month Wise Attendance Status' data={month_data}/>
                    </div>
                  </div>
                  <div className="col-lg-4 col-md-6 col-sm-12" >
                    <div className="graph-chart-wrapper">
                      <GenderWise title='Gender Wise Attendance Status' data={gender_data}/>
                    </div>
                  </div>
                  <div className="col-lg-4 col-md-6 col-sm-12" >
                    <div className="graph-chart-wrapper">
                      <AgeWise title="Age Wise Attendance Status" data={age_data}/>
                    </div>
                  </div>
                </div>
              </Loader>
            </section>

        )
    }
}

export default SewikaAttendance;
