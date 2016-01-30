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

import java.util.List;

import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.WSIEntity;
import org.tellervo.schema.WSIOdkFormDefinition;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.schema.WSITag;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoEntityAssociatedResource;
import org.tridas.interfaces.ICoreTridas;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasIdentifier;


/**
 * @author Lucas Madar
 * 
 */
public class WSIOdkFormDefinitionResource<T extends WSIOdkFormDefinition> extends
		TellervoEntityAssociatedResource<WSIOdkFormDefinition> {



	/**
	 * Create constructor
	 * 
	 * @param entity
	 *            The entity to create
	 * @param parentEntity
	 *            The parent entity
	 * @param entityType
	 *            The class of the entity to create
	 */
	public WSIOdkFormDefinitionResource(ICoreTridas entity, ICoreTridas parentEntity) {
		super(entity, parentEntity);

	}

	/**
	 * Update/delete constructor
	 * 
	 * @param entity
	 * @param queryType
	 * @param entityType
	 *            Class of the entity to update/delete
	 */
	public WSIOdkFormDefinitionResource(ICoreTridas entity, TellervoRequestType queryType) {
		super(entity, null, queryType);

	}

	/**
	 * Read/delete constructor
	 * 
	 * @param identifier
	 * @param entityType
	 * @param queryType
	 * @param entityClassType
	 *            Class of the entity
	 */
	public WSIOdkFormDefinitionResource(TridasIdentifier identifier, TellervoRequestType queryType) {
		super(identifier, EntityType.ODK_FORM_DEFINITION, queryType);

	}

	/**
	 * Constructor for read or delete
	 * 
	 * @param entity a tellervo WS entity to perform an operation on
	 * @param queryType one of read or delete
	 * @param entityClassType class of the entity to return
	 */
	public WSIOdkFormDefinitionResource(WSIEntity entity, TellervoRequestType queryType) {
		super(entity, queryType);
		
	}
	
	/**
	 * Constructor for update 
	 * 
	 * @param entity
	 * @param newParentEntityID
	 * @param entityType
	 */
	public WSIOdkFormDefinitionResource(ICoreTridas entity) {
		super(entity, null, TellervoRequestType.UPDATE);
	}

	
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		Class<T> entityType = (Class<T>) WSIOdkFormDefinition.class;

		// get the type we want
		List<T> values = ListUtil.subListOfType(object.getContent().getSqlsAndObjectsAndElements(), entityType);
		
		// set our value, maybe?
		setAssociatedResult((values.size() == 0) ? null : values.get(0));

		return true;
	}
	
	

}
