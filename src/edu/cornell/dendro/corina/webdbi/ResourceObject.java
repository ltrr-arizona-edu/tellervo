package edu.cornell.dendro.corina.webdbi;

/*
 * A template class for a wrapper around an object
 * 
 * Extend:
 * public class Yeargh extends ResourceObject<Sample>
 */

public abstract class ResourceObject<OBJTYPE> extends Resource {
	private OBJTYPE object;
	private ResourceIdentifier identifier;
	private SearchParameters searchParams;
	
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
	
	public void attachIdentifier(ResourceIdentifier identifier) {
		// make a copy so we don't steal it
		this.identifier = (ResourceIdentifier) identifier;
	}
	
	public ResourceIdentifier getIdentifier() {
		return identifier;
	}
	
	public void setSearchParameters(SearchParameters params) {
		this.searchParams = params;
	}
	
	public SearchParameters getSearchParameters() {
		return searchParams;
	}
}
