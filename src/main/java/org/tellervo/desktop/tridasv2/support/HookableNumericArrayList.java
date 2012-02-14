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
package org.tellervo.desktop.tridasv2.support;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An array list that: 
 * 1) acts dumb in certain ways (intended only for numbers)
 * 2) allows for easy hooks after a set, get, or remove
 * 
 * @author Lucas Madar
 *
 */
public class HookableNumericArrayList<E extends Number> extends ArrayList<E> {

	/**
	 * @param hook
	 * @param c
	 */
	public HookableNumericArrayList(NumericArrayListHook hook, Collection<? extends E> c) {
		super(c.size());
		super.addAll(c);
		
		this.hook = hook;
	}

	/**
	 * @param <T>
	 * @param hook
	 * @param c
	 * @param translator
	 */
	public <T> HookableNumericArrayList(NumericArrayListHook hook, Collection<T> c, ValueTranslator<T, E> translator) {
		super(c.size());
		
		// go through each element and add its translation
		for(T o : c) 
			super.add(translator.translate(o));
		
		this.hook = hook;
	}	
	
	/**
	 * Create a new, empty list
	 * @param hook
	 */
	public HookableNumericArrayList(NumericArrayListHook hook) {
		super();
		
		this.hook = hook;
	}

	/**
	 * Create a new, empty list with the given capacity
	 * @param initialCapacity
	 */
	public HookableNumericArrayList(int initialCapacity, NumericArrayListHook hook) {
		super(initialCapacity);
		
		this.hook = hook;
	}

	private static final long serialVersionUID = 1L;
	private NumericArrayListHook hook;
	
	@Override
	public E get(int index) {
		hook.getting(this, index);
		return super.get(index);
	}

	@Override
	public boolean add(E e) {
		boolean ret = super.add(e);

		hook.addedElement(this, size(), e);

		return ret;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		
		hook.addedElement(this, index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int location = size();
		boolean ret = super.addAll(c);

		// iterate through the list and notify
		for(E value : c)
			hook.addedElement(this, location++, value);
		
		return ret;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean ret = super.addAll(index, c);
		
		for(E value : c)
			hook.addedElement(this, index++, value);
		
		return ret;
	}

	@Override
	public void clear() {
		super.clear();
		hook.cleared(this);
	}

	@Override
	public E remove(int index) {
		E e = super.remove(index);
		
		hook.removedElement(this, index);
		
		return e;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		int len = toIndex - fromIndex;
		
		if(len < 0 || size() - len < 0)
			throw new IndexOutOfBoundsException();

		// stupidly remove each element, one by one
		for(int i = 0; i < len; i++)
			remove(fromIndex);
	}

	@Override
	public E set(int index, E element) {
		E e = super.set(index, element);
		
		hook.changedElement(this, index, element);
		
		return e;
	}

	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}
	
	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/** 
	 * @throws UnsupportedOperationException Makes no sense with numbers
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
}
