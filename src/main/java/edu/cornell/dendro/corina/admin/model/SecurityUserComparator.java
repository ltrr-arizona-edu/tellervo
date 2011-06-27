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
package edu.cornell.dendro.corina.admin.model;

import java.util.Comparator;

import edu.cornell.dendro.corina.schema.WSISecurityUser;

/**
 * A simple comparator for sorting lists of SecurityUser objects 
 * 
 * @author Peter Brewer
 * @version $Id$
 */
public class SecurityUserComparator implements Comparator<WSISecurityUser> {
	
	
	public SecurityUserComparator() {
		
	}

	
	public int compare(WSISecurityUser o1, WSISecurityUser o2) {
		// TODO Auto-generated method stub
		
		
		o1.getLastName().compareTo(o2.getLastName());
		
		return 0;
	}

}
