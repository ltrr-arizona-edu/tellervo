package edu.cornell.dendro.corina.dictionary;

public class User extends BasicDictionaryElement {
	public User(String internalRepresentation, String value) {
		super(internalRepresentation, value);
	}
	
	private String username;
	private String fullname;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}
	
	/**
	 * @param firstname
	 * @param lastname
	 */
	public void setFullname(String firstname, String lastname) {
		if(firstname == null) {
			if(lastname == null)
				fullname = "[invalid user, id " + getInternalRepresentation() + "]";
			else
				fullname = lastname;
		}
		else if(lastname == null) {
			if(firstname == null)
				fullname = "[invalid user, id " + getInternalRepresentation() + "]";
			else
				fullname = firstname;
		}
		else 
			fullname = lastname + ", " + firstname;
	}
}
