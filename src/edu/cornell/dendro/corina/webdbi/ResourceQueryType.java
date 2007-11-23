/**
 * 
 */
package edu.cornell.dendro.corina.webdbi;

/**
 * @author Lucas Madar
 *
 */
public class ResourceQueryType {
	private int queryType;
	
	public ResourceQueryType(int queryType) {
		this.queryType = queryType;
	}
	
	public String getVerb() {
		switch(queryType) {
		case CREATE:
			return "create";
			
		case READ:
			return "read";
			
		case UPDATE:
			return "update";
			
		case DELETE:
			return "delete";
			
		case SECURELOGIN:
			return "securelogin";
		}
		
		throw new IllegalArgumentException("Invalid query type");
	}
	
	public static final int CREATE = 1;
	public static final int READ = 2;
	public static final int UPDATE = 3;
	public static final int DELETE = 4;
	public static final int SECURELOGIN = 5;
}
