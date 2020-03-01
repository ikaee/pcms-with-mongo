import React from 'react';


const MetricsDashboard = (
  statedata = [
  {label:"Total ", totalcount: 0},
  {label:"Present", presentcount: 0},
  {label:"Percentage ", percentage: 0}]) =>{

     return (
    <div>
      <div className="state-overview metrics-state-overview-blocks">
        <div className="state-overview-block" >
          <section className="panel">
            <div className="symbol terques">
              <div className="value">
                <h1 className="count">
                  {statedata.data[0].totalcount}
                </h1>

              </div>
            </div>
            <div className="state-title">
              {statedata.data[0].label}
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol red">
              <div className="value">
                <h1 className=" count2">
                  {statedata.data[1].presentcount}
                </h1>
              </div>
            </div>
            <div className="state-title">
              {statedata.data[1].label}
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol yellow">
              <div className="value">
                <h1 className=" count3">
                  {statedata.data[2].percentage}%
                </h1>

              </div>

            </div>

            <div className="state-title">
              {statedata.data[2].label}
            </div>
          </section>
        </div>

      </div>
    </div>)
  };

export default MetricsDashboard;
