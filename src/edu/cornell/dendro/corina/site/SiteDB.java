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

package edu.cornell.dendro.corina.site;

import java.awt.print.Printable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * A database of sites.
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 * @version $Id$
 */
/*
 * TODO: -- move to SiteFile, extends SiteStorage,
 */
public class SiteDB { // implements PrintableDocument {
	// make it able to import/export/append stuff?

	// TODO: call the file "Corina Sites", not "Site DB"

	// TODO: add undo support.  this will probably consist of 2
	// stacks, with an inner class Undo (field, oldval, newval),
	// and undo()/redo(), canUndo()/canRedo() methods.
	// (new SiteList(SiteDB) will actually bind them to accel-Z, accel-shift-Z.)

	// TODO: move all i/o to its own (non-public) class, like
	// SiteDBFile.  that's about 50% of the code in here, and will
	// make things easier to work with, i presume.

	// list of the sites -- use a different data struct? (private?)
	public List sites = null;

	private static SiteDB db = null;

	// returns null on failure
	public static SiteDB getSiteDB() {
		if (db == null) {
			db = new SiteDB();
			db.sites = new ArrayList();
			try {
				db.loadDB();
			} catch (IOException ioe) {
				// !!! this is an important one.  why don't we throw this?
				System.out.println("ioe! -- " + ioe);
				ioe.printStackTrace();
				db.sites = null;
			}
			// this debug saves after loading.
			/*
			finally {
				try {
					db.saveDB();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			*/
		}
		// return it
		return db;
	}
	
	/* Return true on a successful save! */
	public boolean save() {
		System.out.println("Saving Site DB!");
		try {
			saveDB();
			return true;
		} catch (IOException ioe) {
			System.out.println("trying to save db, ioe=" + ioe);
			ioe.printStackTrace();
		}
		return false;
	}
	

	// this causes all sorts of failures if corina.dir.data==null!
	// OBSOLETE: moved to SiteDBFile.getDBFilename() -- only used in this file
	// for watching for file changes, which should either be moved to SiteDBFile,
	// or at least use SiteDBFile.getFilename().
	private static String getDBFilename() {
		return App.prefs.getPref("corina.dir.data") + File.separator
				+ "Site DB";
	}

	private void loadDB() throws IOException {
		System.out.println("reloading database");

		try {
			boolean lock = getLock(getDBFilename());
			if(!lock) {
				throw new IOException("Could not lock file for loading");
			}

			// create XML reader
			XMLReader xr = XMLReaderFactory.createXMLReader();

			// i'm just updating myself now! -- don't worry, after
			// loadDB() returns, the events get fired (up one level).
			selfUpdating = true;

			// set it up as a sitedb loader
			SiteDBLoader loader = new SiteDBLoader();
			xr.setContentHandler(loader);
			xr.setErrorHandler(loader);

			// load it -- use FileInputStream, InputStreamReader to force UTF-8
			modDate = new File(getDBFilename()).lastModified(); // RACE: lock file during entire load!
			db.startWatcher();
			InputStream is = new FileInputStream(getDBFilename());
			Reader r = new InputStreamReader(is, "UTF8");
			xr.parse(new InputSource(r));

			// done updating
			selfUpdating = false;

			// let it go
			// FIXME: make this final?  see same call in saveDB() for discussion
			r.close();
			Lock.release(getDBFilename());
		} catch (SAXException se) {
			// ack! don't keep the lock on an error!
			selfUpdating = false;			
			Lock.release(getDBFilename());
			throw new IOException(se.getMessage());
		} catch (IOException ioe) {
			selfUpdating = false;
			Alert.error("Error loading site database", 
					"There was an error while loading the site database.\n" + 
					"Most likely this means your data directory is set improperly.\n" + 
					"Various corina functions will NOT work without a site database.\n\n" + 
					"Error: " + ioe.toString());
			throw ioe;
		}
	}

	// save the sitedb to disk -- not used yet!
	// FIXME: lock file during entire save, and set |modDate|
	// TODO: what happens if i throw an ioe?  restore a backup?
	private void saveDB() throws IOException {
		boolean lock = getLock(getDBFilename());
		if(!lock) {
			throw new IOException("Could not lock file for saving");
		}

		// this is a sanity check. shouldn't happen.
		if(selfUpdating) {
			throw new IOException("SELF UPDATING ALREADY, DURING SAVE??");
		}
		
		selfUpdating = true;

		// use utf-8!
		File outfile = new File(getDBFilename() + " - Saving");
		OutputStream os = new FileOutputStream(outfile);
		Writer wr = new OutputStreamWriter(os, "UTF8");
		BufferedWriter w = new BufferedWriter(wr);

		try {
			w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			w.newLine();

			w.write("<!--");
			w.newLine();
			
			w.write(" WARNING WARNING WARNING WARNING WARNING WARNING ");
			w.newLine(); w.newLine();
			w.write("   This file is NOT MANUALLY EDITABLE. Modifying this file in a text editor such as notepad");
			w.newLine();
			w.write("   will destroy the internationalized text contained within.");
			w.newLine();
			w.write("   DO NOT EDIT UNLESS YOU ARE COMPLETELY SURE YOU KNOW WHAT YOU ARE DOING");
			w.newLine(); w.newLine();
			w.write(" WARNING WARNING WARNING WARNING WARNING WARNING ");
			w.newLine(); w.newLine();

			w.write(" File automatically generated by Corina, the Cornell Tree Ring Analysis system.");
			w.newLine();
			w.write(" " + nameCreator());
			w.newLine();
			
			w.write("-->");
			w.newLine();
			
			w.newLine();
			w.write("<sitedb>");
			w.newLine();

			w.newLine();

			// (loop for s in sites do (write w (site-to-xml s)))
			for (int i = 0; i < sites.size(); i++) {
				Site s = (Site) sites.get(i);
				w.write(s.toXML());
				w.newLine();
			}

			w.newLine();
			w.write("</sitedb>");
			w.newLine();
		} catch (Exception e) {
			// an error.. writing the file? ack!
			// clean up and bail!
			outfile.delete();
			selfUpdating = false;
			Lock.release(getDBFilename());

			if(e instanceof IOException)
				throw (IOException) e;
			else
				e.printStackTrace();
		} finally {
			try {
				w.close();
			} catch (IOException ioe) {
				// an error.. writing the file? ack!
				// clean up and bail!
				outfile.delete();
				selfUpdating = false;
				Lock.release(getDBFilename());
				
				throw ioe;
			}
		}

		// only after complete success do we rename the file.
		File realoutfile = new File(getDBFilename());
		
		// first, move the Site DB to Site DB - Old YYYYMMDD HHMMSS
		Date now = new Date();
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
		File oldoutfile = new File(getDBFilename() + " - Old " + dFormat.format(now));
		realoutfile.renameTo(oldoutfile);
		// then, since realoutfile changed, reinit it...
		realoutfile = new File(getDBFilename());
		// and finally, move the new sitedb to the real sitedb.
		if(outfile.renameTo(realoutfile) == false) {
			System.out.println("Couldn't rename siteDB! I should handle this better!");
		}
		
		// before you unlock it, update |modDate|, so it doesn't look
		// like it was changed by somebody else.
		modDate = new File(getDBFilename()).lastModified();
		selfUpdating = false;
		
		// let it go
		// FIXME: should this be finally?  or would that cause problems, if i don't restore a backup?
		Lock.release(getDBFilename());
	}

	// XML loader ------------------------------------------------------------
	private class SiteDBLoader extends DefaultHandler {
		private String state = "";

		private String data = "";

		private Site site = null;

		@Override
		public void startElement(String uri, String name, String qName,
				Attributes atts) {
			if (name.equals("site"))
				site = new Site();
			else
				state = name;
		}

		@Override
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
					site.setId(data);
				else if (state.equals("species"))
					site.setSpecies(data);
				else if (state.equals("type")) {
					site.setTypeString(data);
				} else if (state.equals("filename")) { 
					// shouldn't this be "folder"?
					// yes, it should. setFileName converts to folder for compatibility with older SiteDBs.
					site.setFilename(data);
				} else if (state.equals("folder")) { 
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

		@Override
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
		for (int i = 0; i < sites.size(); i++) {
			Site s = (Site) sites.get(i);
			if (s.getCode().equals(code))
				return s;
		}
		throw new SiteNotFoundException();
	}
	
	private String folderToLocalPath(String folder) {
		String s = folder.replace(":", File.separator);
		
		return App.prefs.getPref("corina.dir.data") + File.separator + s;
	}

	public Site getSite(File folder) throws SiteNotFoundException {
		// return the site for folder |folder|
		for (int i = 0; i < sites.size(); i++) {
			Site s = (Site) sites.get(i);
			if (matchesFilename(folder.getPath(), s))
				return s;
		}
		throw new SiteNotFoundException();
	}

	public Site getSite(Sample sample) throws SiteNotFoundException {
		String filename = (String) sample.getMeta("filename");

		// make sure it's been saved
		if (filename == null)
			throw new IllegalArgumentException();

		// hack: no sites
		if (sites == null)
			throw new SiteNotFoundException();

		// look through the database for that filename
		for (int i = 0; i < sites.size(); i++) {
			Site s = (Site) sites.get(i);
			if (s.getFolder() == null)
				continue;

			// (i think this might not always work, depending on OS, rel/abs filenames, etc.
			// -- no, it seems to...)
			if (matchesFilename(filename, s))
				return s;
		}
		throw new SiteNotFoundException();
	}

	// returns true, iff |filename| represents a file in |site|
	private boolean matchesFilename(String filename, Site site) {
		String folder = site.getFolder();
		if (folder == null)
			return false;

		// convert our folder to contain our local path.
		folder = folderToLocalPath(folder).toUpperCase();

		// chop off any trailing separators
		if (folder.endsWith(File.separator))
			folder = folder.substring(0, folder.length()
					- File.separator.length());

		// find the last separator
		if (folder.lastIndexOf(File.separator) != -1)
			folder = folder.substring(folder.lastIndexOf(File.separator) + 1,
					folder.length());

		// find the last separator, but do the same with the file
		if (filename.lastIndexOf(File.separator) != -1)
			filename = filename
					.substring(filename.lastIndexOf(File.separator) + 1,
							filename.length());

		return filename.toUpperCase().startsWith(folder);

		//||
		//  filename.startsWith(App.prefs.getPref("corina.dir.data") + File.separator + folder) ||
		//  filename.startsWith(App.prefs.getPref("corina.dir.data") + folder);
		// this matches if (1) folder is relative,
		//                 (2) absolute and dir.data has no file.sep, or
		//                 (3) absolute and dir.data ends with file.sep
		// FIXME: folder will now always be relative to location of sitedb file
	}

	// is this still used?
	public Site getSite(Location l) throws SiteNotFoundException {
		// return the site at |l|.  if there's more than one, return
		// an arbitrary one.
		for (int i = 0; i < sites.size(); i++) {
			Site s = (Site) sites.get(i);
			if (s.getLocation() != null && s.getLocation().equals(l))
				return s;
		}

		// none was there, but let's look for something nearby.
		for (int i = 0; i < sites.size(); i++) {
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

		for (int i = 0; i < sites.size(); i++) {
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
		for (int i = 0; i < n; i++) {
			Site s = (Site) sites.get(i);
			countries.add(s.getCountry());
		}
		return (String[]) countries.toArray(new String[0]);
	}

	// return a list containing all of the site names
	public List getSiteNames() {
		List names = new ArrayList();

		for (int i = 0; i < sites.size(); i++) {
			String name = ((Site) sites.get(i)).getName();
			if (name != null && !names.contains(name))
				names.add(name);
		}
		return names;
	}

	// -----------------------------------------------------------------------------
	// demeter, again.  (SELECT country FROM sites GROUP BY COUNT(?))
	public List getCountriesInOrder() {
		// get countries
		String cs[] = getCountries();
		int n = cs.length; // (number of unique countries)

		// put in array of tuples
		Tuple tuple[] = new Tuple[n];
		for (int i = 0; i < n; i++)
			tuple[i] = new Tuple(cs[i]);

		// loop through sites, counting frequency
		for (int i = 0; i < sites.size(); i++) {
			// country
			String c = ((Site) sites.get(i)).getCountry();

			// look up in list, and count it
			for (int j = 0; j < tuple.length; j++)
				if (tuple[j].name.equals(c))
					tuple[j].freq++;
		}

		// sort (freq, then name)
		Arrays.sort(tuple);

		// copy names to array, and return
		List l = new ArrayList();
		for (int i = 0; i < n; i++)
			l.add(tuple[i].name);
		return l;
	}

	private static class Tuple implements Comparable {
		String name;

		int freq = 0;

		Tuple(String name) {
			this.name = name;
		}

		public int compareTo(Object o2) {
			Tuple t2 = (Tuple) o2;
			if (freq < t2.freq)
				return +1;
			else if (freq > t2.freq)
				return -1;
			else
				return Country.getName(name)
						.compareTo(Country.getName(t2.name));
		}
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

	/*
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
	 // is this me?  then drop it.
	 if (selfUpdating)
	 return;

	 // alert all listeners
	 Vector l;
	 synchronized (this) {
	 l = (Vector) listeners.clone();
	 }

	 int size = l.size();

	 if (size == 0)
	 return;

	 SiteEvent e = new SiteEvent(source);

	 try {
	 Class types[] = new Class[] { SiteEvent.class };
	 Method m = SiteDBListener.class.getMethod(method, types);
	 Object args[] = new Object[] { e };

	 for (int i=0; i<size; i++) {
	 SiteDBListener listener = (SiteDBListener) l.elementAt(i);

	 // this is like "listener.method(e)", though it's not terribly elegant.
	 m.invoke(listener, args);
	 }
	 } catch (Exception ex) {
	 // just ignore them all... (?)
	 }

	 // when you're done telling everybody in this JVM, update the
	 // disk file so other people can find out.
	 save();
	 }

	 public void fireSiteMoved(Site source) { fireSiteEvent("siteMoved", source); }
	 public void fireSiteNameChanged(Site source) { fireSiteEvent("siteNameChanged", source); }
	 public void fireSiteCodeChanged(Site source) { fireSiteEvent("siteCodeChanged", source); }
	 public void fireSiteIDChanged(Site source) { fireSiteEvent("siteIDChanged", source); }
	 public void fireSiteCommentsChanged(Site source) { fireSiteEvent("siteCommentsChanged", source); }
	 public void fireSiteCountryChanged(Site source) { fireSiteEvent("siteCountryChanged", source); }
	 */

	/*
	 NOTICE!  when you add a site event, it needs to be added to
	 -- SiteDBListener, SiteDBAdapter
	 -- SiteDB (new fireXXX() method)
	 -- SiteDB (call save() in its own listener)
	 */

	// ------------------------------------------------------
	// keep disk updated with my copy
	// BUG: don't use a listener, because this means when somebody
	// else changed it, i'll save it again, whis downright wrong.
	private static boolean selfUpdating = false;


	// ------------------------------------------------------
	// keep disk updated with my copy
	private long modDate;

	private void startWatcher() { // FIXME: if a method, make sure i get called only once
		// FIXME: run at low priority

		Runnable r = new Runnable() {
			public void run() {
				// file to check
				File f = new File(getDBFilename());

				for (;;) {
					
					if(selfUpdating) {
						// sleep 1sec, don't load! causes corruption!
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {}
						continue;
					}
					
					// check moddate on disk file
					long diskModDate = f.lastModified();

					// has it been changed?  better re-load.
					if (diskModDate > modDate) {
						// erase existing database
						db.sites = new ArrayList();

						// PERF: most of the database will be the same ... way to re-use
						// old database, instead of letting it all get gc'd?

						// load fresh
						try {
							db.loadDB();
						} catch (IOException ioe) {
							System.out.println("ioe -- " + ioe);
							ioe.printStackTrace();
						}

						// FIXME: need new event model
						// -- listeners listen on the database, not one site, so deal with it
						// -- sites need an id number (but users should never see it)
						// -- a site-db-changed-event must contain:
						// ---- a type of event (reloaded, site-added, site-removed, site-changed)
						// ---- if it's site-added or site-removed, which site(s) were affected
						// ---- if it was a site-changed event, which site(s) and fields were affected

						System.out.println("throwing load events");
						// DESIGN: how do i say "everything changed!"?
						// if the file changed, i have no idea what to fire!
						// (fire everything?)
						// selfUpdating = true;
						for (int i = 0; i < db.sites.size(); i++) {
							Site s = (Site) db.sites.get(i);
							// WAS: db.fireSiteNameChanged(s);
						}
						// db.fireSiteNameChanged((Site) db.sites.get(0)); // how about just 1?
						// selfUpdating = false;
					}

					// sleep 10sec
					try {
						Thread.sleep(1 * 1000);
					} catch (InterruptedException ie) {
						// ignore
					}
				}
			}
		};

		Thread t = new Thread(r);
		t.start();
	}

	// --------------------------------------------------
	// printing
	// FIXME: move to PrintableDocument method of MapFrame
	public Printable print() {
		return new SitePrinter(sites);
	}
	
    private static String nameCreator() {
        Date date = new Date();
        String dateString = DateFormat.getDateInstance().format(date);
        String timeString = DateFormat.getTimeInstance().format(date);

        Object args[] = new Object[] {
        		System.getProperty("user.name", "(unknown user)"),
        		dateString,
        		timeString,
        };

        String byline = MessageFormat.format(I18n.getText("saved_by"), args);
        return byline;
    }

    private boolean getLock(String fullFilename) {
		// lock the file
		boolean lock = false;

		while (!lock) {
			lock = Lock.acquire(fullFilename);
			if (!lock) {
				String labels[] = { "Try again", "Delete it", "Cancel" };

				int ret = JOptionPane
						.showOptionDialog(
								null,
								"The site database appears to be locked. If nobody else is Using Corina,\n" +
								"it's likely that this is a stale lock." +
								"What should I do?\n",
								"Can't access site database",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, labels,
								labels[0]);

				switch (ret) {
				case 0:
					continue;
				case 1:
					Lock.release(fullFilename);
					continue;
				case 2:
					return false;
				}
			}
		}
		return true;
    }
}