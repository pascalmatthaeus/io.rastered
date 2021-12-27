import React, {useState, useEffect, useRef} from "react";
import {Form,FormControl} from "react-bootstrap";
import styled from "styled-components";

const SessionButton = styled.button`
	border: none;
	border-radius: 50px;
	box-shadow: 0px 3px 8px rgba(0,0,0,0.6);
	transition: box-shadow 0.3s ease,
		background-color 0.1s linear;
	/*border: 1px solid green;*/
	-webkit-appearance: none;
	font-weight: bold;
	color: #FFFFFF;
	padding: 2% 10% 2% 10%;
	flex: auto;
	/*width: 100px;
	height: 50px;*/
	background: #061223;
	&:hover {
		background-color: #21a7eb;
		box-shadow: 0px 6px 14px rgba(0,0,0,0.6);
	}
`;

export const SessionBtn = (props) => {
	return (
		<SessionButton onClick={props.onClick}>Start Session</SessionButton>
	);
};
