/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaEntityAssociatedResource;

/**
 * @author Lucas Madar
 *
 */
public class SeriesResource extends CorinaEntityAssociatedResource<Sample> {

	/**
	 * @param resourceName
	 * @param entity
	 * @param queryType
	 */
	public SeriesResource(String resourceName, WSIEntity entity,
			CorinaRequestType queryType) {
		super(resourceName, entity, queryType);
	}

	@Override
	protected void populateRequest(WSIRequest request, WSIEntity entity) {
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		return false;
	}
}
