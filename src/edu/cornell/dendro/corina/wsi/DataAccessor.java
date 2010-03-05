package edu.cornell.dendro.corina.wsi;

import java.io.IOException;

/**
 * Defines a generic way for accessing a data resource
 * given a resource.
 * 
 * INTYPE: The resource we are receiving
 * OUTTYPE: The resource we are sending
 */

public interface DataAccessor<INTYPE, OUTTYPE> {
	
	/**
	 * Specify the object to use to access the object we will receive
	 * 
	 * @param reqObj
	 */
	public void setRequestObject(OUTTYPE reqObj);
	
	/**
	 * Acquire the input object. Must be called after setRequestObject!
	 * 
	 * @return The object we were trying to access
	 * @throws IOException
	 */
	public INTYPE query() throws IOException;
	
	/**
	 * Execute the query, discarding any result.
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException;
}
