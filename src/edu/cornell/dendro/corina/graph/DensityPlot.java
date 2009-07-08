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

package edu.cornell.dendro.corina.graph;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class DensityPlot extends StandardPlot implements CorinaGraphPlotter {	
	public DensityPlot() {
		super();
	}

	@Override
	protected int yTransform(float y) {
		return (int) y;
	}
	
	@Override
	protected boolean validValue(int value) {
		return true;
	}

	@Override
	public void draw(GraphInfo gInfo, Graphics2D g2, int bottom, Graph g, int thickness, int xscroll) {
		// cache yearsize, we use this a lot
		int yearWidth = gInfo.getYearWidth(); // the size of a year, in pixels
		float unitScale = gInfo.getTenUnitHeight() / 10.0f; // the size of 1 "unit" in pixels.
												   // using another var in case these become independent
		
		// set pen
		g2.setStroke(makeStroke(thickness, false));

		// left/right
		int l = g2.getClipBounds().x;
		int r = l + g2.getClipBounds().width;

		// baseline
		if (gInfo.isShowBaselines()) {
			int y = bottom - (int) (g.yoffset * unitScale);
			g2.drawLine(xscroll, y, xscroll + 10 * yearWidth, y); // 1 decade wide -- ok?
		}
		
		// no data?  stop.
		if (g.graph.getData().isEmpty())
			return;

		// compare g.getClipBounds() to [x,0]..[x+yearSize*data.size(),bottom]
		tempRect.x = yearWidth
				* (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset); // REDUNDANT! see x later
		tempRect.y = 0; // - g.yoffset, IF you're sure there are no negative values (but there are)
		tempRect.width = yearWidth * (g.graph.getData().size() - 1);
		tempRect.height = bottom;
		// TODO: compute top/bottom as min/max?
		// REFACTOR: will this be obsolete with the start/end stuff below?
		if (!tempRect.intersects(g2.getClipBounds())) {
			// skip this graph, it's off the screen
			return;
		}

		// my path
		GeneralPath p = new GeneralPath();

		// x-position
		int x = yearWidth
				* (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset);

		// move to the first point -- THIS IS NOT REALLY A SPECIAL CASE!
		int value;
		try {
			value = yTransform(((Number) g.graph.getData().get(0)).intValue() * g.scale);
		} catch (ClassCastException cce) {
			value = yTransform(0); // BAD!  instead: (1) just continue now, and (2) NEXT point is a move-to.
		}
		int y;
		
		y = bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale);
		p.moveTo(x, y);

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
			x += yearWidth;

			// if we're past the end, draw what we've got, and say goodbye
			// (go +_yearsize so the line going off the screen is visible)
			if (x > r + yearWidth) {
				break;
			}

			// y-position for this point
			try {
				value = yTransform(((Number) g.graph.getData().get(i)).intValue() * g.scale);
			} catch (ClassCastException cce) {
				value = yTransform(0); // e.g., if it's being edited, it's still a string
				// BAD!  instead: (1) draw what i've got so far, and (2) NEXT point is a move-to.
				// -- try to parse String as an integer?
			}
			int oldy = y;
			y = bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale);

			// if we're not where this sample starts, don't bother drawing yet
			if (x < l - yearWidth) {
				p.moveTo(x, y);
				continue;
			}

			// draw a horizontal line to this point.
			p.lineTo(x, oldy);
			// draw a line to this point
			p.lineTo(x, y);
		}

		// draw it!
		g2.draw(p);
	}
	
}
