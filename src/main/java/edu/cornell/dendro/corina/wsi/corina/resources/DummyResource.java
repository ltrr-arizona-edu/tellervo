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
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.io.IOException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;

public class DummyResource extends CorinaAssociatedResource<Object> {
	/**
	 * @param resourceName
	 * @param queryType
	 */
	public DummyResource(CorinaRequestType queryType) {
		super("dummy", queryType);
	}

	@Override
	protected void populateRequest(WSIRequest request) {
		WSIEntity entity = new WSIEntity();
		
		entity.setId("1101");
		entity.setType(EntityType.MEASUREMENT_SERIES);
		
		request.getEntities().add(entity);
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
	
		DummyResource d = new DummyResource(CorinaRequestType.READ);
		
		d.queryWait();
	}
}
