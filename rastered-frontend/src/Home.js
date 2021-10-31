import React from "react";
import styled from "styled-components";

const GridWrapperOld = styled.div`
  color: white;
  display: grid;
  grid-gap: 10px;
  margin-top: 1em;
  margin-left: 6em;
  margin-right: 6em;
  grid-template-columns: repeat(12, 1fr);
  grid-auto-rows: minmax(25px, auto);
`;

const GridWrapper = styled.div`
  display: grid;
  position: relative;
  color: white;
`;

export const Home = (props) => <GridWrapper></GridWrapper>;
