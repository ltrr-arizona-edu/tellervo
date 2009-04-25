package edu.cornell.dendro.corina.wsi.corina.resources;

import java.io.IOException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.ResourceQueryType;

public class DummyResource extends CorinaAssociatedResource<Object> {
	/**
	 * @param resourceName
	 * @param queryType
	 */
	public DummyResource(ResourceQueryType queryType) {
		super("dummy", queryType);
	}

	@Override
	protected void populateRequest(WSIRequest request) {
		WSIEntity entity = new WSIEntity();
		
		entity.setId(1101);
		entity.setType("measurementSeries");
		
		request.getEntity().add(entity);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		return false;
	}

	public static void main(String args[]) throws IOException {	
		App.platform = new Platform();
		App.platform.init();
		App.prefs = new Prefs();
		App.prefs.init();
	
		DummyResource d = new DummyResource(ResourceQueryType.READ);
		
		d.queryWait();
	}
}
