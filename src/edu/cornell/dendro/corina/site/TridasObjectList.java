/**
 * 
 */
package edu.cornell.dendro.corina.site;

import java.util.ArrayList;
import java.util.Collection;
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
		super("tridas:objects", ResourceQueryType.SEARCH);		
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
	protected boolean processQueryResult(Document doc) throws ResourceException {
		Element root = doc.getRootElement();
		Element dataElement = root.getChild("content", CorinaXML.CORINA_NS);

		System.out.println("Loading objects...");
		
		if(dataElement == null) {
			System.out.println("No content element in dictionary; ignoring this.");
			return false;
		}
				
		List<?> data = dataElement.getChildren("site");
		Iterator<?> itr = data.iterator();
		TreeMap<String, Site> newsites = new TreeMap<String, Site>();
		
		while(itr.hasNext()) {
			Element se = (Element) itr.next();
			Site site = Site.xmlToSite(se);
			
			// problem with the site?
			if(site == null)
				continue;
			
			// add the site to our map
			newsites.put(site.getCode(), site);
			
		}

		// set our current list to our new list; discard the old list
		siteMap = newsites;
		
		return true;
	}
	
	/**
	 * Gets the list of sites sorted alphabetically by code (as a Collection)
	 * @return
	 */
	public Collection<Site> getSites() {
		return siteMap.values();
	}
	
	/**
	 * Find a site, by code
	 * 
	 * @param code
	 * @return
	 */
	public Site findSite(String code) {
		return siteMap.get(code);
	}
	
	/**
	 * Get a sorted map of codes equal to or greater than code
	 * @param code
	 * @return
	 */
	public SortedMap<String, Site> getSimilarSites(String code) {
		return siteMap.tailMap(code);
	}
	
	/**
	 * Add a site to the list
	 * Use sparingly!
	 * @param site
	 */
	public void addSite(Site site) {
		siteMap.put(site.getCode(), site);
	}
	
	// the site map
	private TreeMap<String, Site> siteMap;
}
