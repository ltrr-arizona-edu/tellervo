/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.support;

import java.util.AbstractList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;

/**
 * A "better" way of handling the data/count lists: 
 * Wrap completely around the TridasValue list!
 * 
 * Count handling is a little sketchy in places where we can add/remove data, 
 * but this should never be an issue as there's no reason people should
 * be able to modify series with non-trivial counts.
 * 
 * @author Lucas Madar
 */

public final class TridasWidthValueWrapper {
	private List<TridasValue> values;
	private DataWrapper data;
	private CountWrapper count;
	private final static Logger log = LoggerFactory.getLogger(TridasWidthValueWrapper.class);

	private final class CountWrapper extends AbstractList<Integer> {
		/** The index that we've "added" up to */
		private int countIndex;
		
		/** Are all the counts 1? Safety check, not 100% accurate */
		private boolean countsAreAllOne;

		public CountWrapper() {
			// start out with a 'clean slate'
			clear();
		}
				
		// we should never change a count in place, should we?
		// this is why I don't override set

		@Override
		public void add(int index, Integer element) {
			if(index < 0 || index > countIndex) 
				throw new IndexOutOfBoundsException("Index " + index + 
						" not in count list (max " + countIndex + ")");

			// this might rely on our values list being in a proper state
			// which is dangerous. Oh well.
			TridasValue tridasValue = values.get(index);
			tridasValue.setCount(element);
			
			// not null or one? we have a real count
			if(element != null && element != 1)
				countsAreAllOne = false;
		}

		@Override
		public void clear() {
			// don't really do anything, just ensure our 'add' starts back at zero
			countIndex = 0;
			// mark all our counts as 'one' by default
			countsAreAllOne = true;
		}

		@Override
		public Integer get(int index) {
			try{
			TridasValue tridasValue = values.get(index);
			// if no count is present, count = 1
			return tridasValue.isSetCount() ? tridasValue.getCount() : 1;
			
			
			} catch (IndexOutOfBoundsException e)
			{
				log.warn("Out of bounds exception while getting count");
				return 1;
			}
		

		}

		@Override
		public Integer remove(int index) {
			// we must 'remove' a value that doesn't exist...
			if(index < 0 || index >= countIndex) 
			{
				//throw new IndexOutOfBoundsException("Index " + index + 
					//	" not in count list (max " + countIndex + ")");
				log.error("Index " + index + 
						" not in count list (max " + countIndex + ")");
				
			}
			
			// basic sanity check, but not safe though!
			if(!countsAreAllOne)
				throw new IllegalStateException("Removing a value from a non-trivial count list");
			
			// this is totally unsafe, but should probably never be used
			countIndex--;
			
			return 1;
		}
		
		@Override
		public int size() {
			return values.size();
		}	
	}
	
	/**
	 * Class that emulates a data value list around TridasValue
	 * Implemented for legacy reasons - in the future, please
	 * get rid of this gross, nasty kludge
	 */
	private final class DataWrapper extends AbstractList<Number> {
		// get the indexes to where we want them
		public DataWrapper() {
		}
		
		/**
		 * Get the 'Number' represented by a tridasValue
		 * @param tridasValue
		 * @return a Number
		 * @throws NumberFormatException if the value is not valid
		 */
		private final Number tridasValueAsNumber(TridasValue tridasValue) {
			String value = tridasValue.getValue();
			
			// lack of a number is invalid!
			if(value == null)
				throw new NumberFormatException();
		
			// decimal = Double, otherwise Integer
			if(value.indexOf('.') < 0)
				return Integer.valueOf(value);
			else
				return Double.valueOf(value);
		}
				
		@Override
		public Number get(int index) {
			
			try{
				TridasValue tridasValue = values.get(index);
				return tridasValueAsNumber(tridasValue);
			} catch (IndexOutOfBoundsException e)
			{
				log.error("Unable to get index "+index+" from TRiDaS. Series only has "+values.size()+" values.");
			}
			
			return null;
			
		}
		
		@Override
		public void clear() {
			// clearing the data is the same as clearing the values list...
			values.clear();			
		}
		
		@Override
		public void add(int index, Number element) {
			
			
			
			TridasValue newValue = new TridasValue();
			
			if(element==null)
			{
				newValue.setValue("");
			}
			else
			{
				newValue.setValue(element.toString());
			}
						
			values.add(index, newValue);
		}

		@Override
		public Number remove(int index) {
			return tridasValueAsNumber(values.remove(index));
		}

		@Override
		public Number set(int index, Number element) {
			TridasValue tridasValue = values.get(index);
			Number ret = tridasValueAsNumber(tridasValue);
			
			tridasValue.setValue(element.toString());
			
			return ret;
		}

		@Override
		public int size() {
			return values.size();
		}
	}
	
	/**
	 * Create a new ring width wrapper around these values
	 * 
	 * @param tridasValues
	 * @param usesCounts
	 */
	public TridasWidthValueWrapper(TridasValues tridasValues) {
		// sanity check
	/*	if(!tridasValues.getVariable().isSetNormalTridas()) 
		{
				//throw new IllegalArgumentException("RingWidthWrapper only works on ring, earlywood or latewood widths");
			log.warn("RingWidthWrapper only works on ring, earlywood or latewood widths");
		}
		else if(tridasValues.getVariable().getNormalTridas() != NormalTridasVariable.RING_WIDTH &&
		   tridasValues.getVariable().getNormalTridas() != NormalTridasVariable.EARLYWOOD_WIDTH &&
		   tridasValues.getVariable().getNormalTridas() != NormalTridasVariable.LATEWOOD_WIDTH	)
		{
			//throw new IllegalArgumentException("RingWidthWrapper only works on ring, earlywood or latewood widths");
			log.warn("RingWidthWrapper only works on ring, earlywood or latewood widths");
		}
		*/
		
		if(tridasValues==null)
		{
			log.error("TridasValues is null");
			return;
		}
		
		if(tridasValues.getValues()==null)
		{
			log.error("No values in TridasValues");
			return;
		}
		
		log.debug("Series has "+tridasValues.getValues().size()+" values");
		
		this.values = tridasValues.getValues();
		
		data = new DataWrapper();
		count = new CountWrapper();
	}
	
	public void setData(List<Number> in) {
		data.clear();

		// clearing the data? just bail out now...
		if(in == null)
			return;
		
		for(Number n : in)
			data.add(n);
	}
	
	public final List<Number> getData() {
		return data;
	}
	
	public void setCount(List<Integer> in) {
		count.clear();

		// clearing the data? just bail out now...
		if(in == null)
			return;
		
		for(Integer n : in)
			count.add(n);
	}
	
	public final List<Integer> getCount() {
		return count;
	}
	
	public boolean hasCount() {
		if(count==null) return false;
		return count.size() > 0;
	}
}
