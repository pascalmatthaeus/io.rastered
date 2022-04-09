import React, { useState, useEffect } from "react";
import styled from "styled-components/macro";
import { createGlobalStyle } from "styled-components";
import { SessionBtn } from "./sessionbtn";
import { FilterDetails } from "./filterdetails";
import { VideoPlayer } from "./videoplayer";
import { Loader } from "./spinner";
import { Dropzone } from "./dropzone";
import { PresetList } from "./presetlist";
import { Error } from "./error";

export const GlobalTheme = createGlobalStyle`
body {
	background-color: rgba(60,60,60,255) !important;
	overflow:hidden;
}`;

const ViewportContainer = styled.div`
	display:grid;
	grid-row:2/3;
	grid-column:2/3;
	grid-template-columns: auto-fit;
	grid-template-rows: 5vh 85vh;
	@media (max-width:768px) {
		grid-template-rows: 5vh 85vh;
		grid-column: 1/2;
	}
`;

const Viewport = styled.div`
	background-color: rgba(35,35,35,1.0);
	height: 100%;
	color: white;
	font-weight: bold;
	grid-row:2/3;
	grid-column:1/2;
	display: flex;
	flex-wrap: wrap;
	justify-content: center;
	/*border: 2px solid blue;*/
`;

const TabbarContainer = styled.div`
	position: fixed;
	grid-row: 1 / 2;
	grid-column: 1 / 2;
	display: flex;
	flex: auto;
	align-items: center;
	justify-content: center;
	width: 97vw;
	height: 5vh;
	box-shadow: 0px 3px 20px rgba(0,0,0,0.5);
	@media (max-width: 768px) {
		box-shadow: none;
		border-radius: 60px;
		/*flex-direction: column;*/
		width: 100vw;
	}
`

const Tab = styled.div`
	/*border: 3px solid red;*/
	text-align: center;
	vertical-align: middle;
	line-height: 5vh;
	color: white;
	font-size: 16px;
	font-weight: bold;
	background-color: ${props => props.selected ? 'rgba(20,20,20,1)' : 'rgba(50,50,50,1)'};
	transition: background-color 0.1s ease-out,
		color 0.2s ease,
		flex 0.5s ease,
		box-shadow 0.3s ease;
	flex: ${props => props.selected ? '1.25' : '1'};
	z-index:9;
	overflow: hidden;
	white-space: nowrap;
	/*border-radius: 0px 0px 68px 68px;*/
	&:hover {
		box-shadow: 0px 20px 50px rgba(0,0,0,0.25);
		color: ${props => props.selected ? 'white' : 'rgba(127,206,245,1)'};
		background-color: ${props => props.selected ? 'rgba(35,35,35,1)' : 'rgba(90,90,90,1)'};
	}
	@media (max-width: 768px) {
		width: 100vw;
		margin-left: 1vw;
		margin-right: 1vw;
		font-size: ${props => props.selected ? '15px' : '13px'};
		border-radius: 60px;
		font-size: 13px;
		box-shadow: 0px 3px 6px rgba(0,0,0,0.4);
		&:hover {
			box-shadow: 0px 4px 16px rgba(0,0,0,0.35);
		}
	}
	@media (max-height: 480px) {
		font-size: 3vh;
	}
`;

const DocumentCard = styled.div`
	/*border: 3px solid yellow;*/
	margin: 20px 30px 20px 30px;
	flex: auto;
	min-width: ${props => props.minWidth};
	max-width: ${props => props.maxWidth};
`;

const ParamSlider = styled.input`
	-webkit-appearance: none;
	appearance: none;
	min-width: 100px;
	width: 100%;
	height: 12px;
	background: #061223;
	outline: none;
	opacity: 0.8;
	-webkit-transition: 0.2s;
	transition: opacity 0.2s;
	border-radius: 2px;
	box-shadow: 1.5px 5px 10px rgba(0, 0, 0, 0.3);
`;

const Tabbar = (props) => {
	return (
		<TabbarContainer>
			<Tab id="0" onClick={props.selectTab} selected={props.tabSelected==0}>Upload Image</Tab>
			<Tab id="1" onClick={props.selectTab} selected={props.tabSelected==1}>Create Pipeline</Tab>
			<Tab id="2" onClick={props.selectTab} selected={props.tabSelected==2}>Editor</Tab>
		</TabbarContainer>	
	);
};

const ViewSwitch = (props) => {
	switch(props.tabSelected) {
		case '0': return(<UploadView triggerNextView={props.triggerNextView}/>);
		case '1': return(<PipelineView/>);
		case '2': return(<EditorView fetchFinished={props.fetchFinished} streamKey={props.streamKey}/>);
		default: return null;
	};
};

const UploadView = (props) => {
	return(
		<DocumentCard>
			<Dropzone triggerNextView={props.triggerNextView}/>
		</DocumentCard>
	);
};

const PipelineView = (props) => {
	return(
		<DocumentCard>
			<PresetList/>
		</DocumentCard>
	);
};

const EditorView = (props) => {
	const [showVP, setShowVP] = useState(false);
	const [amountFrames, setAmountFrames] = useState(0);
	
	const tellAmountFrames = (amount) => {
		setAmountFrames(amount);
		console.log("Amount of filter API calls: "+amountFrames);
	};
	return(
		<>
		<DocumentCard minWidth="100px" maxWidth="20vw">
			{ showVP && props.fetchFinished ?
			<FilterDetails fncTellAmountFrames={tellAmountFrames} amountFrames={amountFrames}/>
			: <SessionBtn onClick={e => {setShowVP(true)}}/> }
		</DocumentCard>
		<DocumentCard minWidth="300px" maxWidth="80vw">
			{ amountFrames>5 ?
			<VideoPlayer streamKey={props.streamKey}/>
			: <Loader/> }
		</DocumentCard>
		</>
	);
};

export const AppViewport = (props) => {
	const [tabSelected,setTabSelected] = useState('0');
	
	const selectTab = (e) => {
		setTabSelected(e.currentTarget.attributes.id.value);
		console.log(e.currentTarget.attributes.id.value);
	}
	
	return (
		<>
		<ViewportContainer>
			<Tabbar selectTab={selectTab} tabSelected={tabSelected}/>
			<Viewport>
				<ViewSwitch 
					fetchFinished={props.fetchFinished} 
					tabSelected={tabSelected} 
					streamKey={props.streamKey}
					triggerNextView={() => setTabSelected(1)}
				/>
			</Viewport>
		</ViewportContainer>
		</>
	);
};
