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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import javax.swing.JPanel;

import corina.Range;
import corina.Sample;
import corina.Year;
import corina.core.App;
import corina.index.Index;

public class StandardPlot {

	// these i get from sys props
	//private boolean _baselines;
	//private int _yearsize;

	private boolean _dottedIndexes;

	// these get passed
	private Range _bounds;
	private GraphInfo _graphinfo;

	// nb: this gets recreated every from refreshFromPreferences()
	public StandardPlot(Range bounds, GraphInfo graphinfo) {
		// read sys props
		update();

		// copy parms
		_bounds = bounds;
		_graphinfo = graphinfo;
	}

	// PERF: too many new's!  can i memoize this?  or just use constants for the 6(?) strokes i use?
	private BasicStroke makeStroke(float width, boolean dotted) {
		if (dotted)
			return new BasicStroke(width, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL, 10f, new float[] { 8f }, 0f);
		else
			return new BasicStroke(width, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL);
	}

	// TESTING: perf
	private Rectangle tempRect = new Rectangle();
	
	public void draw(Graphics2D g2, int bottom, Graph g, int thickness, int xscroll) {
		// cache yearsize, we use this a lot
		int yearSize = _graphinfo.getYearSize(); // the size of a year, in pixels
		float unitScale = (float) yearSize / 10.0f; // the size of 1 "unit" in pixels.
												   // using another var in case these become independent
		
		// set pen
		boolean dotted = (_dottedIndexes && (g.graph instanceof Index));
		g2.setStroke(makeStroke(thickness, dotted));

		// left/right
		int l = g2.getClipBounds().x;
		int r = l + g2.getClipBounds().width;

		// baseline
		if (_graphinfo.drawBaselines()) {
			int y = bottom - (int) (g.yoffset * unitScale);
			g2.drawLine(xscroll, y, xscroll + 10 * yearSize, y); // 1 decade wide -- ok?
		}

		// no data?  stop.
		if (g.graph.getData().isEmpty())
			return;

		// compare g.getClipBounds() to [x,0]..[x+yearSize*data.size(),bottom]
		tempRect.x = yearSize
				* (g.graph.getStart().diff(_bounds.getStart()) + g.xoffset); // REDUNDANT! see x later
		tempRect.y = 0; // - g.yoffset, IF you're sure there are no negative values (but there are)
		tempRect.width = yearSize * (g.graph.getData().size() - 1);
		tempRect.height = bottom;
		// TODO: compute top/bottom as min/max?
		// REFACTOR: will this be obsolete with the start/end stuff below?
		if (!tempRect.intersects(g2.getClipBounds())) {
			// skip this graph, it's off the screen
			return;
		}

		// compute sapwood
		int sapwoodIndex, sapwoodCount = 0, unmeasPost = 0;
		if (g.graph instanceof Sample) {
			Sample sample = (Sample) g.graph;
			// PERF: isOak(), counting sapwood, and counting unmeas_post are expensive!
			// -- do them once, and store that info in the Graph object, perhaps.
			if (sample.isOak()) {
				try {
					if (sample.meta.containsKey("sapwood"))
						sapwoodCount = ((Integer) sample.meta.get("sapwood"))
								.intValue();
					if (sample.meta.containsKey("unmeas_post"))
						unmeasPost = ((Integer) sample.meta.get("unmeas_post"))
								.intValue();
				} catch (ClassCastException cce) {
					// we've already warned the user before, ignore it now.
				}
			}
		}
		sapwoodIndex = g.graph.getData().size() - sapwoodCount + unmeasPost + 1;

		// my path
		GeneralPath p = new GeneralPath();

		// x-position
		int x = yearSize
				* (g.graph.getStart().diff(_bounds.getStart()) + g.xoffset);

		// move to the first point -- THIS IS NOT REALLY A SPECIAL CASE!
		int value;
		try {
			value = ((Number) g.graph.getData().get(0)).intValue();
		} catch (ClassCastException cce) {
			value = 0; // BAD!  instead: (1) just continue now, and (2) NEXT point is a move-to.
		}
		p.moveTo(x, bottom - (int) (value * g.scale * unitScale) - (int) (g.yoffset * unitScale));

		/*
		 -- i really want to start at year max(graph.start, bounds.start)
		 -- there are 3 things going on:
		 ---- x is the pixel position
		 ---- i is the index into data[]
		 ---- y is the year
		 -- y isn't updated each time through the loop, so that's not too bad
		 -- but: starting y is easy to compute; from that, i and x are easy
		 */

		// connect the lines through the rest of the graph
		int n = g.graph.getData().size(); // THIS is the third time it's called; why not use it above?
		for (int i = 1; i < n; i++) {
			// new x-position for this point
			x += yearSize;

			// if we're past the end, draw what we've got, and say goodbye
			// (go +_yearsize so the line going off the screen is visible)
			if (x > r + yearSize) {
				break;
			}

			// sapwood?  draw what we've got, and start a new (thicker) path
			if (i == sapwoodIndex) {
				g2.draw(p);
				g2.setStroke(makeStroke(2 * thickness, false));
				p = new GeneralPath();
				p.moveTo(yearSize * (i - 1 + g.graph.getStart().diff(_bounds.getStart()) + g.xoffset),
						 bottom - (int) (value * g.scale * unitScale) - (int) (g.yoffset * unitScale));
			}

			// y-position for this point
			try {
				value = ((Number) g.graph.getData().get(i)).intValue();
			} catch (ClassCastException cce) {
				value = 0; // e.g., if it's being edited, it's still a string
				// BAD!  instead: (1) draw what i've got so far, and (2) NEXT point is a move-to.
				// -- try to parse String as an integer?
			}
			int y = bottom - (int) (value * g.scale * unitScale) - (int) (g.yoffset * unitScale);

			// if we're not where this sample starts, don't bother drawing yet
			if (x < l - yearSize) {
				p.moveTo(x, y);
				continue;
			}

			// if MR, draw a vertical line -- use Sample.MR, for now
			if (value <= Sample.MR && g.graph instanceof Sample)
				g2.drawLine(x, y - 20, x, y + 20);

			// draw a line to this point
			p.lineTo(x, y);
		}

		// draw it!
		g2.draw(p);
	}

	// if it's within this many pixels, it's considered a hit (see "correct?" comment)
	private final static int NEAR = 5;

	// BUG: if you click exactly on the rightmost pixel of a graph, it doesn't hit
	public boolean contact(Graph g, Point p, int bottom) {
		// snap to year
		int yearSize = _graphinfo.getYearSize();
		int x1 = p.x - p.x % yearSize;
		int x2 = x1 + yearSize;

		// fraction of the way between x1 and x2
		float f = (p.x - x1) / (float) (x2 - x1);

		// get year of click
		Year y1 = _bounds.getStart().add(x1 / yearSize); // REFACTOR: does this look like yearForPosition()?
		Year y2 = y1.add(1);

		// --- everything above this is independent of graph ---

		// not in range?  no hit.
		if (!g.getRange().contains(y1))
			return false;
		if (!g.getRange().contains(y2)) // correct?
			return false;

		// get expected y-locs
		int yloc1 = getPosition(g, y1, bottom);
		int yloc2 = getPosition(g, y2, bottom);

		// get adjusted expected y-loc
		int yloc = (int) (yloc1 + (yloc2 - yloc1) * f);

		// hit?
		return (Math.abs(yloc - p.y) < NEAR);
	}

	private int getDataValue(Graph g, Year y) {
		int i = y.diff(g.graph.getStart().add(g.xoffset));
		return ((Number) g.graph.getData().get(i)).intValue();
	}

	private int getYValue(Graph g, int value, int bottom) {
		float unitScale = (float) _graphinfo.getYearSize() / 10.0f;
		return bottom - (int) (value * g.scale * unitScale) - 
						(int) (g.yoffset * unitScale); // DUPLICATE: this line appears above 3 times
	}

	private int getPosition(Graph g, Year y, int bottom) {
		return getYValue(g, getDataValue(g, y), bottom);
	}

	// REFACTOR: use this same method above when actually drawing it

	public void update() {
		// read sys props
		_dottedIndexes = Boolean.getBoolean("corina.graph.dotindexes");
	}
}
