import { useState, useEffect } from "react";
import styled from "styled-components";
import DoubleArrow from '@material-ui/icons/DoubleArrow';

const Container = styled.div`
	display: flex;
	flex-direction: column;
	text-align: left;
`;

const AvailablePresetsZone = styled.div`
	display: flex;
	flex-flow: row wrap;
	flex: auto;
`;

const PipelineZone = styled.div`
	/*border: 1px solid red;*/
	display: flex;
	flex-direction: column;
	flex: auto;
	min-height: 50px;
	background-color: #111111;
	border-radius: 8px;
`;

const ZoneTitle = styled.p`
	min-height: 3px;
	border-bottom: 1px solid white
`;

const AvailablePreset = styled.div`
	/*border: 1px solid red;*/
	text-align: center;
	user-select: none;
	flex: 0 0 19%;
	background-color: #061223;
	box-shadow: 0px 3px 10px rgba(0,0,0,0.3);
	border-radius: 24px;
	padding: 1%;
	margin: 0.5%;
	&:hover {
		background-color: #21a7eb;
		cursor: pointer;
	}
`;

const SelectedPresetContentContainer = styled.div`
	display: flex;
	flex-direction: row;
	align-items: center;
	justify-content: center;
`;

const SelectedPreset = styled.div`
	/*border: 1px solid red;*/
	opacity: ${props => props.dragged ? 0 : props.draggedOver ? 50 : 100};
	transform: scale(${props => props.draggedOver ? 0.8 : 1.0});
	text-align: center;
	user-select: none;
	flex: auto;
	background-color: #333333;
	box-shadow: 0px 3px 6px rgba(0,0,0,0.2);
	border-radius: 6px;
	padding: 1%;
	margin: 1%;
	transition: all 0.1s ease;
	&:hover {
		background-color: #444444;
		cursor: pointer;
	}
`;

const SelectedPresetName = styled.div`
	flex-grow: 8;
`;

const RemoveInfo = styled.div`
	font-size: 50%;
	font-style: italic;
	color: red;
	opacity: ${props => props.hovered ? 100 : 0};
`;

const ArrowIconStyled = styled(DoubleArrow)`
	&& {
		font-size: 150%;
		margin-right: 5px;
		transform: rotate(90deg);
	}
`;

const SelectedPresetDraggable = (props) => {
	const [dragged,setDragged] = useState(false);
	const [droppedIdx,setDroppedIdx] = useState(props.targetPreset.idx);
	const [draggedOver,setDraggedOver] = useState(false);
	const [hovered,setHovered] = useState(false);
	
	const handleDrag = (e) => {
		setDragged(true);
		e.stopPropagation();
		e.preventDefault();
		let rect = e.nativeEvent.target.getBoundingClientRect();
		let posOrig = (rect.bottom + rect.top)*0.5;
		let posDragged = e.pageY;
		let idxOffset = Math.trunc((posDragged-posOrig)/(rect.bottom-rect.top)+0.5);
		let newIdx = Math.max(0,
			Math.min(props.presets.filter( p => p.active).length-1,
				props.targetPreset.idx + idxOffset
			)
		);
		setDroppedIdx( newIdx );
		console.log( droppedIdx );
		
		
	};
	
	const handleDragOver = e => {
		e.stopPropagation();
		e.preventDefault();
		setDraggedOver(true);
	};
	
	const handleDragEnd = e => {
		setDragged(false);
		e.stopPropagation();
		e.preventDefault();
		console.log("Dragend "+props.targetPreset.friendlyName);
		let oldIdx = props.targetPreset.idx;
		if (droppedIdx !== props.targetPreset.idx) {
			props.setPresets( props.presets.map( (p) => {
				if (p === props.targetPreset) {
					p.idx = droppedIdx;
				}
				return p;
			}));
		}
		props.setPresets(props.presets.map( p => {
			if (p !== props.targetPreset && p.idx === droppedIdx) {
				p.idx = oldIdx;
			}
			return p;
		}));
		setDroppedIdx(props.targetPreset.idx);
		setDraggedOver(false);
	};
	
	const handleDrop = e => {
		e.stopPropagation();
		e.preventDefault();
		setDraggedOver(false);
		console.log("Drop "+props.targetPreset.friendlyName);
	};
	const handleDragLeave = e => {
		e.stopPropagation();
		e.preventDefault();
		setDraggedOver(false);
	};
	
	return (
		<SelectedPreset 
		onMouseEnter={() => setHovered(true)}
		onMouseLeave={() => setHovered(false)}
		draggable droppable
		onClick={() => props.remove()}
		onDrag={handleDrag}
		onDragOver={handleDragOver}
		onDrop={handleDrop}
		onDragEnd={handleDragEnd}
		onDragLeave={handleDragLeave}
		dragged={dragged}
		draggedOver={draggedOver}>
			<SelectedPresetContentContainer>
				<ArrowIconStyled/>
				<SelectedPresetName>{props.name}</SelectedPresetName>
				<RemoveInfo hovered={hovered}>Click to remove</RemoveInfo>
			</SelectedPresetContentContainer>
		</SelectedPreset>
	);
};

export const PresetList = (props) => 
{
	const [presets,setPresets] = useState([{}]);

	useEffect ( async () => {
		const response = await fetch("https://app.rastered.io/getpresets");
		let json = await response.json();
		await json.map( (p,idx) => {
			p.active = false;
			p.idx = idx;
			return p;
		});
		await setPresets( json );
	},[]);
	
	const setActive = (preset,value) => {
		console.log(preset);
		let activePresetsAmount = presets.filter(p=>p.active).length;
		let idxActive = 0;
		setPresets(presets.map( p => { 
			if (p === preset) p.active = value;
			if (!p.active) p.idx = p.active-1;
			else {
				p.idx = idxActive;
				idxActive++;
			}
			return p;
		}));
	};
	
	console.log(presets);
	
	return (
		<Container>
		<ZoneTitle>AVAILABLE PRESETS</ZoneTitle>
		<AvailablePresetsZone>
			{ presets
				.filter(p => !p.active)
				.sort ( (p1,p2) => p1.friendlyName[0].localeCompare(p2.friendlyName[0]) )
				.map( p => 
				<AvailablePreset onClick={ e => setActive(p,true) }>
					{p.friendlyName}
				</AvailablePreset>
			) }
		</AvailablePresetsZone>
		<p/>
		<ZoneTitle>PIPELINE</ZoneTitle>
		<PipelineZone>
			{ presets
				.filter( preset => preset.active )
				.sort ( (p1,p2) => p1.idx - p2.idx )
				.map( preset => 
					<SelectedPresetDraggable
					remove={() => setActive(preset,false)}
					name={preset.friendlyName}
					presets={presets}
					setPresets={setPresets}
					targetPreset={preset}/>
			) }
		</PipelineZone>
		</Container>
	);
};
