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
package org.tellervo.desktop.wsi.tellervo.resources;

import java.util.ArrayList;

import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIEntity;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;


public class WSIEntityResource extends TellervoAssociatedResource<WSIEntity> {

	WSIEntity entity = null;
	

	public WSIEntityResource(TellervoRequestType rType, WSIEntity entity) {
		
		
		super("wsientity", rType);
		this.entity = entity;
		
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(TellervoRequestFormat.SUMMARY);
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
