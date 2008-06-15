package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class Specimen extends GenericIntermediateObject {
	public Specimen(String id, String name) {
		super(id, name);
	}
	
	public static Specimen xmlToSpecimen(Element root) {
		String id, name;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Specimen lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		if(name == null) {
			System.out.println("Specimen lacking an name? " + root.toString());
			return null;			
		}
		
		Specimen specimen = new Specimen(id, name);
		return specimen;
	}
}
