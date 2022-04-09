import { useState, useEffect } from "react";
import styled from "styled-components";

const Container = styled.div`
	display: flex;
	flex-direction: column;
	text-align: left;
`;

const AvailablePresetsZone = styled.div`
	display: flex;
	flex-flow: row wrap;
	flex: auto;
`;

const PipelineZone = styled.div`
	/*border: 1px solid red;*/
	display: flex;
	flex-direction: column;
	flex: auto;
	background-color: #111111;
	border-radius: 8px;
`;

const ZoneTitle = styled.p`
	min-height: 3px;
	border-bottom: 1px solid white
`;

const AvailablePreset = styled.div`
	/*border: 1px solid red;*/
	text-align: center;
	user-select: none;
	flex: 0 0 19%;
	background-color: #061223;
	box-shadow: 0px 3px 10px rgba(0,0,0,0.3);
	border-radius: 18px;
	padding: 1%;
	margin: 0.5%;
	&:hover {
		background-color: #21a7eb;
	}
`;

const SelectedPreset = styled.div`
	/*border: 1px solid red;*/
	text-align: center;
	user-select: none;
	flex: auto;
	background-color: #333333;
	box-shadow: 0px 3px 6px rgba(0,0,0,0.2);
	border-radius: 6px;
	padding: 1%;
	margin: 1%;
	&:hover {
		background-color: #444444;
	}
`;

const SelectedPresetDraggable = (props) => {
	const handleDrag = e => {
		console.log(e.pageX);
	};
	
	const handleDragOver = e => {
		console.log(e.nativeEvent.target);
	};
	
	return (
		<SelectedPreset 
		draggable droppable
		onDrag={handleDrag}
		onDragOver={handleDragOver}>
		
			{props.name}
		
		</SelectedPreset>
	);
};

export const PresetList = (props) => 
{
	const [presets,setPresets] = useState([{}]);

	useEffect ( async () => {
		const response = await fetch("https://app.rastered.io/getpresets");
		const json = await response.json();
		await setPresets( json );
		},[]);
	
	
	console.log(presets);
	
	return (
		<Container>
		<ZoneTitle>AVAILABLE PRESETS</ZoneTitle>
		<AvailablePresetsZone>
			{ presets.map( preset => 
				<AvailablePreset>{preset.friendlyName}</AvailablePreset>
			) }
			{/*<AvailablePresetDraggable name="Bloom"/>
			<AvailablePresetDraggable name="Gaussian Blur"/>
			<AvailablePresetDraggable name="Cartoonize"/>
			<AvailablePresetDraggable name="FFT"/>
			<AvailablePresetDraggable name="Sketch"/>
			<AvailablePresetDraggable name="Unsharp Mask"/>*/}
		</AvailablePresetsZone>
		<p/>
		<ZoneTitle>PIPELINE</ZoneTitle>
		<PipelineZone>
			{ presets.map( preset => 
			<SelectedPresetDraggable name={preset.friendlyName} />
			) }
		</PipelineZone>
		</Container>
	);
};
