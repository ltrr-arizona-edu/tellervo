package edu.cornell.dendro.corina.tridas;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.webdbi.CorinaXML;
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
	 * Create a tridas entity from this tridas identifier
	 * @param identifier
	 * @param title
	 */
	public TridasEntityBase(TridasIdentifier identifier, String title) {
		if(title == null)
			throw new IllegalArgumentException("tridas titles may not be null");
		
		this.identifier = identifier;
		this.title = title;
	}

	/**
	 * Create a tridas entity from this tridas identifier given as an XML element
	 * @param entityType
	 * @param identifier
	 * @param title
	 */
	public TridasEntityBase(String entityType, Element identifier, String title) {
		this(new TridasIdentifier(identifier, entityType), title);
	}
	
	/**
	 * Create a tridas entity from a root tridas element
	 * @param rootElement
	 */
	public TridasEntityBase(Element rootElement) {
		this(rootElement.getName(), 
				rootElement.getChild("identifier", CorinaXML.TRIDAS_NS),
				rootElement.getChildText("title", CorinaXML.TRIDAS_NS)
		);
	}

	
	/** The object's title */
	protected String title;

	/** The identifer (id,domain) the object is from */
	protected TridasIdentifier identifier;
	
	public final static String NAME_INVALID = "::INVALID::";

	public String toString() {
		return title;
	}

	/**
	 * Get the ID of this object.
	 * Same as calling getIdentifier().getID()
	 */
	public String getID() {
		return identifier.getID();
	}
	
	public boolean isNew() {
		return identifier.getID().equals(TridasIdentifier.ID_NEW);
	}
	
	@Override
	public boolean equals(Object o) {
		// if it's a site, check ids.
		if(o instanceof TridasEntityBase) {
			TridasEntityBase b = (TridasEntityBase) o;
			
			return b.identifier.equals(identifier);
		}
		
		return super.equals(o);
	}

	public int compareTo(Object o) {
		if(o instanceof TridasEntityBase)
			return ((TridasEntityBase)o).title.compareToIgnoreCase(this.title);
	
		// errr? yeah.
		return 0;
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public ResourceIdentifier getResourceIdentifier() {
		throw new IllegalArgumentException("This function is deprecated. Fix your code.");
	}
	
	/**
	 * Takes obj's identity
	 * 
	 * @param obj
	 */
	/*
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
	*/
	
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