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
public abstract class TridasEntityBase {
	/**
	 * Use the domain-positive constructor instead
	 * @deprecated
	 * @param id
	 * @param name
	 */
	public TridasEntityBase(String id, String name) {
		this("!deprecated", id, name);
	}
	
	public TridasEntityBase(String domain, String id, String title) {
		this.domain = domain;
		this.id = id;
		this.title = title;
	}

	/** The object's internally represented id */
	protected String id;	
	/** The object's title */
	protected String title;
	/** The domain the object is from */
	protected String domain;
	
	/** A resource identifier */
	protected ResourceIdentifier resourceIdentifier;
	
	public final static String ID_NEW = "::NEW::";
	public final static String ID_INVALID = "::INVALID::";
	public final static String NAME_INVALID = "::INVALID::";

	public String toString() {
		return title;
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
		if(o instanceof TridasEntityBase)
			return (((TridasEntityBase)o).id.equals(this.id));
		
		return super.equals(o);
	}

	public int compareTo(Object o) {
		if(o instanceof TridasEntityBase)
			return ((TridasEntityBase)o).title.compareToIgnoreCase(this.title);
	
		// errr? yeah.
		return 0;
	}
	
	/**
	 * Takes obj's identity
	 * 
	 * @param obj
	 */
	public void assimilateIntermediateObject(TridasEntityBase obj) {
		this.id = obj.id;
		this.resourceIdentifier = obj.resourceIdentifier;
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
	private Vector<TridasEntityListener> listeners = new Vector<TridasEntityListener>();
	
	public void addIntermediateObjectListener(TridasEntityListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}
	
	public void removeIntermediateObjectListener(TridasEntityListener l) {
		listeners.remove(l);
	}

	protected void fireEvent(String method) {
		// alert all listeners
		Vector<TridasEntityListener> l;
		synchronized (this) {
			l = (Vector<TridasEntityListener>) listeners.clone();
		}

		int size = l.size();

		if (size == 0)
			return;

		TridasEntityEvent e = new TridasEntityEvent(this);

		try {
			Class<?> types[] = new Class[] { TridasEntityEvent.class };
			Method m = TridasEntityListener.class.getMethod(method, types);
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