package edu.cornell.dendro.corina.graph;

 import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.CorinaAction;
import edu.cornell.dendro.corina.ui.ToggleableAction;
import edu.cornell.dendro.corina.ui.ToggleableBoundAction;
import edu.cornell.dendro.corina.ui.ToggleableActionGroup;

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
	
	public void setLabelsSelected(Boolean b){
		info.setShowGraphNames(b);
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

	protected CorinaAction squeezeVertically;
	protected CorinaAction spreadB25;
	protected CorinaAction spreadB50;
	protected CorinaAction spreadB100;
	protected CorinaAction spreadB200;
	
	protected CorinaAction squishBaselines;
	protected CorinaAction fitHorizontally;
	protected CorinaAction fitBoth;
	protected CorinaAction zoomInHorizontally;
	protected CorinaAction zoomOutHorizontally;
	
	protected CorinaAction scaleUp;
	protected CorinaAction scaleDown;
	protected CorinaAction scaleReset;
	
	protected CorinaAction zoomIn;
	protected CorinaAction zoomOut;
	protected CorinaAction zoomReset;
	
	protected CorinaAction setToLogScale;
	protected CorinaAction setToNormalScale;
	
	
	@SuppressWarnings("serial")
	private void createControllerActions() {
		squeezeVertically = new CorinaAction("graph.baselines_align", "squeezevertically.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squeezeTogether();
			}		
		};
		
		spreadB25 = new CorinaAction("graph.units25") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(25);
			}
		};
		spreadB50 = new CorinaAction("graph.units50") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(50);
			}
		};
		spreadB100 = new CorinaAction("graph.units100") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(100);
			}
		};
		spreadB200 = new CorinaAction("graph.units200") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(200);
			}
		};
		
		squishBaselines = new CorinaAction("graph.baselines_squish") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
			}
		};

		fitHorizontally = new CorinaAction("graph.fit_horiz", "fitcharthoriz.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.scaleToFitWidth();
			}
		};

		fitBoth = new CorinaAction("graph.fit_both") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.scaleToFitWidth();
				controller.scaleToFitHeight();
			}
		};
		
		scaleDown = new CorinaAction("graph.escale_halve"){
			public void actionPerformed(ActionEvent e) {
				controller.halveScale();
			}
		};
		scaleUp = new CorinaAction("graph.escale_double"){
			public void actionPerformed(ActionEvent e) {
				controller.doubleScale();
			}
		};
		scaleReset = new CorinaAction("graph.escale_reset"){
			public void actionPerformed(ActionEvent e) {
				controller.resetScaling();
			}
		};
		
		zoomInHorizontally = new CorinaAction("graph.hzoom_in", "haxiszoomin.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.zoomInHorizontal();
			}
		};		
		
		zoomOutHorizontally = new CorinaAction("graph.hzoom_out", "haxiszoomout.png", Builder.ICONS, 22) {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.zoomOutHorizontal();
			}
		};	
		
		zoomIn = new CorinaAction("graph.vzoom_in", "vaxiszoomin.png", Builder.ICONS, 22){
			public void actionPerformed(ActionEvent e) {
				int height = info.getTenUnitHeight();
				float fheight = ((float) height) * 1.25f;
				
				info.setTenUnitHeight((height == (int) fheight) ? height - 1 : (int) fheight);
			}
		};
		zoomOut = new CorinaAction("graph.vzoom_out", "vaxiszoomout.png", Builder.ICONS, 22){
			public void actionPerformed(ActionEvent e) {
				int height = info.getTenUnitHeight();
				float fheight = ((float) height) / 1.25f;
				
				info.setTenUnitHeight((height == (int) fheight) ? height + 1 : (int) fheight);
			}
		};
		zoomReset = new CorinaAction("graph.zoom_reset"){
			public void actionPerformed(ActionEvent e) {
				// reload pref
				info.setTenUnitHeight(GraphPrefs.TENUNIT_HEIGHT.get());
			}
		};
		

	}

	protected CorinaAction[] plotTypes;
	
	@SuppressWarnings("serial")
	private void createAgentActions() {
		PlotAgent[] agents = PlotAgent.values();
		PlotAgent defaultAgent = PlotAgent.getDefault();
		
		// create an array of actions for the agents
		plotTypes = new CorinaAction[agents.length];
		
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
