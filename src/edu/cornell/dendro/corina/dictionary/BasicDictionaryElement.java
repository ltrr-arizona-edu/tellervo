package edu.cornell.dendro.corina.dictionary;

/*
 * A basic dictionary type simply contains two strings: an internal reprsentation (such as an ID)
 * and a value, which is generally displayed to the user.
 */

public abstract class BasicDictionaryElement {
	private String internalRepresentation;
	private String value;
	
	public BasicDictionaryElement(String internalRepresentation, String value) {
		//System.out.println("Element: " + internalRepresentation + " = " + value);
		this.internalRepresentation = internalRepresentation;
		this.value = value;
	}
	
	public String getValue() { return value; }
	@Override
	public String toString() { return value; }
	public String getInternalRepresentation() { return internalRepresentation; }
}
