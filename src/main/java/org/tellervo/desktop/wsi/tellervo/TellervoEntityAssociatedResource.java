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
package org.tellervo.desktop.wsi.tellervo;

import javax.xml.bind.annotation.XmlRootElement;

import org.tellervo.schema.EntityType;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSICuration;
import org.tellervo.schema.WSIEntity;
import org.tellervo.schema.WSILoan;
import org.tellervo.schema.WSIOdkFormDefinition;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSITag;
import org.tridas.interfaces.ICoreTridas;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;


/**
 * @author Lucas Madar
 *
 */
public abstract class TellervoEntityAssociatedResource<T> extends
		TellervoAssociatedResource<T> {
	
	private WSIEntity readDeleteOrMergeEntity;
	private ICoreTridas createOrUpdateEntity;
	private String parentEntityID;
	private String correctEntityIDForMerge;

	/**
	 * Constructor for create 
	 * 
	 * @param entity a tridas entity to perform an operation on
	 * @param parentEntity the parent object
	 * @param queryType one of create, update, or delete
	 */
	public TellervoEntityAssociatedResource(ICoreTridas entity, ICoreTridas parentEntity) {
		super(getXMLName(entity), TellervoRequestType.CREATE);
		
		// objects can have null parents
		if(entity == null || (parentEntity == null && !(entity instanceof TridasObject)))
			throw new NullPointerException("Entity may not be null");
		
		initializeForCUD(entity, 
				parentEntity == null ? null : parentEntity.getIdentifier().getValue(), 
				TellervoRequestType.CREATE);
	}
	
	/**
	 * Constructor for merging records
	 * 
	 * @param entity
	 * @param correctEntityID
	 */
	public TellervoEntityAssociatedResource(ICoreTridas entity, String correctEntityID, EntityType entityType)
	{
		super(getXMLName(entity), TellervoRequestType.MERGE);
		correctEntityIDForMerge = correctEntityID;
		WSIEntity e = new WSIEntity();

		e.setType(entityType);
		e.setId(entity.getIdentifier().getValue());

		this.readDeleteOrMergeEntity = e;
		
		
	}
	
	/**
	 * Constructor for create, update, or delete
	 * 
	 * @param entity a tridas entity to perform an operation on
	 * @param parentEntityID the parent object id, requred only for create, optional for update
	 * @param queryType one of create, update, or delete
	 */
	public TellervoEntityAssociatedResource(ICoreTridas entity, String parentEntityID, 
			TellervoRequestType queryType) {
		super(getXMLName(entity), queryType);
		
		if(entity == null)
			throw new NullPointerException("Entity may not be null");
		
		initializeForCUD(entity, parentEntityID, queryType);
	}
	
	private final void initializeForCUD(ICoreTridas entity, String parentEntityID, 
			TellervoRequestType queryType) {
		switch(queryType) {
		case CREATE:
		case UPDATE:
		case ASSIGN:
		case UNASSIGN:
			this.parentEntityID = parentEntityID;
			this.createOrUpdateEntity = entity;
			break;
			
		case DELETE: {
			readDeleteOrMergeEntity = new WSIEntity();

			if(entity instanceof WSITag)
			{
				if(((WSITag) entity).isSetId()==false)
					throw new IllegalArgumentException("Delete called with no identifier");

				readDeleteOrMergeEntity.setId(((WSITag) entity).getId());

			}
			else if(entity instanceof WSIOdkFormDefinition)
			{
				if(((WSIOdkFormDefinition) entity).isSetId()==false)
					throw new IllegalArgumentException("Delete called with no identifier");

				readDeleteOrMergeEntity.setId(((WSIOdkFormDefinition) entity).getId());

			}
			else
			{
				TridasIdentifier identifier = entity.getIdentifier();

				if(parentEntityID != null)
					throw new IllegalArgumentException("Delete called with parentObjectID!");
				
				if(identifier == null)
					throw new IllegalArgumentException("Delete called with a tridas entity with no identifier!");
				
				readDeleteOrMergeEntity.setId(identifier.getValue());
			}
			// cheap-ish: XmlRootElement (above) is the xml tag, which is also our entity type
			readDeleteOrMergeEntity.setType(EntityType.fromValue(getResourceName()));
			break;
		}
			
		default:
			throw new IllegalArgumentException("Invalid request type: must be one of CREATE, UPDATE or DELETE for this method");
		}
		
		// derived series, objects and box don't have a parent entity ID
		if (queryType == TellervoRequestType.CREATE && parentEntityID == null) {
			if (!(entity instanceof ITridasDerivedSeries 
					|| entity instanceof TridasObject 
				    || entity instanceof WSIBox 
				    || entity instanceof WSILoan 
				    || entity instanceof WSICuration 
				    || entity instanceof WSITag
				    || entity instanceof WSIOdkFormDefinition))
				throw new IllegalArgumentException("CREATE called with ParentObjectID == null!");
		}
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

	/**
	 * Constructor for read or delete
	 * 
	 * @param entity a tellervo WS entity to perform an operation on
	 * @param queryType one of read or delete
	 */
	public TellervoEntityAssociatedResource(WSIEntity entity, TellervoRequestType queryType) {
		super(entity.getClass().getAnnotation(XmlRootElement.class).name(), 
				queryType);

		if(entity == null)
			throw new NullPointerException("Entity may not be null");
		
		switch(queryType) {
		case READ:
		case DELETE:
			this.readDeleteOrMergeEntity = entity;
			break;
					
		default:
			throw new IllegalArgumentException("Invalid request type: must be one of READ or DELETE for this method");
		}		
	}

	/**
	 * Constructor for read or delete
	 * 
	 * @param identifier a tridas identifier
	 * @param entityType the type of entity
	 * @param queryType one of create, update, or delete
	 */
	public TellervoEntityAssociatedResource(TridasIdentifier identifier, 
			EntityType entityType, TellervoRequestType queryType) {
		super(entityType.value(), queryType);
				
		if(identifier == null)
			throw new NullPointerException("Identifier may not be null");
		
		switch(queryType) {
		case READ:
		case DELETE: {
			WSIEntity entity = new WSIEntity();

			entity.setType(entityType);
			entity.setId(identifier.getValue());

			this.readDeleteOrMergeEntity = entity;
			break;
		}
					
		default:
			throw new IllegalArgumentException("Invalid request type: must be one of READ or DELETE for this method");
		}		
	}

	
	private void populateAppropriateList(WSIRequest request) {
		if(createOrUpdateEntity instanceof TridasProject)
			request.getProjects().add((TridasProject) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasObject)
			request.getObjects().add((TridasObject) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasElement)
			request.getElements().add((TridasElement) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasSample)
			request.getSamples().add((TridasSample) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasRadius)
			request.getRadiuses().add((TridasRadius) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasMeasurementSeries)
			request.getMeasurementSeries().add((TridasMeasurementSeries) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasDerivedSeries)
			request.getDerivedSeries().add((TridasDerivedSeries) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof WSIBox)
			request.getBoxes().add((WSIBox) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof WSILoan)
			request.getLoen().add((WSILoan) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof WSICuration)
			request.getCurations().add((WSICuration) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof WSITag)
			request.getTags().add((WSITag) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof WSIOdkFormDefinition)
			request.getOdkFormDefinitions().add((WSIOdkFormDefinition) createOrUpdateEntity);
		else 
			throw new IllegalArgumentException("Unknown/invalid entity type for update/create: " + 
					createOrUpdateEntity.toString());
	}
	
	@Override
	protected final void populateRequest(WSIRequest request) {
		switch(this.getQueryType()) {
		case READ:
		case DELETE:
			request.getEntities().add(readDeleteOrMergeEntity);
			break;
			
		case MERGE:
			request.setMergeWithID(correctEntityIDForMerge);
			request.getEntities().add(readDeleteOrMergeEntity);
			break;
			
		case UPDATE:
		case CREATE:
		case ASSIGN:
		case UNASSIGN:
			request.setParentEntityID(this.parentEntityID);
			populateAppropriateList(request);
			break;
			
		
		}
	}
	
	/**
	 * Gets the entity we're submitting to the server
	 * for create or update.
	 * <p>
	 * Used by SeriesResource to handle version changes. 
	 * 
	 * @return the create or update entity, or null if it isn't present
	 */
	protected ICoreTridas getCreateOrUpdateEntity() {
		return createOrUpdateEntity;
	}
}
