package edu.cornell.dendro.corina.tridasv2.support;

import java.util.List;

import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;

import edu.cornell.dendro.corina.sample.Sample;

public class TridasWeiserjahreWrapper implements NumericArrayListHook {
	private List<TridasValue> values;
	
	private HookableNumericArrayList<Integer> incr;
	private HookableNumericArrayList<Integer> decr;

	private boolean valid;
	
	public TridasWeiserjahreWrapper(TridasValues tridasValues) {
		// sanity check
		if(!Sample.WEISERJAHRE_VARIABLE.equals(tridasValues.getVariable()))
			throw new IllegalArgumentException("Weiserjahre wrapper only works on Corina/Weiserjahre");
		
		this.values = tridasValues.getValues();

		WeiserjahreDataTranslator wjtranslator = new WeiserjahreDataTranslator();
		
		wjtranslator.setField(WeiserjahreDataTranslator.Field.INCR);
		incr = new HookableNumericArrayList<Integer>(this, values, wjtranslator);
		wjtranslator.setField(WeiserjahreDataTranslator.Field.DECR);
		decr = new HookableNumericArrayList<Integer>(this, values, wjtranslator);
		
		valid = true;
	}

	public void setIncr(List<Integer> in) {
		incr = (in != null) ? new HookableNumericArrayList<Integer>(this, in)
				: new HookableNumericArrayList<Integer>(this);		
		
		valid = (incr.size() == decr.size());
		if(valid) {
			values.clear();
			populateWJ();
		}
	}
	
	public void setDecr(List<Integer> in) {
		decr = (in != null) ? new HookableNumericArrayList<Integer>(this, in)
				: new HookableNumericArrayList<Integer>(this);		
		valid = (incr.size() == decr.size());
		if(valid) {
			values.clear();
			populateWJ();
		}
	}

	
	public List<Integer> getIncr() {
		return incr;
	}

	public List<Integer> getDecr() {
		return decr;
	}

	private void populateWJ() {
		if(!valid || !values.isEmpty())
			throw new IllegalStateException("Populating wj when in bad state!");
		
		// erase the list and build
		for(int i = 0, len = incr.size(); i < len; i++) {
			TridasValue tv = new TridasValue();

			tv.setValue(wjForIndex(i));
			
			values.add(tv);
		}
	}
	
	/**
	 * Go through TridasValues and set indexes to be sequential
	 * 
	 * Not used? Well, we never locally modify weiserjahre...
	 * 
	private void reindex() {
		for(int i = 0, len = values.size(); i < len; i++) {
			values.get(i).setIndex(Integer.toString(i));
		}
	}
	 */
	
	public final void addedElement(List<? extends Number> list, int index, Number e) {
		boolean atEnd = !(index < values.size());
		
		if(!atEnd)
			throw new IllegalStateException("Adding values to middle of wj lists is not supported");

		boolean wasValid = valid;
		valid = (incr.size() == decr.size());
		
		if(!wasValid && valid) {
			values.clear();
			populateWJ();
		}
	}

	public void changedElement(List<? extends Number> list, int index, Number e) {
		if(valid)
			values.get(index).setValue(wjForIndex(index));
	}

	public void cleared(List<? extends Number> list) {
		values.clear();
		valid = (incr.size() == decr.size());
	}

	public void removedElement(List<? extends Number> list, int index) {
		throw new IllegalStateException("Can't remove elements from a weiserjahre list!");
	}

	public void getting(List<? extends Number> list, int index) {
		if(!valid)
			throw new IllegalStateException("Getting data from an invalid weiserjahre list!");
	}
	
	/**
	 * Get the WJ string for this index
	 * Does no range checking
	 * @param index
	 * @return A weiserjahre value in "incr/decr" form
	 */
	private String wjForIndex(int index) {
		return incr.get(index) + "/" + decr.get(index);
	}
	
	private static class WeiserjahreDataTranslator implements ValueTranslator<TridasValue, Integer> {
		private enum Field {
			INCR,
			DECR;			
		}
		
		private Field field = Field.INCR;
		
		public void setField(Field field) {
			this.field = field;
		}
		
		public Integer translate(TridasValue o) {
			String strvalue = o.getValue();
			int slashPos;
		
			if(strvalue == null || (slashPos = strvalue.indexOf('/')) < 1)
				throw new NumberFormatException("Invalid Weiserjahre Data Format: " + o.toString());
			
			try {
				switch(field) {
				case INCR:
					return Integer.parseInt(strvalue.substring(0, slashPos));
				case DECR:
					return Integer.parseInt(strvalue.substring(slashPos + 1));
				default:
					throw new IllegalStateException();
				}
			} catch (NumberFormatException nfpe) {
				throw new NumberFormatException("Invalid Weiserjahre data: " + o.toString());
			}			
		}		
	}
}
