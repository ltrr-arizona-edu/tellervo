package corina.site;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import corina.core.App;
import corina.map.Location;

class SiteDBFile {
  // WRITEME: (file name)
  static String getFilename() {
    return App.prefs.getPref("corina.dir.data") + File.separator + "Site DB";
  }

  // WRITEME: public static SiteDB loadDB()
  // WRITEME: public static void saveDB(SiteDB)
  // (that ok?)

  // XML saver -- saved the sitedb to disk
  // FIXME: lock file during entire save, and set |modDate|
  // TODO: what happens if i throw an ioe?  restore a backup?
  // TODO: since i could be waiting for a while, and doing i/o, i should probably run this in
  // its own thread, right?
  void saveDB() throws IOException {
  	// lock the file
  	boolean x = Lock.acquire(getFilename(), 20); // 20 tries = 10 sec
  	if (x == false) {
	    throw new IOException("Can't lock file \"Site DB\"."); // OAOO: name of file
	    // FIXME: ask user if he wants to break the lock here
	    /*
	      Can't lock file 'Site Database'.

	      Corina puts a "lock" on the the Site Database file so multiple
	      users can ???.  However, if your computer crashed, the lock could
	      get stuck.  If you're the only user, click "Steal"...  Otherwise, wait? ...

	      ( Cancel )			( Keep Waiting ) ( Steal Lock )
	    */
  	}

    BufferedWriter w = new BufferedWriter(new FileWriter(getFilename()));

    try {
      w.write("<?xml version=\"1.0\"?>");
      w.newLine();

      // TODO: put a header comment here saying what this file is,
      // who created it (corina), where to find corina (url), etc.
      // so if somebody stumbles across this file and decides to read it,
      // they can understand what it is, where it came from, how to read it, etc.

      w.newLine();

      w.write("<sitedb>");
      w.newLine();

      w.newLine();

      // (loop for s in sites do (write w (site-to-xml s)))
      for (int i=0; i<sites.size(); i++) {
        Site s = (Site) sites.get(i);
        w.write(s.toXML());
      }

      w.newLine();
      w.write("</sitedb>");
      w.newLine();
    } finally {
      try {
        w.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

  	// let it go
  	// FIXME: should this be finally?  or would that cause problems, if i don't restore a backup?
  	Lock.release(getFilename());
  }

  // XML loader ------------------------------------------------------------
  private List sites;

  // TODO: need to synch this?  make it static?
  // @return a List of Site objects (in no particular order)
  List loadDB() throws IOException {
    try {
      // lock the file
      boolean x = Lock.acquire(getFilename(), 20); // 20 tries = 10 sec

      // create list to put sites in
      sites = new ArrayList();

      // create XML reader
      XMLReader xr = XMLReaderFactory.createXMLReader();

      // set it up as a sitedb loader
      SiteDBLoader loader = new SiteDBLoader();
      xr.setContentHandler(loader);
      xr.setErrorHandler(loader);

      // load it
      // DISABLED: modDate = new File(getFilename()).lastModified(); // RACE: lock file during entire load!
      // DISABLED: db.startWatcher();
      FileReader r = new FileReader(getFilename());
      xr.parse(new InputSource(r));

      // let it go
      // FIXME: make this final?  see same call in saveDB() for discussion
      Lock.release(getFilename());
      return sites;
    } catch (SAXException se) {
      throw new IOException(se.getMessage());
    }
  }

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
    		  site.setSpecies(data);
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
}