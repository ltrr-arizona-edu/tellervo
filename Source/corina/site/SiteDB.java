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

import corina.Sample;
import corina.map.Location;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
   A simple in-memory (and one-file) database of sites.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class SiteDB {

    // make it able to import/export/append stuff?

    // make site an inner class?

    // list of the sites -- use a different data struct? (private?)
    public List sites=null;

    private static SiteDB db=null;

    // returns null on failure
    public static SiteDB getSiteDB() {
        if (db == null) {
            db = new SiteDB();
            db.sites = new ArrayList();
            try {
                db.loadDB();
            } catch (IOException ioe) {
                // !!! this is an important one.  why don't we throw this?
                db.sites = null;
            }
        }

        // return it
        return db;
    }

    // this causes all sorts of failures if corina.dir.data==null!
    private String getDBFilename() {
        return System.getProperty("corina.dir.data") + File.separator + "Site DB";
    }
    
    void loadDB() throws IOException {
        try {
            // create XML reader
            XMLReader xr = XMLReaderFactory.createXMLReader();

            // set it up as a sitedb loader
            SiteDBLoader loader = new SiteDBLoader();
            xr.setContentHandler(loader);
            xr.setErrorHandler(loader);

            // load it
            FileReader r = new FileReader(getDBFilename());
            xr.parse(new InputSource(r));
        } catch (SAXException se) {
            throw new IOException(se.getMessage());
        }
    }

    // save the sitedb to disk -- is this never used?
    void saveDB() throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(getDBFilename()));

	w.write("<?xml version=\"1.0\"?>");
	w.newLine();
	w.newLine();
	w.write("<sitedb>");
	w.newLine();
	w.newLine();

	for (int i=0; i<sites.size(); i++) {
	    Site s = (Site) sites.get(i);

	    w.write("<site>");
	    w.newLine();
            maybeSave(w, s.country, "country");
            maybeSave(w, s.code, "code");
	    // need to escape &'s?
            maybeSave(w, s.name, "name");
            maybeSave(w, s.id, "id");
            maybeSave(w, s.species, "species");
            maybeSave(w, s.type2, "type");
            maybeSave(w, s.filename, "filename");
            maybeSave(w, s.location, "location");

	    w.write("</site>");
	    w.newLine();

	    w.newLine();
	}

	w.newLine();
	w.write("</sitedb>");
	w.newLine();

	w.close();
    }

    // save "<tag>value</tag>\n" to w if value!=null
    private void maybeSave(BufferedWriter w, Object value, String tag) throws IOException {
        if (value != null) {
            w.write("    <" + tag + ">" + value + "</" + tag + ">");
            w.newLine();
        }
    }

    // XML loader ------------------------------------------------------------
    private class SiteDBLoader extends DefaultHandler {
	private String state="";
	private String data="";
	private Site site=null;
	public void startElement(String uri, String name, String qName, Attributes atts) {
	    if (name.equals("site"))
		site = new Site();
	    else
		state = name;
	}
	public void endElement(String uri, String name, String qName) {
	    if (name.equals("site")) {
		sites.add(site);
		site = null;
	    } else {
		// ignore whitespace
		data = data.trim();
		if (data.length() == 0)
		    return;

		// ignore if site==null, meaning tag outside of <site>
		if (site == null)
		    return;

		// parse -- use hashtable?
		if (state.equals("country"))
		    site.country = data;
		else if (state.equals("code"))
		    site.code = data;
		else if (state.equals("name"))
		    site.name = data;
		else if (state.equals("id"))
		    site.id = data;
		else if (state.equals("species"))
		    site.species = data;
		else if (state.equals("type")) {
		    // number style
		    site.type2 = data;

		    // verbose style
		    if (data.equals("forest"))
			site.type = Site.TYPE_FOREST;
		    else if (data.equals("medieval"))
			site.type = Site.TYPE_MEDIEVAL;
		    else if (data.equals("ancient"))
			site.type = Site.TYPE_ANCIENT;
		} else if (state.equals("filename")) {
		    site.filename = data;
		} else if (state.equals("location")) {
		    site.location = new Location(data);
		} else {
		    // else ... what?
		    return;
		}

		// something matched => reset data
		data = new String();
	    }
	}
	public void characters(char ch[], int start, int length) {
	    // stringify
	    data += new String(ch, start, length);
	}
    }

    // -----------------------------------------------------------------------------

    // query functions -- only simple ones here, complex sql-selects
    // and stuff can go in their own class.

    public Site getSite(String code) {
	// return the site with code |code|
	for (int i=0; i<sites.size(); i++) {
	    Site s = (Site) sites.get(i);
	    if (s.code.equals(code))
		return s;
	}
	return null;
    }

    public Site getSite(Sample sample) {
        String filename = (String) sample.meta.get("filename");

        // make sure it's been saved
        if (filename == null)
            throw new IllegalArgumentException();

        // look through the database for that filename
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.filename == null)
                continue;

            // i think this might not always work, depending on OS, rel/abs filenames, etc.
            if (filename.startsWith(s.filename) ||
                filename.startsWith(System.getProperty("corina.dir.data") + File.separator + s.filename) ||
                filename.startsWith(System.getProperty("corina.dir.data") + s.filename))
                return s;
        }

        // just return null
        return null;
    }

    public Site getSite(Location l) {
	// return the site at |l|.  if there's more than one, return
	// an arbitrary one.
	for (int i=0; i<sites.size(); i++) {
	    Site s = (Site) sites.get(i);
	    if (s.location!=null && s.location.equals(l))
		return s;
	}
	return null;
    }

    /** Return an array of sites within 100km of the given site.
	@param origin look for sites near this one
	@return an array of Sites within 100km of origin */
    public Site[] getSitesNear(Site origin) {
        Location l1 = origin.getLocation();
        List near = new ArrayList();

        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (l1.isNear(s.getLocation()))
                near.add(s);
        }

        return (Site[]) near.toArray(new Site[0]);
    }

    // !!!REFACTOR: virtually identical to getSitesNear().  ack, no, it's just (collect)...
    public Site[] getSitesAt(Location loc) {
        List output = new ArrayList();

        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (loc.equals(s.getLocation()))
                output.add(s);
        }

        return (Site[]) output.toArray(new Site[0]);
    }

    // -----------------------------------------------------------------------------

    // add feature: dump the entire database into HTML.
    public void toHTML(String filename) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));

	// WRITE ME

	// strategy: make it look similar to what's on the wall now
	// (embed a stylesheet?  sure, not many of these printed)

	// <h2>Turkey</h2>
	// for each country, a table:
	// code | title | epoch | species

	// => use Site.toHTML() for each site.

	// (what sort of database integration will this end up with?
	// number of samples, longest sample, etc. would be really
	// neat)

        w.close();
    }
}	
