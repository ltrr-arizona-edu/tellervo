package edu.cornell.dendro.corina.wsi.corina;

import edu.cornell.dendro.corina.schema.CorinaRequestType;


/**
 * A resource that wraps a "native" corina type
 * 
 * @author Lucas Madar
 */
public abstract class CorinaAssociatedResource<T> extends CorinaResource {

	/**
	 * @param resourceName
	 * @param queryType
	 */
	public CorinaAssociatedResource(String resourceName,
			CorinaRequestType queryType) {
		super(resourceName, queryType);
	}

	/**
	 * @param resourceName
	 * @param queryType
	 * @param badCredentialsBehavior
	 */
	public CorinaAssociatedResource(String resourceName,
			CorinaRequestType queryType,
			BadCredentialsBehavior badCredentialsBehavior) {
		super(resourceName, queryType, badCredentialsBehavior);
	}
	
	/** The associated result from querying this object */
	private T associatedResult;

	/**
	 * Set the type associated with this result
	 * @param associatedResult
	 */
	protected void setAssociatedResult(T associatedResult) {
		this.associatedResult = associatedResult;
	}
	
	/**
	 * Get the type associated with this result
	 * @return
	 */
	public T getAssociatedResult() {
		if(associatedResult == null)
			throw new IllegalStateException("getAssociatedResult() has a null result");
		
		return associatedResult;
	}
}
