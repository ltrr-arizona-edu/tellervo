package edu.cornell.dendro.corina.tridasv2.support;

import java.util.List;

import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;

public class TridasRingWidthWrapper_Legacy implements NumericArrayListHook {
	private List<TridasValue> values;
		
	private HookableNumericArrayList<Number> data;
	private HookableCountArrayList count;

	/**
	 * Create a new ring width wrapper around these values
	 * 
	 * @param tridasValues
	 * @param usesCounts
	 */
	public TridasRingWidthWrapper_Legacy(TridasValues tridasValues, boolean usesCounts) {
		// sanity check
		if(!tridasValues.getVariable().isSetNormalTridas() ||
				tridasValues.getVariable().getNormalTridas() != NormalTridasVariable.RING_WIDTH) 
			throw new IllegalArgumentException("RingWidthWrapper only works on Tridas Ring Widths");
		
		this.values = tridasValues.getValues();
		this.usesCounts = usesCounts;
	
		/**
		 * Translate from string 'value' to Integer or Double
		 */
		ValueTranslator<TridasValue, Number> dataTranslator = new ValueTranslator<TridasValue, Number>() {
			public final Number translate(TridasValue o) {
				String strValue = o.getValue();

				// no value = bad number!
				if(strValue == null)
					throw new NumberFormatException();
				
				if(strValue.indexOf('.') < 0)
					return Integer.parseInt(strValue);
				else
					return Double.parseDouble(strValue);
			}
		};
				
		data = new HookableNumericArrayList<Number>(this, values, dataTranslator);
		
		if(usesCounts) {
			/**
			 * Translate from BigInteger count to count
			 * Note that a null (nonexistent) count means 1
			 */
			ValueTranslator<TridasValue, Integer> countTranslator = new ValueTranslator<TridasValue, Integer>() {
				public final Integer translate(TridasValue o) {
					Integer count = o.getCount();
					
					return (count != null) ? count.intValue() : 1;
				}
			};

			count = new HookableCountArrayList(this, values, countTranslator, data);
			countsValid = true;
		}
		else {
			count = null;
			countsValid = false;
			clearCounts(); // just to be safe?
		}
		
	}
	
	private final void checkCountsValid() {
		countsValid = (count.actualSize() == data.size() || count.actualIsEmpty()); 
	}
	
	/**
	 * Resets the internal values list
	 * @param in
	 */
	public void setData(List<Number> in) {
		data = (in != null) ? new HookableNumericArrayList<Number>(this, in)
				: new HookableNumericArrayList<Number>(this);
		values.clear();

		for (int i = 0, len = values.size(); i < len; i++) {
			TridasValue value = new TridasValue();

			value.setValue(data.get(i).toString());
			value.setIndex(Integer.toString(i));

			values.add(value);
		}

		if (usesCounts) {
			count.setMasterList(data);
			checkCountsValid();

			if (countsValid)
				copyOverCounts();
		}
	}
	
	/**
	 * Resets the internal count list
	 * @param in
	 */
	public void setCount(List<Integer> in) {
		if(!usesCounts) {
			if(in == null || in.isEmpty())
				return;
			
			for(int i = 0, len = in.size(); i < len; i++) {
				Integer v = in.get(i);
				
				if(v != null && v != 1) 
					throw new IllegalArgumentException("Counts contains a non-trival count");
			}
			
			return;
		}
		
		count = (in != null) ? new HookableCountArrayList(this, in, data)
				: new HookableCountArrayList(this, data);
		checkCountsValid();
		
		if(countsValid)
			copyOverCounts();
		else
			clearCounts();
	}
	
	public boolean hasCount() {
		return usesCounts && !count.actualIsEmpty();		
	}
	
	public List<Integer> getCount() {
		return count;
	}

	public List<Number> getData() {
		return data;
	}
	
	/**
	 * Go through TridasValues and set indexes to be sequential
	 */
	private void reindex() {
		for(int i = 0, len = values.size(); i < len; i++) {
			values.get(i).setIndex(Integer.toString(i));
		}
	}

	private final void copyOverCounts() {
		for(int i = 0, len = values.size(); i < len; i++)
			values.get(i).setCount(count.get(i));
	}
	
	private final void clearCounts() {	
		for(int i = 0, len = values.size(); i < len; i++)
			values.get(i).setCount(1);
	}

	private boolean usesCounts;
	private boolean countsValid;
	
	public final void addedElement(List<? extends Number> list, int index, Number e) {
		if(list == data) {
			boolean shouldReindex = (index < values.size());
			boolean countsWereValid = countsValid;
			
			if(shouldReindex && usesCounts && !count.actualIsEmpty()) 
				throw new IllegalStateException("Adding element to middle of data list with counts present is not supported");
			
			// create a new tridas value!
			TridasValue tv = new TridasValue();
			
			tv.setIndex(Integer.toString(index));
			tv.setValue(e.toString());
			tv.setCount(1);
			
			values.add(tv);
						
			// reindex if data was inserted out-of-order
			if(shouldReindex)
				reindex();

			if(usesCounts) {
				checkCountsValid();
				
				if (!countsWereValid && countsValid)
					copyOverCounts();
				else if (countsWereValid && !countsValid)
					clearCounts();
			}
		}
		else if(list == count) {
			if(usesCounts) {
				boolean countsWereValid = countsValid;
				checkCountsValid();
				
				if(!countsWereValid && countsValid)
					copyOverCounts();
				else if(countsWereValid && !countsValid)
					clearCounts();
			}
		}
	}

	public void changedElement(List<? extends Number> list, int index, Number e) {
		if(list == data) {
			values.get(index).setValue(e.toString());
		}
		else if(list == count) {
			values.get(index).setCount(e.intValue());
		}
	}

	public void cleared(List<? extends Number> list) {
		if(list == data) {
			values.clear();
			if(usesCounts)
				checkCountsValid();
		}
		else if(list == count) {
			clearCounts();
			checkCountsValid();
		}
	}
	
	public void removedElement(List<? extends Number> list, int index) {
		if(usesCounts && !count.actualIsEmpty())
			throw new IllegalStateException("Can't remove elements from populated count list!");
	}

	public void getting(List<? extends Number> list, int index) {
		if(list == count && usesCounts && !countsValid)
			throw new IllegalStateException("Count list is not in a valid state");
	}
}
