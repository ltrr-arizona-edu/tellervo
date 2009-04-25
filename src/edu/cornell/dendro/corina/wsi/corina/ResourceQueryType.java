package edu.cornell.dendro.corina.wsi.corina;

/**
 * @author Lucas Madar
 *
 */

public enum ResourceQueryType implements RequestVerb {
	CREATE, READ, UPDATE, DELETE, SEARCH, PLAINLOGIN, SECURELOGIN, NONCE;
	
	// Return the verb in lower case
	public String getVerb() {
		return this.toString().toLowerCase();
	}
}

