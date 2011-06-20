/**
 * 
 */
package edu.cornell.dendro.corina.util;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * An implementation of EqualsBuilder that treats empty strings ("") the same as null
 * 
 * @author Lucas Madar
 *
 */
public class EmptyStringEqualsBuilder extends EqualsBuilder {
	public EqualsBuilder append(String lhs, String rhs) {
		if(lhs != null && lhs.length() == 0)
			lhs = null;
		
		if(rhs != null && rhs.length() == 0)
			rhs = null;
		
		return super.append(lhs, rhs);
	}
}
