package edu.cornell.dendro.corina.admin.command;

import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.DeleteGroupEvent;
import edu.cornell.dendro.corina.admin.view.UserGroupAdminView;
import edu.cornell.dendro.corina.schema.*;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.WSIEntityResource;


public class DeleteGroupCommand implements ICommand {

        public void execute(MVCEvent argEvent) {
        	DeleteGroupEvent event = (DeleteGroupEvent) argEvent;
        	String groupid = event.groupid;
        	UserGroupAdminView view = event.model.getMainView();
        	
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
    			rsrc.getAssociatedResult();
    			JOptionPane.showMessageDialog(view, "Group deleted", "Success", JOptionPane.NO_OPTION);
    		}
    		
    		JOptionPane.showMessageDialog(view, "Unable to delete group as the details are referenced by data in the database.\n" +
    				"If the group is no longer active you can disable instead.", "Error", JOptionPane.ERROR_MESSAGE);
 
        }
}