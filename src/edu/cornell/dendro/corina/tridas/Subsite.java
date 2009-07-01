package edu.cornell.dendro.corina.tridas;

import org.jdom.Element;

/** STOP HAVING SO MANY ERRORS! */

/* This class exists to shush the compiler */

// TODO: DELETE ME!


public class Subsite extends TridasObject {

	/**
	 * @param rootElement
	 */
	public Subsite(Element rootElement) {
		super(rootElement);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param entityType
	 * @param identifier
	 * @param title
	 */
	public Subsite(String entityType, Element identifier, String title) {
		super(entityType, identifier, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param identifier
	 * @param title
	 * @param labCode
	 */
	public Subsite(TridasIdentifier identifier, String title, String labCode) {
		super(identifier, title, labCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param identifier
	 * @param title
	 */
	public Subsite(TridasIdentifier identifier, String title) {
		super(identifier, title);
		// TODO Auto-generated constructor stub
	}

}
