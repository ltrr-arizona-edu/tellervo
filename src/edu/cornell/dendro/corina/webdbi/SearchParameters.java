package edu.cornell.dendro.corina.webdbi;

import org.jdom.Element;

public class SearchParameters {	
	private Element searchElement;
	private String asText;
	
	public SearchParameters(String returnObjectType) {
		searchElement = new CorinaElement("searchParams");
		searchElement.setAttribute("returnObject", returnObjectType);
		asText = "";
	}
	
	/**
	 * Set to true to return all objects of type 'returnObject'
	 * Useful to search for all sites, for instance
	 * @param all
	 */
	public void addSearchForAll() {
		searchElement.addContent(new Element("all"));
		asText += "[all]";
	}
	
	public void addSearchConstraint(String name, String comparison, String value) {
		Element param = new CorinaElement("param");
		
		param.setAttribute("name", name);
		param.setAttribute("operator", comparison);
		param.setAttribute("value", value);
		
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
