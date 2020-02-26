import React from 'react';

const MetricsDashboard = ({data: {totalcount = 0, presentcount = 0, percentage = 0}}) => (
    <div>
      <div className="state-overview metrics-state-overview-blocks">
        <div className="state-overview-block" >
          <section className="panel">
            <div className="symbol terques">
              <div className="value">
                <h1 className="count">
                  {presentcount}
                </h1>

              </div>
            </div>
            <div className="state-title">
              Total Beneficiaries
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol red">
              <div className="value">
                <h1 className=" count2">
                  {totalcount}
                </h1>
              </div>
            </div>
            <div className="state-title">
              Total Meal Distributed
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol yellow">
              <div className="value">
                <h1 className=" count3">
                  {percentage}%
                </h1>

              </div>

            </div>

            <div className="state-title">
              Percentage of Beneficiaries recevied meal
            </div>
          </section>
        </div>

      </div>
    </div>);

export default MetricsDashboard;
