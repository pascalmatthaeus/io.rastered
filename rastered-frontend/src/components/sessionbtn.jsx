import React, {useState, useEffect, useRef} from "react";
import {Form,FormControl} from "react-bootstrap";
import styled from "styled-components";
import {createGlobalStyle} from "styled-components";

const SessionButton = styled.button`
	color: #FFFFFF;
	width: 100px;
	height: 50px;
	left: 50%;
	top: 50%;
	background: #061223;
`;

export const SessionBtn = (props) => {
	return (
		<SessionButton onClick={props.onClick}>GO</SessionButton>
	);
};
