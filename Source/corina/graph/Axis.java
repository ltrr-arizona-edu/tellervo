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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;

public class Axis extends JPanel {

    public Axis() {
	// background -- default is black
	setBackground(Color.getColor("corina.graph.background", Color.black));

	// size of axis-margin (only width matters)
	setPreferredSize(new Dimension(50, 50));
    }

    // this doesn't get drawn very often: maybe half a dozen times,
    // max, even with resizing and scrolling.  so don't worry about
    // efficiency here.
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	int w = getWidth();

	// draw vertical axis
	int bottom = getHeight() - GrapherPanel.AXIS_HEIGHT;
	g2.setColor(Color.getColor("corina.graph.foreground", Color.white));
	g2.drawLine(w-1, 0, w-1, bottom);

	// draw ticks
	int i = 1;
	int y = bottom - i*10;
	while (y > 0) {
	    // draw tick
	    g2.drawLine(w-1, y, w-1 - (i%5==0 ? 10 : 5), y);

	    // draw number -- every 50
	    if (i % 5 == 0)
		g2.drawString(String.valueOf(i*10), w-40, y+5);

	    // update coordinates
	    i++;
	    y = bottom - i*10;
	}

	// draw baselines (if wanted) --WRITE ME
	// if (Boolean.getBoolean("corina.graph.baselines")) {
	// }
    }

}
