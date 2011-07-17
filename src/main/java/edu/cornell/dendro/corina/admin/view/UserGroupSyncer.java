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

package edu.cornell.dendro.corina.admin.view;

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

// for testing. run this to make the user's groups match the groups users

public class UserGroupSyncer extends JDialog {
	
	
	public UserGroupSyncer(){
		
		ArrayList<WSISecurityUser>lstofUsers = (ArrayList<WSISecurityUser>) Dictionary
			.getDictionaryAsArrayList("securityUserDictionary");
        for(WSISecurityUser user:lstofUsers){
        	for(WSISecurityGroup group:(ArrayList<WSISecurityGroup>)user.getMemberOf().getSecurityGroups()){
    			//TODO: check the group for null members property and create one if necessary
        		//group.getMembers().getSecurityUsers().add(user);
        	}
        }
        
        ArrayList<WSISecurityGroup> lstofGroups = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
        int counter = 0;
        for(WSISecurityGroup group:lstofGroups){
    		SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(CorinaRequestType.UPDATE, group);
    		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
    		rsrc.query();
    		//accdialog.setVisible(true);
    		if(accdialog.isSuccessful())
    		{
    			System.out.println(rsrc.getAssociatedResult());
    			counter++;
    		}
        }
        
        System.out.println(""+counter+" out of "+lstofGroups.size()+" groups synced.");	
		
	}
}
