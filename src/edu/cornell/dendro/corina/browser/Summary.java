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

package edu.cornell.dendro.corina.browser;

import edu.cornell.dendro.corina.BaseSample;
import edu.cornell.dendro.corina.ObsFileElement;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.SampleHandle;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.util.GreedyProgressMonitor;
import edu.cornell.dendro.corina.util.GZIP;
import edu.cornell.dendro.corina.metadata.*;
import edu.cornell.dendro.corina.core.App;

import java.io.File;
import java.io.Writer;
import java.io.Reader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable; // consolidate my maps!  at least explain why i pick each.
import java.util.Iterator;

import java.awt.Component;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
   WRITEME

   -- what is summary?  example input, example file, example output (table).

   <h2>Left to do</h2>
   <ul>
     <li>move this to corina.Summary (it's used by both Search and Browser)
     <li>make summary file hidden on Windows, too (is this possible?)
     <li>get rid of the Swing ProgressMonitor stuff?  i don't think that belongs here.
         (but i may need some way to indicate progress -- perhaps accessors for
	 filesDone / filesTotal)
     <li>specify encoding (UTF-8) in XML header, for completeness
   </ul>

   <pre>
   used for:
   - browser (really fast summaries)
   - search (really fast searches)
   </pre>
*/
public class Summary {
	/**
	 Save an element to a writer.

	 @param e the element to save
	 @param w the writer to save it to
	 @exception IOException if something goes wrong
	 */
	private void saveElement(SampleHandle sh, BufferedWriter w) throws IOException {
		String name = StringUtils.escapeForXML(sh.getElement().getBasename());
		w.write("  <sample filename=\"" + name + "\" " + "modified=\""
				+ sh.getLastModified() + "\">");
		w.newLine();

		BaseSample bs = sh.getBaseSample();
		
		// extras, for which no metadata field exists (???)
		w.write("    <range>" + bs.getRange() + "</range>");
		w.newLine();

		Iterator i = MetadataTemplate.getFields();
		while (i.hasNext()) {
			MetadataField f = (MetadataField) i.next();
			String field = f.getVariable();
			if (bs.hasMeta(field)) { // |details| should be |meta|
				Object value = bs.getMeta(field);
				String text = StringUtils.escapeForXML(value.toString());
				w.write("    <" + field + ">" + text + "</" + field + ">");
				w.newLine();
			}
		}

		// filetype isn't a normal metadata field (yet),
		// so i have to store that by hand, as well.
		w.write("    <filetype>" + bs.getMeta("filetype") + "</filetype>");
		w.newLine();

		w.write("  </sample>");
		w.newLine();

		w.newLine();
	}

	/** The filename to store cache files under: ".Corina_Cache". */
	//public static final String CACHE_FILENAME = ".Corina_Cache";
	
	private Component parent;

	// ****************************************
	/*
	 TODO: make background thread to stat() summary file every minute or so (?)
	 to keep browsers in sync (across systems) automatically.

	 BAD IDEA: make browser descend |corina.dir.data| and create
	 cache files in the background, on its own, if they don't exist?
	 no, don't.  just create them lazily.  or, if you add the
	 Sources:Library junk, add it (in the background) when the user
	 adds/selects the folder as their data source.

	 (TODO: add size to files (dendro, other) -- <sample filename="" size="">
	 -- not terribly important, since i only need to stat() the file for that.)

	 (TODO: add public interface for getting folders, other files?)
	 */
	private Map<String, Object> items = new HashMap(); // String filename => {MyFolder, MyFile, Element}

	private static final String FOLDER = "--folder--";

	private static class MyFile {
		long lastMod;

		MyFile(long lastMod) {
			this.lastMod = lastMod;
		}
	}

	public Iterator getFilenames() { // might be synchro issues here...
		// FIXME: see listener invocation for ideas.
		// probably need to clone the keyset first in a synchro block.
		return items.keySet().iterator();
	}

	public SampleHandle getSampleHandle(String filename) { // or null, if it's not an element -- FIXME?
		Object o = items.get(filename);
		if (o instanceof SampleHandle)
			return (SampleHandle) o;
		else
			return null;
	}

	/** Should compression (gzip) be used when saving?  (It
	 autodetects compression when loading.)

	 <p>This seems to have practically no effect, at least for
	 local files.  (ACM is 365 KB without compression, 21 KB with
	 compression, so it might make a difference over the
	 network. */
	private final static boolean USE_COMPRESSION = true; // false for debugging, true for shipping

	// print out each file i need to load?
	private final static boolean verbose = false; // true for debugging, false for shipping

	/**
	 Save the summary.

	 @exception IOException if something goes wrong
	 */
	private void saveSummary() throws IOException {
		//String fullFilename = folder + File.separator + CACHE_FILENAME;
		String fullFilename = cacheNameForFolder();
		
		File basedir = new File(fullFilename).getParentFile();
		if(!basedir.exists())
			basedir.mkdir();

		try {

			/* No locking; file is local. 
			boolean lock = Lock.acquire(fullFilename);
			if (!lock) {
				System.out.println("BUG: can't lock cache file! (FIXME)" + fullFilename);
				throw new IOException("can't lock cache file");
			}
			*/

			Writer fw;
			if (USE_COMPRESSION)
				fw = new OutputStreamWriter(new GZIPOutputStream(
						new FileOutputStream(fullFilename)), "UTF-8");
			else
				fw = new OutputStreamWriter(
						new FileOutputStream(fullFilename), "UTF-8");
			// BUG: this uses native encoding; it should use UTF-8 everywhere!
			BufferedWriter w = new BufferedWriter(fw);

			w.write("<?xml version=\"1.0\" encoding=\"UTF8\"?>");
			w.newLine();

			w.newLine();

			w.write("<summary>");
			w.newLine();

			w.newLine();

			// TODO: do folders need a moddate?  (a folder's moddate is
			// really the moddate of the latest thing in it.)  if they do,
			// then, it should actually be the moddate of the folder which
			// CONTAINS it -- all folders in a summary should have the
			// same moddate, that is.  should i do that, for consistency,
			// or should i just leave it out?  dunno...

			// write out all items
			Iterator keys = items.keySet().iterator();
			while (keys.hasNext()) {
				String filename = (String) keys.next();
				Object item = items.get(filename);
				if (item == FOLDER) {
					File f = new File(filename);
					String encodedFilename = StringUtils.escapeForXML(f
							.getName());
					w.write("  <folder filename=\"" + encodedFilename + "\"/>");
					w.newLine();
				} else if (item instanceof SampleHandle) {
					saveElement((SampleHandle) item, w);
				} else { // instanceof MyFile
					long mod = ((MyFile) item).lastMod;
					File f = new File(filename);
					w.write("  <file filename=\""
							+ StringUtils.escapeForXML(f.getName()) + "\" "
							+ "modified=\"" + mod + "\"/>");
					w.newLine();
				}
			}

			w.write("</summary>");
			w.newLine();

			w.close();

		} finally {
			// always release lock
			//Lock.release(fullFilename);
		}
	}

	/**
	 Load an already-written summary file.

	 @exception FileNotFoundException if the cache file isn't present
	 @exception IOException if something goes wrong
	 */
	private void loadSummary() throws FileNotFoundException, IOException,
			SAXParseException {	
		
		String fullFilename = cacheNameForFolder();

		/* NO LOCKING!! This file is local.
		// lock the file
		boolean lock = false;

		while (!lock) {
			lock = Lock.acquire(fullFilename);
			if (!lock) {
				String labels[] = { "Try again", "Delete it", "Cancel" };

				int ret = JOptionPane
						.showOptionDialog(
								parent,
								"The folder "
										+ folder
										+ " appears to be locked. If nobody else is Using Corina, it's likely that this is a stale lock. What should I do?",
								"Can't access " + folder,
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
					return;
				}
			}
		}
		*/
		
		monitor.setProgressGreedy(0);

		try {
			// make a reader for the file
			Reader r;
			if (GZIP.isCompressed(fullFilename))
				r = new InputStreamReader(new GZIPInputStream(
						new FileInputStream(fullFilename)), "UTF-8");
			else
				r = new InputStreamReader(new FileInputStream(fullFilename),
					"UTF-8");

			// BUG: use UTF-8 always, not native encoding!

			// make a new XML parser
			XMLReader xr = XMLReaderFactory.createXMLReader();

			// ... configure it to use a my SampleHandler ...
			SummaryLoader loader = new SummaryLoader();
			xr.setContentHandler(loader);
			xr.setErrorHandler(loader);

			// ... and feed it the file
			xr.parse(new InputSource(r));
		} catch (SAXException se) {
			//Lock.release(fullFilename);
			// DEBUG: explicit debugging info
			System.out.println("sax exception = " + se);
			
			if (se instanceof SAXParseException) {
				SAXParseException spe = (SAXParseException) se;
				System.out.println("row=" + spe.getLineNumber() + ", " + "col="
						+ spe.getColumnNumber());

				throw (SAXParseException) se;
			}

			throw new IOException(); // INTERFACE: be explicit!
		}
		/*
		finally {
			Lock.release(fullFilename);
		}

		// release the lock
		Lock.release(fullFilename);
		*/
	}

	// BUG: summary doesn't store "filetype" field?

	/** SAX handler for loading summary files. */
	private class SummaryLoader extends DefaultHandler {
		private String field;

		private StringBuffer data;

		private ObsFileElement element;
		private SampleHandle sampleHandle;
		private BaseSample baseSample;

		@Override
		public void startDocument() {
			items = new HashMap();
			// Q: what if items already exists?  can that ever happen?  (i think that's ok now)
		}

		@Override
		public void endDocument() {
			// do nothing
		}

		@Override
		public void startElement(String uri, String name, String qName,
				Attributes atts) throws SAXException {
			data = new StringBuffer();

			if (name.equals("summary")) {
				// ignore
			} else if (name.equals("folder")) {
				// folder: add to list
				items.put(atts.getValue("filename"), FOLDER);
			} else if (name.equals("file")) {
				// file: add to list
				items.put(atts.getValue("filename"), new MyFile(Long
						.parseLong(atts.getValue("modified"))));
			} else if (name.equals("sample")) {
				element = new ObsFileElement(folder + File.separator
						+ atts.getValue("filename"));
				sampleHandle = new SampleHandle(element);
				
				// we're creating a base sample here
				baseSample = new BaseSample();
				sampleHandle.setBaseSample(baseSample);
				
				// -- legal?  (why?)  (why not?)
				sampleHandle.setLastModified(Long.parseLong(atts.getValue("modified")));
				// ooooh.  i guess i'll have to hold temps until i'm ready
				// to make the element, then do it all at once.
			} else {
				// it's a field
				field = name;
			}
		}

		@Override
		public void endElement(String uri, String name, String qName) {
			if (name.equals("sample")) {
				// add to list, and reset for next element
				items.put(element.getBasename(), sampleHandle);
				sampleHandle = null;
				element = null;
				baseSample = null;
			} else if (name.equals("folder")) {
				// folder: ignore
			} else if (name.equals("file")) {
				// file: ignore
			} else if (name.equals("summary")) {
				// end of file: ignore
			} else if (MetadataTemplate.isField(field)) {
				// TODO: if it's an int, parse it, right?
				baseSample.setMeta(field, data.toString());
			} else if (field != null && field.equals("range")) {
				// it's a range

				// TESTING: i've got nulls still... (why?)
				if (!data.toString().equals("null")) {
					Range r = new Range(data.toString());
					baseSample.setRange(r);
				}
			} else if (field != null && field.equals("filetype")) {
				// it's the filetype -- (not a normal metadata field, yet)
				if (!data.toString().equals("null")) {
					baseSample.setMeta("filetype", data.toString());
				}
			} else {
				System.out.println("not a field: what's this mean?  |" + field
						+ "|");
			}

			// (reset for next field)
			field = null;

			// (why's this here, and not in Element.java?  or at least a factory?)
			// -- also need to deal with <range>
			// -- also need to deal with sample's filename="..." attribute.
		}

		@Override
		public void characters(char ch[], int start, int length) {
			data.append(new String(ch, start, length));
		}
	}

	/*
	 issue: what if one person changes a file while somebody else has
	 a browser window open?  it won't know to update itself, so it'll
	 be out-of-date.  ouch.

	 possibility: re-stat the summary file fairly often (10-15s?),
	 and make sure file edits make it back there immediately.

	 so i might need a refresh() method here (returns true if
	 changed?), or maybe recheck whenever a new Summary is made (?).
	 */

	/*
	 PROBLEM: if the browser is looking at a folder, and you decide
	 to search that folder, it'll have to make a new Summary, and
	 that means it'll have to re-load the folder.  there shouldn't be
	 any synchro issues with that, but i'd rather not have to reload
	 a (possibly large) file and re-stat every file in the folder and
	 create another large data structure if i don't have to.  and i
	 don't have to:

	 TODO: change new Summary() into Summary.getSummary().  store a
	 list of Summary objects, by their folder name.  if an
	 already-requested folder is requested again, return the same
	 object (they're immutable, right?).

	 PROBLEM: if 2 different browsers ask for an Element, and one
	 modifies it, then either

	 -- i'm returning different Elements to each, and they have to
	 watch the filesystem for changes (slow and awkward), or

	 -- i'm returning the same Element to each, and a Browser has to
	 watch for changes in an Element (i have no code for)
	 */

	/**
	 Create a new summary, load the necessary data, and update the
	 cache file on disk, if necessary.  This constructor may take a
	 while to return.

	 @param folder the folder to make a summary of
	 @exception IOException if something goes wrong
	 @exception FileNotFoundException if the folder doesn't exist,
	 or isn't a folder
	 */
	public Summary(String folder, GreedyProgressMonitor monitor, Component parent) throws IOException,
			FileNotFoundException {
		this.folder = folder; // store folder
		this.monitor = monitor; // store monitor
		this.parent = parent;
		update(); // update myself
	}

	private GreedyProgressMonitor monitor;

	// FNFE if |folder| doesn't exist, or isn't a folder.
	private void update() throws IOException, FileNotFoundException {
		// load the summary, if there is one
		try {
			loadSummary();
		} catch (FileNotFoundException fnfe) {
			monitor.setNote("Summary does not exist, creating...");
			// that's ok (but any other ioe is not, so it still gets thrown)
		} catch (SAXParseException spe) {
			monitor.setNote("Parsing error (corrupt summary?), recreating cache...");
		   // there was a parser error...
		}

		// make sure folder exists, and is a folder
		File parent = new File(folder);
		if (!parent.exists() || !parent.isDirectory())
			throw new FileNotFoundException();

		// re-list this folder
		File files[] = parent.listFiles();

		// - for every file in the summary but not in the directory, remove it
		Iterator keys = items.keySet().iterator();
		while (keys.hasNext()) {
			String filename = (String) keys.next();

			if (!haveFile(files, filename))
				keys.remove(); // WAS: items.remove(filename);
		}

		// - for every file in the directory but not in the summary, add it (with a moddate in the past?)
		for (int i = 0; i < files.length; i++) {
			// let's ignore hidden files
			if (files[i].isHidden())
				continue;

			// folders
			if (files[i].isDirectory()) {
				// set it to |folder|, no matter what it was
				items.put(files[i].getName(), FOLDER);
			}

			// others -- put everything in "others" now, and when loading them, move to "elements"
			if (!files[i].isDirectory()) {
				if (items.containsKey(files[i].getName())) {

					Object o = items.get(files[i].getName());
					if (o == FOLDER)
						items.put(files[i].getName(), new MyFile(1492L)); // files[i].lastModified()));

					continue;
				}

				items.put(files[i].getName(), new MyFile(1492L)); // files[i].lastModified()));
			}
		}

		// (TEMP: for now, |files| are just filenames, so i'll need to try loading all of them, too.)
		for (Iterator i = items.keySet().iterator(); i.hasNext(); /*--*/) {
			String fn = (String) i.next();
			Object o = items.get(fn);
			if (o instanceof MyFile) {
				ObsFileElement e = new ObsFileElement(folder + File.separator + fn);
				SampleHandle sh = new SampleHandle(e);

				// if disk copy isn't newer, skip it.
				long diskModDate = new File(e.getFilename()).lastModified();
				long summaryModDate = ((MyFile) o).lastMod;
				if (diskModDate <= summaryModDate)
					continue;

				try {
					if (verbose)
						System.out.println("reloading " + fn);
					sh.getBaseSample();
					sh.setLastModified(diskModDate);
					items.put(fn, sh);
				} catch (IOException ioe) {
					// can't load -> stays |FILE|.
					// just update the moddate.
					((MyFile) o).lastMod = diskModDate;
				}
			}
		}

		int done = 0;

		//  - finally, for every element, stat() the file, and if it's newer, re-load it.
		for (Iterator i = items.keySet().iterator(); i.hasNext(); /*--*/) {
			String fn = (String) i.next();
			Object o = items.get(fn);
			if (o instanceof SampleHandle) {
				SampleHandle sh = (SampleHandle) o;
				long diskMod = new File(sh.getElement().getFilename()).lastModified();
				if (sh.getLastModified() < diskMod) {
					try {
						if (verbose)
							System.out.println("reloading(2) " + fn);
						sh.reset();
						sh.getBaseSample();
						sh.setLastModified(diskMod);
					} catch (IOException ioe) {
						items.put(fn, new MyFile(diskMod));
					}
				}
			}

			monitor.setProgressGreedy(++done);
		}

		// PERF: lastMod() gets called twice.  perhaps loadMeta() should
		// check the moddate -- "loadMeta() = load meta if disk is newer".

		// save the summary
		saveSummary();
	}

	/*
	 AUUGHHH.  this is getting really messy, and duplicates are a mess.
	 how about a (filename -> whatever it is) hash?
	 MASTERS    => 'folder (singleton?  null object?)
	 ACM101.IND => (size, modified, ptr to element)
	 ACM101.XLS => (size, modified)
	 things this helps:
	 - no duplicates!
	 - easy to "upgrade" a file to an element, or "downgrade" an element to a file
	 - no need for 3 lists
	 ...
	 */

	/*
	 TODO: a folder should have some idea of how big it is -- i want
	 to display bigger folders with a different icon so you can tell
	 at a glance how much stuff is in one of them.
	 */

	// looks awfully familiar ... IDEA: write a contains()
	// which takes a list/collection and a field, like my sort().
	// x = Contains.contains(files, "name", name);
	private boolean haveFile(File files[], String name) {
		// (*) elements have fully-qualified names, while samples/folders don't.  ick.
		name = new File(name).getName(); // -- substring with last index of file.sep better?

		for (int i = 0; i < files.length; i++)
			if (files[i].getName().equals(name))
				return true;
		return false;
	}

	/** The folder which I'm summarizing.  It's set by the constructor,
	 and never again (immutable). */
	private String folder;

	/** Testing: call "java -cp Corina.jar corina.browser.Summary ABC"
	 to create a summary file for the folder ABC. */
	public static void main(String args[]) throws IOException {
		// use crimson
		if (System.getProperty("org.xml.sax.driver") == null)
			System.setProperty("org.xml.sax.driver",
					"org.apache.crimson.parser.XMLReaderImpl");

		if (args.length != 1) {
			System.out.println("need 1 arg: folder name!");
			System.exit(1);
		}
		String folder = args[0];

		// make a summary
		long t1 = System.currentTimeMillis();
		// Summary s = new Summary(folder); -- DISABLED, no monitor here
		long t2 = System.currentTimeMillis();
		System.out.println("took " + (t2 - t1) + " ms to create summary");

		// --
		// TODO:
		// -- will i need an event framework, so the view knows when they're loaded?
		//
		// ...
		// TODO:
		// -- convert new Element() to Element.makeElement()
		// -- this allows you to return the same Element, and not load it multiple times
		// -- ...
		//
		// SEARCHING:
		// -- this makes db-style searching a folder REALLY FAST
		// -- add that:  i want to be able to say:
		/*
		 Summary f = new Summary(some_folder);
		 f.updateIfNeeded(); // (might be a little slow)
		 Search s = new Search(); // look for all indexed oak samples
		 s.addCriterion(new Criterion("format", Criterion.EQUALS, "I"));
		 s.addCriterion(new Criterion("species", Criterion.STARTS_WITH, "QU"));
		 s.run(); // (super-fast!)
		 Element e[] = s.getResults();
		 */
		// -- add this as Edit->Find..., and also as "Advanced..." button next to browser search field
	}

	// get the local cache file...
	private String cacheNameForFolder() {
		return App.prefs.getCorinaDir() + "Cache" + File.separator + folder.replaceAll("[\\./\\\\:]", "_") + ".cache";
	}
}
