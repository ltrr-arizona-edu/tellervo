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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.map;

import java.awt.Dimension;

// TODO: what's a zoom factor of 1.0 defined to be, exactly?
// TODO: loc should be getCenter/setCenter?
// TODO: admit we're mutable?  who mutes us?
// TODO: "zoom in" / "zoom out" should be methods here (should they? min/max aren't)
// TODO: size should be getSize/setSize/getHeight/getWidth/setHeight/setWidth
// TODO: clone is weird ...

/**
   A particular View of the map.  A View consists of:

   <ul>
     <li>The center (latitude and longitude)
     <li>The zoom factor (1.0 is "normal")
     <li>The size (in pixels for the screen, or points for the printer)
   </ul>

   <p>A View object is mutable.  A window showing a map that the user can manipulate has
   a View associated with it, which gets changed as the user plays with the map.
   To draw the map, Corina first makes a Projection using the current View, and
   then uses that Projection to map Locations to Points.</p>

   @see Projection
   @see Location

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class View implements Cloneable {
    /**
       Make a new View.  The default view is centered at 38&deg;N 30&deg;E
       (the Aegean), has a zoom factor of 1.0, and is 640 x 640 pixels in size.
    */
    public View() {
        // (this constructor is here just for the javadoc tag)
    }

    public Location center = new Location("38*N 30*E");
    
    private float zoom = 1;

    /**
       Get the zoom factor.
       @return the zoom factor
    */
    public float getZoom() {
        return zoom;
    }
    /**
        Set the zoom factor.
        @param zoom the new zoom factor
    */
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
    
    public Dimension size = new Dimension(640, 640);

    // REFACTOR: why do i need clone?  wouldn't a copy-constructor be simpler?
    // USED BY: MapPanel (for printing - making detailedView), MapFrame (png export)
    // so what i really want is "make a copy, but with X factor more detail"
    public Object clone() {
        View v2;
        try {
          v2 = (View) super.clone();
        } catch (CloneNotSupportedException cnse) {
          v2 = new View();
        }
        v2.center = (Location) center.clone();
        v2.zoom = zoom;
        v2.size = (Dimension) size.clone();
        return v2;
    }
}
