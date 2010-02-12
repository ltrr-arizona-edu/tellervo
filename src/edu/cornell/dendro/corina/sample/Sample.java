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

package edu.cornell.dendro.corina.sample;

import edu.cornell.dendro.corina.Preview;
import edu.cornell.dendro.corina.Previewable;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Weiserjahre;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.graph.Graphable;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.tridasv2.support.TridasRingWidthWrapper;
import edu.cornell.dendro.corina.tridasv2.support.TridasWeiserjahreWrapper;
import edu.cornell.dendro.corina.ui.I18n;

import edu.cornell.dendro.corina_indexing.Indexable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;

import java.lang.reflect.Method;

import javax.swing.undo.*;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasUnitless;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;

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

@SuppressWarnings("serial")
public class Sample extends BaseSample implements Previewable, Graphable, Indexable {

	private static class SamplePreview extends Preview {
		SamplePreview(Sample s) {
			title = s.getMeta("title").toString();

			// range -- toStringWithSpan() does "(a - b, n=c)", i want "a - b (n=c)"
			items.add(s.getRange() + " (n=" + s.getRange().span() + ")");

			// species
			if (s.hasMeta("species"))
				items.add(I18n.getText("species") + ": "
						+ s.getMeta("species"));

			// format
			items.add(I18n.getText("format") + ": " + s.getMeta("filetype"));

			// indexed, summed
			if (s.isIndexed())
				items.add(I18n.getText("indexed"));
			if (s.isSummed()) {
				String summedLine = I18n.getText("summed");
				if (s.getElements() != null)
					summedLine += " (" + s.getElements().size() + " "
							+ I18n.getText("elements") + ")";
				items.add(summedLine);
			}
		}
	}

	/** The value of a missing ring, 0.  Anything less than or equal
	 to this value is considered a MR. */
	public static final int MR = 0;

	// copy each part of source to target.  shallow copy, no events, etc.
	// used only by editor (paste) -- bad interface!
	public static void copy(Sample source, Sample target) {
		// copy our base data
		BaseSample.copy(source, target);

		target.ringwidths = source.ringwidths;
		target.weiserjahre = source.weiserjahre;
		target.elements = source.elements;

		// rebuild the target's tables
		target.setSeries(target.getSeries(), false);
	}
	
	private boolean metadataChanged = true;
		
	/** 
	 * Data, as a List of Integers. 
	 * It's not that easy, though;
	 * We put floats in here occasionally, as well as strings.
	 */
	private TridasRingWidthWrapper ringwidths;
	private TridasWeiserjahreWrapper weiserjahre;
		
	/** Elements (in a List) that were put into this sum. */
	private ElementList elements = null;
	
	private boolean modified = false;

	// WRITEME: need corresponding setString(), setInteger().
	// WRITEME: make meta private, eventually.
	// WRITEME: add lazy-loaders here.
	// WRITEME: and don't load on construction!

	private Vector<SampleListener> listeners = new Vector<SampleListener>();

	/* FUTURE: */
	private UndoableEditSupport undoSupport = new UndoableEditSupport();

	/** A map from Tridas variable (RING_WIDTH, etc) to values */
	private EnumMap<NormalTridasVariable, TridasValues> tridasValuesMap;
	
	/** A slower hashmap from Standard/Name to values */
	private HashMap<TridasVariable, TridasValues> otherValuesMap;
	
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
		super(new TridasMeasurementSeries());
		initialize();
		
		// initialize empty metadata with defaults?
		setMeta("title", I18n.getText("general.untitled"));

		// store username, if known
		if (System.getProperty("user.name") != null)
			setMeta("author", System.getProperty("user.name"));
		
		setSeries(getSeries(), false);

		// metadata NOT changed
		metadataChanged = false;
	}
	
	public Sample(ITridasSeries series) {
		super(series);
		initialize();
		
		setSeries(getSeries(), false);
		
		// metadata NOT changed
		metadataChanged = false;		
	}
	
	@Override
	public void setSeries(ITridasSeries series) {
		setSeries(series, true);
	}
	
	private void setSeries(ITridasSeries series, boolean notifySuper) {
		if(notifySuper)
			super.setSeries(series);
		
		repopulateValuesMap();
		
		// must have ring widths!
		if(!tridasValuesMap.containsKey(NormalTridasVariable.RING_WIDTH)) {
			getSeries().getValues().add(createEmptyRingWidths());
			repopulateValuesMap(); // be lazy
		}
		
		// make the ring widths wrapper
		ringwidths = new TridasRingWidthWrapper(tridasValuesMap.get(NormalTridasVariable.RING_WIDTH));
		
		// if weiserjahre exists, make the values wrapper for it as well
		if(otherValuesMap.containsKey(WEISERJAHRE_VARIABLE)) 
			weiserjahre = new TridasWeiserjahreWrapper(otherValuesMap.get(WEISERJAHRE_VARIABLE));
		else
			weiserjahre = null;
	}

	/**
	 * Common setup
	 */
	private void initialize() {
		otherValuesMap = new HashMap<TridasVariable, TridasValues>();
		tridasValuesMap = new EnumMap<NormalTridasVariable, TridasValues>(NormalTridasVariable.class);
	}
	
	/**
	 * Create a default set of TridasValues
	 * - 1/100th mm
	 * - Ring widths
	 * 
	 * @return a representative TridasValues object
	 */
	private TridasValues createEmptyRingWidths() {
		TridasValues values = new TridasValues();
		
		// set default units
		TridasUnit units = new TridasUnit();		
		units.setNormalTridas(NormalTridasUnit.HUNDREDTH_MM);
		values.setUnit(units);
		
		// set as ring widths
		TridasVariable variable = new TridasVariable();
		variable.setNormalTridas(NormalTridasVariable.RING_WIDTH);
		values.setVariable(variable);

		// populate the list of values (empty)
		values.getValues();
		
		return values;
	}	

	/**
	 * Create a default set of TridasValues
	 * - 1/100th mm
	 * - Ring widths
	 * 
	 * @return a representative TridasValues object
	 */
	private TridasValues createEmptyWeiserjahre() {
		TridasValues values = new TridasValues();
		
		values.setUnitless(new TridasUnitless());
		
		// set as Weiserjahre
		values.setVariable(WEISERJAHRE_VARIABLE);

		// populate the list of values (empty)
		values.getValues();
		
		return values;
	}	

	/**
	 * For quick lookups, find TridasVariables via a map
	 */
	private void repopulateValuesMap() {
		tridasValuesMap.clear();
		otherValuesMap.clear();
		
		for(TridasValues values : getSeries().getValues()) {
			TridasVariable variable = values.getVariable();
			
			if(variable.isSetNormalTridas())
				tridasValuesMap.put(variable.getNormalTridas(), values);
			else {				
				otherValuesMap.put(variable, values);
			}
		}
	}

	/**
	 * Shortcut for getRemarksForYear(values, year)
	 * Uses RING_WIDTH variable
	 * 
	 * @param y the year
	 * @return a list of remarks for this year, or an empty list if one exists.
	 */
	public List<TridasRemark> getRemarksForYear(Year y) {
		return getRemarksForYear(tridasValuesMap.get(NormalTridasVariable.RING_WIDTH), y);
	}
	
	/**
	 * Get a list of TridasRemarks for this year on the specified variable
	 * @param values
	 * @param y
	 * @return a list of remarks for this year, or an empty list if one exists.
	 */
	public List<TridasRemark> getRemarksForYear(TridasValues values, Year y) {
		try {
			TridasValue value = getValueForYear(values, y);
			
			// if we've got remarks, return them!
			if(value.isSetRemarks())
				return value.getRemarks();
			
		} catch (IndexOutOfBoundsException ioobe) {
			// fall through
		}
		
		return Collections.emptyList();		
	}

	/**
	 * Shortcut for getRemarksForYear(values, year)
	 * Uses RING_WIDTH variable
	 * 
	 * @param y the year
	 * @return a TridasValue for the given year
	 */
	public TridasValue getValueForYear(Year y) {
		return getValueForYear(tridasValuesMap.get(NormalTridasVariable.RING_WIDTH), y);
	}

	/**
	 * Get a TridasValue for a given year
	 * 
	 * @param values
	 * @param y
	 * @return a TridasValue for the given year
	 */
	public TridasValue getValueForYear(TridasValues values, Year y) {
		Range range = getRange();

		if(!range.contains(y))
			throw new IndexOutOfBoundsException();
		
		int idx = y.diff(range.getStart());
		return values.getValues().get(idx);		
	}
	
	public synchronized void addSampleListener(SampleListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	/** Clear the modified flag. */
	public void clearModified() {
		modified = false;
	}

	// radius of the sample; only relevant for raw samples (better to
	// return 0.0 for indexed sample?  throw ex?)
	public int computeRadius() {
		// (apply '+ data)
		List<Number> data = getData();
		int n = data.size();
		int sum = 0;
		for (int i = 0; i < n; i++)
			sum += ((Number) data.get(i)).intValue();
		return sum;
	}

	// number of intervals with >3 samples
	public int count3SampleIntervals() {
		if (!hasCount())
			return 0;
		
		// (count-if #'(lambda (x) (> x 3)) (sample-count s))
		List<Integer> count = getCount();

		int n = count.size();
		int three = 0;
		for (int i = 0; i < n; i++)
			if ((count.get(i)).intValue() > 3)
				three++;
		return three;
	}

	/** Count the total number of rings.  If this is a raw sample,
	 returns the length.  If this is a summed sample, returns the
	 sum of the count List.
	 @return the total number of rings in this sample */
	public int countRings() {
		// it's not a sum, so the number of rings is just the length
		// (if (null count) (length data) ...
		List<Number> data = getData();
		
		if (!hasCount())
			return data.size();

		List<Integer> count = getCount();

		// it's a sum, so the number of rings is the sum of the number
		// of measurements for each year
		// ... (apply '+ count))
		int n = 0, size = count.size();
		for (int i = 0; i < size; i++)
			n += count.get(i);
		return n;
	}

	// count number of significant (weiserjahre) intervals
	public int countSignificantIntervals() {
		if (!hasWeiserjahre())
			return 0;

		int sig = 0, n = getWJIncr().size();
		for (int i = 0; i < n; i++)
			if (Weiserjahre.isSignificant(this, i))
				sig++;
		return sig;
	}

	public void fireSampleDataChanged() {
		fireSampleEvent("sampleDataChanged");
	}

	public void fireSampleElementsChanged() {
		fireSampleEvent("sampleElementsChanged");
	}

	public void fireSampleMetadataChanged() {
		metadataChanged = true;
		fireSampleEvent("sampleMetadataChanged");
	}
	public void fireSampleRedated() {
		fireSampleEvent("sampleRedated");
	}

	/**
	 * @return the count
	 */
	public List<Integer> getCount() {
		return ringwidths.getCount();
	}

	/** Return the data for a graph
	 * or the data to index.
	 * @see Graphable
	 * @see Indexable
	 * @return data to graph, as a List of Integers 
	 */
	public List<Number> getData() {
		return ringwidths.getData();
	}

	/**
	 * @return the elements
	 */
	public ElementList getElements() {
		return elements;
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

	public Preview getPreview() {
		return new SamplePreview(this);
	}

	@Override
	public SampleType getSampleType() {
		SampleType known = super.getSampleType();
		
		// easy, it was determined for us!
		if(known != SampleType.UNKNOWN)
			return known;
		
		if(isIndexed())
			return SampleType.INDEX; // we saved this in IsIndexed()...
		else if(isSummed())
			return SampleType.SUM;
		else // fall back if we can't determine and it hasn't been loaded...
			return known;
	}

	/** Return the default scale factor for graphing.
	 @return scale factor of 1.0, or 0.1 for indexed files */
	public float getScale() {
		return (isIndexed() ? 0.1f : 1.0f);
	}

	/** Return the start date for a graph.
	 @return start date of data to graph */
	public Year getStart() {
		return getRange().getStart();
	}

	// get a string field from this sample.
	public String getString(String field) {
		// TODO: load, if needed.

		Object val = getMeta(field);
		if (val != null && val instanceof String)
			return (String) val;

		return null;
	}

	/**
	 * @return the decr
	 */
	public List<Integer> getWJDecr() {
		return (weiserjahre != null) ? weiserjahre.getDecr() : null;
	}

	/**
	 * @return the incr
	 */
	public List<Integer> getWJIncr() {
		return (weiserjahre != null) ? weiserjahre.getIncr() : null;
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
		List<Number> data = getData();
		setMeta("format", computeRadius() / data.size() > 800 ? "I" : "R");
	}

	// does it have weiserjahre?
	public boolean hasWeiserjahre() {
		return (weiserjahre != null);
	}

	/** 
	 * Return true if the sample is absolutely dated, else false.
	 * @return true if the sample is absolutely dated 
	 */
	public boolean isAbsolutelyDated() {
		ITridasSeries series = getSeries();
		
		// no interpretation or no dating -> Relative dating
		if(!series.isSetInterpretation() || !series.getInterpretation().isSetDating())
			return false;
		
		switch(series.getInterpretation().getDating().getType()) {
		case ABSOLUTE:
		case DATED___WITH___UNCERTAINTY:
		case RADIOCARBON:
			return true;
			
		case RELATIVE:
			return false;
			
		default:
			new Bug(new IllegalArgumentException("Dating type " + 
					series.getInterpretation().getDating() + " not supported"));
			return false;
		}
	}

	// is this sample editable?  no, if it's been indexed or summed.
	public boolean isEditable() {
		switch(super.getSampleType()) {
		case DIRECT:
			return true;

		case UNKNOWN:
			// argh. guess?
			return (!isIndexed()) && (!isSummed());
			
		default:
			return false;
		}
	}

	/** Return true if the sample is indexed, else false.
	 @return true if the sample is indexed */
	public boolean isIndexed() {
		switch(super.getSampleType()) {
		case INDEX:
			return true; // nice and easy

		// we don't know? guess. and save our guess.
		case UNKNOWN:
			String type = (String) getMeta("format");
			if (type != null && Character.toUpperCase(type.charAt(0)) == 'I') {
				setSampleType(SampleType.INDEX);
				return true;
			}
			return false;
			
		default:
			return false;
		}
	}

	/** Return true if the file was modified since last save.
	 @return if the sample has been modified */
	public boolean isModified() {
		return modified;
	}
	
	/** <p>Return true if the sample is summed, else false.  Here
	 "summed" is defined as:</p>
	 <ul>
	 <li>has a list of elements, or
	 <li>has count data
	 </ul>
	 @return true if the sample is summed */
	public boolean isSummed() {
		switch(super.getSampleType()) {
		case SUM:
			return true; // nice and easy

		// we don't know? guess.
		// why is this "or?"
		case UNKNOWN: {
			if (elements != null || hasCount()) {
				setSampleType(SampleType.SUM);
				return true;
			}
			return false;
		}
			
		default:
			return false;
		}
	}

	public void postEdit(UndoableEdit e) {
		undoSupport.postEdit(e);
	}

	public synchronized void removeSampleListener(SampleListener l) {
		listeners.remove(l);
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(List<Integer> count) {
		ringwidths.setCount(count);
	}
	
	/**
	 * Note that getCount() will return a list that has all ones
	 * when hasCount() can return false
	 * @return true if counts exist
	 */
	public boolean hasCount() {
		return ringwidths.hasCount();
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<Number> data) {
		ringwidths.setData(data);
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(ElementList elements) {
		this.elements = elements;
	}

	/** Set the modified flag. */
	public void setModified() {
		modified = true;
	}

	/**
	 * @param decr the decr to set
	 */
	public void setWJDecr(List<Integer> decr) {
		if(weiserjahre == null) {
			getSeries().getValues().add(createEmptyWeiserjahre());
			repopulateValuesMap();
			weiserjahre = new TridasWeiserjahreWrapper(otherValuesMap.get(WEISERJAHRE_VARIABLE));
		}
		weiserjahre.setDecr(decr);
	}

	/**
	 * @param incr the incr to set
	 */
	public void setWJIncr(List<Integer> incr) {
		if(weiserjahre == null) {
			getSeries().getValues().add(createEmptyWeiserjahre());
			repopulateValuesMap();
			weiserjahre = new TridasWeiserjahreWrapper(otherValuesMap.get(WEISERJAHRE_VARIABLE));
		}
		weiserjahre.setIncr(incr);
	}

	/** Return the sample's title.
	 @return the "title" tag from meta */
	@Override
	public String toString() {
		String title = getDisplayTitle();
		
		if(getSeries() instanceof ITridasDerivedSeries) {
			String version = ((ITridasDerivedSeries) getSeries()).getVersion();
			
			if(version != null && version.length() > 0) {
				title += ": " + version; 
			}
		}
		
		String name = title + " " + getRange().toStringWithSpan();
		if (isModified()) // not aqua-ish, but how to do it the real way?
			name = "* " + name;
		return name;
	}
	
	/** Return a short version of the sample's title (without range info).
	 @return the "title" tag from meta */
	public String toSimpleString() {
		String name = getDisplayTitle();
		if (isModified()) // not aqua-ish, but how to do it the real way?
			name = "* " + name;
		return name;
	}
		
	
	private CorinaMetadata metadataWrapper;
	
	@Override
	public CorinaMetadata meta() {
		if(metadataWrapper == null)
			metadataWrapper = new SampleMetadata(this);
		
		return metadataWrapper;
	}

	// make sure data/count/wj are the same size as range.span, and
	// contain all legit Numbers.  turns nulls/non-numbers into 0's.
	public void verify() {
		List<Number> data = getData();
		int n = getRange().span();

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

	public boolean wasMetadataChanged() {
		return metadataChanged;
	}

	// fire an arbitrary sample event called |method|.  each
	// fireSampleXYZhappened() method is virtually identical, so their
	// guts were refactored into here.  this makes adding new events
	// painless.  (this was taken from a web page -- url?)
	@SuppressWarnings("unchecked")
	private void fireSampleEvent(String method) {
		// alert all listeners
		Vector<SampleListener> l;
		synchronized (this) {
			l = (Vector<SampleListener>) listeners.clone();
		}

		int size = l.size();

		if (size == 0)
			return;

		SampleEvent e = new SampleEvent(this);

		try {

			// **
			Class<?> types[] = new Class[] { SampleEvent.class };
			Method m = SampleListener.class.getMethod(method, types);
			Object args[] = new Object[] { e };

			for (int i = 0; i < size; i++) {
				SampleListener listener = (SampleListener) l.elementAt(i);

				// this is like "listener.method(e)" (along with the 2 lines
				// marked ** above)
				m.invoke(listener, args);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// BUG: these exceptions are caught too coursely!

			// just ignore them all... (?)
		}
	}
	
	public final static TridasVariable WEISERJAHRE_VARIABLE = new TridasVariable() {
		{
			this.setNormalStd(CORINA_STD);
			this.setNormal(WEISERJAHRE);
			this.setValue(""); // this is XML mandatory, as it's a value string
		}
	};
	public final static String CORINA_STD = "Corina";
	public final static String WEISERJAHRE = "Weiserjahre";


}
