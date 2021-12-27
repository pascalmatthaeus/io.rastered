import React, {useState,useRef} from "react";
import styled from "styled-components";

const DropzoneContainerStyled = styled.div`
	width: 80vw;
	height: 80vh;
	margin: 0 auto;
	text-align: center;
`;

const DropzoneStyled = styled.div`
	width: 75%;
	height: 70%;
	margin: 0 auto;
	display: flex;
	justify-content: center;
	align-items: center;
	background-color: rgba(255,255,255,0.25);
	border: 3px solid rgba(195,195,195,0.6);
	border-style: dashed;
	border-radius: 60px;
	transition: all 0.5s ease;
	&:hover {
		background-color: rgba(255,255,255,0.35);
	}
`;

const UploadButton = styled.button`
	color: white;
	font-weight: bold;
	padding: 10px 20px 10px 20px;
	background-color: #061223;
	border: none;
	border-radius: 80px;
	box-shadow: 0px 5px 16px rgba(0,0,0,0.4);
	transition: all 0.2s ease;
	&:hover {
		transform: scale(1.05);
		color: white;
		background-color: #21a7eb;
		box-shadow: 0px 3px 8px rgba(0,0,0,0.75);
	}
`;

export const Dropzone = (props) =>
{
	const inputFile = useRef(null);
	return(
		<DropzoneContainerStyled>
			Pick the image you'd like to edit here.
			<DropzoneStyled>
				<UploadButton onClick={() => inputFile.current.click()}>
					Choose File
				</UploadButton>
				<input type='file' id='file' ref={inputFile} style={{display:'none'}}/>
			</DropzoneStyled>
		</DropzoneContainerStyled>
	);
};
