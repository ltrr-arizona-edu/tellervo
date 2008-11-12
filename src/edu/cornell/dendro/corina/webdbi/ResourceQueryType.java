package edu.cornell.dendro.corina.webdbi;

/**
 * @author Lucas Madar
 *
 */

public enum ResourceQueryType {
	CREATE, READ, UPDATE, DELETE, SEARCH, PLAINLOGIN, SECURELOGIN, NONCE;
	
	// Return the verb in lower case
	public String getVerb() {
		return this.toString().toLowerCase();
	}
}

