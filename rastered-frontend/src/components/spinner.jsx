import React from "react";
import styled, { keyframes } from "styled-components";

/* This component is a placeholder with sizing issues. */

const spin = keyframes`
    0% {
	transform: rotate(0deg);
    }
    100% {
	transform: rotate(360deg);
    }
`;

export const Loader = styled.div`
	margin: auto;
	border: 0.2em solid rgba(0, 0, 0, 0.3);
	border-top: 0.2em solid #bcbcbc;
	border-radius: 50%;
	width: 2.5rem;
	height: 2.5rem;
	flex-basis: 2.5rem;
	max-width: 2.5rem;
	max-height: 2.5rem;
	flex: auto;
	animation: ${spin} 0.5s linear infinite;
`;
