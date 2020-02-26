import React from 'react';

const MetricsDashboard = ({data: {totalcount = 0, suw = 0, muw = 0, normal = 0}}) => (
    <div>
      <div className="state-overview metrics-state-overview-blocks">
        <div className="state-overview-block" >
          <section className="panel">
            <div className="symbol terques">
              <div className="value">
                <h1 className="count">
                  {totalcount}
                </h1>

              </div>
            </div>
            <div className="state-title">
              Wighted
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol red">
              <div className="value">
                <h1 className=" count2">
                  {suw}
                </h1>
              </div>
            </div>
            <div className="state-title">
              Severly Malnourished
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol yellow">
              <div className="value">
                <h1 className=" count3">
                  {muw}
                </h1>

              </div>

            </div>

            <div className="state-title">
              Moderately malnourishment
            </div>
          </section>
        </div>
        <div className="state-overview-block">
          <section className="panel">
            <div className="symbol green">
              <div className="value">
                <h1 className=" count3">
                  {normal}
                </h1>

              </div>

            </div>

            <div className="state-title">
              Normal
            </div>
          </section>
        </div>

      </div>
    </div>);

export default MetricsDashboard;
