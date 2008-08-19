package edu.cornell.dendro.corina.webdbi;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * This simple class is a way to identify resources that we haven't loaded yet.
 * Contains convenience methods for creating a read query, etc.
 * 
 * @author lucasm
 */

public class ResourceIdentifier {
	private Element xmlContent;
	
	/**
	 * Create a resource with no attributes
	 * @param xmlResourceName
	 */
	public ResourceIdentifier(String xmlResourceName) {
		xmlContent = new Element(xmlResourceName);
	}

	/**
	 * Create resource with an id attribute, set to the id parameter's value
	 * @param xmlResourceName
	 * @param id
	 */
	public ResourceIdentifier(String xmlResourceName, String id) {
		xmlContent = new Element(xmlResourceName);
		
		if(id != null)
			xmlContent.setAttribute("id", id);
	}

	/**
	 * Given an element, get its resource identifier
	 * @param rootElement
	 * @return
	 */
	public static ResourceIdentifier fromRootElement(Element rootElement) {
		List<Element> links = rootElement.getChildren("link");
		
		return fromLinks(links);
	}
	
	/**
	 * Given a list of Link elements, get the corina/xml resource identifier
	 * @param links
	 * @return
	 */
	public static ResourceIdentifier fromLinks(List<Element> links) {
		for(Element link : links) {
			String linkType = link.getAttributeValue("type");
			
			if(linkType != null && linkType.equalsIgnoreCase("corina/xml"))
				return fromCorinaXMLLink(link);
		}
		
		return null;
	}
	
	/**
	 * We get something like this:
	 * <link type="corina/xml" url="https://www.blah.com/blah.php" object="measurement" id="xxx" /> 
	 * 
	 * Turn it into a ResourceIdentifier!
	 * 
	 * @param xmlLink
	 * @return
	 */
	public static ResourceIdentifier fromCorinaXMLLink(Element xmlLink) {
		String objtype = xmlLink.getAttributeValue("object");
		if(objtype == null)
			throw new IllegalArgumentException("Corina XML element is missing an object type");
		
		ResourceIdentifier rid = new ResourceIdentifier(objtype);
		List<Attribute> attributes = xmlLink.getAttributes();
		
		// add any other attributes (skipping type, object, url)
		for(Attribute attr : attributes) {
			String attrname = attr.getName();
			
			// this is already the title of our resourceidentifier!
			if(attrname.equalsIgnoreCase("object"))
				continue;
			
			/*
			if(attrname.equalsIgnoreCase("type") ||
			   attrname.equalsIgnoreCase("object") ||
			   attrname.equalsIgnoreCase("url"))
				continue;
				*/
			
			rid.xmlContent.setAttribute(attr.getName(), attr.getValue());
		}
		
		return rid;
	}
	
	/**
	 * Duplicates a resourceidentifier
	 * @param xmlElement
	 * @return
	 */
	public static ResourceIdentifier fromElement(Element xmlElement) {
		ResourceIdentifier rid = new ResourceIdentifier(xmlElement.getName());

		for(Attribute attr : (List<Attribute>) xmlElement.getAttributes()) 
			rid.xmlContent.setAttribute(attr.getName(), attr.getValue());
		
		return rid;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		int attrs = 0;
		
		sb.append(xmlContent.getName());
		
		List<Attribute> attributes = xmlContent.getAttributes();
		for(Attribute attr : attributes) {
			sb.append(attrs == 0 ? " " : ", ");
			sb.append(attr.getName());
			sb.append("=");
			sb.append(attr.getValue());
		}
		
		return sb.toString();
	}

	/**
	 * 
	 * @return an xml object representation
	 */
	public Element asXMLElement() {
		return (Element) xmlContent.clone();
	}
	
	/**
	 * 
	 * @return an xml element with type, object, and url attributes removed
	 */
	public Element asRequestXMLElement() {
		Element e = asXMLElement();
		Iterator<Attribute> iter = ((List<Attribute>) e.getAttributes()).iterator();
		Vector<Attribute> remove = new Vector<Attribute>();

		// stupid concurrent modification of lists!
		while(iter.hasNext()) {
			Attribute attr = iter.next();
			String attrname = attr.getName();

			if(attrname.equalsIgnoreCase("type") ||
					attrname.equalsIgnoreCase("object") ||
					attrname.equalsIgnoreCase("url")) {
				
				remove.add(attr);
			}
		}
		
		// now, remove all those pesky attributes
		for(Attribute a : remove)
			e.removeAttribute(a);
		
		return e;
	}
}
