import React from 'react';
import GraphTitle from "../GraphTitle";
import {Doughnut} from "react-chartjs";

const
    GenderWise = ({title = 'Gender Wise', data = [{value: 0, color: "#6ccac9"}, {value: 0, color: "#ff6c60"}]}) => (
    <div>
        <div><GraphTitle title={title}/></div>
        <Doughnut data={data} width="275" height="180" style={{float: "left"}}/>
        <div>
            <label  style={{color: "#ff6c60", top: "10px", marginLeft: "24%"}}>Female </label>
            <label  style={{color: "#6ccac9", top: "10px", marginLeft: "15%"}}>Male </label>
        </div>
    </div>);


export default GenderWise;
