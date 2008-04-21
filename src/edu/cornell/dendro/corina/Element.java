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

package edu.cornell.dendro.corina;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.net.URI;
import java.net.URISyntaxException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.io.Files;

/**
   An Element, basically a reference to a Sample (stored on disk).

   <p>Normally used as a member of a SampleSet (though at this time
   SampleSet is only an ArrayList that provides text load/save of its
   elements).</p>

   <p>An Element holds:</p>

   <ul>
     <li>An active flag
     <li>A filename
     <li>A summary of details (the metadata from a Sample)
     <li>The range (updated with summary details)
   </ul>

   <p>The active flag, true by default, indicates whether this Element
   is to be used in a particular operation.  For example, Sum will
   ignore inactive Elements, so users can quickly and easily try
   removing Elements from a Sum to see how the result changes.</p>

   <p>The filename is the unique name of this Sample.  It is expected
   that at this place in the filesystem, there will be a Sample that
   can be loaded by the Sample constructor Sample(String filename).
   There will be trouble (i.e., IOExceptions) if this is not the case.
   An Element's filename is immutable: once an Element has been
   created, it cannot be pointed at any other sample.</p>

   <p>(Note: I have considered adding a new field, filetype, so
   non-auto-detectable filetypes can be loaded, but so far there has
   been no demand for this.)</p>

   <p>An Element also holds space for a summary of its details.  By
   default, this is empty, until the loadMeta() method has been
   called.  After a loadMeta(), there will be a Map of details,
   namely, the metadata fields from the Sample's metadata Map.  The
   entire metadata map is usually 500 bytes or less, so all fields are
   loaded.  (At 500 bytes each, a huge chronology with 200 elements
   would take up 100K for all of the preview information -- usually
   masters are much smaller, so this is only a couple kilobytes.
   There's no reason to try to load only some of the fields.)

   <p>OLD RAMBLINGS I DON'T WANT TO DELETE YET: there should be a way
   to load the Range - incorporate it into meta?.  If it could do
   this, then visual.Bargraph.Bar wouldn't be needed, and visual.*
   could be a lot cleaner, and then I could use a real MVC model for
   everything, and the whole GUI would be cleaner.  How about
   that.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
 */
public class Element implements Comparable {

	// these members should probably be PRIVATE!
	private boolean active;

	public Map<String, Object> details = null;

	// filename is the full path (ie, G:\DATA\ACM\asdf.moo)
	public final String filename;	
	
	// folder is the path under the data directory,
	// using : as a path separator.
	// If we're passed a filename beginning with a ?, treat it as a folder:basename pair
	// can be null if element is not in our data directory tree!
	public String folder = null;
	
	// basename is the name of the file without any path information
	public final String basename;

	private Range range;
	public Range getRange() { // i really should USE this!
		// lazy-load!
		if (details == null)
			try {
				loadMeta();
			} catch (WrongFiletypeException wfte) {
				// System.out.println("wfte!");
				return null;
				// ignore?
			} catch (IOException ioe) {
				System.out.println("on " + filename + ", " + ioe);
				ioe.printStackTrace();
				return null; // !!!
			}

		return range;
	}

	// needed by Summary.  is this bad?  no, because when Element and Sample are
	// merged, it'll be required.  get used to it.
	public void setRange(Range r) {
		this.range = r;
	}

	public Exception error = null; // not used here!  (but probably should be!)

	/**
	 Construct an Element from a filename.  This Element will, by
	 default, be active.

	 @param filename the filename of the Sample to reference
	 */
	public Element(String filename) {
		this(filename, true);
	}

	/**
	 Construct an Element from a filename, and a preset Active flag.

	 @param filename the filename of the Sample to reference
	 @param active true if this Element is to be active
	 */
	public Element(String filename, boolean active) {
		this.active = active;
		
		// if it starts with a ?, this is a relative path, using :'s as separators
		// ie, ?FOREST:ACM:moo123.pik
		if(filename.startsWith("?")) {
			String fn = filename.substring(1);
			
			int pos = fn.lastIndexOf(':');
			
			if(pos >= 0) {
				this.folder = fn.substring(0, pos);
				this.basename = fn.substring(pos + 1, fn.length());
				
				this.filename = App.prefs.getPref("corina.dir.data") + File.separator +
								this.folder.replace(":", File.separator) + File.separator +
								this.basename;
			}
			else {
				this.filename = filename;
				this.basename = new File(filename).getName();
			}
		} else if(filename.startsWith("@")) {
			/* this is from undocumented code in sample.java
			 * I don't know if it means anything, or if it's even used
			 * But, I'm going to wrap this here because I'm forcing everything
			 * to use elements as a go-between for samples for the new databasing
			 * - lucas
			 */
			
			// new @-notation
			// (assumes c.d.r ends with file.sep!)
			this.filename = System.getProperty("corina.dir.data", ".") + filename.substring(1);
			this.basename = new File(this.filename).getName();
		}
		// otherwise, we got passed a whole file name. try and parse it up.
		else {
			String fn = filename;
			
			// if, for some reason, adaptive reading is turned off... 
			// don't bother to parse it!
			if(Boolean.valueOf(App.prefs.getPref("corina.dir.adaptiveread")).booleanValue() == false) {
				this.filename = filename;
				this.basename = new File(filename).getName();
				return;				
			}
			
			// chop off any sort of beginning cruft
			if(fn.startsWith("G:\\DATA\\")) {
				fn = fn.substring(8);
			}
			else if(filename.startsWith(App.prefs.getPref("corina.dir.data") + File.separator)) {
				fn = fn.substring(App.prefs.getPref("corina.dir.data").length() + File.separator.length());
			}
			else {
				// we can't convert this into a special path.
				// leave folder null and bail.
				this.filename = filename;
				this.basename = new File(filename).getName();
				return;
			}
			
			// replace forward slashes with a :, which will be our path separator.
			fn = fn.replace("\\", ":");
			// do the same for some other platform
			fn = fn.replace(File.separator, ":");
			
			int pos = fn.lastIndexOf(':');
			
			if(pos >= 0) {
				this.folder = fn.substring(0, pos);
				this.basename = fn.substring(pos + 1, fn.length());
				
				this.filename = App.prefs.getPref("corina.dir.data", ".") + File.separator +
								this.folder.replace(":", File.separator) + File.separator +
								this.basename;
			}
			else {
				this.filename = filename;
				this.basename = new File(filename).getName();
			}
		}
	}

	/**
	 Return the state of the Element's active flag.

	 @return true if this Element is active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Change the state of the Element's active flag
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 Return the Element's filename.

	 @return the filename this Element refers to
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 Return the Element's basename.

	 @return the basename of the file this Element refers to
	 */
	public String getBasename() {
		return basename;
	}
	
	/**
	 Return the Element's folder path.

	 @return the folder path of the file this Element refers to
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 Return this Element's filename, with
	 <code>corina.dir.data</code> replaced by an "?", if it's in a
	 subfolder of that.  (Otherwise, returns the absolute filename.)

	 @return the filename, with ?'s
	 */
	public String getRelativeFilename() {
		// no basename or folder? return relative path...
		if(basename == null || folder == null)
			return filename;
		
		return "?" + folder + ":" + basename;
	}
	
	/**
	 @return the filename, with ?'s
	 */
	public URI getURI() {	
		try {
			URI baseURI = new File(App.prefs.getPref("corina.dir.data", ".")).toURI();
			
			// no basename or folder? return relative path...
			if(basename == null || folder == null)
				return new URI("file", filename, null);
			
			return baseURI.relativize(new File(filename).toURI());
		} catch (URISyntaxException e) {
			// this shouldn't happen...
			new Bug(e);			
			return null;
		}		
	}

	
	/**
	 Load this Element.  Returns this Element in a Sample object.

	 @return the Sample referenced by this Element
	 @exception IOException if an IOException occurred while trying
	 to load it; this can also be the subclasses
	 FileNotFoundException
	 */
	public Sample load() throws IOException {
		// save metadata before i go?
		//return new Sample(filename)
		return Files.load(filename);
	}
	
	public BaseSample loadBasic() throws IOException {
		Sample s = load();
		return new BaseSample(s);
	}

	// dead samples should be dimmed or something
	private boolean dead = false;

	public void reloadMeta() throws IOException {
		details = null;
		loadMeta();
	}

	/**
	 Load the metadata fields for this Element.

	 @exception IOException if the Element could not be loaded
	 */
	public void loadMeta() throws IOException {
		if (dead)
			throw (IOException) error; // new IOException("dead");

		// this only gets set here, so meta must have been loaded already
		if (details != null) {
			return;
		}

		// load sample, and grab reference to fields (data gets GC'd)
		try {
			Sample s = load();
			details = s.cloneMeta();
			range = s.getRange();
		} catch (IOException ioe) {
			dead = true;
			error = ioe;
			throw ioe;
		}
	}

	/**
	 Return the filename, so Element can be used in making Strings
	 without worrying about getFilename() calls.

	 @return the filename
	 */
	@Override
	public String toString() {
		return filename;
	}

	// comparable
	public int compareTo(Object o) {
		return filename.compareTo(((Element) o).filename);
	}

	// these used to be in Bargraph.java, but LoD pushes them up here.
	// they should probably be in a Metadata class, shared by Element and Sample
	// (or unified sample-element model).
	public boolean hasBark() {
		String term = (String) details.get("terminal");
		return (term != null && term.equalsIgnoreCase("B"));
	}

	public boolean hasPith() {
		String pith = (String) details.get("pith");
		return (pith != null && pith.equalsIgnoreCase("Y"));
	}

	public int numSapwood() {
		Integer sapwood = (Integer) details.get("sapwood");
		return (sapwood == null ? 0 : sapwood.intValue());
	}

	private boolean verifyLoaded() {
		if(dead)
			return false;
		
		if (details == null) {
			try {
				loadMeta();
			} catch (WrongFiletypeException wfte) {
				// System.out.println("wfte!");
				return false;
				// ignore?
			} catch (IOException ioe) {
				System.out.println("on " + filename + ", " + ioe);
				ioe.printStackTrace();
				return false; // !!!
			}
		}
			
		return true;
	}
	
	//
	// NEW: lazy-load interface
	//
	public Object getMeta(String field) {
		if(!verifyLoaded())
			return null;

		return details.get(field);
	}
	
	public boolean hasMeta(String field) {
		if(!verifyLoaded())
			return false;

		return details.containsKey(field);
	}
	
	public void setMeta(String field, Object value) {
		if(!verifyLoaded())
			return;
		
		details.put(field, value);
	}
	
	// does this object represent a real sample?
	public boolean isSample() {
		// if we've already loaded it, it's a sample.
		if (details != null)
			return true;

		// otherwise, try to load it.
		try {
			loadMeta();
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}
}
