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
import org.tellervo.schema.WSIRootElement;
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
public class EntityResource<T extends ICoreTridas> extends
		TellervoEntityAssociatedResource<T> {

	/** The expected return type */
	private Class<T> entityType;

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
	public EntityResource(ICoreTridas entity, ICoreTridas parentEntity,
			Class<T> entityType) {
		super(entity, parentEntity);

		this.entityType = entityType;
	}

	/**
	 * Update/delete constructor
	 * 
	 * @param entity
	 * @param queryType
	 * @param entityType
	 *            Class of the entity to update/delete
	 */
	public EntityResource(ICoreTridas entity, TellervoRequestType queryType,
			Class<T> entityType) {
		super(entity, null, queryType);

		this.entityType = entityType;
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
	public EntityResource(TridasIdentifier identifier, EntityType entityType,
			TellervoRequestType queryType, Class<T> entityClassType) {
		super(identifier, entityType, queryType);

		this.entityType = entityClassType;
	}

	/**
	 * Constructor for read or delete
	 * 
	 * @param entity a tellervo WS entity to perform an operation on
	 * @param queryType one of read or delete
	 * @param entityClassType class of the entity to return
	 */
	public EntityResource(WSIEntity entity, TellervoRequestType queryType, Class<T> entityClassType) {
		super(entity, queryType);
		
		this.entityType = entityClassType;
	}
	
	/**
	 * Constructor for update 
	 * 
	 * @param entity
	 * @param newParentEntityID
	 * @param entityType
	 */
	public EntityResource(ICoreTridas entity, String newParentEntityID,
			Class<T> entityType) {
		super(entity, newParentEntityID, TellervoRequestType.UPDATE);
		this.entityType = entityType;
	}

	/**
	 * Constructor for merging records
	 * 
	 * @param entityType
	 * @param entity
	 * @param correctEntityID
	 */
	public EntityResource(EntityType entityType, Class<T> entityClassType, ITridas entity, String correctEntityID)
	{
		
		super(entity, correctEntityID, entityType);
		this.entityType = entityClassType;
	}
	
	
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {

		// get the type we want
		List<T> values = ListUtil.subListOfType(object.getContent()
				.getSqlsAndObjectsAndElements(), entityType);
		
		// set our value, maybe?
		setAssociatedResult((values.size() == 0) ? null : values.get(0));

		return true;
	}
	
	

}
