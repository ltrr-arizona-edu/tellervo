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

package corina;

import corina.formats.WrongFiletypeException;

import java.io.File;
import java.io.IOException;

import java.util.Map;

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
    public boolean active;
    public final String filename;
    public Map details=null;

    public long lastModified=-1;

    private Range range=null;
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

    public Exception error=null; // not used here!  (but probably should be!)

    // public Element() { filename=null; } // HACK!  just for Summary.java

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
	this.filename = filename;
    }

    /**
       Return the state of the Element's active flag.

       @return true if this Element is active
    */
    public boolean isActive() {
	return active;
    }

    /**
       Return the Element's filename.

       @return the filename this Element refers to
    */
    public String getFilename() {
	return filename;
    }

    /**
       Return this Element's filename, with
       <code>corina.dir.data</code> replaced by an "@", if it's in a
       subfolder of that.  (Otherwise, returns the absolute filename.)

       @return the filename, with @'s
    */
    public String getFilenameWithAts() {
	String root = System.getProperty("corina.dir.data");

	// no known root, or isn't a subfolder of root
	// (BUG: i don't think this is 100% correct, for various reasons)
	if (root == null && !filename.startsWith(root))
	    return filename;

	// BUG: file.sep is often (always?) redundant here
	return "@" + File.separator + filename.substring(root.length());
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
	return new Sample(filename);
    }

    // dead samples should be dimmed or something
    private boolean dead=false;

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
	    details = s.meta;
	    range = s.range;
	    lastModified = new File(filename).lastModified();
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
        return (term!=null && term.equalsIgnoreCase("B"));
    }
    public boolean hasPith() {
        String pith = (String) details.get("pith");
        return (pith!=null && pith.equalsIgnoreCase("Y"));
    }
    public int numSapwood() {
        Integer sapwood = (Integer) details.get("sapwood");
        return (sapwood==null ? 0 : sapwood.intValue());
    }

    //
    // NEW: lazy-load interface
    //
    public Object getMeta(String field) {
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

	return details.get(field);
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
