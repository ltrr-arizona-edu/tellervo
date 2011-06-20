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
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;

public class SecurityGroupEntityResource extends CorinaAssociatedResource<WSISecurityGroup> {

	WSISecurityGroup group = null;
	
	/**
	 * Construct a search resource with the given search parameters
	 * @param searchParameters
	 */
	public SecurityGroupEntityResource(CorinaRequestType rType, WSISecurityGroup group) {
		super("group", rType);
		this.group = group;
		
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(CorinaRequestFormat.SUMMARY);
		ArrayList<WSISecurityGroup> groups = new ArrayList<WSISecurityGroup>();
		groups.add(group);
		request.setSecurityGroups(groups);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		// get a list of only the 'series' elements
		List<WSISecurityGroup> groupList = ListUtil.subListOfType(
				object.getContent().getSqlsAndObjectsAndElements(), WSISecurityGroup.class);

		if (groupList.size()>0)
		{
			setAssociatedResult(groupList.get(0));
			return true;
		}
		
		return false;
	}
}
