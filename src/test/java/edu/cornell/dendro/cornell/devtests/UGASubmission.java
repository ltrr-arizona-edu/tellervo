package edu.cornell.dendro.cornell.devtests;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import junit.framework.TestCase;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityGroup.Members;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.schema.WSISecurityUser.MemberOf;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityGroupEntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SecurityUserEntityResource;

public class UGASubmission extends TestCase{
	
	ArrayList<WSISecurityGroup> groupList = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
    ArrayList<WSISecurityUser> userList = (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary");  

	
	public UGASubmission(String name) {
		    super(name);
	}

	@Override
	protected void setUp() throws Exception {
		    super.setUp();
		    if (!App.isInitialized()) App.init(null, null);
		    groupList = (ArrayList<WSISecurityGroup>) Dictionary.getDictionaryAsArrayList("securityGroupDictionary");  
		    userList = (ArrayList<WSISecurityUser>) Dictionary.getDictionaryAsArrayList("securityUserDictionary");  

			
	}
		
	 public void testSecUser(){
	   

	    //try to edit a user's groups and submit
	    
	    WSISecurityUser user = userList.get(0);
		WSISecurityUser.MemberOf m = new WSISecurityUser.MemberOf();
		ArrayList<WSISecurityGroup> testGroups = new ArrayList<WSISecurityGroup>();
		testGroups.add(groupList.get(0));
		testGroups.add(groupList.get(2));
		m.setSecurityGroups(testGroups);
		user.setMemberOf(m);
		
		SecurityUserEntityResource rsrc = new SecurityUserEntityResource(CorinaRequestType.UPDATE, user);
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(new JDialog(), rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		assertTrue(accdialog.isSuccessful());
	 }
	 
	 public void testSecGroup(){
		//try to edit a group's users and submit
	
		WSISecurityGroup group = groupList.get(0);
		WSISecurityGroup.Members m = new WSISecurityGroup.Members();
		ArrayList<WSISecurityUser> testUsers = new ArrayList<WSISecurityUser>();
		testUsers.add(userList.get(0));
		testUsers.add(userList.get(2));
		m.setSecurityUsers(testUsers);
		group.setMembers(m);
		
		SecurityGroupEntityResource rsrc = new SecurityGroupEntityResource(CorinaRequestType.UPDATE, group);
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(new JDialog(), rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		assertTrue(accdialog.isSuccessful());
	}
		
}
