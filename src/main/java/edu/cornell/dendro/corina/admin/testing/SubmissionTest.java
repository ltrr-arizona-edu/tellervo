package edu.cornell.dendro.corina.admin.testing;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityGroup.Members;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.schema.WSISecurityUser.MemberOf;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityGroupEntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

public class SubmissionTest extends JDialog{
	
	public SubmissionTest(){
	
	    ArrayList<WSISecurityGroup> groupList = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
	    ArrayList<WSISecurityUser> userList = (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary");  
	    
	    
	    //try to edit a user's groups and submit
	    
//	    WSISecurityUser user = userList.get(0);
//		WSISecurityUser.MemberOf m = new WSISecurityUser.MemberOf();
//		ArrayList<WSISecurityGroup> testGroups = new ArrayList<WSISecurityGroup>();
//		testGroups.add(groupList.get(0));
//		testGroups.add(groupList.get(2));
//		m.setSecurityGroups(testGroups);
//		user.setMemberOf(m);
//		
//		SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, user);
//		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(this, rsrc);
//		rsrc.query();
//		accdialog.setVisible(true);
//		if(accdialog.isSuccessful())
//		{
//			System.out.println(rsrc.getAssociatedResult());
//		}
//	    
		//try to edit a group's users and submit
		
		
		WSISecurityGroup group = groupList.get(0);
		WSISecurityGroup.Members m2 = new WSISecurityGroup.Members();
		ArrayList<WSISecurityUser> testUsers = new ArrayList<WSISecurityUser>();
		testUsers.add(userList.get(0));
		testUsers.add(userList.get(2));
		m2.setSecurityUsers(testUsers);
		group.setMembers(m2);
		
		SecurityGroupEntityResource rsrc2 = new SecurityGroupEntityResource(CorinaRequestType.UPDATE, group);
		CorinaResourceAccessDialog accdialog2 = new CorinaResourceAccessDialog(this, rsrc2);
		rsrc2.query();
		accdialog2.setVisible(true);
		if(accdialog2.isSuccessful())
		{
			System.out.println(rsrc2.getAssociatedResult());
		}
		
	}
		
}
