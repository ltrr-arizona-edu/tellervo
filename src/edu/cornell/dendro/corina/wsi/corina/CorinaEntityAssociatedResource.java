/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina;

import javax.xml.bind.annotation.XmlRootElement;

import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasEntity;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRequest;

/**
 * @author Lucas Madar
 *
 */
public abstract class CorinaEntityAssociatedResource<T> extends
		CorinaAssociatedResource<T> {
	
	private WSIEntity readOrDeleteEntity;
	private TridasEntity createOrUpdateEntity;
	private String parentEntityID;

	/**
	 * Constructor for create, update, or delete
	 * 
	 * @param entity a tridas entity to perform an operation on
	 * @param parentEntityID the parent object id, requred only for create, optional for update
	 * @param queryType one of create, update, or delete
	 */
	public CorinaEntityAssociatedResource(TridasEntity entity, String parentEntityID, 
			CorinaRequestType queryType) {
		super(entity.getClass().getAnnotation(XmlRootElement.class).name(), 
				queryType);
		
		if(entity == null)
			throw new NullPointerException("Entity may not be null");
		
		switch(queryType) {
		case CREATE:
		case UPDATE:
			this.parentEntityID = parentEntityID;
			this.createOrUpdateEntity = entity;
			break;
			
		case DELETE: {
			TridasIdentifier identifier = entity.getIdentifier();

			if(parentEntityID != null)
				throw new IllegalStateException("Delete called with parentObjectID!");
			
			if(identifier == null)
				throw new IllegalStateException("Delete called with a tridas entity with no identifier!");
			
			readOrDeleteEntity = new WSIEntity();
			readOrDeleteEntity.setId(identifier.getValue());
			// cheap-ish: XmlRootElement (above) is the xml tag, which is also our entity type
			readOrDeleteEntity.setType(EntityType.fromValue(getResourceName()));
			break;
		}
			
		default:
			throw new IllegalStateException("Invalid request type: must be one of CREATE, UPDATE or DELETE for this method");
		}
		
		if(queryType == CorinaRequestType.CREATE && parentEntityID == null) {
			throw new IllegalStateException("CREATE called with ParentObjectID == null!");
		}
	}

	/**
	 * Constructor for read or delete
	 * 
	 * @param entity a corina WS entity to perform an operation on
	 * @param queryType one of create, update, or delete
	 */
	public CorinaEntityAssociatedResource(WSIEntity entity, CorinaRequestType queryType) {
		super(entity.getClass().getAnnotation(XmlRootElement.class).name(), 
				queryType);

		if(entity == null)
			throw new NullPointerException("Entity may not be null");
		
		switch(queryType) {
		case READ:
		case DELETE:
			this.readOrDeleteEntity = entity;
			break;
					
		default:
			throw new IllegalStateException("Invalid request type: must be one of READ or DELETE for this method");
		}		
	}

	/**
	 * Constructor for read or delete
	 * 
	 * @param identifier a tridas identifier
	 * @param entityType the type of entity
	 * @param queryType one of create, update, or delete
	 */
	public CorinaEntityAssociatedResource(TridasIdentifier identifier, 
			EntityType entityType, CorinaRequestType queryType) {
		super(entityType.value(), queryType);
				
		if(identifier == null)
			throw new NullPointerException("Identifier may not be null");
		
		switch(queryType) {
		case READ:
		case DELETE: {
			WSIEntity entity = new WSIEntity();

			entity.setType(entityType);
			entity.setId(identifier.getValue());

			this.readOrDeleteEntity = entity;
			break;
		}
					
		default:
			throw new IllegalStateException("Invalid request type: must be one of READ or DELETE for this method");
		}		
	}

	
	private void populateAppropriateList(WSIRequest request) {
		if(createOrUpdateEntity instanceof TridasObject)
			request.getObject().add((TridasObject) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasElement)
			request.getElement().add((TridasElement) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasSample)
			request.getSample().add((TridasSample) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasRadius)
			request.getRadius().add((TridasRadius) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasMeasurementSeries)
			request.getMeasurementSeries().add((TridasMeasurementSeries) createOrUpdateEntity);
		else if(createOrUpdateEntity instanceof TridasDerivedSeries)
			request.getDerivedSeries().add((TridasDerivedSeries) createOrUpdateEntity);
		else 
			throw new IllegalStateException("Unknown/invalid entity type for update/create: " + 
					createOrUpdateEntity.toString());
	}
	
	@Override
	protected final void populateRequest(WSIRequest request) {
		switch(this.getQueryType()) {
		case READ:
		case DELETE:
			request.getEntity().add(readOrDeleteEntity);
			break;
			
		case UPDATE:
		case CREATE:
			request.setParentEntityID(this.parentEntityID);
			populateAppropriateList(request);
			break;
		}
	}
}
