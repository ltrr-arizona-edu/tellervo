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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.index.Index;
import org.tellervo.desktop.remarks.Remarks;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ColorUtils;
import org.tridas.schema.TridasRemark;


public class StandardPlot implements TellervoGraphPlotter {
	private final static Logger log = LoggerFactory.getLogger(StandardPlot.class);

	public StandardPlot() {
		// no initializing to do, I am STATELESS!
	}

	// PERF: too many new's!  can i memoize this?  or just use constants for the 6(?) strokes i use?
	protected BasicStroke makeStroke(float width, boolean dotted) {
		if (dotted)
			return new BasicStroke(width, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL, 10f, new float[] { 8f }, 0f);
		else
			return new BasicStroke(width, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL);
	}
	
	// We don't do any y transform for the standard plot; this is here for overriding.
	protected int yTransform(float y) {
		return (int) y;
	}
	
	protected boolean validValue(int value) {
		// if MR, draw a vertical line -- use Sample.MR, for now
		if (value <= Sample.missingRingValue)
			return false;
		return true;
	}

	// TESTING: perf
	protected Rectangle tempRect = new Rectangle();

	// returns the maximum size, in pixels, that the graph will take up.
	public int getYRange(GraphSettings gInfo, Graph g) {
		float unitScale = gInfo.getHundredUnitHeight() / 100.0f; // the size of 1 "unit" in pixels.
		int miny = 0; // minimum always starts at zero...
		int maxy = Integer.MIN_VALUE;
		int value;
		
		int n = g.graph.getRingWidthData().size(); 
		for (int i = 0; i < n; i++) {
			value = yTransform(g.graph.getRingWidthData().get(i).intValue() * g.scale);
			int y = (int) (value * unitScale) -	(int) (g.yoffset * unitScale);
			
			if(y < miny)
				miny = y;
			if(y > maxy)
				maxy = y;
		}
		
		return maxy - miny;
	}
	
	public void draw(GraphSettings gInfo, Graphics2D g2, int bottom, Graph g, int thickness, int xscroll) {
		// cache yearsize, we use this a lot
		int yearWidth = gInfo.getYearWidth(); // the size of a year, in pixels
		float unitScale = gInfo.getHundredUnitHeight() / 100.0f; // the size of 1 "unit" in pixels.
		
		// set pen
		boolean dotted = (gInfo.isDottedIndexes() && (g.graph instanceof Index));
		g2.setStroke(makeStroke(thickness, dotted));

		// left/right
		int l = g2.getClipBounds().x;
		int r = l + g2.getClipBounds().width;


		//Image img1 = Toolkit.getDefaultToolkit().getImage("/home/pwb48/dev/java5/tellervo-desktop/src/main/resources/Icons/256x256/tellervo-application.png");
	    //g2.drawImage(img1, 10, 10, null);

		
		// baseline
		if (gInfo.isShowBaselines()) {
			int y = bottom - (int) (g.yoffset * unitScale);
			g2.drawLine(xscroll, y, xscroll + 10 * yearWidth, y); // 1 decade wide -- ok?
		}
		
		// hundred percent line
		if (gInfo.isShowHundredpercentlines() && (g.graph instanceof Sample) && ((Sample) g.graph).isIndexed()) {
			Color oldcolor = g2.getColor();
			g2.setColor(ColorUtils.blend(oldcolor, gInfo.getBackgroundColor()));
		
			// x is 0 if we aren't drawing graph names...
			// x is the pixel at the end of the empty range if we are.
			int x = (gInfo.isShowGraphNames()) ? yearWidth * (gInfo.getEmptyBounds().getSpan() - 1) : 0;			
			int y = bottom - (int) (yTransform(1000 * g.scale) * unitScale) - (int) (g.yoffset * unitScale);
			g2.drawLine((x > xscroll) ? x : xscroll, y, r, y);
						
			g2.setColor(oldcolor);
		}

		// no data?  stop.
		if (g.graph.getRingWidthData().isEmpty())
			return;

		// compare g.getClipBounds() to [x,0]..[x+yearSize*data.size(),bottom]
		tempRect.x = yearWidth
				* (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset); // REDUNDANT! see x later
		tempRect.y = 0; // - g.yoffset, IF you're sure there are no negative values (but there are)
		tempRect.width = yearWidth * (g.graph.getRingWidthData().size() - 1);
		tempRect.height = bottom;
		// TODO: compute top/bottom as min/max?
		// REFACTOR: will this be obsolete with the start/end stuff below?
		if (!tempRect.intersects(g2.getClipBounds())) {
			// skip this graph, it's off the screen
			return;
		}

		// compute sapwood
		int sapwoodIndex, sapwoodCount = 0;
		if (g.graph instanceof Sample) {
			Sample sample = (Sample) g.graph;
			
			if(sample.meta().hasSapwood())
				sapwoodCount = sample.meta().getNumberOfSapwoodRings();			
		}
		sapwoodIndex = g.graph.getRingWidthData().size() - sapwoodCount + 1;

		// my path
		GeneralPath p = new GeneralPath();

		// x-position
		int x = yearWidth
				* (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset);

		// move to the first point -- THIS IS NOT REALLY A SPECIAL CASE!
		int value;
		try {
			value = ((Number) g.graph.getRingWidthData().get(0)).intValue();
			value = yTransform(value * g.scale);
		} catch (ClassCastException cce) {
			value = yTransform(0); // BAD!  instead: (1) just continue now, and (2) NEXT point is a move-to.
		}
		p.moveTo(x, bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale));

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
		int n = g.graph.getRingWidthData().size(); // THIS is the third time it's called; why not use it above?
		for (int i = 1; i < n; i++) {
			// new x-position for this point
			x += yearWidth;

			// if we're past the end, draw what we've got, and say goodbye
			// (go +_yearsize so the line going off the screen is visible)
			if (x > r + yearWidth) {
				break;
			}

			// sapwood?  draw what we've got, and start a new (thicker) path
			// but only do it if sapwoodThicker() is enabled!
			if (gInfo.isThickerSapwood() && i == sapwoodIndex) {
				g2.draw(p);
				g2.setStroke(makeStroke(2 * thickness, false));
				p = new GeneralPath();
				p.moveTo(yearWidth * (i - 1 + g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset),
						 bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale));
			}

			// y-position for this point
			try {
				value = yTransform(((Number) g.graph.getRingWidthData().get(i)).intValue() * g.scale);
			} catch (ClassCastException cce) {
				value = yTransform(0); // e.g., if it's being edited, it's still a string
				// BAD!  instead: (1) draw what i've got so far, and (2) NEXT point is a move-to.
				// -- try to parse String as an integer?
			}

			// Try and paint remark icons
			try{
				List<TridasRemark> remarks = g.graph.getTridasValues().get(i).getRemarks();
				int xcoord = (yearWidth * (i + g.graph.getStart().diff(gInfo.getDrawBounds().getStart())));
				int ycoord = (bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale)); 

				Graph.drawRemarkIcons(g2, gInfo, remarks, g.graph.getTridasValues().get(i), xcoord, ycoord);
				
			} catch (Exception e)
			{
				log.error("Exception drawing icons to graph: " + e.getClass());
			}
			
			
			int y = bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale);

			// if we're not where this sample starts, don't bother drawing yet
			if (x < l - yearWidth) {
				p.moveTo(x, y);
				continue;
			}

			// if MR, draw a vertical line -- use Sample.MR, for now
			if (g.graph instanceof Sample && !validValue(value))
				g2.drawLine(x, y - 20, x, y + 20);

			// draw a line to this point
			p.lineTo(x, y);
		}

		// draw it!
		g2.draw(p);

	}

	// if it's within this many pixels, it's considered a hit (see "correct?" comment)
	private final static int NEAR = 5;

	public boolean contact(GraphSettings gInfo, Graph g, Point p, int bottom) {
		// snap to year
		int yearWidth = gInfo.getYearWidth();
		int firstYearIdx = (p.x / yearWidth) - 1;
		int nYears = 3;
				
		// Check three years' worth of data: the year prior to and after the year the mouse is inside
		Year startYear = gInfo.getDrawBounds().getStart().add(firstYearIdx);
		List<Point2D> points = this.getPointsFrom(gInfo, g, startYear, nYears, bottom);

		int nLines = points.size() - 1;
		for(int i = 0; i < nLines; i++) {
			Line2D line = new Line2D.Float(points.get(i), points.get(i+1));
			
			int distance = Math.round((float) line.ptSegDist(p));
			if(distance <= NEAR)
				return true;
		}
		
		return false;
	}
	
	private final int getDataValue(Graph g, Year y) {
		int i = y.diff(g.graph.getStart().add(g.xoffset));
		return g.graph.getRingWidthData().get(i).intValue();
	}

	private final int getYValue(Graph g, float unitScale, int value, int bottom) {
		return bottom - (int) (yTransform(value * g.scale) * unitScale) - 
						(int) (g.yoffset * unitScale); // DUPLICATE: this line appears above 3 times
	}
	
	/**
	 * Get the X value of a year
	 * @param g the graph
	 * @param plotStartYear the start year of the entire plot
	 * @param yearWidth the width of a year, in pixels
	 * @paran n the number of years into the graph
	 * @return
	 */
	private final int getXValue(Graph g, Year plotStartYear, int yearWidth, int n) {
		return yearWidth * (g.graph.getStart().diff(plotStartYear) + g.xoffset + n);
	}
	
	private final List<Point2D> getPointsFrom(GraphSettings gInfo, Graph g, Year startYear, int nYears, int bottom) {
		// index into our data values for this particular year
		int idx = startYear.diff(g.graph.getStart().add(g.xoffset));

		// make a list of points
		// this is ok, because we know points are continuous
		List<Point2D> points = new ArrayList<Point2D>(nYears);
		
		// the year the plot starts
		Year plotStartYear = gInfo.getDrawBounds().getStart();
		// the adjusted range of this graph
		Range graphRange = g.getRange();
		int yearWidth = gInfo.getYearWidth();
		float unitScale = gInfo.getHundredUnitHeight() / 100.0f;
		
		for(int i = 0; i <= nYears; i++) {
			// skip points that don't exist
			if(!graphRange.contains(startYear.add(i)))
				continue;
			
			int value = g.graph.getRingWidthData().get(idx + i).intValue();
			int x = getXValue(g, plotStartYear, yearWidth, idx + i);
			int y = getYValue(g, unitScale, value, bottom);
			
			points.add(new Point2D.Float(x, y));
		}
		
		return points;
	}

	protected final int getPosition(GraphSettings gInfo, Graph g, Year y, int bottom) {
		return getYValue(g, gInfo.getHundredUnitHeight() / 100.0f, getDataValue(g, y), bottom);
	}

	@Override
	public int getFirstValue(Graph g) {
		return g.graph.getRingWidthData().get(0).intValue();
	}

	// REFACTOR: use this same method above when actually drawing it
}
