package edu.cornell.dendro.corina.webdbi;

import org.jdom.Element;

public class SearchParameters {	
	private Element searchElement;
	private String asText;
	private boolean hasConstraints;
	private boolean hasAllConstraint;
	
	public SearchParameters(String returnObjectType) {
		searchElement = new CorinaElement("searchParams");
		searchElement.setAttribute("returnObject", returnObjectType);
		asText = "";
		
		hasConstraints = hasAllConstraint = false;
	}
	
	/**
	 * Add an attribute to the root search element (e.g., includeChildren="true")
	 * @param name
	 * @param value
	 */
	public void addSearchAttribute(String name, String value) {
		searchElement.setAttribute(name, value);
		
		asText += "{" + name + "=" + value + "}";
	}
	
	/**
	 * Set to true to return all objects of type 'returnObject'
	 * Useful to search for all sites, for instance
	 * @param all
	 */
	public void addSearchForAll() {
		if(hasConstraints || hasAllConstraint)
			throw new IllegalArgumentException("Cannot have both search constraints and all constraint/multiple all constraints");
		searchElement.addContent(new CorinaElement("all"));
		asText += "[all]";
		hasAllConstraint = true;
	}
	
	public void addSearchConstraint(String name, String comparison, String value) {
		if(hasAllConstraint)
			throw new IllegalArgumentException("Cannot have all constraint AND search constraints");
		
		Element param = new CorinaElement("param");
		
		param.setAttribute("name", name);
		param.setAttribute("operator", comparison);
		param.setAttribute("value", value);
	
		hasConstraints = true;
		
		if(asText.length() != 0)
			asText += ",";
		asText += name + comparison + value;
		
		searchElement.addContent(param);
	}
	
	public Element getXMLElement() {
		return searchElement;
	}
	
	public String toString() {
		return asText;
	}
}
