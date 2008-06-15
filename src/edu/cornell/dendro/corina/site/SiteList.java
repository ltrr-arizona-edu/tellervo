/**
 * 
 */
package edu.cornell.dendro.corina.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
			Site site;
			String name, code;
			String id = se.getAttributeValue("id");
			Element child;
			String val;
			
			if(id == null) {
				System.out.println("Site without id?");
				continue;
			}
			
			name = se.getChildText("name");
			code = se.getChildText("code");
			
			if(name == null || code == null) {
				System.out.println("Site " + id + " has no name and/or code");
				continue;
			}
			
			site = new Site(id, name, code);

			// catch a region tag (can we have more than one??)
			// FIXME: Can we have more than one region?
			child = se.getChild("region");
			if(child != null && ((val = child.getAttributeValue("id")) != null)) {
				site.setRegion(val, child.getText());
			}
			
			// now, get any subsites
			child = se.getChild("references");
			if(child != null) {
				List<Element> subsites = child.getChildren("subSite");
				
				for(Element ss : subsites) {
					id = ss.getAttributeValue("id");
					name = ss.getChildText("name");
					
					if(id == null || name == null) {
						System.out.println("Subsite missing an id or name??");
						continue;
					}
					
					// add our subsite
					site.addSubsite(new Subsite(id, name));
				}
			}

			// sort the subsites alphabetically
			site.sortSubsites();

			// finally, add the site to our map
			newsites.put(site.getCode(), site);
			
		}

		// set our current list to our new list; discard the old list
		siteMap = newsites;
		
		return true;
	}
	
	public Collection<Site> getSites() {
		return siteMap.values();
	}
	
	private TreeMap<String, Site> siteMap;
}
