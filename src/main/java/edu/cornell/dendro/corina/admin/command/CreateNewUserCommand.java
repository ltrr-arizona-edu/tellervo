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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.admin.control.CreateNewUserEvent;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.util.StringUtils;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

public class CreateNewUserCommand implements ICommand {

        public void execute(MVCEvent argEvent) {

        	CreateNewUserEvent event = (CreateNewUserEvent) argEvent;
        	String password = new String(event.password);
        	WSISecurityUser user = event.user;
        	JDialog parent = event.parent;
        	
        	
	    	// Set password to hash
	    	MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
				digest.update(password.getBytes());
		    	user.setHashOfPassword(StringUtils.bytesToHex(digest.digest()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
					
			// associate a resource
	    	SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.CREATE, user);
	    
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(parent, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
			{
				rsrc.getAssociatedResult();
				parent.dispose();
			}
			
			JOptionPane.showMessageDialog(parent, "Error creating user.  Make sure the username is unique." + accdialog.getFailException().
					getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
}
