import React, {useState} from 'react';
import styled from 'styled-components';
import CloseIcon from '@material-ui/icons/Close';
import ErrorIcon from '@material-ui/icons/Error';

const ErrorStyled = styled.div`
	position: fixed;
	bottom: 0%;
	z-index: 12;
	width: 75vw;
	left: 50%;
	transform: translate(-50%,0%);
	background-color: rgba(170,30,30,1.0);
	display: flex;
	flex-direction: row;
	justify-content: center;
	align-items: center;
	color: white;
	font-weight: bold;
	padding-top: 1%;
	padding-bottom: 1%;
	border-bottom: none;
	border-radius: 10px 10px 0px 0px;
`;

const ErrorMsg = styled.div`
	flex-basis: 100%;
`;

const ErrorIconStyled = styled(ErrorIcon)`
	&& {
		font-size: 200%;
		margin-right: 10px;
		margin-left: 20px;
		flex: auto;
	}
`;

const CloseIconStyled = styled(CloseIcon)`
	max-width: 6%;
	color: rgba(200,200,200,0.8);
	margin-left: auto; 
	margin-right: 20px;
	flex: auto;
	&:hover {
		color: white;
	}
`;



export const Error = (props) => {
	return(
		<ErrorStyled>
			<ErrorIconStyled/>
			<ErrorMsg>{props.msg}</ErrorMsg>
			<CloseIconStyled onClick={props.hide}/>
		</ErrorStyled>
	);
};
