package edu.cornell.dendro.corina.dictionary;

import org.jdom.Element;

public abstract class DictionaryElement {

	// what kind of dictionary element is this?
	public enum Type {
		Standardized,
		User
	}
	
	/** The id, given by the server */
	protected String id;
	/** The value of this dictionary element */
	protected String value;
	
	public DictionaryElement(Type type, Element e) {
		if(type == Type.Standardized) {
			id = e.getAttributeValue("normalId");
			value = e.getAttributeValue("normal");
		}
		else if(type == Type.User) {
			id = e.getAttributeValue("id");
			value = e.getAttributeValue("username");
		}
	}
	
	/**
	 * Do not use this constructor except in special cases (eg, when we can't find a user in the dictionary)
	 * @param id
	 * @param value
	 */
	public DictionaryElement(String id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
