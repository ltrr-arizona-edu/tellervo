/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.util.Collections;
import java.util.List;

import org.tridas.schema.TridasObject;

import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceCacher;
import edu.cornell.dendro.corina.wsi.corina.ResourceQueryType;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.TridasGenericFieldMap;

/**
 * @author Lucas Madar
 *
 */
public class TridasObjectList extends CorinaResource {

	/**
	 * @param resourceName
	 * @param queryType
	 */
	public TridasObjectList() {
		super("tridas.objects", ResourceQueryType.READ);
		
		// load my cache and unload on a successful remote load
		new CorinaResourceCacher(this, true).load();
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.corina.CorinaResource#populateRequest(edu.cornell.dendro.corina.schema.WSIRequest)
	 */
	@Override
	protected void populateRequest(WSIRequest request) {
		SearchParameters params = new SearchParameters("object");
		
		params.setIncludeChildren(true);
		params.addSearchConstraint("parentObjectID", "is", "NULL");
		
		request.setSearchParams(params);
	}

	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.Resource#processQueryResult(java.lang.Object)
	 */
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		// get a list of only tridas objects
		List<TridasObject> objects = ListUtil.subListOfType(
				object.getContent().getSqlOrObjectOrElement(), TridasObject.class);
		
		for(TridasObject obj : objects) {
			TridasGenericFieldMap map = new TridasGenericFieldMap(obj.getGenericField());
			
			System.out.println(obj.toString());
		}
		
		return false;
	}

}
