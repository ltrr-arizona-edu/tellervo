/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina;

import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRequest;

/**
 * @author Lucas Madar
 *
 */
public abstract class CorinaEntityAssociatedResource<T> extends
		CorinaAssociatedResource<T> {
	private WSIEntity entity;
	
	/**
	 * @param resourceName
	 * @param entity
	 * @param queryType
	 */
	public CorinaEntityAssociatedResource(String resourceName,
			WSIEntity entity, CorinaRequestType queryType) {
		super(resourceName, queryType);
		
		this.entity = entity;
	}

	/**
	 * @param resourceName
	 * @param entity
	 * @param queryType
	 * @param badCredentialsBehavior
	 */
	public CorinaEntityAssociatedResource(String resourceName,
			WSIEntity entity, CorinaRequestType queryType,
			BadCredentialsBehavior badCredentialsBehavior) {
		super(resourceName, queryType, badCredentialsBehavior);
		this.entity = entity;
	}

	@Override
	protected final void populateRequest(WSIRequest request) {
		populateRequest(request, entity);
	}
	
	protected abstract void populateRequest(WSIRequest request, WSIEntity entity);
}
