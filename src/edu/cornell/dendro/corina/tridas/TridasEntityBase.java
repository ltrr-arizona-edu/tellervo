package edu.cornell.dendro.corina.tridas;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Element;

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.util.ISO8601Date;
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
public abstract class TridasEntityBase implements Comparable<Object> {
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
		
		// weak-cache ourselves
		TridasEntityMap.put(this);
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
		
		metadata = flattenXML(rootElement);
	}
	
	/** The object's title */
	protected String title;	

	/** The identifer (id,domain) the object is from */
	protected TridasIdentifier identifier;
	
	/** A hashmap of key -> value pairs for metadata */
	protected Map<String, Object> metadata;
	
	public final static String NAME_INVALID = "::INVALID::";

	public String toString() {
		return title;
	}
	
	/**
	 * Get the tridas identifier associated with this
	 * @return
	 */
	public TridasIdentifier getIdentifier() {
		return identifier;
	}

	/**
	 * Convenience method: get the entity name (radius, sample, etc)
	 * @return
	 */
	public String getTridasEntityName() {
		return identifier.getEntityName();
	}
	
	/**
	 * Get the ID of this object.
	 * Same as calling getIdentifier().getID()
	 */
	public String getID() {
		return identifier.getID();
	}
	
	/**
	 * Check if this is a new entity (does not exist on server)
	 * @return
	 */
	public boolean isNew() {
		return identifier.getID().equals(TridasIdentifier.ID_NEW);
	}
	
	/**
	 * Map this entity onto this sample (set metadata, etc)
	 * @param s
	 */
	public void mapOntoSample(BaseSample s) {
		// by default, do nothing
	}
	
	/**
	 * Access an entity's metadata
	 * Can only call this if metadata has been initialized (via XML constructor or manually)
	 * @param key
	 * @return
	 */
	public Object getMetadata(String key) {
		// no metadata? hrmmm
		if(metadata == null) {
			System.err.println("Warning: Attempt to access metadata when it didn't exist!");
			return null;
		}
		
		return metadata.get(key);
	}
	
	/**
	 * Convenience method: returns a string
	 * @param key
	 * @return
	 */
	public String getMetadataString(String key) {
		Object value = getMetadata(key);
		
		return (value == null) ? null : value.toString();
	}
	
	/**
	 * Check to see if a metadata key exists
	 * @param key
	 * @return
	 */
	public boolean hasMetadata(String key) {
		return (metadata == null) ? false : metadata.containsKey(key);
	}

	/**
	 * Flatten an XML element into a map of strings and object values
	 * @see getFlattenedObject(java.lang.String name, java.lang.String value)
	 * @param root
	 * @return
	 */
	public Map<String, Object> flattenXML(Element root) {
		HashMap<String, Object> flatmap = new HashMap<String, Object>();
		
		flattenXML(root, "", flatmap);
		
		/**
		for(Map.Entry<String, Object> v : flatmap.entrySet())
			System.out.println(v.getKey() + ": " + ((v.getValue() != null) ? v.getValue().toString() : "<NULL?!?!>"));
			*/
		
		return flatmap;
	}
	
	private static Set<String> ignores = new HashSet<String>();
	static {
		// a list of trees not to parse!
		ignores.add("object.object");
		ignores.add("object.element");
		ignores.add("element.sample");
		ignores.add("sample.radius");
		ignores.add("radius.series");
	}
	
	/**
	 * 
	 * Passes a flat XML path and a value; parse this and return any other type to be added to the map
	 * @param flatPath
	 * @param value
	 * @return
	 */
	protected Object getFlattenedObject(String flatPath, String value) {
		Class<?> clazz = classMap.getClassFor(flatPath);
		
		// try for a endswith.extension lookup
		if(clazz == null) {
			String extension = "endswith" + flatPath.substring(flatPath.lastIndexOf('.'));
			
			// look up with extension; default to String.class
			clazz = classMap.getClassFor(extension, String.class);
		}
				
		// String? Alright!
		if(clazz.equals(String.class))
			return value;
		
		// Integer
		if(clazz.equals(Integer.class)) {
			try {
				Integer i = Integer.valueOf(value);
				return i;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(flatPath + " is not an Integer! Value: " + value);
			}
		}

		// Float/double? (Handle all as double, then truncate later as needed 
		if(clazz.equals(Float.class) || clazz.equals(Double.class)) {
			try {
				Double d = Double.valueOf(value);
				return d;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(flatPath + " is not a float/double! Value: " + value);
			}
		}
		
		// XML-style dates
		if(clazz.equals(ISO8601Date.class)) {
			try {
				Date d = new ISO8601Date(value);
				return d;
			} catch (ParseException e) {
				throw new IllegalArgumentException(flatPath + " is not a valid ISO8601 date: " + value);
			}
		}
		
		System.out.println("Unknown handler for class " + clazz.getName() + ", defaulting to String.class");
		
		return value;
	}

	/**
	 * The recursive function that handles flattening XML
	 * 
	 * @param root
	 * @param prefix
	 * @param flatmap
	 */
	private void flattenXML(Element root, String prefix, Map<String, Object> flatmap) {
		// Flatten name to prefix.a.b.c....
		String tag = root.getName();

		// special case: measurementSeries and derivedSeries. For our purpose, they mean the same thing!
		if(tag.equals("measurementSeries") || tag.equals("derivedSeries"))
			tag = "series";
		
		String name = (prefix.length() == 0) ? tag : (prefix + "." + tag);
		List<Element> children = (List<Element>) root.getChildren();
		boolean isGeneric = false;
				
		// special case: generic fields
		if(tag.equals("genericField")) {
			name = prefix + ".generic." + root.getAttributeValue("name");
			isGeneric = true;
		}
		
		// do we ignore this?
		if(ignores.contains(name))
			return;
		
		for(Attribute a : (List<Attribute>) root.getAttributes()) {
			String attrName = a.getName();
			String attrValue = a.getValue();
			
			// ignore name and type for generics
			if(isGeneric && (attrName.equals("name") || attrName.equals("type")))
				continue;
			
			// prefix attributes with '@'
			attrName = name + ".@" + a.getName();
			
			flatmap.put(attrName, getFlattenedObject(attrName, attrValue));
		}
		
		// load any children
		if(children.size() > 0) {
			for(Element e : children) 
				flattenXML(e, name, flatmap);
		}
		else {
			flatmap.put(name, getFlattenedObject(name, root.getText()));
		}
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
	
	/**
	 * Code below is for a type mapping for each tridas field
	 */
	
	private static PropertiesClass classMap;
	
	static {
		InputStream classes = TridasEntityBase.class.getClassLoader().getResourceAsStream("edu/cornell/dendro/corina/tridas/TridasClassMapping.properties");
		classMap = new PropertiesClass();
		
		if(classes != null) {
			try {
				classMap.load(classes);
			} catch (IOException e) {
				// shouldn't happen
			}
		}
		else
			System.out.println("Can't load Tridas Entity Type mappings!");
	}
}