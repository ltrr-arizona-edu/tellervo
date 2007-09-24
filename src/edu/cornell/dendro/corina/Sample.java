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

import edu.cornell.dendro.corina.io.Files;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.graph.Graphable;
import edu.cornell.dendro.corina.ui.I18n;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import java.net.URL;

import java.lang.reflect.Method;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import javax.swing.undo.*;

/**
   Class representing a reading of a dendro sample.

   <p>Currently, this stores:</p>

   <ul>
     <li>series: data, count, wj (up/down, in one list)</li>
     <li>range</li>
     <li>a hashtable for metadata</li>
     <li>a list of elements</li>
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/

// IDEA: make a samplefactory, so 2 calls to sample(filename) return
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
	 table lists the standard keys, their data types, and valid values:

	 <table border="1">
	 <tr> <th>Key</th>         <th>Type</th>    <th>Valid values</th> </tr>
	 <tr> <td>id</td>          <td>Integer</td> <td></td>             </tr>
	 <tr> <td>title</td>       <td>String</td>  <td></td>             </tr>
	 <tr> <td>dating</td>      <td>String</td>  <td>A, R</td>         </tr>
	 <tr> <td>unmeas_pre</td>  <td>Integer</td> <td></td>             </tr>
	 <tr> <td>unmeas_post</td> <td>Integer</td> <td></td>             </tr>
	 <tr> <td>filename</td>    <td>String</td>  <td></td>             </tr>
	 <tr> <td>comments</td>    <td>String</td>  <td></td>             </tr>
	 <tr> <td>type</td>        <td>String</td>  <td>S, H, C</td>      </tr>
	 <tr> <td>species</td>     <td>String</td>  <td></td>             </tr>
	 <tr> <td>sapwood</td>     <td>Integer</td> <td></td>             </tr>
	 <tr> <td>pith</td>        <td>String</td>  <td>+, *, N</td>      </tr>
	 <tr> <td>terminal</td>    <td>String</td>  <td>v, vv, B, W</td>  </tr>
	 <tr> <td>continuous</td>  <td>String</td>  <td>C, R, N</td>      </tr>
	 <tr> <td>quality</td>     <td>String</td>  <td>+, ++</td>        </tr>
	 <tr> <td>format</td>      <td>String</td>  <td>R, I</td>         </tr>
	 <tr> <td>index_type</td>  <td>Integer</td> <td></td>             </tr>
	 <tr> <td>reconciled</td>  <td>String</td>  <td>Y,N</td>          </tr>
	 <tr> <td>author</td>      <td>String</td>  <td></td>             </tr>
	 </table>

	 <code>data</code>, <code>count</code>, <code>range</code>,
	 <code>wj</code>, and <code>elements</code> aren't stored in
	 <code>meta</code> - they're their own members.

	 @see corina.formats.Corina */
	public Map meta;

	// WRITEME: need corresponding setString(), setInteger().
	// WRITEME: make meta private, eventually.
	// WRITEME: add lazy-loaders here.
	// WRITEME: and don't load on construction!

	// get a string field from this sample.
	public String getString(String field) {
		// TODO: load, if needed.

		Object val = meta.get(field);
		if (val != null && val instanceof String)
			return (String) val;

		return null;
	}

	// get an int field from this sample.
	// (would be int, but can't return null then -- use exception?)
	public Integer getInteger(String field) {
		// TODO: load, if needed.

		Object val = meta.get(field);
		if (val != null && val instanceof Integer)
			return (Integer) val;

		return null;
	}

	// get a list-of-numbers field from this sample.
	// (what about elements?)
	public List getList(String field) {
		// TODO: load, if needed.

		if (field.equals("data"))
			return data;
		else if (field.equals("count"))
			return count;
		else if (field.equals("incr"))
			return incr;
		else if (field.equals("decr"))
			return decr;

		return null;
	}

	/** Number of samples in the sum at any given point. */
	public List count = null;

	// weiserjahre
	public List incr = null, decr = null;

	// does it have weiserjahre?
	public boolean hasWeiserjahre() {
		return (incr != null);
	}

	/** Elements (in a List) that were put into this sum. */
	public List elements = null;

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
		meta.put("title", I18n.getText("Untitled"));

		// metadata NOT changed
		metadataChanged = false;
	}

	/*
	 // TESTING: single-instance samples (and Sample(String) to become private)
	 public static Sample getSample(String filename) throws IOException {
	 // check map
	 Sample s = null;
	 if (samples.containsKey(filename)) {
	 s = (Sample) ((Reference) samples.get(filename)).get();
	 // BUG: what if what's on disk is newer than what's in memory?
	 // (if it's ONLY weakly referenced, just update it)
	 // (if somebody else is viewing it, better ask the user)
	 }
	 if (s == null)
	 s = new Sample(filename);
	 samples.put(filename, new WeakReference(s));
	 return s;
	 // won't this map keep accumulating nulls?  well, probably not many.
	 // but shouldn't i try to take them out somehow?
	 }
	 private static Map samples = new HashMap();
	 */

	/** Create a new Sample from a given file on disk.
	 @param filename the name of the file to load
	 @exception FileNotFoundException if the file doesn't exist
	 @exception WrongFiletypeException if the file is not a Sample
	 @exception IOException if there is an I/O error while loading
	 the file */
	/*
	 public Sample(String filename) throws FileNotFoundException, WrongFiletypeException, IOException {
	 // make it like any other Sample
	 this();

	 // load the file; this call throws the exceptions
	 load(filename);

	 // 99%+ of the time you're loading a sample from disk, you
	 // won't be adding to it, so trim it.
	 trimAllToSize();
	 }
	 */

	/*
	 public Sample(URL url) throws IOException {
	 this();
	 try {
	 Class.forName(package edu.cornell.dendro.corinabrowser.ItrdbURLConnection");
	 } catch (ClassNotFoundException cnfe) {
	 corina.gui.Bug.bug(cnfe);
	 }
	 load(new InputStreamReader(url.openStream()));
	 meta.put("filename", url.toString());
	 trimAllToSize();
	 }
	 */

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
	// used only by editor (paste) -- bad interface!
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
		return (type != null && Character.toUpperCase(type.charAt(0)) == 'I');
	}

	/** <p>Return true if the sample is summed, else false.  Here
	 "summed" is defined as:</p>
	 <ul>
	 <li>has a list of elements, or
	 <li>has count data
	 </ul>
	 @return true if the sample is summed */
	public boolean isSummed() {
		return (elements != null || count != null);
	}

	/** Return true if the sample is absolutely dated, else false.
	 @return true if the sample is absolutely dated */
	public boolean isAbsolute() {
		String dating = (String) meta.get("dating");
		return (dating != null && Character.toUpperCase(dating.charAt(0)) == 'A');
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
	public float getScale() {
		return (isIndexed() ? 0.1f : 1.0f);
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
		for (int i = 0; i < size; i++)
			n += ((Integer) count.get(i)).intValue();
		return n;
	}

	// radius of the sample; only relevant for raw samples (better to
	// return 0.0 for indexed sample?  throw ex?)
	public int computeRadius() {
		// (apply '+ data)
		int n = data.size();
		int sum = 0;
		for (int i = 0; i < n; i++)
			sum += ((Number) data.get(i)).intValue();
		return sum;
	}

	// number of intervals with >3 samples
	public int count3SampleIntervals() {
		// (count-if #'(lambda (x) (> x 3)) (sample-count s))
		if (count == null)
			return 0;

		int n = count.size();
		int three = 0;
		for (int i = 0; i < n; i++)
			if (((Integer) count.get(i)).intValue() > 3)
				three++;
		return three;
	}

	// count number of significant (weiserjahre) intervals
	public int countSignificantIntervals() {
		if (!hasWeiserjahre())
			return 0;

		int sig = 0, n = incr.size();
		for (int i = 0; i < n; i++)
			if (Weiserjahre.isSignificant(this, i))
				sig++;
		return sig;
	}

	//
	// load/save
	//

	// create a new sample, from a file on disk
	public Sample(String filename) throws IOException {
		// new @-notation
		if (filename.startsWith("@"))
			filename = System.getProperty("corina.dir.data", ".")
					+ filename.substring(1);
		// (assumes c.d.r ends with file.sep!)

		Sample s = Files.load(filename);
		copy(s, this);
		trimAllToSize();
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

	 (later) pik says there can be raw summed [tucson] files, and pulls out some
	 old datasets to show me ... that there are raw, indexed, and
	 summed indexed formats for tucson.  but he wants it back in,
	 so we give it to him. */
	public void guessIndexed() {
		meta.put("format", computeRadius() / data.size() > 800 ? "I" : "R");
	}

	// make sure data/count/wj are the same size as range.span, and
	// contain all legit Numbers.  turns nulls/non-numbers into 0's.
	public void verify() {
		int n = range.span();

		// what to do if they're the wrong size -- adjust range if the data
		// are all the same size, but pad with zeros if only one is off?

		// data: turn nulls/non-numbers into 0
		for (int i = 0; i < n; i++) {
			Object o = data.get(i);
			if (o == null || !(o instanceof Number))
				data.set(i, new Integer(0));
		}

		// TODO: do count, WJ as well
	}

	/**
	 Save this Sample to disk.

	 @param filename the name of the file to save to
	 @exception IOException if an I/O error occurs
	 */
	public void save(String filename) throws IOException {
		Files.save(this, filename);
	}

	/**
	 Save this Sample to disk to the same filename it had
	 previously.

	 @exception IOException if an I/O error occurs
	 */
	public void save() throws IOException {
		// BUG!  assumes filename exists in meta map -- what if it doesn't?
		save((String) meta.get("filename"));
	}

	public Preview getPreview() {
		return new SamplePreview(this);
	}

	private static class SamplePreview extends Preview {
		SamplePreview(Sample s) {
			title = s.meta.get("title").toString();

			// range -- toStringWithSpan() does "(a - b, n=c)", i want "a - b (n=c)"
			items.add(s.range + " (n=" + s.range.span() + ")");

			// species
			if (s.meta.containsKey("species"))
				items.add(I18n.getText("species") + ": "
						+ s.meta.get("species"));

			// format
			items.add(I18n.getText("format") + ": " + s.meta.get("filetype"));

			// indexed, summed
			if (s.isIndexed())
				items.add(I18n.getText("indexed"));
			if (s.isSummed()) {
				String summedLine = I18n.getText("summed");
				if (s.elements != null)
					summedLine += " (" + s.elements.size() + " "
							+ I18n.getText("elements") + ")";
				items.add(summedLine);
			}
		}
	}

	//
	// miscellaneous procedures that are better off here than elsewhere
	//

	// is this sample oak?  (assumes meta/species is a string, if present)
	// (FIXME: if it's not a string, it's not oak.)
	// checks for "oak" or "quercus".
	public boolean isOak() {
		String species = (String) meta.get("species");
		if (species == null)
			return false;
		species = species.toLowerCase();
		return (species.indexOf("oak") != -1 || species.indexOf("quercus") != -1);
	}

	// is this sample editable?  no, if it's been indexed or summed.
	public boolean isEditable() {
		return (!isIndexed()) && (!isSummed());
	}

	//
	// event model
	//

	private Vector listeners = new Vector();

	public synchronized void addSampleListener(SampleListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public synchronized void removeSampleListener(SampleListener l) {
		listeners.remove(l);
	}

	// fire an arbitrary sample event called |method|.  each
	// fireSampleXYZhappened() method is virtually identical, so their
	// guts were refactored into here.  this makes adding new events
	// painless.  (this was taken from a web page -- url?)
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

		try {

			// **
			Class types[] = new Class[] { SampleEvent.class };
			Method m = SampleListener.class.getMethod(method, types);
			Object args[] = new Object[] { e };

			for (int i = 0; i < size; i++) {
				SampleListener listener = (SampleListener) l.elementAt(i);

				// this is like "listener.method(e)" (along with the 2 lines
				// marked ** above)
				m.invoke(listener, args);
			}
		} catch (Exception ex) {
			// BUG: these exceptions are caught too coursely!

			// just ignore them all... (?)
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
		metadataChanged = true;
		fireSampleEvent("sampleMetadataChanged");
	}

	public void fireSampleElementsChanged() {
		fireSampleEvent("sampleElementsChanged");
	}

	// see if the metadata was changed -- true (loaded samples) unless zero-arg constructor called
	private boolean metadataChanged = true;

	public boolean wasMetadataChanged() {
		return metadataChanged;
	}

	/* FUTURE: */
	private UndoableEditSupport undoSupport = new UndoableEditSupport();

	public void postEdit(UndoableEdit e) {
		undoSupport.postEdit(e);
	}
}
