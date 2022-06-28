/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.remarks.Remarks;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasValue;


/*
  the purpose of this class is:
  - group together a Graphable with its properties (offsets, scale, etc.)
  - (this makes drag-n-drop *much* easier to implement)
  - allows adding other properties without much work, like flip-vertical for skeleton plots
  - moving towards abstract DrawingAgent -> (StandardPlot, SkeletonPlot, BargraphPlot)
  - DrawingAgents will make printing nearly trivial

  for DP addicts, i believe it's a decorator.
*/

public final class Graph {
	private final static Logger log = LoggerFactory.getLogger(Graph.class);

	// the thing-to-graph
    public final Graphable graph;

    // offsets
    public int xoffset=0, yoffset=0;

    // scaling
    public float scale=1f;
    
    /** Can the graph agent be changed? */
    private boolean canChangeAgent = true;
    
    /** Can this graph be dragged? */
    private boolean draggable = true;

    /** Is this graph selection-highlighted? */
    private boolean highlighted = false;
    
    /** Create a graph from a Graphable object.
        @param g the Graphable object */
    public Graph(Graphable g) {
        // copy graph
        graph = g;

        // default scale
        scale = g.getScale();
        
        // save name...
        graphName = g.toString();
        
        if(graph instanceof DensityGraph) {
        	setAgent(PlotAgent.TOOTHED);
        	canChangeAgent = false;
        }
        else
        	setAgent(PlotAgent.getDefault());
    }
    
    // an arbitrary List of Numbers, starting at a Year.  (used for graphing density of masters.)
    public Graph(List<Integer> l, Year y, String n) {
        // create graph
        graph = new DensityGraph(l, y, n);
        
        // default scale
        scale = graph.getScale();
        
        // save name...
        graphName = graph.toString();
        
        setAgent(PlotAgent.TOOTHED);
        canChangeAgent = false;
    }

    // meaning, 25%, but (fixme) i should just say that
    private final static float SCALE = 1.25f;

    public void bigger() {
        scale *= SCALE;
    }
    public void smaller() {
        scale /= SCALE;
    }

    public void left() {
        xoffset--;
    }
    public void right() {
        xoffset++;
    }

    // why is slide(int), but left()/right()?  something's funny here, methinks.

    public void slide(int pixels) {
        yoffset += pixels;
    }

    // as "<graph scale=... xoffset=... yoffset=...>filename</graph>
    public String toXML() {
        // can't save indexes or other non-samples, yet
        if (!(graph instanceof Sample))
            return "<!-- not a sample (" + graph + ") -->"; // what to do?

        // filename
        String filename = (String) ((Sample) graph).getDisplayTitle();

        // crunch together an XML tag
        return "<graph scale=\"" + scale + "\" " +
            "xoffset=\"" + xoffset + "\" " +
            "yoffset=\"" + yoffset + "\">" + filename +
            "</graph>";
    }

    // (graph is an interface, not an abstract class, so it can't be
    // moved any higher.) -- not true any longer, but this still seems correct.
    public Range getRange() {
        return new Range(graph.getStart().add(xoffset),
                         graph.getRingWidthData().size());
    }
    // this method smells funny to me now.  how's it used?
    
    // the following data is internal to the grapher
    // it is used entirely to maintain visual information
    private Color mainColor;
    private Color printerColor;
    
    public void setColor(Color gcolor, Color pcolor) {
    	mainColor = gcolor;
    	printerColor = pcolor;
    }
    
    public void setColor(Color c, boolean isPrinting) {
    	if(isPrinting)
    		printerColor = c;
    	else
    		mainColor = c;
    }
    
    public Color getColor(boolean isPrinting) {
    	if(isPrinting)
    		return printerColor;
    	return mainColor;
    }
    
    public boolean hasColor() {
    	return mainColor != null;
    }
    
    // default line thickness
    int lineThickness = 1;
    int printerlineThickness = 1;
    
    public void setThickness(int thickness, boolean isPrinting) {
    	if(isPrinting)
    		printerlineThickness = thickness;
    	else
    		lineThickness = thickness;
    }
    
    public int getThickness(boolean isPrinting) {
    	if(isPrinting) {
    		return printerlineThickness;
    	}
    	return lineThickness;
    }
    
    // displayed name...
    private String graphName;
    public String getGraphName() { return graphName; }
    public void setGraphName(String name) { graphName = name; }
    
    /** The Plot Agent that holds the plotter that draws this graph */
    private PlotAgent graphAgent;
    
    /**
     * Set the plotting agent
     * @param agent
     */
    public void setAgent(PlotAgent agent) { 
    	if(canChangeAgent)
    		graphAgent = agent; 
    }

    /**
     * Set whether or not the user can click on this graph and drag it
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
    	this.draggable = draggable;
    }
    
    public boolean isDraggable() {
    	return draggable;
    }

    /**
     * Set the highlighted state of this graph
     * @param highlighted
     */
    public void setHighlighted(boolean highlighted) {
    	this.highlighted = highlighted;
    }
    
    /**
     * Is this graph mouseover highlighted
     * @return true on highlgiht
     */
    public boolean isHighlighted() {
    	return highlighted;
    }
    
    /**
     * Get the plotter
     * @return The plotter used to graph this
     */
    public TellervoGraphPlotter getPlotter() {
    	return graphAgent.getPlotter();
    }
    
    public void draw(GraphSettings gInfo, Graphics2D g2, int bottom, int thickness, int xscroll) {
    	graphAgent.getPlotter().draw(gInfo, g2, bottom, this, thickness, xscroll);
    }
    
    /**
     * Helper function to plot remark icons on a chart.  
     * 
     * @param g2      - Graphics2D to plot on 
     * @param remarks - list of remarks to plot
     * @param xcoord  - xcoord of the data point (start point for the first remark)
     * @param ycoord  - ycoord of the data point (start point for the first remark)
     */
    public static void drawRemarkIcons(Graphics2D g2, GraphSettings info, List<TridasRemark> remarks, TridasValue value, int xcoord, int ycoord)
    {
    	drawRemarkIcons(g2, info, remarks, value, xcoord, ycoord, false);
    }
    
    public static void drawRemarkIcons(Graphics2D g2, GraphSettings info, List<TridasRemark> remarks, TridasValue value, int xcoord, int ycoord, Boolean stackBelow)
    {
		xcoord = xcoord-8;
		
		if(stackBelow)
		{
			ycoord = ycoord+3;
		}
		else
		{
			ycoord = ycoord-22;
		}
		
		if(!info.isShowGraphRemarks()) return;
    	
    	if(remarks.size()>0)
		{
			log.debug(remarks.size()+" remarks found");
			int remarkoffset = 0;
			for(TridasRemark remark : remarks)
			{
				if(Remarks.isRemarkVisible(remark, value)==false) continue;
				
				
				Image img = null;
				
				if(remark.isSetNormalTridas()) 
				{
					//log.debug("Remark is a TRiDaS remark: "+ Remarks.getTridasRemarkIcons().get(remark.getNormalTridas()));						
					img = Builder.getIconAsImage(Remarks.getTridasRemarkIcons().get(remark.getNormalTridas()), 16);
				}
				else if (remark.isSetNormalStd())
				{
					//log.debug("Remark is a Tellervo remark: "+Remarks.getTellervoRemarkIcons().get(remark.getNormal()));
					img = Builder.getIconAsImage(Remarks.getTellervoRemarkIcons().get(remark.getNormal()), 16);
				}
				else
				{
					//log.debug("Remark is a free text remark");
					img = Builder.getIconAsImage("user.png", 16);
				}
				
				if(img==null)
				{
					img = Builder.getIconAsImage("missingicon.png", 16);
				}
				
				
				g2.drawImage(img,xcoord, ycoord-remarkoffset, null);
				
				if(stackBelow)
				{
					remarkoffset = remarkoffset - 18;
				}
				else
				{
					remarkoffset = remarkoffset + 18;
				}
			}
		}
    }
}
