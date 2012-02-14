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
package org.tellervo.desktop.admin.command;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tellervo.desktop.admin.control.CreateNewGroupEvent;
import org.tellervo.desktop.admin.model.UserGroupAdminModel;
import org.tellervo.desktop.schema.CorinaRequestType;
import org.tellervo.desktop.schema.WSISecurityGroup;
import org.tellervo.desktop.wsi.corina.CorinaResourceAccessDialog;
import org.tellervo.desktop.wsi.corina.resources.SecurityGroupEntityResource;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


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
				group.setId(rsrc.getAssociatedResult().getId());
				mainModel.addGroup(group);
				parent.dispose();
			}
			else{
				JOptionPane.showMessageDialog(parent, "Error creating group." + accdialog.getFailException().
						getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
        }
}
