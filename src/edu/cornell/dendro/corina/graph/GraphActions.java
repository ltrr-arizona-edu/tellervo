package edu.cornell.dendro.corina.graph;

import java.awt.event.ActionEvent;

import edu.cornell.dendro.corina.ui.CorinaAction;
import edu.cornell.dendro.corina.ui.ToggleableAction;
import edu.cornell.dendro.corina.ui.ToggleableActionGroup;

/**
 * Implements all the actions that the view menu and graph toolbar would use
 * 
 * @author Lucas Madar
 */
public class GraphActions {
	private GrapherPanel graph;
	private GraphElementsPanel elements;
	private GraphController controller;
	private PlotAgents agents;
		
	public GraphActions(GrapherPanel graph, GraphElementsPanel elements, GraphController controller, PlotAgents agents) {
		this.graph = graph;
		this.elements = elements;
		this.controller = controller;
		this.agents = agents;
		
		if(graph != null)
			createGraphActions();
		
		if(elements != null)
			createElementActions();

		if(controller != null)
			createControllerActions();
		
		if(agents != null)
			createAgentActions();
	}
	
	protected ToggleableAction showVerticalAxis;
	protected ToggleableAction showGridlines;
	protected ToggleableAction showBaselines;
	protected ToggleableAction showHundredPercentLines;
	protected ToggleableAction showComponentNames;
	
	@SuppressWarnings("serial")
	private void createGraphActions() {
		showVerticalAxis = new ToggleableAction("corina.graph.vertical-axis", true, 
				"vert_show", "vert_hide", "axisshow.png") {
			public void togglePerformed(ActionEvent e, Boolean value) {
				graph.setAxisVisible(value);
			}
		};

		showGridlines = new ToggleableAction("corina.graph.graphpaper", true,
				"grid_show", "grid_hide", "showgrid.png") {
			public void togglePerformed(ActionEvent e, Boolean value) {
				graph.setGraphPaperVisible(value);
			}
		};

		showBaselines = new ToggleableAction("corina.graph.baselines", true,
				"base_hide", "base_show", null) {
			public void togglePerformed(ActionEvent e, Boolean value) {
				graph.setBaselinesVisible(value);
			}
		};

		showComponentNames = new ToggleableAction("corina.graph.componentnames", true, 
				"compn_show", "compn_hide", "label.png") {
			public void togglePerformed(ActionEvent e, Boolean value) {
				graph.setComponentNamesVisible(value);
			}
		};
		
		showHundredPercentLines = new ToggleableAction("corina.graph.hundredpercentlines", false, 
				"compn_show", "compn_hide", "label.png") {
			public void togglePerformed(ActionEvent e, Boolean value) {
				graph.setHundredpercentlinesVisible(value);
			}
		};
	}
	
	protected ToggleableAction showElementsPanel;
	
	@SuppressWarnings("serial")
	private void createElementActions() {
		showElementsPanel = new ToggleableAction("view_elements", false, "legend.png") {
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
	
	protected CorinaAction zoomIn;
	protected CorinaAction zoomOut;
	protected CorinaAction zoomReset;
	
	@SuppressWarnings("serial")
	private void createControllerActions() {
		squeezeVertically = new CorinaAction("baselines_align", "squeezevertically.png", "Icons") {
			public void actionPerformed(ActionEvent e) {
				controller.squeezeTogether();
			}		
		};
		
		spreadB25 = new CorinaAction("units25") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(25);
			}
		};
		spreadB50 = new CorinaAction("units50") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(50);
			}
		};
		spreadB100 = new CorinaAction("units100") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(100);
			}
		};
		spreadB200 = new CorinaAction("units200") {
			public void actionPerformed(ActionEvent e) {
				controller.spreadOut(200);
			}
		};
		
		squishBaselines = new CorinaAction("baselines_squish") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
			}
		};

		fitHorizontally = new CorinaAction("fit_horiz", "fitcharthoriz.png", "Icons") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.scaleToFitWidth();
			}
		};

		fitBoth = new CorinaAction("fit_both") {
			public void actionPerformed(ActionEvent e) {
				controller.squishTogether();
				controller.scaleToFitWidth();
				controller.scaleToFitHeight();
			}
		};
		
		zoomOut = new CorinaAction("escale_halve", "axiszoomout.png", "Icons"){
			public void actionPerformed(ActionEvent e) {
				controller.halveScale();
			}
		};
		zoomIn = new CorinaAction("escale_double", "axiszoomin.png", "Icons"){
			public void actionPerformed(ActionEvent e) {
				controller.doubleScale();
			}
		};
		zoomReset = new CorinaAction("escale_reset"){
			public void actionPerformed(ActionEvent e) {
				controller.resetScaling();
			}
		};
	}

	protected CorinaAction[] plotTypes;
	
	@SuppressWarnings("serial")
	private void createAgentActions() {
		String[] agentnames = agents.getAgents();
		
		plotTypes = new CorinaAction[agentnames.length];
		ToggleableActionGroup agentGroup = new ToggleableActionGroup();

		for(int i = 0; i < agentnames.length; i++) {
			final int glue = i;
			ToggleableAction action = new ToggleableAction(agentnames[i], agents.isDefault(i)) {
				public void togglePerformed(ActionEvent ae, Boolean value) {
					agents.setAgent(glue);
					graph.update();
				}
			};
			
			agentGroup.add(action);
			plotTypes[i] = action;
		}
	}
}
