//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.graph;

import corina.Year;
import corina.Range;
import corina.Sample;

import java.util.List;
import java.awt.Color;

/*
  the purpose of this class is:
  - group together a Graphable with its properties (offsets, scale, etc.)
  - (this makes drag-n-drop *much* easier to implement)
  - allows adding other properties without much work, like flip-vertical for skeleton plots
  - moving towards abstract DrawingAgent -> (StandardPlot, SkeletonPlot, BargraphPlot)
  - DrawingAgents will make printing nearly trivial

  for DP addicts, i believe it's a decorator.
*/

public class Graph {

    // should these members be public?  or private, with getters and
    // setters?  that way it could extend Observable, which would be
    // very cool.  [what did i mean by that?]

    // the thing-to-graph
    public Graphable graph;

    // offsets
    public int xoffset=0, yoffset=0;

    // scaling
    public float scale=1.0f;

    /** Create a graph from a Graphable object.
        @param g the Graphable object */
    public Graph(Graphable g) {
        // copy graph
        graph = g;

        // default scale
        scale = g.getScale();
        
        // save name...
        graphName = g.toString();
    }

    // an arbitrary List of Numbers, starting at a Year.  (used for graphing density of masters.)
    public Graph(List l, Year y, String n) {
        // ack, glue -- there's a better way, right?  fixme.
        final List ll = l;
        final Year yy = y;
        final String nn = n;

        // create graph
        graph = new Graphable() {
            public List getData() {
                    return ll;
                }
            public Year getStart() {
                    return yy;
                }
            public float getScale() {
                    return 1.0f;
                }
            public String toString() {
                    return nn;
                }
        };
        
        // default scale
        scale = graph.getScale();
        
        // save name...
        graphName = graph.toString();        
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
        String filename = (String) ((Sample) graph).meta.get("filename");

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
                         graph.getData().size());
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
}
