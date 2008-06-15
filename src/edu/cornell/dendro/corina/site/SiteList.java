/**
 * 
 */
package edu.cornell.dendro.corina.site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		List<Site> newsites = new ArrayList<Site>();
		
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
			
			newsites.add(site);
		}

		// set our current list to our new list; discard the old list
		sites = newsites;
		
		return true;
	}
	
	public List<Site> getSites() {
		return sites;
	}

	private List<Site> sites;
}
