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
package edu.cornell.dendro.corina.admin.command;

import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.DeleteUserEvent;
import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;
import edu.cornell.dendro.corina.schema.*;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.WSIEntityResource;


public class DeleteUserCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        	DeleteUserEvent event = (DeleteUserEvent) argEvent;
        	String usrid = event.usrid;
        	UserGroupAdminView view = event.model.getMainView();
        	
    		WSIEntity entity = new WSIEntity();
    		entity.setId(usrid);
    		entity.setType(EntityType.SECURITY_USER);
        			
    		// associate a resource
        	WSIEntityResource rsrc = new WSIEntityResource(CorinaRequestType.DELETE, entity);
        	
    		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(view, rsrc);
    		rsrc.query();
    		accdialog.setVisible(true);
    		
    		if(accdialog.isSuccessful())
    		{
    			rsrc.getAssociatedResult();
    			JOptionPane.showMessageDialog(view, "User deleted", "Success", JOptionPane.NO_OPTION);
    		}
    		else{
    			JOptionPane.showMessageDialog(view, "Unable to delete user as their details are referenced by data in the database.\n" +
    					"If the user is no longer active you can disable instead.", "Error", JOptionPane.ERROR_MESSAGE);
    		}
        }
}
