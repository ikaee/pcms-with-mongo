import React from "react";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import DocumentTitle from "react-document-title";
import faviconImage from "../images/icds.ico";
import Favicon from "react-favicon";
import AMR from "./AMR";
import THR from "./THR";
import GMR from "./GMR";
import SAMR from "./SAMR" ;
import HMR from "./HMR";
import NavMenu from "./NavMenu";
import Header from "./Header";
import HotCooked from "./HotCooked";
import AttendanceDashboard from "./AttendanceDashBoard";
import GrowthMonitoringDashboard from "./GrowthMonitoringDashboard";
import MealDashBorad from "./MealDashBorad";
import THRDashBorad from "./THRDashBorad";
import SewikaAttendance from "./SewikaAttancdance";


class Main extends React.Component {

  constructor() {
      super();
      this.container = React.createRef();
  }

  onClickHandler =  () =>  {
    this.container.current.classList.toggle("sidebar-closed")
  }

  shouldComponentUpdate(nextProps, nextState){
    return this.state.sidebarVisibilty !== nextState.sidebarVisibilty
  }

  render() {
      return (<Router>
        <section id="container" ref={this.container}>
          <DocumentTitle title="PCMS"/>
          <Favicon url={faviconImage}/>
          <Header title={"PCMS"} onClickHandler = {this.onClickHandler}/>
          <NavMenu />
          <section id={"main-content"} style1={{marginLeft: 0}}>
            <Route exact path="/" component={AttendanceDashboard}/>
            <Route path="/gm" component={GrowthMonitoringDashboard}/>
            <Route path="/hm" component={MealDashBorad}/>
            <Route path="/th" component={THRDashBorad}/>
            <Route path="/thr" component={THR} />
            <Route path="/amr" component={AMR}/>
            <Route path="/gmr" component={GMR}/>
            <Route path="/hmr" component={HMR}/>
            <Route path = "/samr" component={SAMR} />
            <Route path="/sa" component ={SewikaAttendance} />
            <Route path="/hot-cooked" component={HotCooked}/>
          </section>
          {/*<Footer />*/}
        </section>
      </Router>);
  }

}

export default Main;
