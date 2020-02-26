import React from 'react';

const MetricsDashboard = ({data: {totalcount = 0, presentcount = 0, percentage = 0}}) => (
    <div>
      <div className="row state-overview">
        <div className="col-lg-2 col-sm-6" >
          <section className="panel">
            <div className="symbol terques">
              <div className="value">
                <h1 className="count">
                  {presentcount}
                </h1>
                <p>Present</p>
              </div>
            </div>
          </section>
        </div>
        <div className="col-lg-2 col-sm-6">
          <section className="panel">
            <div className="symbol red">
              <div className="value">
                <h1 className=" count2">
                  {totalcount}
                </h1>
                <p>Total</p>
              </div>
            </div>

          </section>
        </div>
        <div className="col-lg-2 col-sm-6">
          <section className="panel">
            <div className="symbol yellow">
              <div className="value">
                <h1 className=" count3">
                  {percentage}
                </h1>
                <p>Percentage</p>
              </div>

            </div>
          </section>
        </div>

      </div>
    </div>);

export default MetricsDashboard;
