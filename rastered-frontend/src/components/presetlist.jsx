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
`;

const ZoneTitle = styled.p`
	min-height: 3px;
	border-bottom: 1px solid white
`;

const AvailablePreset = styled.div`
	/*border: 1px solid red;*/
	text-align: center;
	user-select: none;
	flex: 0 0 18%;
	background-color: #061223;
	box-shadow: 0px 3px 10px rgba(0,0,0,0.3);
	border-radius: 18px;
	padding: 1%;
	margin: 1%;
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

export const PresetList = (props) => 
{
	const [presets,setPresets] = useState([]);

	useEffect ( () => {
		const response = fetch("https://app.rastered.io/getpresets")
			.then( response => response.json() )
			.then( setPresets )
	},[]);
	
	console.log(presets);
	
	return (
		<Container>
		<ZoneTitle>AVAILABLE PRESETS</ZoneTitle>
		<AvailablePresetsZone>
			{ presets.map( (preset) => 
				<AvailablePreset>{preset.friendlyName}</AvailablePreset>
			) }
			<AvailablePreset>Bloom</AvailablePreset>
			<AvailablePreset>Gaussian Blur</AvailablePreset>
			<AvailablePreset>Cartoonize</AvailablePreset>
			<AvailablePreset>FFT</AvailablePreset>
			<AvailablePreset>Sketch</AvailablePreset>
			<AvailablePreset>Unsharp Mask</AvailablePreset>
		</AvailablePresetsZone>
		<p/>
		<ZoneTitle>PIPELINE</ZoneTitle>
		<PipelineZone>
			{ presets.map( (preset) => 
			<SelectedPreset>{preset.friendlyName}</SelectedPreset>
			) }
		</PipelineZone>
		</Container>
	);
};
