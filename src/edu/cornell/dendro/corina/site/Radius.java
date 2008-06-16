package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class Radius extends GenericIntermediateObject {
	public Radius(String id, String name) {
		super(id, name);
	}
	
	public static Radius xmlToRadius(Element root) {
		String id, name;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Radius lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		if(name == null) {
			System.out.println("Radius lacking an name? " + root.toString());
			return null;			
		}
		
		Radius radius = new Radius(id, name);
		return radius;
	}
	
	public Element toXML() {
		return null;
	}
}
