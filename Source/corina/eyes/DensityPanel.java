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

package corina.eyes;

import corina.Year;

import java.util.Collections;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
   A panel showing the density graph.

   the (programmer's) interface to this sucks.  it uses the catch-all
   Scanner class.  see the constructor for what it should take.

   TODO:
   -- allow dragging thresholds -- sliders on side?
   -- document, document, document
   -- make width wide enough to see last year?  +(width of that text)?
   -- get rid of axes, and huge space on left/bottom.  ick.

   FUTURE:
   -- allow space-dragging to scroll?
   -- labels: "density", "distance"?
   -- zoom this to the same amount of zoom as the top component is using.
*/
public class DensityPanel extends JComponent {

    // FIXME: as input, should take
    // -- a DensityGraph (a list of [dist brightn] tuples)
    // -- the output of Schweingruber (a list of numbers)
    // -- the thresholds (i'll modify them, too)
    private Scanner scanner;

    /**
       construct a new density panel.

       distances, brightness, and rings.

       dist/bri should be from a DensityGraph class

       rings should be simply a list of numbers from schweingruber
    */
    public DensityPanel(Scanner scanner) {
	this.scanner = scanner;

	setBackground(Color.white);
    }

    /**
       Paint the density graph, thresholds, and years.
    */
    public void paintComponent(Graphics g) {
	// PERF: this will get continuously redrawn while the user drags
	// the path, so it must be fast.  profile, and if it's taking a
	// non-trivial amount of time (>25ms), work on it.

	super.paintComponent(g);

	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

	paintYears(g2);
	paintThresholds(g2);
	paintGraph(g2);

	// TODO: scale?
	// TODO: labels?
	// TODO: title?
    }

    // draw the density graph itself
    private void paintGraph(Graphics2D g2) {
	// graph
	g2.setColor(Color.red);
	GeneralPath gp = new GeneralPath();

	// PERF: if it'll help out (esp. on bigger samples), only draw
	// the part of the graph that needs to be drawn.

	float X = ((Number) scanner.distances.get(0)).floatValue();
	float Y = ((Number) scanner.brightness.get(0)).floatValue();
	Y = y(Y);

	gp.moveTo(X, Y);

	int n = scanner.distances.size();
	for (int i=1; i<n; i++) {
	    X = ((Number) scanner.distances.get(i)).floatValue();
	    Y = ((Number) scanner.brightness.get(i)).floatValue();
	    Y = y(Y);

	    gp.lineTo(X, Y);
	}
	// WRITE ME

	g2.draw(gp);
    }

    // draw the upper and lower threshold values
    private void paintThresholds(Graphics2D g2) {
	g2.setColor(Color.blue);

	int t1 = (int) Schweingruber.THRESHOLD_TOP;
	int t2 = (int) Schweingruber.THRESHOLD_BOTTOM;

	t1 = y(t1);
	t2 = y(t2);

	g2.drawLine(0, t1, getWidth(), t1);
	g2.drawLine(0, t2, getWidth(), t2);

	// TODO: draw jslider thumbs on thresholds for dragging?
    }

    // value->year
    private int y(float v) {
	int h = getHeight();
	v *= h / 256f;
	return Math.round(h - v);
    }

    // for each year, paint a vertical gray line, and label it
    private void paintYears(Graphics2D g2) {
	g2.setColor(Color.lightGray);
	g2.setFont(g2.getFont().deriveFont(9f)); // same font, but 9 points
	int ascent = g2.getFontMetrics().getAscent();

	Year y = new Year(); // 1001
	double x = 0;

	double rings[] = scanner.getWidths();
	for (int i=0; i<rings.length; i++) {
	    x += rings[i];
	    g2.drawLine((int) x, 0,
			(int) x, getHeight());
	    g2.drawString(y.toString(), (int)x+EPS, EPS+ascent);
	    y = y.add(+1);
	}
    }

    // a small value; year labels are this many pixels down from top and right from line
    private final static int EPS = 2;

    //
    // size (for scroll pane)
    //

    /**
       Recompute my size, and update my scrollbar if necessary.
       Whenever the length of the path changes, this should be called.
    */
    public void updateSize() {
	int width = ((Number) Collections.max(scanner.distances)).intValue();
	// -- wouldn't this also be scanner.distances[n-1]?

	Dimension d = new Dimension(width, 100);

	setPreferredSize(d);
	revalidate();
    }
}
