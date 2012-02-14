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
	private GraphInfo info;
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
	
	private void createGraphActions() throws IntrospectionException {
		showVerticalAxis = new ToggleableBoundAction<GraphInfo>("graph.verticalAxis", "graph.verticalAxis", 
				info, GraphInfo.class, GraphInfo.SHOW_VERT_AXIS_PROPERTY, "vaxisshow.png", 
				Builder.ICONS, 22);

		showGridlines = new ToggleableBoundAction<GraphInfo>("graph.grid", "graph.grid",
				info, GraphInfo.class, GraphInfo.SHOW_GRAPH_PAPER_PROPERTY, "showgrid.png", 
				Builder.ICONS, 22);

		showBaselines = new ToggleableBoundAction<GraphInfo>("graph.baselines", "graph.baselines", 
				info, GraphInfo.class, GraphInfo.SHOW_BASELINES_PROPERTY);

		showComponentNames = new ToggleableBoundAction<GraphInfo>("graph.labels", "graph.labels",
				info, GraphInfo.class, GraphInfo.SHOW_GRAPH_NAMES_PROPERTY, "label.png", 
				Builder.ICONS, 22);

		showHundredPercentLines = new ToggleableBoundAction<GraphInfo>("graph.100percentLines", "graph.100percentLines", 
				info, GraphInfo.class, GraphInfo.SHOW_HUNDREDPERCENTLINES_PROPERTY);
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
	protected TellervoAction spreadB25;
	protected TellervoAction spreadB50;
	protected TellervoAction spreadB100;
	protected TellervoAction spreadB200;
	
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
		
		spreadB25 = new TellervoAction("graph.units25") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(25);
			}
		};
		spreadB50 = new TellervoAction("graph.units50") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(50);
			}
		};
		spreadB100 = new TellervoAction("graph.units100") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(100);
			}
		};
		spreadB200 = new TellervoAction("graph.units200") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(200);
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
				System.out.println("Zooming in on vertical scale");
				int height = info.getTenUnitHeight();			
				float fheight = ((float) height) * 1.25f;
				int newHeight = (height == (int) fheight) ? height + 1 : (int) fheight;
				
				System.out.println("Int   height = "+height);
				System.out.println("Float height = "+fheight);
				System.out.println("Setting height to = "+newHeight);
				
				info.setTenUnitHeight(newHeight);
			}
		};
		zoomOut = new TellervoAction("graph.vzoom_out", "vaxiszoomout.png", Builder.ICONS, 22){
			public void actionPerformed(ActionEvent e) {
				System.out.println("Zooming out on vertical scale");

				int height = info.getTenUnitHeight();
				float fheight = ((float) height) / 1.25f;
				int newHeight = (height == (int) fheight) ? height - 1 : (int) fheight;
				if(newHeight==0) newHeight=1;
				
				System.out.println("Int   height = "+height);
				System.out.println("Float height = "+fheight);
				System.out.println("Setting height to = "+newHeight);
				
				info.setTenUnitHeight(newHeight);
			}
		};
		zoomReset = new TellervoAction("graph.zoom_reset"){
			public void actionPerformed(ActionEvent e) {
				// reload pref
				info.setTenUnitHeight(GraphPrefs.TENUNIT_HEIGHT.get());
			}
		};
		

	}

	protected TellervoAction[] plotTypes;
	
	@SuppressWarnings("serial")
	private void createAgentActions() {
		PlotAgent[] agents = PlotAgent.values();
		PlotAgent defaultAgent = PlotAgent.getDefault();
		
		// create an array of actions for the agents
		plotTypes = new TellervoAction[agents.length];
		
		ToggleableActionGroup agentGroup = new ToggleableActionGroup();

		for(int i = 0; i < agents.length; i++) {
			final PlotAgent agent = agents[i];

			// on action, set the plot agent and update/redraw the graph
			ToggleableAction action = new ToggleableAction(agent.getI18nTag(), (agent == defaultAgent)) {
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
