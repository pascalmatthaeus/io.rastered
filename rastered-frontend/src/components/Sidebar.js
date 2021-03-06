import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";

import styled from "styled-components";
/* This defines the actual bar going down the screen */
const StyledSideNav = styled.div`
  position: fixed; /* Fixed Sidebar (stay in place on scroll and position relative to viewport) */
  height: 100vh;
  width: ${props => props.width ? props.width : "40px"}; /* Set the width of the sidebar */
  min-width: ${props => props.minWidth ? props.minWidth : "40px"};
  grid-column:1/2;
  grid-row:1/2;
  z-index: 1; /* Stay on top of everything */
  top: 10vh; /* Stay at the top */
  background-color: #222; /* Black */
  overflow: hidden; /* Disable horizontal scroll */
  padding-top: 25px;
  box-shadow: 2px 4px 16px rgba(0, 0, 0, 25%);
  @media (max-width:768px) {
    	display: none;
    }
`;

const IconContainer = styled.div`
  position: relative;
  top: 2.5%;
  left: 5px;
`;

const StyledNavItem = styled.div`
  height: 5%;
  width: 5%; /* width must be same size as NavBar to center */
  padding-left: 5px;*/
  text-align: center; /* Aligns <a> inside of NavIcon div */
  margin-bottom: 1em; /* Puts space between NavItems */
  a {
    width: 1em;
    font-size:1em;
    color: ${(props) => (props.active ? "#21a7eb" : "grey")};
    text-decoration: none;
    :hover {
      text-decoration: none; /* Gets rid of underlining of icons */
    }
  }
`;

const NavIcon = styled.div`
  text-align: center;
`;

class SideNav extends React.Component {
  constructor(props) {
    super(props);

    this.op = null;

    this.state = {
      activePath: "/",
      items: [
        {
          path:
            "/" /* path is used as id to check which NavItem is active basically */,
          name: "Home",
          css: "fa fa-fw fa-home",
          key: 1 /* Key is required, else console throws error. Does this please you Mr. Browser?! */,
        },
        {
          path: "/about",
          name: "About",
          css: "fa fa-fw fa-clock",
          key: 2,
        },
        {
          path: "/NoMatch",
          name: "NoMatch",
          css: "fas fa-hashtag",
          key: 3,
        },
      ],
    };
  }

  componentDidMount() {
    this.op = window.OvenPlayer;
  }

  onItemClick = (path) => {
    this.setState({
      activePath: path,
    }); /* Sets activePath which causes rerender which causes CSS to change */
  };
  render() {
    const { items, activePath } = this.state;
    return (
      <StyledSideNav width={this.props.width} minWidth={this.props.minWidth}>
        <IconContainer>
          {
            /* items = just array AND map() loops thru that array AND item is param of that loop */
            items.map((item) => {
              /* Return however many NavItems in array to be rendered */
              return (
                <NavItem
                  path={item.path}
                  name={item.name}
                  css={item.css}
                  onItemClick={this.onItemClick}
                  /* Simply passed an entire function to onClick prop */ active={
                    item.path === activePath
                  }
                  key={item.key}
                />
              );
            })
          }
        </IconContainer>
      </StyledSideNav>
    );
  }
}

export default class Sidebar extends React.Component {
  render() {
    return <SideNav width={this.props.width} minWidth={this.props.minWidth}></SideNav>;
  }
}

class NavItem extends React.Component {
  handleClick = () => {
    const { path, onItemClick } = this.props;
    onItemClick(path);
  };
  render() {
    const { active } = this.props;
    return (
      <StyledNavItem active={active}>
        <Link
          to={this.props.path}
          className={this.props.css}
          onClick={this.handleClick}
        >
          <NavIcon></NavIcon>
        </Link>
      </StyledNavItem>
    );
  }
}
