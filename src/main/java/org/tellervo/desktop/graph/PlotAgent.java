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

import java.lang.ref.WeakReference;

/**
 * A list of plotting agents!
 * 
 * @author Lucas Madar
 */

public enum PlotAgent {
	STANDARD(StandardPlot.class, Axis.AXIS_STANDARD),
	SEMILOG(SemilogPlot.class, Axis.AXIS_LOG),
	TOOTHED(DensityPlot.class, Axis.AXIS_STANDARD),
	SKELETON(SkeletonPlot.class, Axis.AXIS_STANDARD);
	
	/** The class of this plotter */
	private final Class<? extends TellervoGraphPlotter> plotterType;
	/** The type of axis associated with this plotter */
	private final int axisType;
	/** A reference to the plotter */
	private WeakReference<TellervoGraphPlotter> plotter;
	
	private PlotAgent(Class<? extends TellervoGraphPlotter> type, int axisType) {
		this.plotterType = type;
		this.axisType = axisType;
	}
	
	/**
	 * Get an instance of this plotter to plot with
	 * @return A TellervoGraphPlotter, never null
	 */
	public final TellervoGraphPlotter getPlotter() {
		TellervoGraphPlotter realPlotter;
		
		if(plotter == null || (realPlotter = plotter.get()) == null) {
			try {
				realPlotter = plotterType.newInstance();
				plotter = new WeakReference<TellervoGraphPlotter>(realPlotter);
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
	public String getI18nTag() 
	{		
		if(this.toString().equals(PlotAgent.SEMILOG.toString())) return "graph.agent_semilog";
		else if (this.toString().equals(PlotAgent.STANDARD.toString()))	return "graph.agent_standard";
		else if (this.toString().equals(PlotAgent.TOOTHED.toString())) return "graph.agent_toothed";
		else if (this.toString().equals(PlotAgent.SKELETON.toString())) return "graph.agent_skeleton";

		else return null;
		
	}
}
