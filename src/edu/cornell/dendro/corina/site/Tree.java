package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class Tree extends GenericIntermediateObject {
	public Tree(String id, String name) {
		super(id, name);
	}
	
	public static Tree xmlToTree(Element root) {
		String id, name;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Tree lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		if(name == null) {
			System.out.println("Tree lacking an name? " + root.toString());
			return null;			
		}
		
		Tree tree = new Tree(id, name);
		return tree;
	}
	
	public Element toXML() {
		return null;
	}
}
