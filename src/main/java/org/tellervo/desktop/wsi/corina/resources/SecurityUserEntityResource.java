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
package org.tellervo.desktop.wsi.corina.resources;

import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.schema.CorinaRequestFormat;
import org.tellervo.desktop.schema.CorinaRequestType;
import org.tellervo.desktop.schema.WSIRequest;
import org.tellervo.desktop.schema.WSIRootElement;
import org.tellervo.desktop.schema.WSISecurityUser;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.corina.CorinaAssociatedResource;


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
