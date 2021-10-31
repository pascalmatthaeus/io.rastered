import React from "react";
import styled from "styled-components";
const GridWrapper = styled.div`
  color: white;
  display: grid;
  grid-gap: 10px;
  margin-top: 1em;
  margin-left: 6em;
  margin-right: 6em;
  grid-template-columns: repeat(12, 1fr);
  grid-auto-rows: minmax(25px, auto);
`;
export const About = () => (
  <GridWrapper>
    <h2>About Page</h2>
    <p>Test1</p>
    <p>Test22</p>
    <p>Test3</p>
  </GridWrapper>
);
