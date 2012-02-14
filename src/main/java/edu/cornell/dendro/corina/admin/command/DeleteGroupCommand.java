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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.DeleteGroupEvent;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;
import edu.cornell.dendro.corina.schema.*;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.WSIEntityResource;


public class DeleteGroupCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(DeleteGroupCommand.class);

        public void execute(MVCEvent argEvent) {
        	DeleteGroupEvent event = (DeleteGroupEvent) argEvent;
        	String groupid = event.groupid;
        	UserGroupAdminModel mainModel = UserGroupAdminModel.getInstance();
        	UserGroupAdminView view = mainModel.getMainView();
        	
    		WSIEntity entity = new WSIEntity();
    		entity.setId(groupid);
    		entity.setType(EntityType.SECURITY_GROUP);
        			
    		// associate a resource
        	WSIEntityResource rsrc = new WSIEntityResource(CorinaRequestType.DELETE, entity);
        	
    		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(view, rsrc);
    		rsrc.query();
    		accdialog.setVisible(true);
    		
    		if(accdialog.isSuccessful())
    		{
    			try{
    			JOptionPane.showMessageDialog(view, "Group deleted", "Success", JOptionPane.NO_OPTION);
    			mainModel.removeGroupById(groupid);
    			} catch (Exception e)
    			{
    				log.error(e.getLocalizedMessage());
    			}
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(view, "Unable to delete group as the details are referenced by data in the database.\n" +
    				"If the group is no longer active you can disable instead.", "Error", JOptionPane.ERROR_MESSAGE);
    		}
 
        }
}
