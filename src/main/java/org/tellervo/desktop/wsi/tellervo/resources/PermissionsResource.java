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
package org.tellervo.desktop.wsi.tellervo.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.PermissionsEntityType;
import org.tellervo.schema.WSIPermission;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.schema.WSISecurityGroup;
import org.tellervo.schema.WSISecurityUser;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;


/**
 * @author Lucas Madar
 * 
 */
public class PermissionsResource extends TellervoAssociatedResource<ArrayList<WSIPermission>> {

	ArrayList<WSIPermission> perms = new ArrayList<WSIPermission>();
	
	private final static Logger log = LoggerFactory.getLogger(PermissionsResource.class);

	/**
	 * Read constructor
	 * 
	 * @param pEntityType
	 * @param pEntityID
	 * @param groupOrUser
	 */
	public PermissionsResource(PermissionsEntityType pEntityType, String pEntityID, Object groupOrUser) {
		super(getXMLName(new WSIPermission()), TellervoRequestType.READ);

		addPermission(pEntityType, pEntityID, groupOrUser);

	}
	
	/**
	 * Read constructor
	 */
	public PermissionsResource()
	{
		super(getXMLName(new WSIPermission()), TellervoRequestType.READ);
	}
	
	/**
	 * Write constructor
	 * 
	 * @param permission
	 */
	public PermissionsResource(WSIPermission permission){
		super(getXMLName(new WSIPermission()), TellervoRequestType.CREATE);
		
		permission.setDecidedBy(null);		
		this.perms.add(permission);
	}
	

	public void addPermission(PermissionsEntityType pEntityType, String pEntityID, Object groupOrUser)
	{
		if(groupOrUser instanceof WSISecurityGroup || groupOrUser instanceof WSISecurityUser)
		{
			
		}
		else
		{
			log.error("PermissionsResource not passed a user or group");
			return;
		}
		
		// Kludge alert!!
		// Override default null entity ids with a fake 
		if(pEntityID == null && pEntityType.equals(PermissionsEntityType.DEFAULT))
		{
			pEntityID = "ae68d6d2-2294-11e1-9c20-4ffbb19115a7";
		}
		
		WSIPermission perm = new WSIPermission();
		
		WSIPermission.Entity entity = new WSIPermission.Entity();
		entity.setType(pEntityType);
		
		entity.setId(pEntityID);
		
		ArrayList<WSIPermission.Entity> entities = new ArrayList<WSIPermission.Entity>();
		entities.add(entity);
		
		perm.setEntities(entities);
		
		if(groupOrUser instanceof WSISecurityGroup)
		{
			ArrayList<Object> securityUsersAndSecurityGroups = new ArrayList<Object>();
			securityUsersAndSecurityGroups.add((WSISecurityGroup) groupOrUser);
			
			perm.setSecurityUsersAndSecurityGroups(securityUsersAndSecurityGroups);
			this.perms.add(perm);
		}
		
		else if(groupOrUser instanceof WSISecurityUser)
		{
			ArrayList<Object> securityUsersAndSecurityGroups = new ArrayList<Object>();
			securityUsersAndSecurityGroups.add((WSISecurityUser) groupOrUser);
			
			perm.setSecurityUsersAndSecurityGroups(securityUsersAndSecurityGroups);
			this.perms.add(perm);
		}
		

	}

	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(TellervoRequestFormat.SUMMARY);
		
		request.setPermissions(perms);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		

		if (object.getContent().getSqlsAndProjectsAndObjects().size()>0)
		{
			setAssociatedResult((ArrayList) object.getContent().getSqlsAndProjectsAndObjects());
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get the name value of the XmlRootElement annotation of this object or its superclasses
	 * @param o
	 * @return a string containing the name attribute
	 */
	private static String getXMLName(Object o) {
		Class<?> clazz = o.getClass();
		
		do {
			XmlRootElement e = clazz.getAnnotation(XmlRootElement.class);
			if(e != null && e.name().length() > 0) {
				return e.name();
			}
		} while((clazz = clazz.getSuperclass()) != null);
		
		throw new IllegalStateException("No XML name for class or superclasses?");
	}

}
