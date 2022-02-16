import logo from "./logo.svg";
import "./App.css";
import React, { useRef, useState, useEffect } from "react";

import { Home } from "./Home";
import { About } from "./About";
import { NoMatch } from "./NoMatch";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import { LayoutContainer } from "./components/layoutcontainer";
import { TopBar } from "./components/topbar";
import Sidebar from "./components/Sidebar";
import { AppViewport, GlobalTheme } from "./components/viewport";

function App() {
	const [streamKey,setStreamKey] = useState(null);
	const [fetchHasFinished,setFetchHasFinished] = useState(false);
	useEffect( async () => {
		async function fetchStreamKey() {
			const response = await fetch("https://app.rastered.io/filter",
				{ method:"POST",
				credentials:"include"}
			);
			const responseJson = await response.json();
			const sk = await responseJson.streamKey;
			await setFetchHasFinished(true);
			return sk;
		}
		
		await setStreamKey(await fetchStreamKey());
	}, []);
	return (
		<React.Fragment>
			<Router>
				<GlobalTheme />
				<LayoutContainer>
					<Sidebar width="3vw" minWidth="35px"/>
					<TopBar />
					<AppViewport streamKey={streamKey} fetchFinished={fetchHasFinished}/>
				</LayoutContainer>
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
