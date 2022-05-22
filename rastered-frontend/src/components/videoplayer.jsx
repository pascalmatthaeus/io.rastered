import React, { useState, useRef, useEffect } from "react";
import { Form, FormControl } from "react-bootstrap";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";

const VidPlayerContainer = styled.div`
	width: 500px;
`;

const VidPlayerStyled = styled.div`
	margin-top: 100px;
	padding-left: 100px;
	box-shadow: 10px 10px 16px rgba(0, 0, 0, 100%);
`;



export const VideoPlayer = (props) => {
	useEffect(async () => {
    		async function createPlayerData() {
			let op = await document.querySelector("#player_id");
			let webrtcSources = await [
			  {
				type: "webrtc",
				file: "ws://ome:3333/app/stream"+props.streamKey,
				label: "Viewport",
			  },
			];
			let player = await window.OvenPlayer.create(op, {
			  sources: webrtcSources,
			  autoStart: true,
			  mute: true,
			  controls: false,
			});
			
		}
		await createPlayerData();
	},[props.streamKey]);
	return (
		<VidPlayerContainer>
		  <VidPlayerStyled id="player_id" />
		</VidPlayerContainer>
	);
};
