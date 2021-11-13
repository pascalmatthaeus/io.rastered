import React, { useState, useRef } from "react";
import { Form, FormControl } from "react-bootstrap";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";

const VidPlayerContainer = styled.div`
  width: 300px;
`;

const VidPlayerStyled = styled.div`
  margin-top: 100px;
  padding-left: 100px;
  box-shadow: 10px 10px 16px rgba(0, 0, 0, 100%);
`;

export const VideoPlayer = (props) => {
	return (
		<VidPlayerContainer>
		  <VidPlayerStyled id="player_id" />
		</VidPlayerContainer>
	);
};
