/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
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

package org.tellervo.desktop.util;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;

/**
   Center windows on the screen, or relative to other windows.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Center {
    // don't instantiate me
    private Center() { }

    /**
       Move this window to the center of the screen.

       <p>On with Xinerama running Sun's Java 1.3, the "screen" is
       considered to be the union of all screens.  If you have an even
       number of screens in either direction, this may place the
       window half one one screen, half on another.  There is a bug
       report (#???) to come up with a solution for this.  (Mac
       systems with multiple monitors will center the window in the
       primary screen.  I haven't tested it, but I think Windows will
       act like Xinerama.)</p>

       @param w the window to center
    */
    public static void center(Window window) {
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	int x = screen.width/2 - window.getWidth()/2;
	int y = screen.height/2 - window.getHeight()/2;
	window.setLocation(new Point(x, y));
    }

    // TODO: consider making this a general purpose "fitting windows
    // on the screen" class:
    // -- tile windows as they're created
    // -- implement zoom/maximize which builds on 1.3, but use
    //    real "maximize" of 1.4
    // -- implement "full screen width" (for graphs), "full screen
    //    height" (for editors)

    /**
       Center a window on another window.

       @param window the window to center
       @param source the window to center it around
    */
    public static void center(Window window, Window source) {
	Point p = source.getLocation();
	Dimension d = source.getSize();
	int x = p.x + d.width/2 - window.getWidth()/2;
	int y = p.y + d.height/2 - window.getHeight()/2;

	// ensure that it doesn't get put off the screen!
	if(x < 0)
		x = 0;
	if(y < 0)
		y = 0;
	
	window.setLocation(new Point(x, y));

	// REFACTOR: combine these 2 center() functions?
    }
}
