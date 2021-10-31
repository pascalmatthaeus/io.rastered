import React, { useEffect } from "react";
import { Form, FormControl } from "react-bootstrap";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";
import { FilterDetails } from "./filterdetails";
import { VideoPlayer } from "./videoplayer";

export const GlobalTheme = createGlobalStyle`
body {
  background-color: rgba(60,60,60,255) !important;
}`;

const Viewport = styled.div`
  color: white;
  font-weight: bolder;
  position: absolute;
  display: grid;
  grid-gap: 5%;
  grid-template-columns: repeat(12, 1fr);
  grid-auto-rows: minmax(25px, auto);
  left: 50%;
  top: 50%;
  padding-left: 5%;
  padding-right: 5%;
  padding-top: 5%;
  padding-bottom: 5%;
  transform: translate(-50%, -50%);
  /*border: 8px solid red;*/
`;

const DocumentCard = styled.div`
  position: relative;
  /*border: 3px solid yellow;*/
`;

const VpImage = styled.img`
  padding-left: 5%;
  padding-right: 5%;
  padding-top: 5%;
  padding-bottom: 5%;
  border: 5px solid rgba(30, 30, 30, 255);
  border-radius: 8px;
  box-shadow: 4px 1px 24px rgba(0, 0, 0, 25%);
`;

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

export const AppViewport = (props) => {
  return (
    <Viewport>
      <p>
        <FilterDetails />
        {/*<FilterDetails />
        <FilterDetails />
        <FilterDetails />*/}
        {/*FILTERA
      <ParamSlider type="range" min="1" max="100" />
      FILTERA
      <ParamSlider type="range" min="1" max="100" />
      FILTERA
      <ParamSlider type="range" min="1" max="100" />
      FILTERA
      <ParamSlider type="range" min="1" max="100" />*/}
      </p>
      <p>
        <DocumentCard>
          <VideoPlayer />
          {/*<VpImage src={props.imgsrc} />*/}
        </DocumentCard>
      </p>
    </Viewport>
  );
};
