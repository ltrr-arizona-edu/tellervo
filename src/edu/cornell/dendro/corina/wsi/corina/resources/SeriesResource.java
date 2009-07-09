/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;

import edu.cornell.dendro.corina.formats.TridasDoc;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.tridasv2.TridasIdentifierMap;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaEntityAssociatedResource;

/**
 * @author Lucas Madar
 *
 */
public class SeriesResource extends CorinaEntityAssociatedResource<List<BaseSample>> {

	/**
	 * @param identifier
	 * @param entityType
	 * @param queryType
	 */
	public SeriesResource(TridasIdentifier identifier, EntityType entityType,
			CorinaRequestType queryType) {
		super(identifier, entityType, queryType);
	}

	/**
	 * @param entity
	 * @param parentEntityID
	 * @param queryType
	 */
	public SeriesResource(ITridasSeries entity, String parentEntityID,
			CorinaRequestType queryType) {
		super(entity, parentEntityID, queryType);
	}

	/**
	 * @param entity
	 * @param queryType
	 */
	public SeriesResource(WSIEntity entity, CorinaRequestType queryType) {
		super(entity, queryType);
	}

	@Override
	protected boolean processQueryResult(WSIRootElement object)
			throws ResourceException {
		List<Object> content = object.getContent().getSqlsAndObjectsAndElements();
		List<TridasObject> tridasObjects = ListUtil.subListOfType(content, TridasObject.class);
		List<ITridasSeries> tridasSeries = ListUtil.subListOfType(content, ITridasSeries.class);
		List<BaseSample> samples = new ArrayList<BaseSample>();

		// create a map for references
		TridasIdentifierMap<BaseSample> refmap = new TridasIdentifierMap<BaseSample>();
		
		// init our loader
		TridasDoc doc = new TridasDoc();
		
		try {
			// load the tridas object tree first
			for(TridasObject obj : tridasObjects) {
				doc.loadFromObject(obj, samples, refmap, true);
			}
			
			// now load any base series lying around (usually derived series here)
			for(ITridasSeries series : tridasSeries) {
				BaseSample bs = doc.loadFromBaseSeries(series, refmap);
				samples.add(bs);
			}
			
		} catch(IOException ioe) {
			throw new ResourceException("Couldn't load series: " + ioe.toString());
		}
		
		for(BaseSample s : samples) {
			ITridasSeries series = s.getSeries();
						
			// populate references if it's a derived sample
			if(s instanceof Sample && s.getSampleType().isDerived())
				doc.finishDerivedSample((Sample) s, refmap);

			// create a loader and associate it with this sample
			CorinaWsiTridasElement loader = new CorinaWsiTridasElement(series.getIdentifier());
			s.setLoader(loader);
			
			// don't forget to populate!
			loader.preload(s);
		}
		
		this.setAssociatedResult(samples);
		
		return true;
	}
	
	/**
	 * Find a sample in our results that matches this identifier
	 * 
	 * @param identifier
	 * @return a sample, or null
	 */
	public Sample getSample(TridasIdentifier identifier) {
		for(BaseSample bs : getAssociatedResult()) {
			if(bs.getSeries().getIdentifier().equals(identifier)) {
				if(!(bs instanceof Sample))
					throw new IllegalStateException("Found identifier but no sample??");
				
				return (Sample) bs;
			}
		}
		
		return null;
	}
}
