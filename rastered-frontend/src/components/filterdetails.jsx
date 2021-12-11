import React, { useState, useRef, useEffect } from "react";
import { Form, FormControl } from "react-bootstrap";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";

function ToggleOpen() {
  const open = useState(false);
}

const Ul = styled.ul`
  height: ${({ height }) => height}px;
  opacity: ${({ height }) => (height > 0 ? 1 : 0)};
  overflow: hidden;
  transition: 0.5s;
`;

const Wrap = styled.div``;

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

export const FilterDetails = (props) => {
	const content = useRef(null);
	const [height, setHeight] = useState(0);
	const toggleFilDetails = () => {
		setHeight(height === 0 ? content.current.scrollHeight : 0);
	};

	const [parameters, setParameters] = useState({params:[]});
  
	const sliChange = (e) => {
		let newParameters = {...parameters};
		newParameters.params[e.currentTarget.attributes.target.value] = e.currentTarget.value;
		console.log("props.target for slider: "+e.currentTarget.attributes.target.value);
		console.log("value: "+e.currentTarget.value)
		setParameters(newParameters);
		console.log("newParameters contains: "+newParameters[e.currentTarget.attributes.target.value]);
	};
  

	const postParameters = () => {
		fetch(
			"https://app.rastered.io/filter?valGam=" +
			parameters.params[0] +
			"&valExp=" +
			parameters.params[1] +
			"&valSli=100&" +
			new Date().getTime(),
			{ method: "POST", 
			credentials: "include",
			body: JSON.stringify(parameters) }
		).then(function (response) {
			console.log(response);
			return response.json();
		}).then(function (jsonResponse) {
			console.log(jsonResponse);
		});
		console.log(JSON.stringify(parameters));
		props.fncTellAmountFrames(props.amountFrames+1);
	};

	useEffect(() => {
		const interval = setInterval(() => {
			postParameters();
		}, 468);
		return () => {
			clearInterval(interval);
		};
	});

	return (
		<>
			<h2 onClick={toggleFilDetails}>FILTER</h2>
			<Ul height={height} ref={content}>
				Gamma
				<ParamSlider type="range" min="1" max="100" target="0" onChange={sliChange} />
				Exposure
				<ParamSlider type="range" min="1" max="100" target="1" onChange={sliChange} />
			</Ul>
		</>
	);
};
