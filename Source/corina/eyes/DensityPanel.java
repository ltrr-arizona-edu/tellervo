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

import corina.eyes.Scanner.Ring;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import javax.swing.JPanel;

// ideas:
// - (this will end up being a second tab in a jframe)
// - do i need events here, like scanner-path-changed-event?
// - should be printable (printing a corina.eyes window prints both?)
// - should be exportable (save data to 2-col, at least.  save png?)
// - (other stuff i can't remember because i'm doped up on sudafed right now)
// - make it light-on-dark, or customizeable?

public class DensityPanel extends JPanel {

    private Scanner _s;

    public DensityPanel(Scanner s) {
	_s = s;

	setBackground(Color.white);
    }

    // probably a good deal of this belongs elsewhere (perhaps in the
    // constructor), and doesn't need to be recomputed every
    // paintComponent()
    public void paintComponent(Graphics g) {
	// boilerplate
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	// don't mess with scrolling.  the user will be pretty much
	// looking at just this, probably in a fairly large window.
	// plus, you'll only want to look at it for an overview, not
	// details.

	// strategy: left axis, bottom axis, title, graph,
	// threshold(s)(?)

	// get panel size
	int w = getWidth();
	int h = getHeight();

	/*
	// get data width (we know its height, right?)
	Ring rings[] = _s.getRings();
	int l = (int) rings[0].dist;
	int r = (int) rings[rings.length-1].dist;
	*/

	// left axis - 50px wide, 10px from top/bottom
	g2.setColor(Color.black);
	g2.drawLine(50, 10, 50, h-50);
	// WRITE ME: scale, label

	// bottom axis
	g2.setColor(Color.black);
	g2.drawLine(50, h-50, w-10, h-50);
	// WRITE ME: scale, label

	/*
	// title
	// WRITE ME

	// graph
	g2.setColor(Color.red);
	GeneralPath gp = new GeneralPath();

	float X = ((Number) _s.distances.get(0)).floatValue();
	float Y = ((Number) _s.brightness.get(0)).floatValue();

	gp.moveTo(X, Y);

	for (int i=1; i<rings.length; i++) {
	    X = ((Number) _s.distances.get(i)).floatValue();
	    Y = ((Number) _s.brightness.get(i)).floatValue();

	    gp.lineTo(X, Y);
	}
	// WRITE ME
	*/
    }
}
