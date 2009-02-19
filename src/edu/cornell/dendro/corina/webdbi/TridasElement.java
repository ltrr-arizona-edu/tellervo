package edu.cornell.dendro.corina.webdbi;

import org.jdom.Element;
import org.jdom.Namespace;

public class TridasElement extends Element {
	public TridasElement(String name) {
		super(name, CorinaXML.TRIDAS_NS);
	}
}
