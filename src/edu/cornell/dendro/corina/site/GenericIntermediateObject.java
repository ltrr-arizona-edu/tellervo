package edu.cornell.dendro.corina.site;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;

/**
 * This class is for any number of generic intermediate objects that we might have
 * in between Subsite -> Measurement
 * 
 * Currently:
 *   Tree Specimen Radius
 *   
 * @author lucasm
 *
 */
public abstract class GenericIntermediateObject {
	public GenericIntermediateObject(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** The object's internally represented id */
	protected String id;
	/** The object's name */
	protected String name;
	/** A resource identifier */
	protected ResourceIdentifier resourceIdentifier;
	
	public final static String ID_NEW = "::NEW::";
	public final static String ID_INVALID = "::INVALID::";
	public final static String NAME_INVALID = "::INVALID::";

	public String toString() {
		return name;
	}

	public String getID() {
		return id;
	}
	
	public boolean isNew() {
		return id.equals(ID_NEW);
	}
	
	@Override
	public boolean equals(Object o) {
		// if it's a site, check ids.
		if(o instanceof GenericIntermediateObject)
			return (((GenericIntermediateObject)o).id.equals(this.id));
		
		return super.equals(o);
	}

	public int compareTo(Object o) {
		if(o instanceof GenericIntermediateObject)
			return ((GenericIntermediateObject)o).name.compareToIgnoreCase(this.name);
	
		// errr? yeah.
		return 0;
	}
	
	public ResourceIdentifier getResourceIdentifier() {
		return resourceIdentifier;
	}
	
	public void setResourceIdentifier(ResourceIdentifier resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}
	
	public void setResourceIdentifierFromElement(Element rootElement) {
		setResourceIdentifier(ResourceIdentifier.fromRootElement(rootElement));
	}
	
	public abstract Element toXML();
	
	// listening interface
	private Vector<IntermediateObjectListener> listeners = new Vector<IntermediateObjectListener>();
	
	public void addIntermediateObjectListener(IntermediateObjectListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}
	
	public void removeIntermediateObjectListener(IntermediateObjectListener l) {
		listeners.remove(l);
	}

	protected void fireEvent(String method) {
		// alert all listeners
		Vector<IntermediateObjectListener> l;
		synchronized (this) {
			l = (Vector<IntermediateObjectListener>) listeners.clone();
		}

		int size = l.size();

		if (size == 0)
			return;

		IntermediateObjectEvent e = new IntermediateObjectEvent(this);

		try {
			Class<?> types[] = new Class[] { IntermediateObjectEvent.class };
			Method m = IntermediateObjectListener.class.getMethod(method, types);
			Object args[] = new Object[] { e };

			for (int i = 0; i < size; i++) {
				SampleListener listener = (SampleListener) l.elementAt(i);
				m.invoke(listener, args);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}