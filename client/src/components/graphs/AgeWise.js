import React from 'react';
import GraphTitle from "../GraphTitle";
import {Cell, Legend, Pie, PieChart} from 'recharts';


//const COLORS = ['#63A69F', '#F2D45D', '#F2836B', '#A79FF2'];
//const COLORS = ['#488F31', '#8AAA47', '#C6C666', '#FFE18D', '#F7AE65', '#EA7852'];
//const COLORS = ['#F6BE74','#DE425B', '#B5D38E',   '#DAE59C', '#6BAD7A','#EB8155'];
//const COLORS = ['#9C27B0','#2196F3', '#009688',   '#CDDC39', '#FF9800','#9E9E9E'];
//const COLORS = ['#EF5350','#7E5762', '#29B6F6',   '#66BB6A', '#FEFE58','#FF7043'];
//const COLORS = ['#D4E157','#42A5F5', '#BDBDBD',   '#AB47BC', '#FFA726','#26A69A'];

//const COLORS = ['#AB47BC','#42A5F5', '#26A69A',   '#D4E157', '#FFA726','#BDBDBD'];
const COLORS = ['#59ace2','#A9D86E', '#8175c7',   '#FCB322', '#FF6C60','#a1a1a1'];
//const COLORS = ['#9c9c9c','#29b392','#61a642','#1caadc','#cab03f','#d1595a'];


const RADIAN = Math.PI / 180;

export const renderCustomizedLabel = ({cx, cy, midAngle, innerRadius, outerRadius, percent, index}) => {

    const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
        <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
          {`${(percent * 100).toFixed(0)}%`}
        </text>
    );
};


const AgeWise = ({
                     title = "Age wise",
                     data = [
                         {name: '0m-1Y', value: 0},
                         {name: '1-2Y', value: 0},
                         {name: '2-3Y', value: 0},
                         {name: '3-4Y', value: 0},
                         {name: '4-5Y', value: 0},
                         {name: '5-6Y', value: 0}
                     ]
                 }) => (
    <div>
      <GraphTitle title={title}/>
      <PieChart width={300} height={240}>
        <Legend dataKey={"name"}/>
        <Pie
          data={data}
          cx={130}
          cy={95}
          labelLine={false}
          label={renderCustomizedLabel}
          outerRadius={100}
        fill="#8884d8">
          {
            data.map((entry, index) => <Cell fill={COLORS[index % COLORS.length]} key={index}/>)
          }
        </Pie>
      </PieChart>
    </div>);

export default AgeWise;
