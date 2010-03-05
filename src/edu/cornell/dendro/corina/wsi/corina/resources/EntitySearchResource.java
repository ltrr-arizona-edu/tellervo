/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.util.List;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;


/**
 * @author Lucas Madar
 *
 */
public class EntitySearchResource<T extends ITridas> extends
		CorinaAssociatedResource<List<T>> {
	
	private SearchParameters searchParameters;
	private Class<? extends ITridas> returnType;

	/**
	 * Search for all direct children of this entity
	 * Works only for: object, element, sample
	 * (children: element, sample, radius)
	 * @param parent
	 */
	public EntitySearchResource(ITridas parent) {
		super(getDirectChildSearchType(parent) + "Search", CorinaRequestType.SEARCH);
		
		searchParameters = new SearchParameters(getDirectChildSearchType(parent));
		searchParameters.addSearchConstraint(getIdNameForEntity(parent), SearchOperator.EQUALS, parent.getIdentifier().getValue());
		this.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
		
		returnType = entityForSearchReturnObject(searchParameters.getReturnObject());
	}
	
	/**
	 * Search for all direct children of this entity but just return minimal data
	 * Works only for: object, element, sample
	 * (children: element, sample, radius)
	 * @param parent
	 */
	public EntitySearchResource(ITridas parent, Boolean minimal) {
		super(getDirectChildSearchType(parent) + "Search", CorinaRequestType.SEARCH);
		
		searchParameters = new SearchParameters(getDirectChildSearchType(parent));
		searchParameters.addSearchConstraint(getIdNameForEntity(parent), SearchOperator.EQUALS, parent.getIdentifier().getValue());
		if(minimal) this.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
		
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
		super(searchParameters.getReturnObject().toString() + "Search", CorinaRequestType.SEARCH);
		
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
