package edu.cornell.dendro.corina.tridas;

import org.jdom.Element;

public class TridasSample extends TridasEntityBase {
	public TridasSample(Element rootElement) {
		super(rootElement);
	}

	public TridasSample(String entityType, Element identifier, String title) {
		super(entityType, identifier, title);
	}

	public TridasSample(TridasIdentifier identifier, String title) {
		super(identifier, title);
	}

	@Override
	public Element toXML() {
		return null;
	}
}
