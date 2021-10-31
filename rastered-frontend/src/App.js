import logo from "./logo.svg";
import "./App.css";
import React, { useRef, useState, useEffect } from "react";

import { Home } from "./Home";
import { About } from "./About";
import { NoMatch } from "./NoMatch";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import { NavBar } from "./components/navbar";
import Sidebar from "./components/Sidebar";
import { AppViewport, GlobalTheme } from "./components/viewport";

function App() {
  useEffect(() => {
    /*const response = fetch(
      "http://127.0.0.1:8080/filter?valGam=100&valExp=100&valSli=100",
      { credentials: "include" }
    );*/

    let webrtcSources = [
      {
        type: "webrtc",
        file: "ws://127.0.0.1:3333/app/stream",
        label: "Viewport",
      },
    ];

    let op = document.querySelector("#player_id");
    let player = window.OvenPlayer.create(op, {
      sources: webrtcSources,
      autoStart: true,
      mute: true,
      controls: false,
    });
  });
  return (
    <React.Fragment>
      <Router>
        <GlobalTheme />
        <Sidebar />
        <NavBar />
        <AppViewport imgsrc="lenna.jpg" />

        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/about" component={About} />
          <Route component={NoMatch} />
        </Switch>
      </Router>
    </React.Fragment>
  );
}

export default App;
