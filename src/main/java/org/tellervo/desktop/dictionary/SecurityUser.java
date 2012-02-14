/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.dictionary;

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
