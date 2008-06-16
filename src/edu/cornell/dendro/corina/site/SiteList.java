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
import edu.cornell.dendro.corina.webdbi.ResourceException;
import edu.cornell.dendro.corina.webdbi.ResourceQueryType;
import edu.cornell.dendro.corina.webdbi.SearchParameters;

/**
 * @author lucasm
 *
 */
public class SiteList extends CachedResource {

	/**
	 * @param cachedResourceName
	 */
	public SiteList() {
		super("sites", ResourceQueryType.SEARCH);		
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.webdbi.Resource#prepareQuery(edu.cornell.dendro.corina.webdbi.ResourceQueryType, org.jdom.Element)
	 */
	@Override
	protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) throws ResourceException {
		/*
		 * <searchParams returnObject="site">
		 *  <all />
		 * </searchParams>
		 */
		SearchParameters sp = new SearchParameters("site");
		sp.addSearchForAll();
		requestElement.addContent(sp.getXMLElement());
		
		return requestElement;
	}

	@Override
	protected void queryFailed(Exception e) {
		System.out.println("Could not load sites:");
		e.printStackTrace();
	}
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.webdbi.Resource#processQueryResult(org.jdom.Document)
	 */
	@Override
	protected boolean processQueryResult(Document doc) throws ResourceException {
		Element root = doc.getRootElement();
		Element dataElement = root.getChild("content");
		
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
	
	public Collection<Site> getSites() {
		return siteMap.values();
	}
	
	public Site findSite(String code) {
		return siteMap.get(code);
	}
	
	public SortedMap<String, Site> getSimilarSites(String code) {
		return siteMap.tailMap(code);
	}
	
	private TreeMap<String, Site> siteMap;
}
