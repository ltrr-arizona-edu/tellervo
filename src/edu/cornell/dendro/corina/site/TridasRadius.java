package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class TridasRadius extends TridasEntityBase {
	public TridasRadius(String id, String name) {
		super(id, name);
	}
	
	public static TridasRadius xmlToRadius(Element root) {
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
		
		TridasRadius radius = new TridasRadius(id, name);
		
		radius.setResourceIdentifierFromElement(root);
		
		return radius;
	}
	
	public Element toXML() {
		Element root = new Element("radius");
		
		if(!isNew())
			root.setAttribute("id", getID());

		root.addContent(new Element("name").setText(title));	

		return root;
	}
}