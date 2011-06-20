/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.util.ArrayList;

import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;

public class WSIEntityResource extends CorinaAssociatedResource<WSIEntity> {

	WSIEntity entity = null;
	

	public WSIEntityResource(CorinaRequestType rType, WSIEntity entity) {
		
		
		super("wsientity", rType);
		this.entity = entity;
		
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(CorinaRequestFormat.SUMMARY);
		ArrayList<WSIEntity> entities = new ArrayList<WSIEntity>();
		entities.add(entity);
		request.setEntities(entities);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		return true;
	}
}
