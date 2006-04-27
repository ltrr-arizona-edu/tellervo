/**
 * 
 */
package corina.graph;

import corina.core.App;

/**
 * @author Lucas Madar
 *
 */
public class PlotAgents {
	public PlotAgents() {
		String defAgentName = App.prefs.getPref("corina.graph.defaultagent", "corina.graph.StandardPlot"); 
		for(int i = 0; i < plotAgentInstance.length; i++) {
			if(plotAgentInstance[i].getClass().getName().equals(defAgentName)) {
				defPlotAgent = i;
				break;
			}
		}
	}
	
	private final String[] plotAgentName =   {
			"Standard Plot", 
			"Semi-Log Plot",
			"Toothed Plot"
			};
	
	private CorinaGraphPlotter[] plotAgentInstance = {
			new StandardPlot(),
			new SemilogPlot(),
			new DensityPlot()
	};
	
	// if we don't have a match (prefs are munged?), defeault to standard plot.
	private int defPlotAgent = 0;
	
	// this is the index in to the array above of plot agents for density plot.
	// perhaps we could 'auto-find' this, but it makes more sense at this point
	// to kludge it in.
	private final int densityPlotAgent = 2;

	// methods to modify agents. used by the graph window.
	public String[] getAgents() {
		return plotAgentName;
	}
	
	public boolean isDefault(int idx) {
		if(idx == defPlotAgent)
			return true;
		return false;
	}
	
	// sets the agent to the specified number
	public void setAgent(int idx) {
		defPlotAgent = idx;
		App.prefs.setPref("corina.graph.defaultagent", plotAgentInstance[idx].getClass().getName());
	}
	
	// methods for getting agents themselves.
	// used by the grapher panel
	public CorinaGraphPlotter acquireAgent(int idx) {
		return plotAgentInstance[idx];
	}

	public CorinaGraphPlotter acquireDefault() {
		return plotAgentInstance[defPlotAgent];
	}
	
	public CorinaGraphPlotter acquireDensity() {
		return plotAgentInstance[densityPlotAgent];
	}
}
