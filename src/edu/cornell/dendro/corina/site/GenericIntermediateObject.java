package edu.cornell.dendro.corina.site;

/**
 * This class is for any number of generic intermediate objects that we might have
 * in between Subsite -> Measurement
 * 
 * Currently:
 *   Tree Specimen Radius
 *   
 * @author lucasm
 *
 */
public class GenericIntermediateObject {
	public GenericIntermediateObject(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** The object's internally represented id */
	private String id;
	/** The object's name */
	private String name;

	public String toString() {
		return name;
	}

	public String getID() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		// if it's a site, check ids.
		if(o instanceof GenericIntermediateObject)
			return (((GenericIntermediateObject)o).id.equals(this.id));
		
		return super.equals(o);
	}

	public int compareTo(Object o) {
		if(o instanceof GenericIntermediateObject)
			return ((GenericIntermediateObject)o).name.compareToIgnoreCase(this.name);
	
		// errr? yeah.
		return 0;
	}

}