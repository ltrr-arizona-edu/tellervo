/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.util.List;

import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasEntity;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
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
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;


/**
 * @author Lucas Madar
 *
 */
public class EntitySearchResource<T extends TridasEntity> extends
		CorinaAssociatedResource<List<T>> {
	
	private SearchParameters searchParameters;
	private Class<? extends TridasEntity> returnType;

	/**
	 * Search for all direct children of this entity
	 * Works only for: object, element, sample
	 * (children: element, sample, radius)
	 * @param parent
	 */
	public EntitySearchResource(TridasEntity parent) {
		super(getDirectChildSearchType(parent) + "Search", CorinaRequestType.SEARCH);
		
		searchParameters = new SearchParameters(getDirectChildSearchType(parent));
		searchParameters.addSearchConstraint(getIdNameForEntity(parent), SearchOperator.EQUALS, parent.getIdentifier().getValue());
		
		returnType = entityForSearchReturnObject(searchParameters.getReturnObject());
	}
	
	public EntitySearchResource(SearchParameters searchParameters) {
		super(searchParameters.getReturnObject().toString() + "Search", CorinaRequestType.SEARCH);
		
		this.searchParameters = searchParameters;
		this.returnType = entityForSearchReturnObject(searchParameters.getReturnObject());
	}
	
	/**
	 * Get the direct descendant type for a search
	 * 
	 * (eg, if parent is Object, we're searching for Elements)
	 * 
	 * @param parent
	 * @return
	 */
	private static SearchReturnObject getDirectChildSearchType(TridasEntity parent) {
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
	private static SearchParameterName getIdNameForEntity(TridasEntity parent) {
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
	private static Class<? extends TridasEntity> entityForSearchReturnObject(SearchReturnObject value) {
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
	
	public static void main(String args[]) {
		App.platform = new Platform();
		App.platform.init();
		App.prefs = new Prefs();
		App.prefs.init();
		
		TridasObject obj = new TridasObject();
		TridasIdentifier identifier = new TridasIdentifier();
		
		identifier.setValue("2753");
		obj.setIdentifier(identifier);
		
		EntitySearchResource<TridasElement> rsrc = new EntitySearchResource<TridasElement>(obj);
		rsrc.queryWait();
		
		for(TridasEntity entity : rsrc.getAssociatedResult())
			System.out.println(entity.toString());
		
		System.out.println("----");
	}
}
