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
 *     Dan Girshovich
 ******************************************************************************/
package edu.cornell.dendro.corina.admin.command;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.CreateNewGroupEvent;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityGroupEntityResource;

public class CreateNewGroupCommand implements ICommand {
	
        public void execute(MVCEvent argEvent) {

        	CreateNewGroupEvent event = (CreateNewGroupEvent) argEvent;
        	WSISecurityGroup group = event.group;
        	JDialog parent = event.parent;
        	UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
        	
			// associate a resource
	    	SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(CorinaRequestType.CREATE, group);
	    
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(parent, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				mainModel.addGroup(group);
				parent.dispose();
			}
			else{
				JOptionPane.showMessageDialog(parent, "Error creating group." + accdialog.getFailException().
						getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
        }
}
