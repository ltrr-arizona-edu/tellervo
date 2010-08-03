/**
 * 
 */
package edu.cornell.dendro.corina.admin;

import java.util.Comparator;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

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
