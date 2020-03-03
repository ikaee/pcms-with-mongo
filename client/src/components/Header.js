import React from "react";

const Header = ({title, date,onClickHandler}) => {

    return (<header className="header white-bg">
      <div className="sidebar-toggle-box" onClick= {onClickHandler}>
        <i className="fa fa-bars"></i>
      </div>
      <a href="/" className="logo">{title}<span>{date}</span></a>
    </header>)
  }
export default Header;
