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

import corina.files.Filetype;
import corina.files.WrongFiletypeException;
import corina.graph.Graphable;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;

import java.lang.reflect.Method;

import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

/**
   Class representing a reading of a dendro sample.

   <p>Currently, this stores:</p>

   <ul>
     <li>series: data, count, wj (up/down, in one list)</li>
     <li>range</li>
     <li>a hashtable for metadata</li>
     <li>a list of elements</li>
   </ul>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

// idea: make a samplefactory, so 2 calls to sample(filename) return
// the same object.  better yet, an editor factory so a second
// editor(sample) bringstofront the existing editor.

public class Sample implements Previewable, Graphable {
    
    /** The value of a missing ring, 2.  Anything less than or equal
	to this value is considered a MR. */
    public static final int MR = 2;

    /** Data, as a List of Integers. */
    public List data;

    /** Data range. */
    public Range range;

    /** Sample metadata, as a (String, Object) Map.  The following
	table lists the standard keys, their data types, and valid
	values:

<table border="1">
    <tr>
      <th>Internal name (key)</th>
      <th>Data type</th>
      <th>Valid values</th>
    </tr>
    <tr>
      <td>id</td>
      <td>Integer</td>
      <td></td>
    </tr>
    <tr>
      <td>title</td>
      <td>String</td>
      <td></td>
    </tr>
    <tr>
      <td>dating</td>
      <td>String</td>
      <td>A, R</td>
    </tr>
    <tr>
      <td>unmeas_pre</td>
      <td>Integer</td>
      <td></td>
    </tr>
    <tr>
      <td>unmeas_post</td>
      <td>Integer</td>
      <td></td>
    </tr>
    <tr>
      <td>filename</td>
      <td>String</td>
      <td></td>
    </tr>
    <tr>
      <td>comments</td>
      <td>String</td>
      <td></td>
    </tr>
    <tr>
      <td>type</td>
      <td>String</td>
      <td>S, H, C</td>
    </tr>
    <tr>
      <td>species</td>
      <td>String</td>
      <td></td>
    </tr>
    <tr>
      <td>sapwood</td>
      <td>Integer</td>
      <td></td>
    </tr>
    <tr>
      <td>pith</td>
      <td>String</td>
      <td>+, *, N</td>
    </tr>
    <tr>
      <td>terminal</td>
      <td>String</td>
      <td>v, vv, B, W</td>
    </tr>
    <tr>
      <td>continuous</td>
      <td>String</td>
      <td>C, R, N</td>
    </tr>
    <tr>
      <td>quality</td>
      <td>String</td>
      <td>+, ++</td>
    </tr>
    <tr>
      <td>format</td>
      <td>String</td>
      <td>R, I</td>
    </tr>
    <tr>
      <td>index_type</td>
      <td>Integer</td>
      <td></td>
    </tr>
    <tr>
      <td>reconciled</td>
      <td>String</td>
      <td>Y,N</td>
    </tr>
    <tr>
      <td>author</td>
      <td>String</td>
      <td></td>
    </tr>
    <tr>
      <td>modified?</td>
      <td>Boolean</td>
      <td></td>
    </tr>
</table>

	<code>data</code>, <code>count</code>, <code>range</code>,
	<code>wj</code>, and <code>elements</code> aren't stored in
	<code>meta</code> - they're their own members.

	@see corina.files.Corina */
    public Map meta;
    
    /** Number of samples in the sum at any given point. */
    public List count=null;

    // weiserjahre
    public List incr=null, decr=null;

    // does it have weiserjahre?
    public boolean hasWeiserjahre() {
	return (incr != null);
    }

    /** Elements (in a List) that were put into this sum. */
    public List elements=null;

    /** Default constructor.  Defaults:
	<ul>
	  <li><code>data</code> and <code>count</code> are initialized but empty
	  <li><code>range</code> is initialized
	  <li><code>meta</code> is initialized, and:
	      <ul>
	        <li>Tag "title" is set to "Untitled"
		<li>Tag "author" is set to the value of system property
		    <code>user.name</code>
	      </ul>
	  <li><code>wj</code> is <code>null</code>
	  <li><code>elements</code> is <code>null</code>
	</ul>
	@see #meta */
    public Sample() {
	// make defaults: empty
	data = new ArrayList();
	range = new Range();
	meta = new HashMap();

	// store username, if known
	if (System.getProperty("user.name") != null)
	    meta.put("author", System.getProperty("user.name"));

	// initialize empty metadata with defaults?
	meta.put("title", "Untitled");
    }

    /** Create a new Sample from a given file on disk.
	@param filename the name of the file to load
	@exception FileNotFoundException if the file doesn't exist
	@exception WrongFiletypeException if the file is not a Sample
	@exception IOException if there is an I/O error while loading
	the file */
    public Sample(String filename) throws FileNotFoundException, WrongFiletypeException, IOException {
	// make it like any other Sample
	this();

	// load the file; this call throws the exceptions
	load(filename);

	// 99%+ of the time you're loading a sample from disk, you
	// won't be adding to it, so trim it.
	trimAllToSize();
    }

    private void trimAllToSize() {
	((ArrayList) data).trimToSize();
	if (count != null)
	    ((ArrayList) count).trimToSize();
	if (hasWeiserjahre()) {
	    ((ArrayList) incr).trimToSize();
	    ((ArrayList) decr).trimToSize();
	}
    }

    // copy each part of source to target.  shallow copy, no events, etc.
    public static void copy(Sample source, Sample target) {
        target.data = source.data;
        target.range = source.range;
        target.count = source.count;
        target.meta = source.meta;
        target.incr = source.incr;
        target.decr = source.decr;
        target.elements = source.elements;
    }
    
    /** Return true if the sample is indexed, else false.
	@return true if the sample is indexed */
    public boolean isIndexed() {
	String type = (String) meta.get("format");
	return (type!=null && Character.toUpperCase(type.charAt(0))=='I');
    }

    /** <p>Return true if the sample is summed, else false.  Here
	"summed" is defined as:</p>
	<ul>
	  <li>has a list of elements, or
	  <li>has count data
	</ul>
	@return true if the sample is summed */
    public boolean isSummed() {
	return (elements!=null || count!=null);
    }

    /** Return true if the sample is absolutely dated, else false.
	@return true if the sample is absolutely dated */
    public boolean isAbsolute() {
	String dating = (String) meta.get("dating");
	return (dating!=null && Character.toUpperCase(dating.charAt(0))=='A');
    }

    private boolean modified = false;

    /** Return true if the file was modified since last save.
	@return if the sample has been modified */
    public boolean isModified() {
	return modified;
    }

    /** Set the modified flag. */
    public void setModified() {
	modified = true;
    }

    /** Clear the modified flag. */
    public void clearModified() {
	modified = false;
    }

    /** Return the data for a graph.
	@return data to graph, as a List of Integers */
    public List getData() {
	return data;
    }

    /** Return the start date for a graph.
	@return start date of data to graph */
    public Year getStart() {
	return range.getStart();
    }

    /** Return the default scale factor for graphing.
	@return scale factor of 1.0, or 0.1 for indexed files */
    public double getScale() {
	return (isIndexed() ? 0.1 : 1.0);
    }

    /** Return the sample's title.
	@return the "title" tag from meta */
    public String toString() {
        String name = meta.get("title") + " " + range.toStringWithSpan();
        if (isModified()) // not aqua-ish, but how to do it the real way?
            name = "* " + name;
        return name;
    }

    /** Count the total number of rings.  If this is a raw sample,
	returns the length.  If this is a summed sample, returns the
	sum of the count List.
	@return the total number of rings in this sample */
    public int countRings() {
        // it's not a sum, so the number of rings is just the length
        // (if (null count) (length data) ...
        if (count == null)
            return data.size();

        // it's a sum, so the number of rings is the sum of the number
        // of measurements for each year
        // ... (apply '+ count))
        int n = 0, size = count.size();
        for (int i=0; i<size; i++)
            n += ((Integer) count.get(i)).intValue();
        return n;
    }

    // radius of the sample; only relevant for raw samples (better to
    // retun 0.0 for indexed sample?)
    public int computeRadius() {
        // (apply '+ data)
        int n = data.size();
        int sum = 0;
        for (int i=0; i<n; i++)
            sum += ((Number) data.get(i)).intValue();
        return sum;
    }

    // number of intervals with >3 samples
    public int count3SampleIntervals() {
	if (count == null)
	    return 0;

	// (count-if #'(lambda (x) (> x 3)) (sample-count s))
	int n = count.size();
	int three = 0;
	for (int i=0; i<n; i++)
	    if (((Integer) count.get(i)).intValue() > 3)
		three++;
	return three;
    }

    // count number of significant (weiserjahre) intervals
    public int countSignificantIntervals() {
        if (!hasWeiserjahre())
            return 0;

        int sig=0, n=incr.size();
        for (int i=0; i<n; i++)
            if (Weiserjahre.isSignificant(this, i))
                sig++;
        return sig;
    }

    //
    // load/save
    //

    /** Class loaders to try, as an array of strings (fully-qualified
	class names).  Elements:
	<ol>
	<li>"Corina"
	<li>"TSAPMatrix"
	<li>"Hohenheim"
	<li>"Heidelberg"
	<li>"Tucson"
	<li>"TwoColumn"
	</ol> */
    public final static String LOADERS[] = {
	"corina.files.Corina",
	"corina.files.TSAPMatrix",
	"corina.files.Hohenheim",
	"corina.files.Heidelberg",
	"corina.files.Tucson",
	"corina.files.TwoColumn", // <-- should always be last
    };

    // need more sophisticated extension=>type assocs?  hmm, possibly.
    // i'll always check every type, because you never know what crazy
    // extensions users might use, but being able to say "*.TU files
    // are _probably_ tucson, so check that first" could be helpful.

    /** Filename extensions of files to ignore out of principle: .XLS,
        .DOC, .JPG, .GIF, and .TIF are a good start.  They
        should be all upper case. */
    public final static String SKIP_EXTENSIONS[] = {
	".XLS",
	".DOC",
	".RTF",
	".EXE",
	".ZIP", // auto-(de)-compression?  no, not enough benefit.
	".JPG", ".JPEG",
	".GIF",
	".TIF", ".TIFF",
	".14I", ".14S", ".14D", ".14P", ".14L", // Oxcal, my arch-nemesis!
    };

    /** Load a Sample from disk.
	@param filename the file to load
	@exception FileNotFoundException if the file could not be found
	@exception WrongFiletypeException if the file is not a Sample
	@exception IOException if a low-level I/O error occurs */
    public void load(String filename) throws IOException {
	// don't try a bunch of formats that we know a priori will all
	// fail: try some quick tests first
	File f = new File(filename);
	if (!f.isFile())
	    throw new IOException("Not a file.");
	if (!f.canRead())
	    throw new IOException("No read access allowed.");

        // i can ignore a lot of common filetypes without even
        // looking -- see SKIP_EXTENSIONS, above
        for (int i=0; i<SKIP_EXTENSIONS.length; i++)
            if (filename.toUpperCase().endsWith(SKIP_EXTENSIONS[i]))
                throw new WrongFiletypeException();

        // try each loader in turn:
        for (int i=0; i<LOADERS.length; i++) {
            try {
                // use factory to make a Filetype from the class name
                Filetype format = Filetype.makeFiletype(LOADERS[i]);

                // try loading; on success, set the filename/type, and return.
                Sample s = format.load(filename);
                String filetype = format.toString();
                s.meta.put("filetype", filetype); // (used only for preview)
                s.meta.put("filename", filename);

                // if we made it this far without throwing a
                // WrongFiletypeException or IOException (or any other
                // Exception), it must have loaded correctly.  so copy
                // s to this.

                // have to copy s into this ... hmm
                copy(s, this);
                return;
            } catch (ClassNotFoundException cnfe) {
                continue; // loader couldn't be made; i don't care why, skip it.
            } catch (WrongFiletypeException wfe) {
                continue;
            } catch (IOException ioe) {
                String l = LOADERS[i];
                l = l.substring(l.lastIndexOf('.') + 1);
                throw new IOException(l + ": " + ioe.getMessage());
            } catch (Exception e) {
                // load() failed -- unknown reason -- log me?  (once,
                // some loaders threw crazy things like
                // NullPointerExceptions, so this is a catch-all for
                // those.  it should be unnecessary now, but it can't
                // hurt to have too much error-checking.)

                // use Bug to report it if this ever happens!
                continue;
            }
        }

        // fall-through: no loader worked
        throw new WrongFiletypeException(); // "No usable format found"
    }

    /* Determining if a file is indexed: The 800 Rule

    (This was originally for Tucson files, but now I don't think it
    applies to them, so it got dumped here.  It might actually be
    useful here.)

    If a Tucson file is summed, is it indexed?  There doesn't appear
    to be any way to know for sure.  If it's not summed, and it's
    processed, then it must be indexing that caused it to be processed.
    But if it's summed, you know it's processed, but there's no way to
    tell if it's indexed.
    
    So I invented

       /The 800-Rule/: If dataset is known to be processed, but not
       summed, then it is indexed if the average data value is greater
       than 800.

    Because indexes are based around 1000 (parts-per-thousand), the
    average would be near 1000.  Raw data are much lower, like 50-100.
    This fails if you have a raw dataset with rings larger than 8mm on
    average (a very big tree), or an indexed dataset where the curve
    overshoots by more than 25% on average (a very poor index).  It
    doesn't really matter that this fails <i>sometimes</i>, because
    without this rule, it would fail <i>always</i>.  So it's a hack,
    but it's still the right thing to do.

    I've recently learned that there's really no need for the
    800-rule on Tucson files.  Apparently nobody at Tucson ever thought
    of summing non-indexed files, so summed implies indexed.  I think.
    (How do they store indexed files for summing, then?)  Oh well, we
    had fun, anyway.

    This used to be in Tucson.java, then in TwoColumn.java, but it
    only looks at the Sample, so it really belongs here.  At least I
    can put it with the load/save stuff here.

update: pik says there can be raw summed files, and pulls out some
        old datasets to show me ... that there are raw, indexed, and
        summed indexed formats for tucson.  but he wants it back in,
        so we give it to him. */
    public void guessIndexed() {
        meta.put("format", computeRadius() / data.size() > 800 ? "I" : "R");
    }

    // make sure data/count/wj are the same size as range.span, and
    // contain all legit Numbers
    public void verify() {
        int n = range.span();

        // what to do if they're the wrong size -- adjust range if the data
        // are all the same size, but pad with zeros if only one is off?

        // data: turn nulls/non-numbers into 0
        for (int i=0; i<n; i++) {
            Object o = data.get(i);
            if (o==null || !(o instanceof Number))
                data.set(i, new Integer(0));
        }

        // TODO: do count, WJ as well
    }
    
    /** Default saver class.  Value is "corina.files.Corina".
	@see Corina */
    private final static String DEFAULT_SAVER = "corina.files.Corina";

    /** Save this Sample to disk.
	@param filename the name of the file to save to
	@exception IOException if an I/O error occurs */
    public void save(String filename) throws IOException {
	// make the format
	try {
	    Filetype format = Filetype.makeFiletype(DEFAULT_SAVER);
	    format.save(filename, this);
	} catch (IOException ioe) {
	    throw ioe;
	} catch (Exception e) {
	    // bug!
	    throw new IOException("Random error (\"bug\") while saving file: " + e);
	}
    }

    /** Save this Sample to disk to the same filename it had
	previously.
	@exception IOException if an I/O error occurs */
    public void save() throws IOException {
	// BUG!  assumes filename exists in meta map
	save((String) meta.get("filename"));
    }

    /*
      WRITE ME: function to return all comment lines as a String[] --
      that would be useful, right?
    */

    /** Make an HTML preview of this Sample.  Lists the title, range,
        species, format, and if the sample is indexed and/or summed,
        and how many elements compose it.
	@return the preview string */
    public String getHTMLPreview() {
	// html: title
	String line = "<html>";
	line += "<b>" + (String) meta.get("title") + "</b><ul>";

	// range
	line += "<li>" + range + " (n=" + range.span() + ")";

	// species
	if (meta.get("species") != null)
	    line += "<li>" + msg.getString("species") + ": " + meta.get("species");

	// format
	line += "<li>" + msg.getString("format") + ": " + meta.get("filetype");

	// indexed, summed
	if (isIndexed())
	    line += "<li>" + msg.getString("indexed");
	if (isSummed()) {
	    line += "<li>" + msg.getString("summed");
	    if (elements != null)
		line += " (" + elements.size() + " " + msg.getString("elements") + ")";
	}

	// return it
	return line;
    }

    public Preview getPreview() {
	// preview: title
	Preview p = new Preview();
	p.title = (String) meta.get("title");
	p.items = new ArrayList();

	// range
	p.items.add(range + " (n=" + range.span() + ")");

	// species
	if (meta.get("species") != null) // use CONTAINS?
	    p.items.add(msg.getString("species") + ": " + meta.get("species"));

	// format
	p.items.add(msg.getString("format") + ": " + meta.get("filetype"));

	// indexed, summed
	if (isIndexed())
	    p.items.add(msg.getString("indexed"));
	if (isSummed()) {
	    String summedLine = msg.getString("summed");
	    if (elements != null)
		summedLine += " (" + elements.size() + " " + msg.getString("elements") + ")";
	    p.items.add(summedLine);
	}

	return p;
    }

    //
    // miscellaneous procedures that are better off here than elsewhere
    //

    // is this sample oak?  (assumes meta/species is a string, if present)
    public boolean isOak() {
	String species = (String) meta.get("species");
	if (species == null)
	    return false;
	species = species.toLowerCase();
	return (species.indexOf("oak")!=-1 || species.indexOf("quercus")!=-1);
    }

    //
    // event model
    //

    private Vector listeners = new Vector();

    public synchronized void addSampleListener(SampleListener l) {
	if (listeners.contains(l))
	    return;
	listeners.add(l);
    }

    public synchronized void removeSampleListener(SampleListener l) {
	listeners.remove(l);
    }

    // fire an arbitrary sample event called |method|.  each
    // fireSampleXYZhappened() method is virtually identical, so their
    // guts were refactored into here.  this makes adding new events
    // painless.
    private void fireSampleEvent(String method) {
	// alert all listeners
	Vector l;
	synchronized (this) {
	    l = (Vector) listeners.clone();
	}

	int size = l.size();

	if (size == 0)
	    return;

	SampleEvent e = new SampleEvent(this);

	for (int i=0; i<size; i++) {
	    SampleListener listener = (SampleListener) l.elementAt(i);

	    // this is like "listener.method(e)", though it's not terribly elegant.
	    try {
		Method m = SampleListener.class.getMethod(method, new Class[] { SampleEvent.class });
		m.invoke(listener, new Object[] { e });
	    } catch (Exception ex) {
		// just ignore them all... (?)
	    }
	}
    }

// there's an elegant refactoring waiting to be done here, but i'm too wired on caffiene right now to see it.
// => see also mapframe's toolbox decorators.
    
    public void fireSampleRedated() {
	fireSampleEvent("sampleRedated");
    }
    public void fireSampleDataChanged() {
	fireSampleEvent("sampleDataChanged");
    }
    public void fireSampleMetadataChanged() {
	fireSampleEvent("sampleMetadataChanged");
    }
    public void fireSampleFormatChanged() {
	fireSampleEvent("sampleFormatChanged");
    }
    public void fireSampleElementsChanged() {
	fireSampleEvent("sampleElementsChanged");
    }

    //
    // i18n
    //
    private static ResourceBundle msg = ResourceBundle.getBundle("SampleBundle");

    //
    // undo/redo
    //
    private UndoableEditSupport undoSupport = new UndoableEditSupport();
    public void postEdit(UndoableEdit x) {
	undoSupport.postEdit(x);
    }
    public UndoableEditSupport getUndoSupport() { // only used once -- undoadapter
	return undoSupport;
    }
}
