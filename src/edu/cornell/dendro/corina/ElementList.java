package edu.cornell.dendro.corina;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cornell.dendro.corina.gui.Bug;

public class ElementList extends ArrayList<Element> {
	private static final long serialVersionUID = 1L;

	/**
	 * Convenience method: create a list with a single element, e
	 * @param e
	 * @return
	 */
	public static ElementList singletonList(Element e) {
		ElementList list = new ElementList();
		list.add(e);
		return list;
	}

	public ElementList() {
		super();
	}

	public ElementList(ElementList src) {
		super();
		
		copyFrom(src);
	}
	
	public void copyFrom(ElementList src) {
		// clear out my info
		clear();
		activeMap.clear();
		
		// copy over
		for(Element e : src) {
			// add stuff
			add(e);
			
			// copy active map
			if(!src.isActive(e))
				setActive(e, false);
		}		
	}
	
	public void setActive(Element e, boolean active) {
		activeMap.put(e, active);
	}
	
	public boolean isActive(Element e) {
		Boolean b = activeMap.get(e);
		
		// not found in map? nobody set a value -- default to true!
		if(b == null)
			return true;
		
		return b.booleanValue();
	}
	
	/**
	 * Shortcut method: returns a list of only active elements
	 * @return
	 */
	public ElementList toActiveList() {
		ElementList activeList = new ElementList();
		
		for(Element e : this)
			if(isActive(e))
				activeList.add(e);
		
		return activeList;
	}

	/**
	 * Copies this list; copying each element using its constructor
	 * to be used to upclass things inside the list (e.g. Element->CachedElement)
	 * Copies activeMap as well
	 * @param src
	 * @param clazz
	 * @return
	 */
	public static ElementList toListClassCopy(ElementList src, Class<? extends Element> clazz) {
		ElementList dest = new ElementList();
		Constructor<? extends Element> constructor;
		
		// it's already this kind of list; don't do anything
		if(src.getClass().getName().equals(clazz.getName()))
			return src;
		
		try {
			constructor = clazz.getConstructor(new Class[] { Element.class });
			
			for(int i = 0; i < src.size(); i++) {
				Element olde = src.get(i);
				Element newe = constructor.newInstance(new Object[] { olde });
				Boolean isActive = src.activeMap.get(olde);

				dest.add(newe);
				if(isActive != null)
					dest.activeMap.put(newe, isActive);
			}
		} catch (Exception e) {
			new Bug(e);
		}		
		
		return dest;
	}
	
	/**
	 * Force all elements in this list to be of a particular class
	 * Useful to upclass in place!
	 * 
	 * @param clazz
	 * @return
	 */
	public void forceListClass(Class<? extends Element> clazz) {
		ArrayList<Element> oldList = new ArrayList<Element>(this.size());
		HashMap<Element, Boolean> oldMap = new HashMap<Element, Boolean>();
		Constructor<? extends Element> constructor;
		
		// copy our list over
		oldList.addAll(this);
		oldMap.putAll(activeMap);
		this.clear();
		activeMap.clear();

		try {
			constructor = clazz.getConstructor(new Class[] { Element.class });
			
			for(Element olde : oldList) {
				
				// is it the same? don't clone it 
				if(olde.getClass().getName().equals(clazz.getName())) {
					add(olde);
					if(oldMap.containsKey(olde))
						activeMap.put(olde, oldMap.get(olde));
					
					continue;
				}
			
				// ok, create a new instance
				Element newe = constructor.newInstance(new Object[] { olde });
				Boolean isActive = oldMap.get(olde);

				// add it to ourselves
				add(newe);
				if(isActive != null)
					activeMap.put(newe, isActive);
			}
		} catch (Exception e) {
			new Bug(e);
		}		
		
		// force quick cleanup
		oldList.clear();
		oldMap.clear();
	}
	
	private HashMap<Element, Boolean> activeMap = new HashMap<Element, Boolean>();
}
