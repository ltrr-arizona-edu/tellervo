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
import corina.index.Index;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.geom.GeneralPath;
import javax.swing.JPanel;

public class StandardPlot {

    // these i get from sys props
    private boolean _baselines;
    private int _yearsize;
    private float _width;
    private boolean _dottedIndexes;

    // these get passed
    private Range _bounds;
    private JPanel _panel;

    // nb: this gets recreated every from refreshFromPreferences()
    public StandardPlot(Range bounds, JPanel panel) {
        // read sys props
        update();

        // copy parms
        _bounds = bounds;
        _panel = panel;
    }

    private BasicStroke makeStroke(float width, boolean dotted) {
	if (dotted)
	    return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
				   10f, new float[] { 8f }, 0f);
	else
	    return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    }

    public void draw(Graphics2D g2, Graph g, boolean isCurrent, int xscroll) {
        // set pen
        g2.setStroke(makeStroke(isCurrent ? 2f*_width : _width, false));

        // use dotted line for indexes?  (refactor me?)
        if (_dottedIndexes && (g.graph instanceof Index)) {
            g2.setStroke(makeStroke(isCurrent ? 2f*_width : _width, true));
        }

        // bottom
        int bottom = GrapherPanel.getBottom(_panel);

        // baseline
        if (_baselines) {
            int y = bottom - g.yoffset;
            g2.drawLine(xscroll, y, xscroll+10*_yearsize, y); // 1 decade wide -- ok?
        }

        // no data?  stop.
        if (g.graph.getData().isEmpty())
            return;

        // compute sapwood
        int sapwoodIndex, sapwoodCount=0, unmeasPost=0;
        if (g.graph instanceof Sample) {
            Sample sample = (Sample) g.graph;
            if (sample.isOak()) {
                try {
                    if (sample.meta.containsKey("sapwood"))
                        sapwoodCount = ((Integer) sample.meta.get("sapwood")).intValue();
                    if (sample.meta.containsKey("unmeas_post"))
                        unmeasPost = ((Integer) sample.meta.get("unmeas_post")).intValue();
                } catch (ClassCastException cce) {
                    // we've already warned the user above, ignore it now.
                }
            }
        }
        sapwoodIndex = g.graph.getData().size() - sapwoodCount + unmeasPost + 1;

        // my path
        GeneralPath p = new GeneralPath();

        // x-position
        int x = _yearsize*(g.graph.getStart().diff(_bounds.getStart()) + g.xoffset);

        // move to the first point -- THIS IS NOT REALLY A SPECIAL CASE!
        int value;
        try {
            value = ((Number) g.graph.getData().get(0)).intValue();
        } catch (ClassCastException cce) {
            value = 0; // BAD!  instead: (1) just continue now, and (2) NEXT point is a move-to.
        }
        p.moveTo(x, bottom - (int)(value*g.scale) - g.yoffset);

        // connect the lines through the rest of the graph
        int n = g.graph.getData().size(); // does this help performance any?
        for (int i=1; i<n; i++) {
            // sapwood?  draw what we've got, and start a new (thicker) path
            if (i==sapwoodIndex) {
                g2.draw(p);
                g2.setStroke(makeStroke(isCurrent ? 4f*_width : 2f*_width, false));
                p = new GeneralPath();
                p.moveTo(_yearsize*(i-1 + g.graph.getStart().diff(_bounds.getStart()) + g.xoffset),
                         bottom - (int)(value*g.scale) - g.yoffset);
            }

            // compute this value
            try {
                value = ((Number) g.graph.getData().get(i)).intValue();
            } catch (ClassCastException cce) {
                value = 0; // e.g., if it's being edited, it's still a string
                           // BAD!  instead: (1) draw what i've got so far, and (2) NEXT point is a move-to.
                           // -- try to parse String as an integer?
            }

            // (x,y) position
            x += _yearsize;
            int y = bottom - (int)(value*g.scale) - g.yoffset;

            // if MR, draw a vertical line -- use (int) Sample.MR, for now
            if (value <= Sample.MR && g.graph instanceof Sample)
                g2.drawLine(x, y-20, x, y+20);

            // move to next point
            p.lineTo(x, y);
        }

        // draw it!
        g2.draw(p);
    }

    public boolean contact(Graph gg, Point p) {
	// get X of click
	int x = (int) p.getX();

	// snap to year
	if (x % _yearsize < _yearsize/2)
	    x -= x%_yearsize;
	else
	    x -= x%_yearsize - _yearsize;

	// get year of click
	Year yr = _bounds.getStart().add(x / _yearsize);

	// --- everything before this point is the same for every
	// graph; i don't suppose it can be cached, but it's pretty
	// quick, anyway. ---

	// not in range?  no hit.
	if (!gg.getRange().contains(yr))
	    return false;

	// get graphable
	Graphable g = gg.graph;

	// get x-offset
	int xoff = gg.xoffset;

	// get expected value (ugly)
	int v = ((Number) g.getData().get(yr.diff(g.getStart().add(xoff)))).intValue();

	// get expected y-loc
	int bottom = GrapherPanel.getBottom(_panel);
	int yloc = bottom - (int)(v*gg.scale) - gg.yoffset;
	// THIS is VER' BAD: this should use exactly the same
	// algorithm for screen positioning as the actual drawing
	// does.  implementing the same thing in 2 different places is
	// just asking for trouble.

	// hit?
	return (Math.abs(yloc - p.getY()) < 5); // fixme: use const epsilon!
    }

    public void update() {
        // read sys props
        _baselines = Boolean.getBoolean("corina.graph.baselines");
        _yearsize = Integer.parseInt(System.getProperty("corina.graph.pixelsperyear", "10"));
        _width = Float.parseFloat(System.getProperty("corina.graph.thickness", "1.0"));
        _dottedIndexes = Boolean.getBoolean("corina.graph.dotindexes");
    }
}
