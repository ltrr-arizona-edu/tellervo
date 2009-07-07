package edu.cornell.dendro.corina.graph;

import java.lang.ref.WeakReference;

import edu.cornell.dendro.corina.core.App;

/**
 * A list of plotting agents!
 * 
 * @author Lucas Madar
 */

public enum PlotAgent {
	STANDARD(StandardPlot.class, Axis.AXIS_STANDARD),
	SEMILOG(SemilogPlot.class, Axis.AXIS_LOG),
	TOOTHED(DensityPlot.class, Axis.AXIS_STANDARD);
	
	/** The class of this plotter */
	private final Class<? extends CorinaGraphPlotter> plotterType;
	/** The type of axis associated with this plotter */
	private final int axisType;
	/** A reference to the plotter */
	private WeakReference<CorinaGraphPlotter> plotter;
	
	private PlotAgent(Class<? extends CorinaGraphPlotter> type, int axisType) {
		this.plotterType = type;
		this.axisType = axisType;
	}
	
	/**
	 * Get an instance of this plotter to plot with
	 * @return A CorinaGraphPlotter, never null
	 */
	public final CorinaGraphPlotter getPlotter() {
		CorinaGraphPlotter realPlotter;
		
		if(plotter == null || (realPlotter = plotter.get()) == null) {
			try {
				realPlotter = plotterType.newInstance();
				plotter = new WeakReference<CorinaGraphPlotter>(realPlotter);
			} catch (Exception e) {
				throw new RuntimeException("Can't instantiate plotter class for " + this);
			}
		}
		
		return realPlotter;
	}
	
	/** 
	 * Get the axis type associated with this plot 
	 * @return the axis type 
	 */
	public final int getAxisType() {
		return axisType;
	}
	
	/**
	 * Get the default Plot Agent type
	 * @return the default plot agent, usually "STANDARD"
	 */
	public static PlotAgent getDefault() {
		String defaultType = App.prefs.getPref("corina.graph.defaultagent", null);

		// no default, so go with standard
		if(defaultType == null)
			return STANDARD;
		
		// go with standard if anything fails
		try {
			return valueOf(defaultType);
		} catch (Exception e) {
			// remove the bad pref
			App.prefs.setPref("corina.graph.defaultagent", null);
			return STANDARD;
		}
	}

	/**
	 * Set this as the default
	 */
	public void setDefault() {
		App.prefs.setPref("corina.graph.defaultagent", this.toString());
	}
	
	/**
	 * Get the internationalization tag for this plot agent
	 * @return "agent_xxx"
	 */
	public String getI18nTag() {
		return "agent_" + this.toString().toLowerCase();
	}
}
