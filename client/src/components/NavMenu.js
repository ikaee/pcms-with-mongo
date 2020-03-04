import React, {Component} from "react";
import {Link} from "react-router-dom";

export default class NavMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            linkKey: 1,
        }
    }

    activeClass = key => this.state.linkKey === key ? "active" : ""
    activeKey = linkKey => this.setState({linkKey});
    render() {
        return (

            <aside>
              <div id="sidebar" className="nav-collapse">
                <ul className="sidebar-menu" id="nav-accordion">
                  <li>
                    <Link className={this.activeClass(1)} onClick={() => this.activeKey(1)} to={"/"}>
                      <i className="fa fa-line-chart"></i>
                      <span>Attendance</span>
                    </Link>
                  </li>
                  <li>
                    <Link className={this.activeClass(5)} onClick={() => this.activeKey(5)} to={"/gm"}>
                      <i className="fa fa-line-chart"></i>
                      <span>Growth Monitoring</span>
                    </Link>
                  </li>

                  <li>
                    <Link className={this.activeClass(2)} onClick={() => this.activeKey(2)} to={"/hm"}>
                      <i className="fa fa-line-chart"></i>
                      <span>Hot Cooked Meals</span>
                    </Link>
                  </li>
                  <li>
                    <Link className={this.activeClass(3)} onClick={() => this.activeKey(3)} to={"/th"}>
                      <i className="fa fa-line-chart"></i>
                      <span>Take Home Ration</span>
                    </Link>
                  </li>
                  <li>
                    <Link className={this.activeClass(4)} onClick={() => this.activeKey(4)} to={"/sa"}>
                      <i className="fa fa-line-chart"></i>
                      <span>Sewika Attendance</span>
                    </Link>
                  </li>
                </ul>
              </div>
            </aside>

        )
    }
}
