package edu.cornell.dendro.corina.tridas;

import org.jdom.Element;

public class TridasRadius extends TridasEntityBase {
	/**
	 * @param rootElement
	 */
	public TridasRadius(Element rootElement) {
		super(rootElement);
	}

	/**
	 * @param identifier
	 * @param title
	 */
	public TridasRadius(TridasIdentifier identifier, String title) {
		super(identifier, title);
	}
	
	public static TridasRadius xmlToRadius(Element root) {
		TridasRadius radius = new TridasRadius(root);
		
		return radius;
	}
	
	public Element toXML() {
		Element root = new Element("radius");
		
		root.addContent(new Element("name").setText(title));	

		return root;
	}
}