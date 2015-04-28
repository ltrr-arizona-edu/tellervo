/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.graph;

 import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;

import javax.swing.Action;

import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.TellervoAction;
import org.tellervo.desktop.ui.ToggleableAction;
import org.tellervo.desktop.ui.ToggleableActionGroup;
import org.tellervo.desktop.ui.ToggleableBoundAction;


/**
 * Implements all the actions that the view menu and graph toolbar would use
 * 
 * @author Lucas Madar
 */
public class GraphActions {
	private GrapherPanel graph;
	private GraphSettings info;
	private GraphElementsPanel elements;
	private GraphController controller;
		
	public GraphActions(GrapherPanel graph, GraphElementsPanel elements, GraphController controller) {
		this.graph = graph;
		this.elements = elements;
		this.controller = controller;
		
		if(graph != null) {
			this.info = graph.getGraphInfo();
			try {
				createGraphActions();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalStateException("Broken graphinfo structure?");
			}
		}
		else
			throw new IllegalArgumentException("Must specify a graph and graphInfo!");
		
		if(elements != null)
			createElementActions();

		if(controller != null)
			createControllerActions();
		
		createAgentActions();
	}
	
	public boolean hasElements() {
		return elements != null;
	}
	
	public boolean hasController() {
		return controller != null;
	}
		
	protected ToggleableAction showVerticalAxis;
	protected ToggleableAction showGridlines;
	protected ToggleableAction showBaselines;
	protected ToggleableAction showHundredPercentLines;
	protected ToggleableAction showComponentNames;
	protected ToggleableAction showRemarks;
	
	private void createGraphActions() throws IntrospectionException {
		showVerticalAxis = new ToggleableBoundAction<GraphSettings>("graph.verticalAxis", "graph.verticalAxis", 
				info, GraphSettings.class, GraphSettings.SHOW_VERT_AXIS_PROPERTY, "vaxisshow.png", 
				Builder.ICONS, 22);

		showGridlines = new ToggleableBoundAction<GraphSettings>("graph.grid", "graph.grid",
				info, GraphSettings.class, GraphSettings.SHOW_GRAPH_PAPER_PROPERTY, "showgrid.png", 
				Builder.ICONS, 22);

		showBaselines = new ToggleableBoundAction<GraphSettings>("graph.baselines", "graph.baselines", 
				info, GraphSettings.class, GraphSettings.SHOW_BASELINES_PROPERTY);

		showComponentNames = new ToggleableBoundAction<GraphSettings>("graph.labels", "graph.labels",
				info, GraphSettings.class, GraphSettings.SHOW_GRAPH_NAMES_PROPERTY, "label.png", 
				Builder.ICONS, 22);
		
		showRemarks = new ToggleableBoundAction<GraphSettings>("graph.remarks", "graph.remarks",
				info, GraphSettings.class, GraphSettings.SHOW_GRAPH_REMARKS, "note.png", 
				Builder.ICONS, 22);

		showHundredPercentLines = new ToggleableBoundAction<GraphSettings>("graph.100percentLines", "graph.100percentLines", 
				info, GraphSettings.class, GraphSettings.SHOW_HUNDREDPERCENTLINES_PROPERTY);
	}
	
	protected ToggleableAction showElementsPanel;
	
	@SuppressWarnings("serial")
	private void createElementActions() {
		showElementsPanel = new ToggleableAction("graph.view_elements", false, "legend.png", 
				Builder.ICONS, 22) {
			public void togglePerformed(ActionEvent e, Boolean value) {
				elements.setVisible(value);
			}
		};		
	}

	protected TellervoAction squeezeVertically;
	protected TellervoAction spreadB250;
	protected TellervoAction spreadB500;
	protected TellervoAction spreadB1000;
	protected TellervoAction spreadB2000;
	
	protected TellervoAction squishBaselines;
	protected TellervoAction fitHorizontally;
	protected TellervoAction fitBoth;
	protected TellervoAction zoomInHorizontally;
	protected TellervoAction zoomOutHorizontally;
	
	protected TellervoAction scaleUp;
	protected TellervoAction scaleDown;
	protected TellervoAction scaleReset;
	
	protected TellervoAction zoomIn;
	protected TellervoAction zoomOut;
	protected TellervoAction zoomReset;
	
	protected TellervoAction setToLogScale;
	protected TellervoAction setToNormalScale;
	
	
	@SuppressWarnings("serial")
	private void createControllerActions() {
		squeezeVertically = new TellervoAction("graph.baselines_align", "squeezevertically.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squeezeTogether();
			}		
		};
		
		spreadB250 = new TellervoAction("graph.units250") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(250);
			}
		};
		spreadB500 = new TellervoAction("graph.units500") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(500);
			}
		};
		spreadB1000 = new TellervoAction("graph.units1000") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(1000);
			}
		};
		spreadB2000 = new TellervoAction("graph.units2000") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(2000);
			}
		};
		
		squishBaselines = new TellervoAction("graph.baselines_squish") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
			}
		};

		fitHorizontally = new TellervoAction("graph.fit_horiz", "fitcharthoriz.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.scaleToFitWidth();
			}
		};

		fitBoth = new TellervoAction("graph.fit_both") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.scaleToFitWidth();
				controller.scaleToFitHeight();
			}
		};
		
		scaleDown = new TellervoAction("graph.escale_halve"){
			public void actionPerformed(ActionEvent e) {
				controller.halveScale();
			}
		};
		scaleUp = new TellervoAction("graph.escale_double"){
			public void actionPerformed(ActionEvent e) {
				controller.doubleScale();
			}
		};
		scaleReset = new TellervoAction("graph.escale_reset"){
			public void actionPerformed(ActionEvent e) {
				controller.resetScaling();
			}
		};
		
		zoomInHorizontally = new TellervoAction("graph.hzoom_in", "haxiszoomin.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.zoomInHorizontal();
			}
		};		
		
		zoomOutHorizontally = new TellervoAction("graph.hzoom_out", "haxiszoomout.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.zoomOutHorizontal();
			}
		};	
		
		zoomIn = new TellervoAction("graph.vzoom_in", "vaxiszoomin.png", Builder.ICONS, 22){
			public void actionPerformed(ActionEvent e) {				
				info.increaseHundredUnitHeight();
			}
		};
		zoomOut = new TellervoAction("graph.vzoom_out", "vaxiszoomout.png", Builder.ICONS, 22){
			public void actionPerformed(ActionEvent e) {				
				info.decreaseHundredUnitHeight();
			}
		};
		zoomReset = new TellervoAction("graph.zoom_reset"){
			public void actionPerformed(ActionEvent e) {
				info.resetHundredUnitHeight();
			}
		};
		

	}

	protected TellervoAction[] plotTypes;
	
	@SuppressWarnings("serial")
	private void createAgentActions() {
		PlotAgent[] agents = PlotAgent.values();
		
		
		// create an array of actions for the agents
		plotTypes = new TellervoAction[agents.length];
		
		ToggleableActionGroup agentGroup = new ToggleableActionGroup();

		for(int i = 0; i < agents.length; i++) {
			final PlotAgent agent = agents[i];

			// on action, set the plot agent and update/redraw the graph
			TellervoAction action = new ToggleableAction(agent.getI18nTag(), (agent == graph.getPlotAgent()), Builder.getIcon(agent.getI18nTag()+".png", 22)) {
				public void togglePerformed(ActionEvent ae, Boolean value) {
					graph.setPlotAgent(agent);
					graph.update();
				}
			};
			
			agentGroup.add(action);
			plotTypes[i] = action;
		}
	}
}
