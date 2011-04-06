package edu.cornell.dendro.corina.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

@SuppressWarnings("serial")
public class ArrayListModel<E> extends ArrayList<E> implements ListModel, ComboBoxModel {
	private Object selectedObject = null;
	
	public ArrayListModel() {
		super();
	}

	public ArrayListModel(Collection<? extends E> c) {
		super(c);
	}
	
	/**
	 * Convenience method to recreate a list without making a new one
	 * @param c
	 */
	public void replaceContents(Collection<? extends E> c) {
		if(!isEmpty())
			clear();
		addAll(c);
	}

	@Override
	public boolean add(E o) {
		boolean ret = super.add(o);
		
		if(ret) {
			// new item is now at size() - 1
			int idx = size() - 1;
			fireIntervalAdded(idx, idx);
		}
		
		return ret;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		
		fireIntervalAdded(index, index);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int first = size();
		boolean ret = super.addAll(c);
		int last = size() - 1;
		
		if(ret)
			fireIntervalAdded(first, last);
		
		return ret;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean ret = super.addAll(index, c);
		
		if(ret)
			fireIntervalAdded(index, index + c.size());
		
		return ret;
	}

	@Override
	public void clear() {
		int last = size() - 1;
		
		super.clear();
		selectedObject = null;
		fireIntervalRemoved(0, last);
	}

	@Override
	public E remove(int index) {
		E ret = super.remove(index);
		
		if(ret != null) {
			
			if(ret == selectedObject)
				selectedObject = null;
			
			fireIntervalRemoved(index, index);
		}
		
		return ret;
	}

	@Override
	public boolean remove(Object o) {
		// bah, reengineer this!
		int idx = indexOf(o);
		
		if(idx == -1)
			return false;
		
		if(o == selectedObject)
			selectedObject = null;
		
		return (remove(idx) == null) ? false : true;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		
		if(selectedObject != null && indexOf(selectedObject) != -1)
			selectedObject = null;
		
		fireIntervalRemoved(fromIndex, toIndex);
	}

	@Override
	public E set(int index, E element) {
		E ret = super.set(index, element);
		
		if(ret == selectedObject)
			selectedObject = element;
		
		fireIntervalChanged(index, index);
		
		return ret;
	}

	/**
	 * Implementation of the ListModel interface
	 */
	public Object getElementAt(int index) {
		return this.get(index);
	}

	/**
	 * Implementation of the ListModel interface
	 */
	public int getSize() {
		return this.size();
	}
	
	/**
	 * Implementation of the method in the ListModel interface
	 * @param listener ListDataListener
	 */
	public void addListDataListener(ListDataListener listener) {
		if (listDataListeners == null) {
			listDataListeners = new ArrayList<ListDataListener>();
		}
		if (!listDataListeners.contains(listener)) {
			listDataListeners.add(listener);
		}
	}

	/**
	 * Implementation of the method in the ListModel interface
	 * @param listener ListDataListener
	 */
	public void removeListDataListener(ListDataListener listener) {
		if (listDataListeners != null) {
			listDataListeners.remove(listener);
		}
	}
	
	/**
	 * Helper method to fire an event to all listeners when an interval in the collection
	 * is added
	 * @param firstIndex  int
	 * @param lastIndex int
	 */
	protected void fireIntervalAdded(int firstIndex, int lastIndex) {
		if (listDataListeners != null) {
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, firstIndex, lastIndex);
			for (ListDataListener listener: listDataListeners) {
				listener.intervalAdded(e);
			}
		}
	}
	
	/**
	 * Helper method to fire an event to all listeners when an interval in the collection
	 * is removed
	 * @param firstIndex  int
	 * @param lastIndex int
	 */
	protected void fireIntervalRemoved(int firstIndex, int lastIndex) {
		if (listDataListeners != null) {
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, firstIndex, lastIndex);
			for (ListDataListener listener: listDataListeners) {
				listener.intervalRemoved(e);
			}
		}
	}
	
	/**
	 * Helper method to fire an event to all listeners when an interval in the collection
	 * is updated
	 * @param firstIndex  int
	 * @param lastIndex int
	 */
	protected void fireIntervalChanged(int firstIndex, int lastIndex) {
		if (listDataListeners != null) {
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, firstIndex, lastIndex);
			for (ListDataListener listener: listDataListeners) {
				listener.contentsChanged(e);
			}
		}		
	}
	
	private List<ListDataListener> listDataListeners;

	public Object getSelectedItem() {
		return selectedObject;
	}

	public void setSelectedItem(Object anItem) {
		if(anItem == null) {
			selectedObject = null;
			return;
		}
		
		for(Object o : this) {
			if(anItem.equals(o)) {
				selectedObject = o;
				return;
			}
		}
		
		selectedObject = null;
	}
}
