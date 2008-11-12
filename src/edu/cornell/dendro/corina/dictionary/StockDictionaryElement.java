package edu.cornell.dendro.corina.dictionary;

/*
 * This class is for elements that have standard values and non-standard values (user input)
 * They simply contain an additional boolean.
 */

public abstract class StockDictionaryElement extends BasicDictionaryElement {
	public StockDictionaryElement(String internalRepresentation, String value) {
		super(internalRepresentation, value);
		standard = true;
	}
	
	private boolean standard;
	
	public boolean isStandard() { return standard; }
	public void setStandard(boolean standard) { this.standard = standard; }
}
