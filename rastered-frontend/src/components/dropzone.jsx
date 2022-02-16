import React, {useState,useRef} from "react";
import styled,{keyframes} from "styled-components";
import AddIcon from '@material-ui/icons/Add';
import { Error } from "./error";

const DropzoneContainerStyled = styled.div`
	width: 100%;
	height: 100%;
	margin: 0 auto;
	display: flex;
	flex-direction: column;
	text-align: center;
`;

const pulsate = keyframes`
	0% {
		transform: scale(1.0):
		transform: rotate3d(0.3,1,0.4,0deg);
	}
	50% {
		transform: scale(1.25);
		transform: rotate3d(0.3,1,0.4,180deg);
	}
	100% {
		transform: scale(1.0);
		transform: rotate3d(0.3,1,0.4,360deg);
	}
`;

const DropzoneStyled = styled.div`
	margin: 0 auto;
	flex: auto;
	width: 40%;
	max-height: 50%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	${props => {
		if (props.submitting) {
			//return `background: linear-gradient(to right, #21a7eb 0%, #21a7eb ${props.progress}%, rgba(255,255,255,0.25) ${props.progress}%)`;
			return `background: linear-gradient(to right, #21a7eb 0%, #21a7eb 50%, rgba(255,255,255,0.25) 50%);
				background-size: 200% 100%;
				background-position: ${(100-props.progress)}% 0%;`;
		}
		if (props.dragged) return 'background-color: #21a7eb';
		return `background-color: rgba(255,255,255,0.25); 
			&:hover { 
				background-color: rgba(255,255,255,0.35); 
			}`;
		
	}};
	background-origin: border-box;
	font-weight: normal;
	border: 3px solid ${props => props.dragged ? 'rgba(30,30,30,0.8)' : 'rgba(195,195,195,0.6)'};
	border-style: dashed;
	border-radius: 60px;
	transition: all 0.5s ease,
		${props => { if (props.progress <= 1 && props.submitting) return 'background-size none,'; } }
		background-position 0.2s ease,
		width 0.3s ease,
		height 0.3s ease,
		background-color 0.6s ease;
	@media (max-width: 768px) {
		width: 90%;
		max-height: 60%;
	}
	@media (max-height: 480px) {
		max-height: 90%;
	}
`;

const AddIconStyled = styled(AddIcon)`
	&& {
		font-size: 100%;
		margin-right: 5px;
	}
`;

const UploadIcon = styled.img`
	width: 20%;
	opacity: 50%;
	animation: ${pulsate} 1.0s linear infinite;
`;

const UploadButton = styled.button`
	display: flex;
	flex-direction: row;
	flex-wrap: wrap;
	align-items: center;
	justify-content: center;
	margin: auto;
	color: white;
	font-weight: bold;
	padding: 10px 15px 10px 15px;
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
	const [dragged,setDragged] = useState(false);
	const [dropped,setDropped] = useState(false);
	const [submitting,setSubmitting] = useState(false); //this needs to be set to false after testing CloudUploadIcon animation.
	const [uploadProgress,setUploadProgress] = useState(0);
	
	const [errorVisible,setErrorVisible] = useState(false);
	const [errorMsg,setErrorMsg] = useState("Undefined Error.");
	
	const dragOverHandler = (e) => {
		e.preventDefault();
		e.stopPropagation();
		setDragged(true);
	};
	
	const dropHandler = async (e) => {
		e.preventDefault();
		e.stopPropagation();
		setDragged(false);
		
		if (!submitting) {
			setDropped(true);
			if (e.dataTransfer.items && e.dataTransfer.items[0].kind === 'file') {
				inputFile = e.dataTransfer.items[0].getAsFile();
				console.log("File name: "+inputFile.name);
				if (inputFile.type === 'image/jpeg') {
					setErrorVisible(false);
					setSubmitting(true);
					await xhrFileUpload(inputFile);
				}
				else {
					setErrorMsg("This file is not an image.");
					setErrorVisible(true);
					console.log("File type: "+inputFile.type);
				}
			};
		}
		else {
			setErrorMsg("An upload is in progress already.");
			setErrorVisible(true);
		}
	};
	
	let inputFile = useRef(null);
	
	const handleSubmit = async (e) => {
		if (e.target.files[0] && e.target.files[0] !== null) {
			let file = e.target.files[0];
			console.log(file.name);
			if (file.type === 'image/jpeg') {
				setErrorVisible(false);
				setSubmitting(true);
				await xhrFileUpload(file);
			}
			else {
				setErrorMsg("This file is not an image.");
				setErrorVisible(true);
				console.log("File type: "+file.type);
			}	
		}
	};
	
	const xhrFileUpload = (file) => {
		// const blob = new Blob([new Uint8Array(10*1024*1024)]);
		const xhr = new XMLHttpRequest();
		xhr.upload.onprogress = (e) => {
			if (e.lengthComputable) {
				setUploadProgress(Math.floor((e.loaded/e.total)*100));
			}
		};
		xhr.upload.onloadend = () => {
			setTimeout( 
				() => {
					setSubmitting(false);
					setDropped(false);
					props.triggerNextView();
				}, 1000
			);
		};
		xhr.open("POST","https://app.rastered.io/push",true);
		xhr.setRequestHeader("Content-Type","application/octet-stream");
		xhr.withCredentials = true;
		xhr.send(file);
		// xhr.send(blob);
	};
	console.log(uploadProgress);
	
	
	return(
		<>
		<DropzoneContainerStyled>
			{ submitting ? `Uploading...` : `Pick the image you'd like to edit here.` }
			<DropzoneStyled 
				onDragOver={dragOverHandler} 
				onDrop={dropHandler} 
				onDragLeave={() => setDragged(false)} 
				dragged={dragged}
				submitting={submitting}
				progress={uploadProgress}
			>
				{ submitting ? 
					<>
						<UploadIcon src="upload.svg"/>
						{ uploadProgress !== 100 ? 
							uploadProgress+'%' 
							: 'Upload successful.'
						}
					</> 
				: !dragged ?
					<UploadButton onClick={() => inputFile.current.click()}>
						<AddIconStyled/> Choose File
					</UploadButton>
					: null
				}
				<input type='file' id='file' ref={inputFile} onChange={handleSubmit} style={{display:'none'}}/>
			</DropzoneStyled>
		</DropzoneContainerStyled>
		{ errorVisible ?
			<Error msg={errorMsg} hide={() => setErrorVisible(false)}/>
		: null }
		</>
	);
};
