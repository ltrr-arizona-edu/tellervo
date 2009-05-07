/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.util.ArrayList;
import java.util.List;

public class Property {
	public Property() {
		childProperties = new ArrayList<Property>();
		nChildProperties = 0;
		isList = required = false;
	}
	
	public String getNiceName() {
		StringBuffer sb = new StringBuffer();
		String[] nameTokens = name.split("\\.");
		String camelCaseName = nameTokens[nameTokens.length - 1];
		int len = camelCaseName.length();
		
		boolean didCapitalize = false;
		for(int i = 0; i < len; i++) {
			char c = camelCaseName.charAt(i);
			
			// skip attribute
			if(i == 0 && c == '@')
				continue;
			
			if(!didCapitalize && Character.isLowerCase(c)) {
				sb.append(Character.toUpperCase(c));
				didCapitalize = true;
				continue;
			}
			
			if(Character.isUpperCase(c)) {
				sb.append(' ');
				sb.append(Character.toLowerCase(c));	
				continue;
			}
			
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	public String name;
	public Class<?> clazz;
	public boolean isList;
	public boolean required;
	public int nChildProperties;
	public List<Property> childProperties;
}