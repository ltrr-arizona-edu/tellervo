/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.wsi.tellervo.resources;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;


public class SecurityGroupEntityResource extends TellervoAssociatedResource<WSISecurityGroup> {

	WSISecurityGroup group = null;
	
	/**
	 * Construct a search resource with the given search parameters
	 * @param searchParameters
	 */
	public SecurityGroupEntityResource(TellervoRequestType rType, WSISecurityGroup group) {
		super("group", rType);
		this.group = group;
		
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(TellervoRequestFormat.SUMMARY);
		ArrayList<WSISecurityGroup> groups = new ArrayList<WSISecurityGroup>();
		groups.add(group);
		request.setSecurityGroups(groups);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		// get a list of only the 'series' elements
		List<WSISecurityGroup> groupList = ListUtil.subListOfType(
				object.getContent().getSqlsAndProjectsAndObjects(), WSISecurityGroup.class);

		if (groupList.size()>0)
		{
			setAssociatedResult(groupList.get(0));
			return true;
		}
		
		return false;
	}
}
