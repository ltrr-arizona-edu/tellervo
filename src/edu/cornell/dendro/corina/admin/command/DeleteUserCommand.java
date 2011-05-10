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
    		
    		JOptionPane.showMessageDialog(view, "Unable to delete user as their details are referenced by data in the database.\n" +
    				"If the user is no longer active you can disable instead.", "Error", JOptionPane.ERROR_MESSAGE);
 
        }
}