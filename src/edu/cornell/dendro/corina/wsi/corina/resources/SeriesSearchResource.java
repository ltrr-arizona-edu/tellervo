/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.ResourceQueryType;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;

/**
 * @author Lucas Madar
 *
 */
public class SeriesSearchResource extends CorinaAssociatedResource<ElementList> {
	/** The associated search parameters */
	private SearchParameters params;
	
	/**
	 * Construct a search resource with the given search parameters
	 * @param searchParameters
	 */
	public SeriesSearchResource(SearchParameters searchParameters) {
		super("seriesSearch", ResourceQueryType.SEARCH);
		
		this.params = searchParameters;
	}

	@Override
	protected void populateRequest(WSIRequest request) {
		request.setSearchParams(params);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		return false;
	}
}
