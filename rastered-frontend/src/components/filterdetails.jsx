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
  const list = ["", "", ""];
  const content = useRef(null);
  const [height, setHeight] = useState(0);
  const valueSli = useState(100);
  const toggleFilDetails = () => {
    setHeight(height === 0 ? content.current.scrollHeight : 0);
  };
  const [valSli, setValSli] = useState(69);
  const sliChange = (e) => {
    setValSli(e.currentTarget.value);
  };

  const getData = () => {
    fetch(
      "https://app.rastered.io/filter?valGam=100&valExp=100&valSli=" +
        valSli +
        "&" +
        new Date().getTime(),
      { method: "GET", credentials: "include" }
    )
      .then(function (response) {
        console.log(response);
        return response.json();
      })
      .then(function (jsonResponse) {
        console.log(jsonResponse);
      });
  };

  useEffect(() => {
    const interval = setInterval(() => {
      getData();
    }, 468);
    return () => {
      clearInterval(interval);
    };
  });

  return (
    <>
      <h2 onClick={toggleFilDetails}>FILTER</h2>
      <Ul height={height} ref={content}>
        {/*list.map((item, index) => (
          <li key={index}>{item}</li>
        ))*/}
        <ParamSlider type="range" min="1" max="100" onChange={sliChange} />
        {/*}<b>{valSli}</b>*/}
        {/*<ParamSlider type="range" min="1" max="100" />
        <ParamSlider type="range" min="1" max="100" />
        <ParamSlider type="range" min="1" max="100" />*/}
      </Ul>
    </>
  );
};
