package edu.cornell.dendro.corina.dictionary;

import org.jdom.Element;

public class SecurityUser extends DictionaryElement {
	public SecurityUser(Element e) {
		super(DictionaryElement.Type.SECURITYUSER, e);
		
		fullname = e.getAttributeValue("lastName") + ", " + e.getAttributeValue("firstName");
		firstname = e.getAttributeValue("firstName");
		lastname = e.getAttributeValue("lastName");
		
		if (e.getAttributeValue("enabled").compareTo("true")==1){
			enabled = true;
		}
		else{
			enabled = false;
		}
		
	}

	private String fullname;
    private String firstname;
    private String lastname;
    private Boolean enabled; 
    
	
	/**
	 * Only for creating a user when we don't have one in the dictionary
	 * @param name
	 */
	public SecurityUser(String name) {
		super("invalid", name);
		fullname = name;
	}
	
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return getValue();
	}
		
	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}
	
	public String getFirstname(){
		return firstname;
	}
	
	public String getLastname(){
		return lastname;
	}
	
	public Boolean isEnabled(){
		return enabled;
	}		
		
	public String toString() {
		return fullname;
	}
}
