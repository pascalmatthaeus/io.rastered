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
  useEffect( () => {
    async function createPlayerData() {
		async function setSource() {
			const response = await fetch("https://app.rastered.io/filter",
				{credentials:"include"});
			const responseJson = await response.json();
			const sk = await responseJson.streamKey;
			let webrtcSources = await [
			  {
				type: "webrtc",
				file: "wss://stream.rastered.io:3334/app/stream"+sk,
				label: "Viewport",
			  },
			];
			return webrtcSources;
		}
		const webrtcSources = await setSource();

		let op = await document.querySelector("#player_id");
		let player = await window.OvenPlayer.create(op, {
		  sources: webrtcSources,
		  autoStart: true,
		  mute: true,
		  controls: false,
		});
		
		console.log(webrtcSources);
	}
	createPlayerData();
	
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
