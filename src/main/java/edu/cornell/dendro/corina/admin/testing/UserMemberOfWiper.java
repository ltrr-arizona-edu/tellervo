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
 *     Dan Girshovich
 ******************************************************************************/

package edu.cornell.dendro.corina.admin.testing;
import java.util.ArrayList;

import javax.swing.JDialog;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityGroup.Members;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityGroupEntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

// for testing. clears the memberOf property for all users

public class UserMemberOfWiper extends JDialog {
	
	
	public UserMemberOfWiper(){
		
    	int counter = 0;

		ArrayList<WSISecurityUser>lstofUsers = (ArrayList<WSISecurityUser>) Dictionary
			.getDictionaryAsArrayList("securityUserDictionary");
		
        for(WSISecurityUser user:lstofUsers){
        	
        	if(user.isSetMemberOf() && user.getMemberOf().isSetSecurityGroups()){
        		user.getMemberOf().unsetSecurityGroups();        	
	    		SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, user);
	    		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
	    		rsrc.query();
	    		accdialog.setVisible(true);
	    		
	    		if(accdialog.isSuccessful())
	    		{
	    			System.out.println(rsrc.getAssociatedResult());
	    			counter++;
	    		}
        	}else{
        		counter++;
        	}
        }
        
        System.out.println(""+counter+" out of "+lstofUsers.size()+" users wiped.");	
		
	}
}
