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
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;

import java.lang.reflect.Method;

// REFACTOR: move i/o to a separate class?
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

    // TODO: call the file "Corina Sites", not "Site DB"

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

    // save the sitedb to disk -- not used yet!
    void saveDB() throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(getDBFilename()));

        w.write("<?xml version=\"1.0\"?>");
        w.newLine();
        w.write("<sitedb>");
        w.newLine();

	// (loop for s in sites do (write w (site-to-xml s)))
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
	    w.write(s.toXML());
        }

        w.newLine();
        w.write("</sitedb>");
        w.newLine();

        w.close();
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
		    site.setCountry(data);
		else if (state.equals("code"))
		    site.setCode(data);
		else if (state.equals("name"))
		    site.setName(data);
		else if (state.equals("id"))
		    site.setID(data);
		else if (state.equals("species"))
		    site.species = data;
		else if (state.equals("type")) {
                    site.type = data;
		} else if (state.equals("filename")) { // shouldn't this be "folder"?
		    site.setFolder(data);
		} else if (state.equals("location")) {
		    site.setLocation(new Location(data));
		} else if (state.equals("comments")) {
		    site.setComments(data);
		} else {
		    // else ... what?
		    return;
		}

		// something matched => reset data
                data = "";
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

    public Site getSite(String code) throws SiteNotFoundException {
        // return the site with code |code|
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.getCode().equals(code))
                return s;
        }
        throw new SiteNotFoundException();
    }

    public Site getSite(File folder) throws SiteNotFoundException {
        // return the site for folder |folder|
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (matchesFilename(folder.getPath(), s))
                return s;
        }
        throw new SiteNotFoundException();
    }

    public Site getSite(Sample sample) throws SiteNotFoundException {
        String filename = (String) sample.meta.get("filename");

        // make sure it's been saved
        if (filename == null)
            throw new IllegalArgumentException();

        // look through the database for that filename
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.getFolder() == null)
                continue;

            // i think this might not always work, depending on OS, rel/abs filenames, etc. -- no, it seems to...
            if (matchesFilename(filename, s))
                return s;
        }

        throw new SiteNotFoundException();
    }

    private boolean matchesFilename(String filename, Site site) {
        return filename.startsWith(site.getFolder()) ||
	       filename.startsWith(System.getProperty("corina.dir.data") + File.separator + site.getFolder()) ||
	       filename.startsWith(System.getProperty("corina.dir.data") + site.getFolder());
	// this matches if (1) folder is relative,
	//                 (2) absolute and dir.data has no file.sep, or
	//                 (3) absolute and dir.data ends with file.sep
    }

    // is this still used?
    public Site getSite(Location l) throws SiteNotFoundException {
        // return the site at |l|.  if there's more than one, return
        // an arbitrary one.
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.getLocation()!=null && s.getLocation().equals(l))
                return s;
        }

        // none was there, but let's look for something nearby.
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.getLocation() == null)
                continue; // wha?
            if (s.getLocation().isNear(l, 10))
                return s;
        }
        throw new SiteNotFoundException();
    }

    // ---------------------------------------------------------------------------

    // (loop for s in +sitedb+ when (eq loc (site-location s)) collect s)
    public Site[] getSitesAt(Location loc) {
        List output = new ArrayList();

        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (loc.equals(s.getLocation()))
                output.add(s);
        }

        return (Site[]) output.toArray(new Site[0]);
    }

    // return an array of the 2-letter codes of all countries represented in the sitedb
    public String[] getCountries() {
        int n = sites.size();
        Set countries = new HashSet();
        for (int i=0; i<n; i++) {
            Site s = (Site) sites.get(i);
            countries.add(s.getCountry());
        }
        return (String[]) countries.toArray(new String[0]);
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

        // print = { print header, print EACH country, print footer }
        // print country = { print header, print EACH site, print footer }
        // print site = { ... about 7 lines ... }

        // -- embed stylesheet in header; there won't be multiple sitedb.html's floating around,
        // so there's no reason to complicate things by keeping it separate.
        // (UNLESS that's the only way to switch on media -- ???)
        w.close();
    }

    // ----
    // event handling -- stolen from Sample.java
    // REFACTOR: extract event handling to external (abstract) class?
    private Vector listeners = new Vector();

    public synchronized void addSiteDBListener(SiteDBListener l) {
        if (!listeners.contains(l))
            listeners.add(l);
    }
    public synchronized void removeSiteDBListener(SiteDBListener l) {
        listeners.remove(l);
    }

    private void fireSiteEvent(String method, Site source) {
        // alert all listeners
        Vector l;
        synchronized (this) {
            l = (Vector) listeners.clone();
        }

        int size = l.size();

        if (size == 0)
            return;

        SiteEvent e = new SiteEvent(source);

        for (int i=0; i<size; i++) {
            SiteDBListener listener = (SiteDBListener) l.elementAt(i);

            // this is like "listener.method(e)", though it's not terribly elegant.
            try {
                Method m = SiteDBListener.class.getMethod(method, new Class[] { SiteEvent.class });
                m.invoke(listener, new Object[] { e });
            } catch (Exception ex) {
                // just ignore them all... (?)
            }
        }
    }

    public void fireSiteMoved(Site source) { fireSiteEvent("siteMoved", source); }
    public void fireSiteNameChanged(Site source) { fireSiteEvent("siteNameChanged", source); }
    public void fireSiteCodeChanged(Site source) { fireSiteEvent("siteCodeChanged", source); }
    public void fireSiteIDChanged(Site source) { fireSiteEvent("siteIDChanged", source); }
    public void fireSiteCommentsChanged(Site source) { fireSiteEvent("siteCommentsChanged", source); }
    public void fireSiteCountryChanged(Site source) { fireSiteEvent("siteCountryChanged", source); }

    /*
      NOTICE!  when you add a site event, it needs to be added to
      -- SiteDBListener, SiteDBAdapter
      -- SiteDB (new fireXXX() method)
      -- SiteDB (call save() in its own listener)
     */

    // ------------------------------------------------------
    // keep disk updated
    static {
	getSiteDB().addSiteDBListener(new SiteDBListener() {
		public void siteMoved(SiteEvent e) { save(e); }
		public void siteNameChanged(SiteEvent e) { save(e); }
		public void siteCodeChanged(SiteEvent e) { save(e); }
		public void siteIDChanged(SiteEvent e) { save(e); }
		public void siteCountryChanged(SiteEvent e) { save(e); }
		public void siteCommentsChanged(SiteEvent e) { save(e); }
		private void save(SiteEvent e) {
		    System.out.println("saving sitedb (" + ((Site) e.getSource()).getCode() + " just changed)...");
		    /* TODO:
		       -- actually call save() here
		       -- the first time it fails, let the user know.
		       -- (and give the user options: try again, cancel, don't warn again?)
		       -- call it after a delay: every 10sec or so, say, in a background thread
		       -- (if it's updated, then again 1sec later, cancel the first one, of course)
		       -- if user quits corina before it would have run, make sure to explicitly run it
		     */
		}
	    });
    }
}
