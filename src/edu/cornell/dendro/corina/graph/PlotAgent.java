package edu.cornell.dendro.corina.graph;

import java.lang.ref.WeakReference;

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
		return GraphPrefs.PLOT_AGENT.get();
	}

	/**
	 * Set this as the default
	 */
	public void setDefault() {
		GraphPrefs.PLOT_AGENT.set(this);
	}
	
	/**
	 * Get the internationalization tag for this plot agent
	 * @return "agent_xxx"
	 */
	public String getI18nTag() {
		return "agent_" + this.toString().toLowerCase();
	}
}
