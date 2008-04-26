package edu.cornell.dendro.corina;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.cornell.dendro.corina.gui.Bug;

public class ElementList extends ArrayList<Element> {
	private static final long serialVersionUID = 1L;

	public ElementList() {
	}

	public ElementList(Collection<Element> c) {
		super(c);
	}
	
	public void setActive(Element e, boolean active) {
		activeMap.put(e, active);
	}
	
	public boolean isActive(Element e) {
		Boolean b = activeMap.get(e);
		
		// not found in map? nobody set a value -- default to true!
		if(!b)
			return true;
		
		return b.booleanValue();
	}

	/**
	 * Copies this list; copying each element using its constructor
	 * to be used to upclass things inside the list (e.g. Element->CachedElement)
	 * Copies activeMap as well
	 * @param src
	 * @param clazz
	 * @return
	 */
	public ElementList toListClass(ElementList src, Class<? extends Element> clazz) {
		ElementList dest = new ElementList();
		Constructor<? extends Element> constructor;
		
		try {
			constructor = clazz.getConstructor(new Class[] { Element.class });
			
			for(int i = 0; i < src.size(); i++) {
				Element olde = src.get(i);
				Element newe = constructor.newInstance(new Object[] { olde });
				Boolean isActive = activeMap.get(olde);

				dest.add(newe);
				if(isActive != null)
					dest.activeMap.put(newe, isActive);
			}
		} catch (Exception e) {
			new Bug(e);
		}		
		
		return dest;
	}
	
	private HashMap<Element, Boolean> activeMap = new HashMap<Element, Boolean>();
}
