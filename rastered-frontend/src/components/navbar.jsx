import React from "react";
import { Nav, Navbar, Form, FormControl } from "react-bootstrap";
import styled from "styled-components";
const Styles = styled.div`
  .navbar {
    background-color: #222;
    box-shadow: 2px 4px 16px rgba(0, 0, 0, 25%);
    z-index: 2;
  }
  a,
  .navbar-nav,
  .navbar-light .nav-link {
    font-weight: bold;
    padding-left: 12.5%;
    color: white !important;
    &:hover {
      color: #21a7eb !important;
    }
  }
  .navbar-brand {
    font-size: 30px;
    color: white;
    &:hover {
      color: #21a7eb;
    }
  }
  .form-center {
    position: absolute !important;
    left: 75%;
    right: 5%;
  }
  img,
  .navbar-brand {
    width: 30px;
    padding-left: 3px;
  }
`;
export const NavBar = () => (
  <Styles>
    <Navbar expand="lg">
      <Navbar.Brand href="/">
        <img src="tesseract.png" />
        <a>rastered.io</a>
      </Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Form className="form-center">
        <FormControl type="text" placeholder="Search" className="" />
      </Form>
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="ml-auto">
          <Nav.Item>
            <Nav.Link href="/">Home</Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link href="/about">About</Nav.Link>
          </Nav.Item>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  </Styles>
);
