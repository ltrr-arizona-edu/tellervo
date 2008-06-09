package edu.cornell.dendro.corina.webdbi;

import org.jdom.Element;

/**
 * This simple class is a way to identify resources that we haven't loaded yet.
 * Contains convenience methods for creating a read query, etc.
 * 
 * @author lucasm
 */

public class ResourceIdentifier {
	private String xmlAttribute;
	private String xmlAttributeValue;
	
	public ResourceIdentifier(String xmlAttribute, String xmlAttributeValue) {
		this.xmlAttribute = xmlAttribute;
		this.xmlAttributeValue = xmlAttributeValue;
	}
	
	public ResourceIdentifier(String xmlAttributeValue) {
		this("id", xmlAttributeValue);
	}

	public void AttachIdentifier(Element xmlElement) {
		xmlElement.setAttribute(xmlAttribute, xmlAttributeValue);
	}
	
	public String toString() {
		return xmlAttribute + "=\"" + xmlAttributeValue + "\"";
	}
}
