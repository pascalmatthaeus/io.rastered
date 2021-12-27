import React, {useState} from "react";
import styled from "styled-components";

const TopBarStyled = styled.div`
	height: 10vh;
	width: 100vw;
	z-index: 9;
	background-color: rgba(30,30,30,1.0);
	display: flex;
	flex-direction: row;
	align-items: center;
	justify-content: center;
	
`;

const Logo = styled.img`
	height: 8vh;
	transition: all 0.2s ease;
	padding-left: 10px;
	&:hover { 
		transform: rotate3d(0,1,0,1080deg);
	}
`;

const Slogan = styled.div`
	color: white;
	font-style: italic;
	font-weight: bold;
	padding-left: 10px;
	max-height: 100%;
	font-size: calc(8px + 0.25vw);
	text-transform: uppercase;
	@media (max-width: 768px) {
		max-width: 10vw;
	}
	@media (max-width: 468px), (max-height: 576px) {
		display: none;
	}
`;

const MenuIconStyled = styled.div`
	display: none;
	flex-direction: column;
	margin-right: 20px;
	margin-left: auto;
	z-index: 9;
	@media (max-width: 640px) {
		display: flex;
	}
`;

const Menu = styled.div`
	justify-content: center;
	align-items: center;
	margin-left: auto;
	flex: ${props => props.openMenu ? 'auto' : '0'};
	margin-right: 0px;
	width: ${props => props.openMenu ? '100vw' : '0px'};
	overflow: hidden;
	transition: all 0.75s ease;
	z-index: 11;
	display: flex;
	flex-direction: row;
	height: inherit; 
	&:hover {
		flex: auto;
	}
	@media (max-width: 240px) {
		flex-direction: column;
	}
`;

const MenuLink = styled.a`
	margin-left: 10px;
	margin-right: 10px;
	text-decoration: none;
	font-weight: bold;
	font-size: calc(8px + 1.25vw);
	color: white;
	&:hover {
		color: #6fccfc;
	}
`;

const MenuIconStroke = styled.div`
	margin: 2px 0px 2px 0px;
	width: 22px;
	height: 2px;
	border-radius: 3px;
	background-color: ${props => props.openMenu ? '#6fccfc' : 'white'};
`;

const LinkContainer = styled.div`
	display: flex;
	flex-direction: row;
	margin-right: auto;
	margin-left: 25px;
	@media (max-width: 768px) {
		margin-left: 20px;
	}
	@media (max-width: 640px) {
		display: none;
	}
`;

const Link = styled.a`
	text-decoration: none;
	font-weight: bold;
	color: white;
	margin-left: 12px;
	margin-right: 12px;
	@media (max-width: 768px) {
		margin-right: 10px;
	}
	&:hover {
		color: #6fccfc;
	}
`;

const Name = styled.a`
	font-size: calc(16px + 1vw);
	text-decoration: none;
	color: white;
	text-shadow: 5px 4px 0px rgba(0,0,0,1.0);
	font-weight: bold;
	transition: all 0.3s ease;
	&:hover {
		color: #6fccfc;
	}
`;

const MenuIcon = (props) =>
{
	return(
		<MenuIconStyled onMouseEnter={() => props.handleMouse(true)} onMouseLeave={() => props.handleMouse(false)} >
			<MenuIconStroke openMenu={props.openMenu}/>
			<MenuIconStroke openMenu={props.openMenu}/>
			<MenuIconStroke openMenu={props.openMenu}/>
		</MenuIconStyled>
	);
};

export const TopBar = (props) => {
	const [openMenu,setOpenMenu] = useState(false);
	
	return(
		<TopBarStyled>
			<Logo src="rio-logo.png"/>
			<Slogan openMenu={openMenu}>Image Processing made simple.</Slogan>
			<LinkContainer>
				<Link href="/">Home</Link>
				<Link href="/">About</Link>
				<Link href="/">Privacy</Link>
			</LinkContainer>
			<Menu openMenu={openMenu} onMouseEnter={() => setOpenMenu(true)} onMouseLeave={() => setOpenMenu(false)}>
				<MenuLink openMenu={openMenu} href="/">Home</MenuLink>
				<MenuLink openMenu={openMenu} href="/">About</MenuLink>
				<MenuLink openMenu={openMenu} href="/">Privacy</MenuLink>
			</Menu>
			<MenuIcon handleMouse={(value) => setOpenMenu(value)} openMenu={openMenu}/>
			{/*<Name href="https://rastered.io/">rastered.io</Name>*/}
		</TopBarStyled>
	);
};
