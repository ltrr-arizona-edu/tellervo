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

package corina.site;

import corina.map.Location;

import java.awt.Color;
import java.awt.Graphics2D;

/*
  todo:
  - add string i/o (of xml tags)
  - encapsulate members better
*/

/**
   <p>Site object, which holds name/ID information, location, and
   other assorted data (like lists of both masters and non-fits).</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Site {

    /** The 3-digit identification number. */
    public String id=null;

    /** The 3-letter code. */
    public String code=null;

    /** The name. */
    public String name=null;

    // country
    public String country=null;

    // the type: FOREST(1), MEDIEVAL(2), ANCIENT(3), or UNKNOWN(4->0)
    // => make bitfield
    public final static int TYPE_UNKNOWN = 0;
    public final static int TYPE_FOREST = 1;
    public final static int TYPE_MEDIEVAL = 2;
    public final static int TYPE_ANCIENT = 3;
    public int type=TYPE_UNKNOWN;
    public String type2 = null;

    public String species=null;

    /** The location (latitude, longitude). */
    public Location location=null;

    /** The altitude, in meters. */
    public Integer altitude=null;

    /** All samples with a filename starting with this, belong to this
	site.  This is just a hack, until a new file format, which
	contains the site explicitly
	("<code>&lt;site&gt;ZKB&lt;/site&gt;</code>") is in place. */
    public String filename=null;

    /** A pretty-printer for sites.
       @return the site in human-readable form */
    public String toString() {
	String result;

	// name, if available, else code
	if (name != null)
	    result = name;
	else if (code != null)
	    result = code;
	else
	    result = "untitled site";

	// location, if available
	if (location != null)
	    result += " (" + location + ")";

	// return it
	return result;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location l) {
        location = l;
    }

    // make this customizable -- (use map.Pallette?  no)
    public Color getSiteColor() {
        int x;
        try {
            x = Integer.parseInt(type2);
        } catch (NumberFormatException nfe) {
            x = TYPE_UNKNOWN;
        }

        switch (x) {
            case TYPE_FOREST:
                return Color.green;
            case TYPE_MEDIEVAL:
                return Color.red;
            case TYPE_ANCIENT:
                return Color.blue;
            default:
                return Color.lightGray;
        }
    }
}
