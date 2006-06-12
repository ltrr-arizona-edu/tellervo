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

import corina.core.App;
import corina.util.ColorUtils;
import corina.util.StringUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;

/*
  todo:
  -- add string i/o (of xml tags)
  -- encapsulate members better
 -- javadoc!

  -- implement toString(), so i can see what's going on!
*/

/**
   Site object, which holds name/ID information, location, and
   other assorted data.  For example, lists of masters and non-fits.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Site implements Cloneable {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		// WAS: SiteDB.getSiteDB().fireSiteIDChanged(this);
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
		// WAS: SiteDB.getSiteDB().fireSiteCodeChanged(this);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		// WAS: SiteDB.getSiteDB().fireSiteNameChanged(this);
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
		// WAS: SiteDB.getSiteDB().fireSiteCountryChanged(this);
	}

	// the type: FOREST(1), MEDIEVAL(2), ANCIENT(3), or UNKNOWN(4->0)
	// => make bitfield?
	public final static int TYPE_UNKNOWN = 0;
	public final static int TYPE_FOREST = 1;
	public final static int TYPE_MEDIEVAL = 2;
	public final static int TYPE_ANCIENT = 3;

	//    public int type=TYPE_UNKNOWN;
	
	// eh? what is this?
	//public String type2 = ""; // a string, like "AU" (=ancient+unknown)

	// 10-may-2003: what i want:
	// in xml:
	//   <types>ancient,forest</types> (empty string = none = same as no tag)
	// as getter:
	//   bool[4] getTypes()??
	// as setter:
	//   void setTypes(bool[4])??
	// as data structure:
	//   bool[4]
	// migration strategy:
	//   add methods; make old types private; convert to new methods
	// what's on disk now:
	//   "2", or "1,3".  (but trivial to convert -- or, just accept either)
	public boolean[] getTypes() {
		// this is the new api i want.  in the future, i'll just say:
		/*
		 int n = this.types.length;
		 boolean types[] = new boolean[n];
		 for (int i=0; i<n; i++)
		 types[i] = this.types[i]; // -- can i just clone() it, even?
		 return types;
		 */

		// but for now, i've got the old "type=2,3" legacy crap.
		// oh well, i'll just write a simple adapter.
		boolean types[] = new boolean[4];
		for (int i = 0; i < type.length(); i++) {
			char c = type.charAt(i);
			if (c == '0' || c == '4')
				types[3] = true;
			if (c == '3')
				types[0] = true;
			if (c == '2')
				types[1] = true;
			if (c == '1')
				types[2] = true;
		}
		// note: the old way had 0=unkn,1=for,2=med,3=anc;
		// the new way is bool[] { anc, med, for, unkn }
		return types;
	}

	public void setTypes(boolean types[]) {
		// new api, legacy backing.  see getTypes().
		// future:
		/*
		 int n = this.types.length;
		 for (int i=0; i<n; i++)
		 this.types[i] = types[i]; // -- can i just copy it, even?
		 */
		String type = "";
		if (types[0])
			type += "3";
		if (types[1])
			type += "2";
		if (types[2])
			type += "1";
		if (types[3])
			type += "4";
		this.type = type;
	}

	// e.g., [t,t,f,f] => "Ancient, Medieval"
	public String getTypesAsString() {
		// future: types will be native.  until then, just call getTypes().
		boolean types[] = getTypes();

		StringBuffer buf = new StringBuffer();
		if (types[0])
			buf.append("Ancient"); // FIXME: i18n!
		if (types[1]) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append("Medieval");
		}
		if (types[2]) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append("Forests");
		}
		if (types[3]) {
			if (buf.length() > 0)
				buf.append(", ");
			buf.append("Unknown");
		}
		return buf.toString();
	}
	
	public String getTypeString() {
		return type;
	}
	public void setTypeString(String type) {
		this.type = type;
	}

	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}

	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
		// WAS: SiteDB.getSiteDB().fireSiteMoved(this);
	}

	// BUG: but location is mutable, for performance!  so theoretically
	// the user could say getLocation().setSomething() and screw this
	// up ...  oops (fortunately this never happens, i think; at
	// least, it doesn't show up when grepping for "getLocation()" and
	// "=" without "==", so it doesn't happen in any 1-line
	// expression, at least.)
	// -- SOLUTION: make Mutable and Immutable Location classes.

	public Integer getAltitude() {
		return altitude;
	}
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}

	/** All samples with a filename starting with this, belong to this
	 site.  This is just a hack, until a new file format, which
	 contains the site explicitly
	 ("<code>&lt;site&gt;ZKB&lt;/site&gt;</code>") is in place. */
	public String getFolder() {
		return folder;
	}

	//
	// Remove G:\DATA cruft, and silly filename tag in sitedb
	// Also removes data directory, if anyone else is using corina (??)
	// TODO: maybe remove this, once sitedbs are updated?
	public void setFilename(String filename) {
		String folder = filename;
		
		if(filename.startsWith("G:\\DATA\\")) {
			folder = filename.substring(8);
		}
		else if(filename.startsWith(App.prefs.getPref("corina.dir.data") + File.separator)) {
			folder = filename.substring(App.prefs.getPref("corina.dir.data").length() + File.separator.length());
		}
		
		// replace forward slashes with a :, which will be our path separator.
		folder = folder.replace("\\", ":");
		// do the same for some other platform
		folder = folder.replace(File.separator, ":");
		
		setFolder(folder);
	}
	
	public void setFolder(String folder) {
		this.folder = folder;
		// WRITEME: fire event!
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
		// WRITEME: fire event!
	}

	/** A pretty-printer for sites.
	 @return the site in human-readable form */
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("[Site: ");

		// name, code, id
		result.append("name='" + name + "' ");
		result.append("code='" + code + "' ");
		result.append("id='" + id + "' ");

		// location, if available
		if (location != null)
			result.append("location=" + location + " ");

		// return it
		result.append("]");
		return result.toString();
	}

	/*
	 ok, i officially don't like toString() any more.  it can be
	 really useful, but i want to have more than one toString() and
	 choose among them when i use it -- toString() and
	 toStringWithLocation(), perhaps, and then say add(site :toString
	 'toStringWithLocation);
	 */

	/**
	 Make an XML String which represents this Site.

	 @return this Site as an XML String
	 */
	public String toXML() {
		String lineSeparator = System.getProperty("line.separator");
		
		StringBuffer buf = new StringBuffer("   <site>" + lineSeparator);
		appendIfNonNull(buf, getCountry(), "country", lineSeparator);
		appendIfNonNull(buf, code, "code", lineSeparator);
		appendIfNonNull(buf, getName(), "name", lineSeparator);
		appendIfNonNull(buf, getId(), "id", lineSeparator);
		appendIfNonNull(buf, species, "species", lineSeparator);
		appendIfNonNull(buf, type, "type", lineSeparator);
		appendIfNonNull(buf, getFolder(), "folder", lineSeparator); 
		
		Location loc = getLocation();
		if(loc != null)
			appendIfNonNull(buf, loc.toISO6709(), "location", lineSeparator);
		
		appendIfNonNull(buf, getComments(), "comments", lineSeparator);
		buf.append("   </site>" + lineSeparator);
		return buf.toString();
	}

	// append "<tag>value</tag>\n" to buf if value!=null and value!=""
	private void appendIfNonNull(StringBuffer buf, Object value, String tag, String lineSeparator) {
		if (value == null || value.equals(""))
			return;

		// escape for XML!! AGH! Yay for no more corrupted site databases.
		String outvalue = StringUtils.escapeForXML(value.toString());

		buf.append("      <" + tag + ">" + outvalue + "</" + tag + ">" + lineSeparator);
		
	}

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

	// temp?  though it may be otherwise useful...
	public boolean isForest() {
		return (type.indexOf("" + TYPE_FOREST) != -1);
	}

	// colors
	private static Color FOREST_COLOR = whiten(new Color(22, 144, 58)); // green

	private static Color MEDIEVAL_COLOR = whiten(new Color(196, 28, 28)); // red

	private static Color ANCIENT_COLOR = whiten(new Color(25, 81, 162)); // blue

	private static Color UNKNOWN_COLOR = whiten(new Color(128, 128, 128)); // gray

	private static Color whiten(Color c) {
		return c; // WAS: ColorUtils.blend(c, 0.3f, Color.white, 0.7f);
	}

	// for law of demeter, because 99% of the time you want my
	// location, you want it for the distance to another site.
	public int distanceTo(Site s2) {
		return location.distanceTo(s2.location);
	}

	//
	// equals/hashCode
	//
	public boolean equals(Object o2) {
		// WRITEME: compare name,code,id,location,altitude,country,species,type,comments,folder?
		Site s2 = (Site) o2;
		return eq(name, s2.name) && eq(code, s2.code) && eq(id, s2.id)
				&& eq(location, s2.location) && eq(altitude, s2.altitude)
				&& eq(country, s2.country) && eq(species, s2.species)
				&& eq(type, s2.type) && eq(comments, s2.comments)
				&& eq(folder, s2.folder);
	}

	// o1.equals(o2), but valid for null/null as well.
	// also, null string can equal null for this compare.
	private boolean eq(Object o1, Object o2) {
		if(o1 == null && o2 != null)
			return o2.equals("");
		if(o2 == null && o1 != null)
			return o1.equals("");
		
		if (o1 == null)
			return (o2 == null);
		else
			return o1.equals(o2);
	}

	public int hashCode() {
		int x = 0;
		x += hash(name) + hash(code) + hash(id);
		x += hash(location) + hash(altitude) + hash(country);
		x += hash(species) + hash(type);
		x += hash(comments);
		x += hash(folder);
		return x;
	}

	// .hashCode(), or 0 if null
	private int hash(Object o) {
		return (o == null ? 0 : o.hashCode());
	}
	
	// make a duplicate copy of this site.
	public Object clone() {
		Site clone = new Site();

		clone.id = new String(id);
		clone.code = new String(code);
		clone.name = new String(name);
		if(country != null)
			clone.country = new String(country);
		clone.type = new String(type);
		clone.species = new String(species);
		if(location != null)
			clone.location = (Location) location.clone();
		if(altitude != null)
			clone.altitude = new Integer(altitude);
		if(folder != null)
			clone.folder = new String(folder);
		if(comments != null)
			clone.comments = new String(comments);
		
		return clone;
	}
	
	private String id = ""; 			// The 3-digit identification number.
	private String code = "";			// The 3-letter code.
	private String name = "";   		// Site name
	private String country = null;		// country
	private String type = "";			// Site type
	private String species = "";		// Species
	private Location location = null;	// The location (latitude, longitude).  
										// Null means no location entered by user (yet).
	private Integer altitude = null;	// The altitude, in meters.
	private String folder = null;		// The folder path which contains the file
	private String comments = null;		// Site comments
}
