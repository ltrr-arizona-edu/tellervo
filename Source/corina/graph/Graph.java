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
    public double scale=1.0;

    /** Create a graph from a Graphable object.
	@param g the Graphable object */
    public Graph(Graphable g) {
	// copy graph
	graph = g;

	// default scale
	scale = g.getScale();
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
		public double getScale() {
		    return 1.0;
		}
		public String toString() {
		    return nn;
		}
	    };
    }

    private final static double SCALE = 1.25; // meaning, 25%, but (fixme) i should just say that
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
	    return null;

	// filename
	String filename = (String) ((Sample) graph).meta.get("filename");

	// crunch together an XML tag
	return "<graph scale=\"" + scale + "\" " +
	              "xoffset=\"" + xoffset + "\" " +
	              "yoffset=\"" + yoffset + "\">" + filename +
	       "</graph>";
    }

    // (graph is an interface, not an abstract class, so it can't be
    // moved any higher.)
    public Range getRange() {
	return new Range(graph.getStart().add(xoffset),
			 graph.getData().size());
    }
}
