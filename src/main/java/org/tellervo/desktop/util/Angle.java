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
// Tellervo is free software; you can redistribute it and/or modify
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

import java.awt.Point;

// -- float is ~7 sigfigs, and sin(.0001) is 1/1000 of a pixel at 1000 pixels out -- that's plenty

/**
   Compute the angle of a line drawn between two given points.

   <p>(A Java float is about 7 significant figures, and sin(0.0001) is
   1/1000 of 1 pixel at 1000 pixels out, so I don't think I'll need
   more precision than that.)</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Angle {
    // don't instantiate me
    private Angle() { }

    /** Compute the angle of a line drawn between two points.
	The result is always between 0 and 2*Math.PI.
	@param a the first point
	@param b the second point
	@return the angle between a line drawn from a to b
    */
    public static float angle(Point a, Point b) {
        float theta;
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        if (dx == 0)
            theta = (float) Math.PI * (b.y<a.y ? 3/2f : 1/2f); // force floats!
        else
            theta = (float) Math.atan(dy/dx);
        if (b.x < a.x)
            theta += Math.PI;
        return theta;
    }
}
