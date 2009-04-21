package edu.cornell.dendro.corina.tridas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Element;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.webdbi.CorinaXML;

public class TridasObject extends TridasEntityBase implements Comparable<Object> {
	public TridasObject(TridasIdentifier identifier, String title) {
		super(identifier, title);

		initialize();
	}
	
	public TridasObject(Element rootElement) {
		super(rootElement);
		
		initialize();
	}

	public TridasObject(String entityType, Element identifier, String title) {
		super(entityType, identifier, title);

		initialize();
	}

	public TridasObject(TridasIdentifier identifier, String title, String labCode) {
		this(identifier, title);

		initialize();

		this.labCode = labCode;
		hasCode = true;
	}
	
	private void initialize() {
		children = new ArrayList<TridasObject>();
		parent = null;
	
		// maybe this should be more generic
		hasCode = hasMetadata("object.generic.corina.objectLabCode");
		if(hasCode)
			labCode = getMetadataString("object.generic.corina.objectLabCode");
		
		// do we have any counts?
		if(hasMetadata("object.generic.corina.countOfChildSeries")) {
			mySeriesCount = childSeriesCount = (Integer) getMetadata("object.generic.corina.countOfChildSeries");
			countsValid = true;
		}
		else
			countsValid = false;
	}

	/** The site's code (e.g., CYL) */
	private String labCode;
	private boolean hasCode;
	
	/** A list of any children */
	private List<TridasObject> children;
	private TridasObject parent;
	
	/** How many child series do I have (vmeasurements) */
	private int mySeriesCount;
	/** How many child series do my children and I have? */
	private int childSeriesCount;
	/** Are these counts valid? */
	private boolean countsValid;
	
	/**
	 * Determines if this is a top level object (has no parent)
	 * @return
	 */
	public boolean isTopLevelObject() {
		return (parent == null);
	}
	
	/**
	 * Determines if this has any children
	 * @return
	 */
	public boolean hasChildren() {
		return children.isEmpty();
	}
	
	/**
	 * Does this object have a lab code?
	 * @return
	 */
	public boolean hasLabCode() {
		return hasCode;
	}
	
	/**
	 * Are my counts of child series valid?
	 * @return
	 */
	public boolean seriesCountsValid() {
		return countsValid;
	}
	
	/**
	 * Get the number of series that this particular object has
	 * @return
	 */
	public int getObjectSeriesCount() {
		return this.mySeriesCount;
	}
	
	/**
	 * Get the number of objects that this series and its children have
	 * @return
	 */
	public int getObjectChildSeriesCount() {
		return this.childSeriesCount;
	}
	
	public String toFullString() {
		if(!hasCode || labCode.equals(title))
			return title;
		
		return "[" + labCode + "] " + title;
	}
		
	public String getLabCode() {
		return hasCode ? labCode : "(n/a)";
	}
	
	/**
	 * Set my parent object (may be null)
	 * @param parent
	 */
	public void setParent(TridasObject parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns parent, or null if I am a toplevel object
	 * @return
	 */
	public TridasObject getParent() {
		return parent;
	}

	/**
	 * Add a child object
	 * @param subsite
	 */
	public void addChild(TridasObject o) {
		children.add(o);
		
		// keep track of the number of child series
		childSeriesCount += o.childSeriesCount;
	}

	/**
	 * Get a read-only list of children
	 * @return
	 */
	public List<TridasObject> getChildren() {
		return Collections.unmodifiableList(children);
	}	
	
	/**
	 * Create a TridasObject from an XML Element
	 * Does not handle the recursive case
	 * @param root
	 * @return
	 */
	public static TridasObject xmlTaaaoSite(Element root) {
		String id, title;
		Element identifier;
		TridasIdentifier tid;

		// this shouldn't fail if schema validated correctly
		if((identifier = root.getChild("identifier", CorinaXML.TRIDAS_NS)) == null)
				throw new IllegalArgumentException("missing object identifier");

		tid = new TridasIdentifier(identifier, "object");
			
		if((title = root.getChildText("title", CorinaXML.TRIDAS_NS)) == null)
			throw new IllegalArgumentException("missing object title");
				
		// Finally, create our object
		TridasObject obj = new TridasObject(tid, title);
		
		String val;
		List<Element> el;

		// TODO: Handle type
		
		el = root.getChildren("genericField", CorinaXML.TRIDAS_NS);
		for(Element e : el) {
			String genericFieldName = e.getAttributeValue("name");
			
			if(genericFieldName.equals("corina.objectLabCode")) {
				obj.labCode = e.getText();
				obj.hasCode = true;
			}
			else if(genericFieldName.equals("corina.countOfChildSeries")) {
				obj.mySeriesCount = Integer.parseInt(e.getText());
				obj.childSeriesCount = obj.mySeriesCount;
				obj.countsValid = true;
			}
		}
		
		return obj;
	}
	
	public Element toXML() {
		Element root = new Element("site");
		
		if(!isNew())
			root.setAttribute("id", getID());
		
		root.addContent(new Element("name").setText(title));
		root.addContent(new Element("code").setText(labCode));
		
		// are these necessary?
		// TODO: Regions?
		// TODO: Subsites?
		
		return root;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof String)
			return o.toString().equals(this.labCode);
		
		return (compareTo(o) == 0);
	}

	@Override
	public int compareTo(Object ob) {
		if(!(ob instanceof TridasObject))
			return -1;
		
		TridasObject o = (TridasObject) ob;
		
		// wow, they're the same!
		if(identifier.equals(o.identifier))
			return 0;
		
		// ok, compare by lab codes, then
		if(this.labCode != null && o.labCode != null)
			return notZeroCompare(labCode.compareToIgnoreCase(o.labCode));
		
		return notZeroCompare(title.compareToIgnoreCase(o.title));
	}
	
	// convenience method: if they're equal, but the object isn't equal, it comes after!
	private int notZeroCompare(int origVal) {
		return (origVal == 0) ? 1 : origVal;
	}
}
