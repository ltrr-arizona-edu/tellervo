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
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.PermissionsEntityType;
import edu.cornell.dendro.corina.schema.WSIPermission;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.schema.WSISecurityGroup;
import edu.cornell.dendro.corina.schema.WSISecurityUser;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;

/**
 * @author Lucas Madar
 * 
 */
public class PermissionsResource extends CorinaAssociatedResource<ArrayList<WSIPermission>> {

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
		super(getXMLName(new WSIPermission()), CorinaRequestType.READ);

		addPermission(pEntityType, pEntityID, groupOrUser);

	}
	
	/**
	 * Read constructor
	 */
	public PermissionsResource()
	{
		super(getXMLName(new WSIPermission()), CorinaRequestType.READ);
	}
	
	/**
	 * Write constructor
	 * 
	 * @param permission
	 */
	public PermissionsResource(WSIPermission permission){
		super(getXMLName(new WSIPermission()), CorinaRequestType.CREATE);
		
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
		ArrayList<Object> securityUsersAndSecurityGroups = new ArrayList<Object>();
		securityUsersAndSecurityGroups.add(groupOrUser);
		
		perm.setSecurityUsersAndSecurityGroups(securityUsersAndSecurityGroups);
		this.perms.add(perm);
	}

	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(CorinaRequestFormat.SUMMARY);
		
		request.setPermissions(perms);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		

		if (object.getContent().getSqlsAndObjectsAndElements().size()>0)
		{
			setAssociatedResult((ArrayList) object.getContent().getSqlsAndObjectsAndElements());
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
