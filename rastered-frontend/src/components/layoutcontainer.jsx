import React from "react";
import styled from "styled-components";

export const LayoutContainer = styled.div`
	/*border: 3px solid yellow;*/
	overflow: hidden;
	display: grid;
	grid-template-rows: 10vh 90vh;
	grid-template-columns: 3vw 97vw;
	@media (max-width: 768px) {
		grid-template-columns: 100vw;
	}
`;
