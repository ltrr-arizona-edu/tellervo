package edu.cornell.dendro.corina;

import java.util.HashMap;
import java.util.Map;

import edu.cornell.dendro.corina.graph.Graphable;
import edu.cornell.dendro.corina_indexing.Indexable;

public class BaseSample {

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

	public static void copy(BaseSample source, BaseSample target) {
		target.range = source.range;
		target.meta = source.meta;
	}

	public BaseSample() {
		meta = new HashMap<String, Object>();
		range = new Range();
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

}