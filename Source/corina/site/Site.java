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
import corina.util.ColorUtils;

import java.awt.Color;
import java.awt.Graphics2D;

/*
  todo:
  - add string i/o (of xml tags)
  - encapsulate members better
*/

/**
   Site object, which holds name/ID information, location, and
   other assorted data (like lists of both masters and non-fits).

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Site {
    /** The 3-digit identification number. */
    private String id=null;
    public String getID() {
	return id; }
    public void setID(String id) {
	this.id = id; }

    /** The 3-letter code. */
    private String code=null;
    public String getCode() {
	return code; }
    public void setCode(String code) {
	this.code = code; }

    /** The name. */
    private String name=null;
    public String getName() {
        return name; }
    public void setName(String name) {
        this.name = name; }

    // country
    private String country=null;
    public String getCountry() {
	return country; }
    public void setCountry(String country) {
	this.country = country; }

    // the type: FOREST(1), MEDIEVAL(2), ANCIENT(3), or UNKNOWN(4->0)
    // => make bitfield?
    public final static int TYPE_UNKNOWN = 0;
    public final static int TYPE_FOREST = 1;
    public final static int TYPE_MEDIEVAL = 2;
    public final static int TYPE_ANCIENT = 3;
//    public int type=TYPE_UNKNOWN;
    public String type2 = ""; // a string, like "AU" (forests and unknown)

    public String type="";

    public String species=null;

    /** The location (latitude, longitude).  Null means no location
	entered by user (yet). */
    private Location location=null;
    public Location getLocation() {
	return location; }
    public void setLocation(Location location) { // used by: 
	this.location = location; }
    // BUG: but location is mutable, for performance!  so theoretically the user
    // could say getLocation().setSomething() and screw this up ... oops
    // (fortunately this never happens, i think; at least, it doesn't show
    // up when grepping for "getLocation()" and "=" without "==", so it doesn't
    // happen in any 1-line expression, at least.)

    /** The altitude, in meters. */
    public Integer altitude=null;

    /** All samples with a filename starting with this, belong to this
	site.  This is just a hack, until a new file format, which
	contains the site explicitly
	("<code>&lt;site&gt;ZKB&lt;/site&gt;</code>") is in place. */
    private String folder=null;
    public String getFolder() {
	return folder; }
    public void setFolder(String folder) {
	this.folder = folder; }

    /** Comments. */
    private String comments=null;
    public String getComments() {
	return comments; }
    public void setComments(String comments) {
	this.comments = comments; }

    /** A pretty-printer for sites.
       @return the site in human-readable form */
    public String toString2() {
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
    /*
     ok, i officially don't like toString() any more.  it can be really useful, but i
     want to have more than one toString() and choose among them when i use
     it -- toString() and toStringWithLocation(), perhaps, and then say add(site :toString 'toStringWithLocation);
     */

    public String toXML() {
	StringBuffer buf = new StringBuffer("   <site>\n");
	appendIfNonNull(buf, getCountry(), "country");
	appendIfNonNull(buf, code, "code");
	// BUG: need to escape &'s?
	appendIfNonNull(buf, getName(), "name");
	appendIfNonNull(buf, getID(), "id");
	appendIfNonNull(buf, species, "species");
	appendIfNonNull(buf, type, "type");
	appendIfNonNull(buf, getFolder(), "filename"); // "filename"? -- wouldn't "folder" be better?
	appendIfNonNull(buf, getLocation(), "location");
	appendIfNonNull(buf, getComments(), "comments");
	buf.append("   </site>\n");
	return buf.toString(); }

    // append "<tag>value</tag>\n" to buf if value!=null
    private void appendIfNonNull(StringBuffer buf, Object value, String tag) {
	if (value != null)
	    buf.append("      <" + tag + ">" + value + "</" + tag + ">\n"); }

    // make this customizable? -- (use map.Pallette?  no...)
    // -- use type.contains("F") => green, etc.?  (.indexOf(TYPE_FOREST), really)
    // RENAME: just getColor() -- but don't rename it until it's been moved
    // REFACTOR: belongs in SiteRenderer, not here
    public Color getSiteColor() {
        // better to hook this into some sort of scheme-chooser.  what for?  color ...
        // -- by species
        // -- by country
        // -- by epoch
        // and maybe even others i can't think of
        int x;
        try {
            x = Integer.parseInt(type); // was: type2
        } catch (NumberFormatException nfe) {
            x = TYPE_UNKNOWN;
        }

        switch (x) {
            case TYPE_FOREST:
                return FOREST_COLOR;
            case TYPE_MEDIEVAL:
                return MEDIEVAL_COLOR;
            case TYPE_ANCIENT:
                return ANCIENT_COLOR;
            default:
                return UNKNOWN_COLOR;
        }
    }

    // colors
    private static Color FOREST_COLOR = add70percentWhite(Color.green);
    private static Color MEDIEVAL_COLOR = add70percentWhite(Color.red);
    private static Color ANCIENT_COLOR = add70percentWhite(Color.blue);
    private static Color UNKNOWN_COLOR = add70percentWhite(Color.lightGray);
    private static Color add70percentWhite(Color c) {
        return ColorUtils.blend(c, 0.3f, Color.white, 0.7f);
    }

    // for law of demeter, because 99% of the time you want my
    // location, you want it for the distance to another site.
    public int distanceTo(Site s2) {
        return location.distanceTo(s2.location); }
}
