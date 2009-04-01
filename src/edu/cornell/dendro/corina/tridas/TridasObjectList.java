/**
 * 
 */
package edu.cornell.dendro.corina.tridas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.webdbi.CachedResource;
import edu.cornell.dendro.corina.webdbi.CorinaXML;
import edu.cornell.dendro.corina.webdbi.ResourceException;
import edu.cornell.dendro.corina.webdbi.ResourceQueryType;
import edu.cornell.dendro.corina.webdbi.SearchParameters;

/**
 * @author lucasm
 *
 */
public class TridasObjectList extends CachedResource {

	/**
	 * @param cachedResourceName
	 */
	public TridasObjectList() {
		super("tridas.objects", ResourceQueryType.SEARCH);
		
		// make sure we don't start off with null class member
		if(values == null)
			values = new DataValues();
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.webdbi.Resource#prepareQuery(edu.cornell.dendro.corina.webdbi.ResourceQueryType, org.jdom.Element)
	 */
	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) throws ResourceException {
		/*
		 * <searchParams returnObject="object">
		 *  <all />
		 * </searchParams>
		 * 
		 * No, now
		 * 
		 * <searchParams returnObject="object" includeChildren="true">
		 *   <param name="parentObjectId" operator="is" value="NULL" />
		 * </searchParams>
		 */
		SearchParameters sp = new SearchParameters("object");
		sp.addSearchAttribute("includeChildren", "true");
		sp.addSearchConstraint("parentobjectid", "is", "NULL");
		requestElement.addContent(sp.getXMLElement());
		
		return requestElement;
	}

	@Override
	protected void queryFailed(Exception e) {
		System.out.println("Could not load objects:");
		e.printStackTrace();
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.webdbi.Resource#processQueryResult(org.jdom.Document)
	 */
	@Override
	protected synchronized boolean processQueryResult(Document doc) throws ResourceException {
		Element root = doc.getRootElement();
		Element dataElement = root.getChild("content", CorinaXML.CORINA_NS);

		System.out.println("Loading objects...");
		
		if(dataElement == null) {
			System.out.println("No content element in objectlist; ignoring this.");
			return false;
		}
				
		List<Element> data = dataElement.getChildren("object", CorinaXML.TRIDAS_NS);
		DataValues dv = new DataValues();
		
		for(Element e : data) {
			addObject(null, e, dv);
		}
		
		// set our current list to our new list; discard the old list
		values = dv;
		
		return true;
	}
	
	/**
	 * Adds an object to our internal lists
	 * Useful for when we create new objects internally
	 * 
	 * @param parent
	 * @param obj
	 */
	public void addObject(TridasObject parent, TridasObject obj) {
		obj.setParent(parent);
		
		if(parent != null)
			parent.addChild(obj);
	}
	
	/**
	 * Add an object based on an XML element
	 * 
	 * @param parent
	 * @param e
	 */
	private void addObject(TridasObject parent, Element e, DataValues dv) {
		TridasObject obj = TridasObject.xmlToSite(e);
		
		// problem with object? stop here
		if(obj == null)
			return;
		
		// set the parent
		obj.setParent(parent);
		
		// ok, now recursively add my children
		List<Element> children = e.getChildren("object", CorinaXML.TRIDAS_NS);
		for(Element childE : children) {
			addObject(obj, childE, dv);
		}
		
		// if I have a parent, mark myself as its child
		// Note: we do this last to keep childSeriesCount!
		if(parent != null)
			parent.addChild(obj);		

		addObjectToDataValues(obj, dv);
	}
	
	private void addObjectToDataValues(TridasObject o, DataValues dv) {
		String sortVal = o.hasLabCode() ? o.getLabCode() : o.toString();
		
		if(o.isTopLevelObject())
			dv.topLevelObjectMap.put(sortVal, o);
		
		dv.sortedCodeToObjectMap.put(sortVal, o);
	}
	
	/**
	 * Get a list of objects sorted by lab code
	 * 
	 * @return
	 */
	public Collection<TridasObject> getObjectList() {
		return Collections.unmodifiableCollection(values.sortedCodeToObjectMap.values());
	}
	
	/**
	 * Get a list of toplevel objects sorted by lab code
	 * 
	 * @return
	 */
	public Collection<TridasObject> getToplevelObjectList() {
		return Collections.unmodifiableCollection(values.topLevelObjectMap.values());
	}
	
	/**
	 * Find an object, by lab code
	 * 
	 * @param code
	 * @return
	 */
	public TridasObject findObjectByCode(String code) {
		return values.sortedCodeToObjectMap.get(code);
	}
	
	/**
	 * Get a sorted map of lab codes equal to or greater than code
	 * @param code
	 * @return
	 */
	public SortedMap<String, TridasObject> getSimilarObjectsByCode(String code) {
		return Collections.unmodifiableSortedMap(values.sortedCodeToObjectMap.tailMap(code));
	}
	
	private DataValues values;
	
	private class DataValues {
		TreeMap<String, TridasObject> sortedCodeToObjectMap = new TreeMap<String, TridasObject>();
		TreeMap<String, TridasObject> topLevelObjectMap = new TreeMap<String, TridasObject>();
	}
}
