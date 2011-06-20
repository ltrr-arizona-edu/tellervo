package edu.cornell.dendro.corina.dictionary;

import org.jdom.Element;

@SuppressWarnings("unchecked")
public class Taxon extends DictionaryElement implements Comparable {
	public Taxon(Element e) {
		super(DictionaryElement.Type.Standardized, e);
	}

	public int compareTo(Object o) {
		if(o instanceof Taxon)
			return this.getValue().compareToIgnoreCase(((Taxon)o).getValue());
			
		return 0;
	}

}
