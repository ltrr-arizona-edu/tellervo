package edu.cornell.dendro.corina.webdbi;

/*
 * A template class for a wrapper around an object
 * 
 * Extend:
 * public class Yeargh extends ResourceObject<Sample>
 */

public abstract class ResourceObject<OBJTYPE> extends Resource {
	private OBJTYPE object;
	
	public OBJTYPE getObject() {
		return object;
	}
	
	public void setObject(OBJTYPE object) {
		this.object = object;
	}

	public ResourceObject(String resourceName) {
		super(resourceName);
		object = null;
	}

	public ResourceObject(String resourceName, ResourceQueryType queryType) {
		super(resourceName, queryType);
		object = null;
	}
	
	

}
