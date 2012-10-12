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

package org.tellervo.desktop.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.index.Index;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.util.ColorUtils;


public class SkeletonPlot implements TellervoGraphPlotter {
	private final static Logger log = LoggerFactory.getLogger(SkeletonPlot.class);

	public SkeletonPlot() {
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
	public int getYRange(GraphInfo gInfo, Graph g) {
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
	
	public void draw(GraphInfo gInfo, Graphics2D g2, int bottom, Graph g, int thickness, int xscroll) {
		// cache yearsize, we use this a lot
		int yearWidth = gInfo.getYearWidth(); // the size of a year, in pixels
		float unitScale = gInfo.getHundredUnitHeight() / 100.0f; // the size of 1 "unit" in pixels.
		
		// set pen
		boolean dotted = (gInfo.isDottedIndexes() && (g.graph instanceof Index));
		g2.setStroke(makeStroke(thickness, dotted));

		// left/right
		int l = g2.getClipBounds().x;
		int r = l + g2.getClipBounds().width;

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

		// Draw mean line
		int x = yearWidth * (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset);
		int value1 = yTransform((float) getMeanValue(g));
		int meanYVal = bottom - (int) (value1 * unitScale) - (int) (g.yoffset * unitScale);
		try {
			// x-position
			int x1 = x;
			int x2 = yearWidth * (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset +g.graph.getRingWidthData().size());

			// if we're past the end, draw only as far as we need
			if (x2 > r + yearWidth) {
				x2 = r+yearWidth;
			}
			
			//log.debug("Drawing mean line: "+x1+", "+meanYVal+", "+x2+", "+meanYVal);
			g2.drawLine(x1, meanYVal, x2, meanYVal);
			
		} catch (ClassCastException cce) {
			
		}
	
		int value;
		try {
			value = ((Number) g.graph.getRingWidthData().get(0)).intValue();
			value = yTransform(value * g.scale);
		} catch (ClassCastException cce) {
			value = yTransform(0); 
		}
		
		int n = g.graph.getRingWidthData().size(); 
		int runningMean = meanYVal;
		for (int i = 1; i < n; i++) {
			// new x-position for this point
			x += yearWidth;

			// if we're past the end, draw what we've got, and say goodbye
			// (go +_yearsize so the line going off the screen is visible)
			if (x > r + yearWidth) {
				break;
			}

			// Calculate running mean for 3 rings either side of this value
			double[] window;
			if(i<=3)
			{
				// One of the first few rings
				window = new double[i+3];
				int k = 0;
				for(int j =0; j<=i+2; j++)
				{
					window[k] = (double) g.graph.getRingWidthData().get(j).intValue();
					k++;
				}
				
				log.debug("Ring "+i+" has "+window.length+" values in window");
			}
			else if (i> n-3)
			{
				// One of the last few rings
				window = new double[n-i];
				int k = 0;
				for(int j =i-4; j<=i+2; j++)
				{
					try{
						window[k] = (double) g.graph.getRingWidthData().get(j).intValue();
					} catch (Exception e){	}
					k++;
				}
				
				log.debug("Ring "+i+" has "+window.length+" values in window");

			}
			else
			{
				// In middle of dataset
				window = new double[7];
				int k = 0;
				for(int j =i-4; j<=i+2; j++)
				{
					window[k] = (double) g.graph.getRingWidthData().get(j).intValue();
					k++;
				}
				
				log.debug("Ring "+i+" has "+window.length+" values in window");

			}
			DescriptiveStatistics windowStats = new DescriptiveStatistics(window);
			if(i==4 || i>278) 
			{
				log.debug("Stats for ring: "+i);
				try{
					log.debug("  Window 0: "+window[0]);
					log.debug("  Window 1: "+window[1]);
					log.debug("  Window 2: "+window[2]);
					log.debug("  Window 3: "+window[3]);
					log.debug("  Window 4: "+window[4]);
					log.debug("  Window 5: "+window[5]);
					log.debug("  Window 6: "+window[6]);
				} catch (ArrayIndexOutOfBoundsException e){}
				log.debug("  Mean  is "+i+" - "+(int) windowStats.getMean());
				log.debug("  Min   is "+i+" - "+(int) windowStats.getMin());
				log.debug("  Std   is "+i+" - "+(int) windowStats.getStandardDeviation());
				log.debug("  Std/2 is "+i+" - "+(int) windowStats.getStandardDeviation()/2);
			}
			runningMean = (int) windowStats.getMean();
			

			
			// y-position for this point
			try {
				value = yTransform(((Number) g.graph.getRingWidthData().get(i)).intValue() * g.scale);
			} catch (ClassCastException cce) {
				value = yTransform(0); // e.g., if it's being edited, it's still a string
				// BAD!  instead: (1) draw what i've got so far, and (2) NEXT point is a move-to.
				// -- try to parse String as an integer?
			}
			int y = bottom - (int) (value * unitScale) - (int) (g.yoffset * unitScale);

			Integer skeletonCategory = 0;
			// Calculate skeleton category
			if(value == (int) windowStats.getMin())
			{
				skeletonCategory = 10;
			}
			else if(value < windowStats.getPercentile(10))
			{
				skeletonCategory = 9;
			}
			else if(value < windowStats.getPercentile(15))
			{
				skeletonCategory = 8;
			}
			else if(value < windowStats.getPercentile(20))
			{
				skeletonCategory = 7;
			}
			else if(value < windowStats.getPercentile(25))
			{
				skeletonCategory = 6;
			}
			else if(value < windowStats.getPercentile(30))
			{
				skeletonCategory = 5;
			}
			else if(value < windowStats.getPercentile(35))
			{
				skeletonCategory = 4;
			}
			else if(value < windowStats.getPercentile(40))
			{
				skeletonCategory = 3;
			}
			else if(value < windowStats.getPercentile(45))
			{
				skeletonCategory = 2;
			}
			else if(value < windowStats.getPercentile(50))
			{
				skeletonCategory = 1;				
			}
			
			g2.drawLine(x, meanYVal, x, meanYVal-(skeletonCategory*5));

		}
	}

	// if it's within this many pixels, it's considered a hit (see "correct?" comment)
	private final static int NEAR = 5;

	public boolean contact(GraphInfo gInfo, Graph g, Point p, int bottom) {
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
	
	
	private final List<Point2D> getPointsFrom(GraphInfo gInfo, Graph g, Year startYear, int nYears, int bottom) {

		// make a list of points
		// this is ok, because we know points are continuous
		List<Point2D> points = new ArrayList<Point2D>(nYears);
		
		int yearWidth = gInfo.getYearWidth();
		float unitScale = gInfo.getHundredUnitHeight() / 100.0f;
		
		List<? extends Number> data = g.graph.getRingWidthData();
		double[] dataDbl = new double[data.size()];
		for(int i=0; i<data.size(); i++)
		{
			Integer intval = (Integer) data.get(i);
			dataDbl[i] = (double) intval ;
		}
		DescriptiveStatistics stats = new DescriptiveStatistics(dataDbl);
		
		int x = yearWidth * (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset);
		int value1 = yTransform((float) stats.getMean());
		int meanYVal = bottom - (int) (value1 * unitScale) - (int) (g.yoffset * unitScale);
		try {
			// x-position
			int x1 = x;
			int x2 = yearWidth * (g.graph.getStart().diff(gInfo.getDrawBounds().getStart()) + g.xoffset +g.graph.getRingWidthData().size());

			points.add(new Point2D.Float(x1, meanYVal));
			points.add(new Point2D.Float(x2, meanYVal));
			
		} catch (ClassCastException cce) {
			
		}
				
		return points;
	}

	protected final int getPosition(GraphInfo gInfo, Graph g, Year y, int bottom) {
		return getYValue(g, gInfo.getHundredUnitHeight() / 100.0f, getDataValue(g, y), bottom);
	}

	@Override
	public int getFirstValue(Graph g) {
		return getMeanValue(g);
	}
	
	private int getMeanValue(Graph g)
	{
		// Computer average 
		List<? extends Number> data = g.graph.getRingWidthData();
		double[] dataDbl = new double[data.size()];
		for(int i=0; i<data.size(); i++)
		{
			Integer intval = (Integer) data.get(i);
			dataDbl[i] = (double) intval ;
		}
		DescriptiveStatistics stats = new DescriptiveStatistics(dataDbl);
		
		return (int) stats.getMean();
	}

	// REFACTOR: use this same method above when actually drawing it
}
