package edu.cornell.dendro.corina.dictionary;

/*
 * A basic dictionary type simply contains two strings: an internal reprsentation (such as an ID)
 * and a value, which is generally displayed to the user.
 */

public abstract class BasicDictionaryType {
	private String internalRepresentation;
	private String value;
	
	public BasicDictionaryType(String internalRepresentation, String value) {
		this.internalRepresentation = internalRepresentation;
		this.value = value;
	}
	
	public String getValue() { return value; }
	public String getInternalRepresentation() { return internalRepresentation; }
}
