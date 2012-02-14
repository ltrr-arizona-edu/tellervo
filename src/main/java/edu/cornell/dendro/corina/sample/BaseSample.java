/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.sample;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.interfaces.TridasIdentifiable;
import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.gui.Bug;

public class BaseSample implements TridasIdentifiable {

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
	
	 @see edu.cornell.dendro.corina.formats.Corina */

	private Map<String, Object> meta;
	private ITridasSeries series;

	public static void copy(BaseSample source, BaseSample target) {
		target.range = source.range;
		target.meta = source.meta;
		target.loader = source.loader;
		target.sampleType = source.sampleType;
		target.series = source.series;
	}
	
	public BaseSample() {
		meta = new HashMap<String, Object>();
		
		// call these so anyone overloading us can handle properly
		setRange(new Range());
		setLoader(null); // start with no loader
		setSampleType(SampleType.UNKNOWN);
	}
	
	public BaseSample(ITridasSeries series) {
		this();
		
		this.series = series;

		// set sampleType based on series
		if(series instanceof ITridasDerivedSeries) {
			ITridasDerivedSeries dseries = (ITridasDerivedSeries) series;

			// try to establish sample type
			if(!dseries.isSetType() || !dseries.getType().isSetValue())
				setSampleType(SampleType.UNKNOWN);
			else
				setSampleType(SampleType.fromString(dseries.getType().getValue()));
		}
		else
			setSampleType(SampleType.DIRECT);
	}
	
	public BaseSample(BaseSample source) {
		copy(source, this);
	}

	public Map<String, Object> cloneMeta() {
		return new HashMap<String, Object>(meta);
	}

	public boolean metaIsEmpty() {
		return meta.isEmpty();
	}

	public Object getMeta(String key) {
		return meta.get(key);
	}
	
	/**
	 * Get meta as a particular class.
	 * 
	 * @param key
	 * @param clazz
	 * @return 
	 */
	public <T> T getMeta(String key, Class<T> clazz) {
		Object o = meta.get(key);
		
		try {
			return (o != null) ? clazz.cast(o) : null;
		} catch (ClassCastException cce) {
			new Bug(cce);
			return null;
		}
	}
	
	/**
	 * Returns null if meta[key] doesn't exist, otherwise calls the object's tostring method
	 * @param key
	 * @return
	 */
	public String getMetaString(String key) {
		Object o = meta.get(key);
		
		return (o == null) ? null : o.toString();
	}
	
	/**
	 * 
	 * @param key
	 * @return an integer, or null
	 */
	public Integer getMetaInteger(String key) {
		Object o = meta.get(key);
		
		return (o == null) ? null : Integer.valueOf(o.toString());
	}

	public boolean hasMeta(String key) {
		return meta.containsKey(key);
	}

	public void removeMeta(String key) {
		meta.remove(key);
	}

	public void resetMeta() {
		meta.clear();
	}

	public void setMeta(String key, Object value) {
		meta.put(key, value);
	}

	/**
	 * Returns a sorted (by key) COPY of the metadata
	 * @return
	 */
	public Map<String, Object> getMetadata() {
		return new TreeMap<String, Object>(meta);
	}
	
	/** Data range. */
	private Range range;

	/**
	 * @param range the range to set
	 */
	public void setRange(Range range) {
		this.range = range;
	}

	/**
	 * @return the range
	 */
	public Range getRange() {
		return range;
	}

	/** The type of this sample (e.g., Direct, Sum, etc) */
	private SampleType sampleType;

	/** Get the type of sample */
	public SampleType getSampleType() {
		return sampleType;
	}
	
	/** Set the type of sample */
	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}

	/**
	 * Get the attached series
	 * @return The attached series
	 */
	public ITridasSeries getSeries() {
		return series;
	}

	/**
	 * Attach a different series
	 * @param series
	 */
	public void setSeries(ITridasSeries series) {
		this.series = series;

		// update our sample type to reflect what's going on
		if(series instanceof ITridasDerivedSeries) {
			if(!sampleType.isDerived())
				sampleType = SampleType.UNKNOWN_DERIVED;
		}
		else if(sampleType.isDerived())
			sampleType = SampleType.UNKNOWN;
	}
	
	/** Our implementation of Metadata */
	private BaseSampleMetadata metadata;

	/** Get the metadata interface */
	public CorinaMetadata meta() {
		if(metadata == null)
			metadata = new BaseSampleMetadata(this);
		
		return metadata;
	}
	
	/**
	 * Get the display title of this sample
	 * Generally, this is the lab code
	 * 
	 * For a menubar title, use toString()
	 * 
	 * @return a String, probably lab code
	 */
	public String getDisplayTitle() {
		return getMeta("title", String.class);
	}	

	// loader
	private SampleLoader loader;
	
	public SampleLoader getLoader() {
		return loader;
	}
	
	public void setLoader(SampleLoader loader) {
		this.loader = loader;
	}

	/**
	 * Get the associated tridas identifier
	 */
	public TridasIdentifier getIdentifier() {
		return (series != null) ? series.getIdentifier() : null;
	}
}
