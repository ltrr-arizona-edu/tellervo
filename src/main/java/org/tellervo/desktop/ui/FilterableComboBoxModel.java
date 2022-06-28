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
package org.tellervo.desktop.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 * From: http://forums.sun.com/thread.jspa?threadID=712032
 * Slightly modified
 * @author Lucas Madar
 */
@SuppressWarnings("serial")
public class FilterableComboBoxModel extends AbstractListModel implements
		MutableComboBoxModel {

	public static interface Filter {
		public boolean accept(Object obj);
	}

	private List<Object> items;
	private Filter filter;
	private List<Object> filteredItems;
	private Object selectedItem;

	public FilterableComboBoxModel() {
		items = new ArrayList<Object>();
		filteredItems = new ArrayList<Object>();
		updateFilteredItems();
	}
	
	public FilterableComboBoxModel(List<Object> items) {
		this.items = new ArrayList<Object>(items);
		filteredItems = new ArrayList<Object>(items.size());
		updateFilteredItems();
	}

	public List<Object> getElements() {
		return items;
	}

	public void addElement(Object obj) {

		items.add(obj);
		updateFilteredItems();
	}
	
	public void addElements(List<?> objs) {
		items.addAll(objs);
		updateFilteredItems();
	}

	public void clearElements() {		
		items.clear();
		updateFilteredItems();
	}
	
	public void removeElement(Object obj) {

		items.remove(obj);
		updateFilteredItems();
	}

	public void removeElementAt(int index) {

		items.remove(index);
		updateFilteredItems();
	}

	public void insertElementAt(Object obj, int index) {
		items.add(index, obj);
		updateFilteredItems();
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
		updateFilteredItems();
	}
	
	protected void updateFilteredItems() {

		fireIntervalRemoved(this, 0, filteredItems.size());
		filteredItems.clear();

		if (filter == null)
			filteredItems.addAll(items);
		else {
			for (Object item : items) {
				if (filter.accept(item))
					filteredItems.add(item);
			}
		}
		fireIntervalAdded(this, 0, filteredItems.size());
	}

	public int getSize() {
		return filteredItems.size();
	}

	public Object getElementAt(int index) {
		return filteredItems.get(index);
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Object val) {

		if ((selectedItem == null) && (val == null))
			return;

		if ((selectedItem != null) && selectedItem.equals(val))
			return;

		if ((val != null) && val.equals(selectedItem))
			return;

		selectedItem = val;
		fireContentsChanged(this, -1, -1);
	}

	public void replaceContents(List<?> objs) {
		clearElements();
		addElements(objs);
		
	}
}
