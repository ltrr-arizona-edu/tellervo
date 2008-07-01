package edu.cornell.dendro.corina.webdbi;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

public class MapLink {
	private String mapURL;
	
	// don't instantiate me!
	private MapLink(String mapURL) {
		// hack this poorly! :)
		String hacklink = mapURL.replaceFirst("https://dendro.cornell.edu/", "http://dendro.cornell.edu/maphack/");
		
		this.mapURL = hacklink;
		//this.mapURL = mapURL;
	}
	
	public String getMapLinkURL() {
		return mapURL;
	}
	
	/**
	 * Given an element, get its map link (if it exists)
	 * @param rootElement
	 * @return
	 */
	public static MapLink fromRootElement(Element rootElement) {
		List<Element> links = rootElement.getChildren("link");
		
		return fromLinks(links);
	}

	/**
	 * Given a list of Link elements, get the map/html link
	 * @param links
	 * @return
	 */
	public static MapLink fromLinks(List<Element> links) {
		for(Element link : links) {
			String linkType = link.getAttributeValue("type");
			
			if(linkType != null && linkType.equalsIgnoreCase("map/html"))
				return fromMapHTMLLink(link);
		}
		
		return null;
	}
	
	/**
	 * We get something like this:
	 * <link type="map/html" url="https://www.blah.com/blah.php" /> 
	 * 
	 * Turn it into a MapLink!
	 * 
	 * @param xmlLink
	 * @return
	 */
	public static MapLink fromMapHTMLLink(Element xmlLink) {
		String url = xmlLink.getAttributeValue("url");

		if(url == null)
			return null;
		
		return new MapLink(url);
	}
}
