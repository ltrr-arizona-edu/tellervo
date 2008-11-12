package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class Subsite extends GenericIntermediateObject implements Comparable {
	public Subsite(String id, String name) {
		super(id, name);
	}
	
	public static Subsite xmlToSubsite(Element root) {
		String id, name;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Subsite lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		if(name == null) {
			System.out.println("Subsite lacking a name? " + root.toString());
			return null;			
		}
		
		// create the subsite
		Subsite subsite = new Subsite(id, name);
		
		// setup our links
		subsite.setResourceIdentifierFromElement(root);
		
		return subsite;
	}

	public Element toXML() {
		Element root = new Element("subSite");
		
		if(!isNew())
			root.setAttribute("id", getID());

		// all we have is a name...
		root.addContent(new Element("name").setText(name));
		
		return root;
	}

}
