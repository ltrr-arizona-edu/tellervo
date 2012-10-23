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

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.TridasDoc;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.desktop.tridasv2.TridasIdentifierMap;
import org.tellervo.desktop.util.ListUtil;
import org.tellervo.desktop.wsi.ResourceException;
import org.tellervo.desktop.wsi.tellervo.TellervoAssociatedResource;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tridas.interfaces.ITridasSeries;


/**
 * @author Lucas Madar
 *
 */
public class SeriesSearchResource extends TellervoAssociatedResource<ElementList> {
	
	private final static Logger log = LoggerFactory.getLogger(SeriesSearchResource.class);
	/** The associated search parameters */
	private final SearchParameters params;
	
	/**
	 * Construct a search resource with the given search parameters
	 * @param searchParameters
	 */
	public SeriesSearchResource(SearchParameters searchParameters) {
		super("seriesSearch", TellervoRequestType.SEARCH);
		
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
		request.setFormat(TellervoRequestFormat.SUMMARY);
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
				log.error("Couldn't loadFromBaseSeries: " + e.toString());
				continue;
			}
			
			// create an loader and associate it with this sample
			TellervoWsiTridasElement loader = new TellervoWsiTridasElement(series.getIdentifier());
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
