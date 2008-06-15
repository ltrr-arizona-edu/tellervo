package edu.cornell.dendro.corina.webdbi;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * This simple class is a way to identify resources that we haven't loaded yet.
 * Contains convenience methods for creating a read query, etc.
 * 
 * @author lucasm
 */

public class ResourceIdentifier extends org.jdom.Element {
	/**
	 * Create a resource with no attributes
	 * @param xmlResourceName
	 */
	public ResourceIdentifier(String xmlResourceName) {
		super(xmlResourceName);
	}

	/**
	 * Create resource with an id attribute, set to the id parameter's value
	 * @param xmlResourceName
	 * @param id
	 */
	public ResourceIdentifier(String xmlResourceName, String id) {
		super(xmlResourceName);
		
		setAttribute("id", id);
	}
	
	public static ResourceIdentifier fromCorinaXMLLink(Element xmlLink) {
		/*
		 * We get something like this:
		 * <link type="corina/xml" url="https://www.blah.com/blah.php" object="measurement" id="xxx" /> 
		 */
		
		String objtype = xmlLink.getAttributeValue("object");
		if(objtype == null)
			throw new IllegalArgumentException("Corina XML element is missing an object type");
		
		ResourceIdentifier rid = new ResourceIdentifier(objtype);
		List<Attribute> attributes = xmlLink.getAttributes();
		
		// add any other attributes (skipping type, object, url)
		for(Attribute attr : attributes) {
			String attrname = attr.getName();
			
			if(attrname.equalsIgnoreCase("type") ||
			   attrname.equalsIgnoreCase("object") ||
			   attrname.equalsIgnoreCase("url"))
				continue;
			
			rid.setAttribute(attr.getName(), attr.getValue());
		}
		
		return rid;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		int attrs = 0;
		
		sb.append(getName());
		
		List<Attribute> attributes = getAttributes();
		for(Attribute attr : attributes) {
			sb.append(attrs == 0 ? " " : ", ");
			sb.append(attr.getName());
			sb.append("=");
			sb.append(attr.getValue());
		}
		
		return sb.toString();
	}
}
