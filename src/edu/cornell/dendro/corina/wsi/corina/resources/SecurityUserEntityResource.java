/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.util.ArrayList;
import java.util.List;

import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;

public class SecurityUserEntityResource extends CorinaAssociatedResource<WSISecurityUser> {

	WSISecurityUser usr = null;
	
	/**
	 * Construct a search resource with the given search parameters
	 * @param searchParameters
	 */
	public SecurityUserEntityResource(CorinaRequestType rType, WSISecurityUser usr) {
		super("user", rType);
		this.usr = usr;
		
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(CorinaRequestFormat.SUMMARY);
		ArrayList<WSISecurityUser> users = new ArrayList<WSISecurityUser>();
		users.add(usr);
		request.setSecurityUsers(users);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {

		
		// get a list of only the 'series' elements
		List<WSISecurityUser> usrList = ListUtil.subListOfType(
				object.getContent().getSqlsAndObjectsAndElements(), WSISecurityUser.class);

		if (usrList.size()>0)
		{
			setAssociatedResult(usrList.get(0));
			return true;
		}
		
		return false;
	}
}
