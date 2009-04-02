package edu.cornell.dendro.corina.dictionary;

import org.jdom.Element;

public class User extends DictionaryElement {
	public User(Element e) {
		super(DictionaryElement.Type.User, e);
		
		fullname = e.getAttributeValue("lastName") + ", " + e.getAttributeValue("firstName");
	}
	
	/**
	 * Only for creating a user when we don't have one in the dictionary
	 * @param name
	 */
	public User(String name) {
		super("invalid", name);
		fullname = name;
	}
	
	private String fullname;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return getValue();
	}
		
	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}
		
	public String toString() {
		return fullname;
	}
}
