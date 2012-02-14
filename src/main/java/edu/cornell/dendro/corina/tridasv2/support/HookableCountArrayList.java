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
/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.support;

import java.util.Collection;
import java.util.List;

/**
 * Wraps a count data list, returning '1' for elements that don't actually have a count set
 * 
 * @author Lucas Madar
 */
public class HookableCountArrayList extends HookableNumericArrayList<Integer> {
	private static final long serialVersionUID = 1L;

	/** The default value we return when we don't have a count */
	private static final Integer defaultValue = 1;
	
	/** The list whose count we're keeping */
	private List<? extends Number> masterList;
	
	public HookableCountArrayList(int initialCapacity, NumericArrayListHook hook,
			List<? extends Number> masterList) {
		super(initialCapacity, hook);
		
		this.masterList = masterList;
	}

	public HookableCountArrayList(NumericArrayListHook hook,
			Collection<? extends Integer> c, List<? extends Number> masterList) {
		super(hook, c);
		
		this.masterList = masterList;
	}

	public <T> HookableCountArrayList(NumericArrayListHook hook,
			Collection<T> c, ValueTranslator<T, Integer> translator,
			List<? extends Number> masterList) {
		super(hook, c, translator);
		
		this.masterList = masterList;
	}

	public HookableCountArrayList(NumericArrayListHook hook,
			List<? extends Number> masterList) {
		super(hook);
		
		this.masterList = masterList;
	}
	
	/**
	 * Update the master list
	 * @param masterList a new list, can't be null
	 */
	public void setMasterList(List<? extends Number> masterList) {
		if(masterList == null)
			throw new NullPointerException();
		
		this.masterList = masterList;
	}
	
	@Override
	public Integer get(int index) {
		// out of bounds of master list? error
		if(index >= masterList.size())
			throw new IndexOutOfBoundsException("Index " + index + ", Size " + size());
		
		// out of bounds of our list, but not master list? return default value
		if(index >= super.size())
			return defaultValue;
		
		// we have it? ok then.
		return super.get(index);
	}
	
	@Override
	public int size() {
		return masterList.size();
	}
	
	@Override
	public boolean isEmpty() {
		return masterList.isEmpty();
	}
	
	/** 
	 * Get the actual underlying size of the count list
	 * (size() is overridden to match the master list)
	 * @return the actual size
	 */
	protected int actualSize() {
		return super.size();
	}
	
	/**
	 * Get the actual underlying empty status
	 * @return true if no count exists, false otherwise
	 */
	protected boolean actualIsEmpty() {
		return super.isEmpty();
	}
}
