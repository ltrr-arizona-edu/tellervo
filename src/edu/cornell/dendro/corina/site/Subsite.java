package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class Subsite extends GenericIntermediateObject implements Comparable {
	public Subsite(String id, String name) {
		super(id, name);
		
		System.out.println("New subsite: " + id + " " + name);
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
		
		Subsite subsite = new Subsite(id, name);
		return subsite;
	}

}
