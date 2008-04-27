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
	public static ElementList toListClass(ElementList src, Class<? extends Element> clazz) {
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
	
	public ElementList toListClass(Class<? extends Element> clazz) {
		return toListClass(this, clazz);
	}
	
	private HashMap<Element, Boolean> activeMap = new HashMap<Element, Boolean>();
}
