import React from "react";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import DocumentTitle from "react-document-title";
import faviconImage from "../images/icds.ico";
import Favicon from "react-favicon";
import Dashboard from "./DashBoard";
import AMR from "./AMR";
import THR from "./THR";
import NavMenu from "./NavMenu";
import Header from "./Header";
import Footer from "./Footer";
import HotCooked from "./HotCooked";

export default () =>
    <Router>
        <section id="container">
            <DocumentTitle title="BFR"/>
            <Favicon url={faviconImage}/>
            <Header title={"ICDS"} date={"January 2018"} />
            <NavMenu/>
            <section id={"main-content"} style1={{marginLeft: 0}}>
                <Route exact path="/" component={Dashboard}/>
                <Route path="/amr" component={AMR}/>
                <Route path="/thr" component={THR}/>
                <Route path="/hot-cooked" component={HotCooked}/>
            </section>
            {/*<Footer />*/}
        </section>
    </Router>


