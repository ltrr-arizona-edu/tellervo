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

import java.util.List;

import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;



/**
 * @author Lucas Madar
 *
 */
public class EntitySearchResource<T extends ITridas> extends
		TellervoAssociatedResource<List<T>> {
	
	private SearchParameters searchParameters;
	private Class<? extends ITridas> returnType;

	/**
	 * Search for all direct children of this entity
	 * Works only for: object, element, sample
	 * (children: element, sample, radius)
	 * @param parent
	 */
	public EntitySearchResource(ITridas parent) {
		super(getDirectChildSearchType(parent) + "Search", TellervoRequestType.SEARCH);
		
		searchParameters = new SearchParameters(getDirectChildSearchType(parent));
		searchParameters.addSearchConstraint(getIdNameForEntity(parent), SearchOperator.EQUALS, parent.getIdentifier().getValue());
		this.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		returnType = entityForSearchReturnObject(searchParameters.getReturnObject());
	}
	
	/**
	 * Search for all direct children of this entity but just return minimal data
	 * Works only for: object, element, sample
	 * (children: element, sample, radius)
	 * @param parent
	 */
	public EntitySearchResource(ITridas parent, Boolean minimal) {
		super(getDirectChildSearchType(parent) + "Search", TellervoRequestType.SEARCH);
		
		searchParameters = new SearchParameters(getDirectChildSearchType(parent));
		searchParameters.addSearchConstraint(getIdNameForEntity(parent), SearchOperator.EQUALS, parent.getIdentifier().getValue());
		if(minimal) this.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		returnType = entityForSearchReturnObject(searchParameters.getReturnObject());
	}
	
	
	/**
	 * Search for an entity based on searchParameters, guessing the return type
	 * 
	 * Note: This is somewhat unsafe, as you can muck up return types.
	 * Better to use the two-param constructor.
	 * 
	 * @param searchParameters
	 */
	@SuppressWarnings("unchecked")
	public EntitySearchResource(SearchParameters searchParameters) {
		this(searchParameters, (Class<T>) entityForSearchReturnObject(searchParameters.getReturnObject()));
	}
	
	/**
	 * Search for an entity based on searchParameters, with the given return object class
	 * 
	 * @param searchParameters The search parameters
	 * @param returnObjectClass The base type of entity we're going to have, in a list
	 */
	public EntitySearchResource(SearchParameters searchParameters, Class<T> returnObjectClass) {
		super(searchParameters.getReturnObject().toString() + "Search", TellervoRequestType.SEARCH);
		
		this.searchParameters = searchParameters;
		this.returnType = returnObjectClass;		
	}
	
	/**
	 * Get the direct descendant type for a search
	 * 
	 * (eg, if parent is Object, we're searching for Elements)
	 * 
	 * @param parent
	 * @return
	 */
	private static SearchReturnObject getDirectChildSearchType(ITridas parent) {
		if(parent instanceof TridasObject)
			return SearchReturnObject.ELEMENT;
		if(parent instanceof TridasElement)
			return SearchReturnObject.SAMPLE;
		if(parent instanceof TridasSample)
			return SearchReturnObject.RADIUS;
		if(parent instanceof TridasRadius)
			return SearchReturnObject.MEASUREMENT_SERIES;
		
		throw new IllegalArgumentException("Unknown/invalid parent entity type for Entity Search: " + parent.getClass());
	}
	
	/**
	 * Get an ID name for a tridas entity
	 * @param parent
	 * @return
	 */
	private static SearchParameterName getIdNameForEntity(ITridas parent) {
		if(parent instanceof TridasObject)
			return SearchParameterName.OBJECTID;
		if(parent instanceof TridasElement)
			return SearchParameterName.ELEMENTID;
		if(parent instanceof TridasSample)
			return SearchParameterName.SAMPLEID;
		if(parent instanceof TridasRadius)
			return SearchParameterName.RADIUSID;
		throw new IllegalArgumentException("Unknown/invalid parent entity type for Entity Search: " + parent.getClass());		
	}
		
	/**
	 * Get the actual class for a SearchReturnObject
	 * @param value
	 * @return
	 */
	private static Class<? extends ITridas> entityForSearchReturnObject(SearchReturnObject value) {
		switch(value) {
		case OBJECT:
			return TridasObject.class;
		case ELEMENT:
			return TridasElement.class;
		case SAMPLE:
			return TridasSample.class;
		case RADIUS:
			return TridasRadius.class;
		case MEASUREMENT_SERIES:
			return TridasMeasurementSeries.class;
		case DERIVED_SERIES: 
			return TridasDerivedSeries.class;
			
		default:
			throw new IllegalArgumentException("Unimplemented search return object search");
		}
	}

	@Override
	protected void populateRequest(WSIRequest request) {
		request.setSearchParams(searchParameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		// make a list of all of the types we want
		setAssociatedResult((List<T>) ListUtil.subListOfType(object.getContent().getSqlsAndObjectsAndElements(), returnType));
		
		return true;
	}
	
}
