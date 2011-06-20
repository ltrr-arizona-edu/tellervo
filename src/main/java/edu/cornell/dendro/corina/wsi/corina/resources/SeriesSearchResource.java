/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.io.IOException;
import java.util.List;

import org.tridas.interfaces.ITridasSeries;

import edu.cornell.dendro.corina.io.TridasDoc;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.tridasv2.TridasIdentifierMap;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;

/**
 * @author Lucas Madar
 *
 */
public class SeriesSearchResource extends CorinaAssociatedResource<ElementList> {
	/** The associated search parameters */
	private final SearchParameters params;
	
	/**
	 * Construct a search resource with the given search parameters
	 * @param searchParameters
	 */
	public SeriesSearchResource(SearchParameters searchParameters) {
		super("seriesSearch", CorinaRequestType.SEARCH);
		
		this.params = searchParameters;
	}

	/**
	 * @return the search parameters we are operating on
	 */
	public SearchParameters getSearchParameters() {
		return params;
	}
	
	@Override
	protected void populateRequest(WSIRequest request) {
		request.setFormat(CorinaRequestFormat.SUMMARY);
		request.setSearchParams(params);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		
		// get a list of only the 'series' elements
		List<ITridasSeries> seriesList = ListUtil.subListOfType(
				object.getContent().getSqlsAndObjectsAndElements(), ITridasSeries.class);

		// a list of our elements
		ElementList elements = new ElementList(seriesList.size());
		TridasDoc reader = new TridasDoc();
		TridasIdentifierMap<BaseSample> refmap = new TridasIdentifierMap<BaseSample>();
		
		for(ITridasSeries series : seriesList) {
			BaseSample sample;
			
			try {
				sample = reader.loadFromBaseSeries(series, refmap);
			} catch (IOException e) {
				System.err.println("Couldn't loadFromBaseSeries: " + e.toString());
				continue;
			}
			
			// create an loader and associate it with this sample
			CorinaWsiTridasElement loader = new CorinaWsiTridasElement(series.getIdentifier());
			sample.setLoader(loader);
			
			// create an associated element and add it to our list
			CachedElement cachedElement = new CachedElement(sample);
			elements.add(cachedElement);
		}
		
		// associate our result..
		setAssociatedResult(elements);
		
		return true;
	}
}
