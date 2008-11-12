package edu.cornell.dendro.corina.dictionary;

public class Taxon extends BasicDictionaryElement implements Comparable {

	public Taxon(String internalRepresentation, String value) {
		super(internalRepresentation, value);
	}

	public int compareTo(Object o) {
		if(o instanceof Taxon)
			return this.getValue().compareToIgnoreCase(((Taxon)o).getValue());
			
		return 0;
	}

}
