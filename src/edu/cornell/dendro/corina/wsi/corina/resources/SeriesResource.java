/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tridas.schema.BaseSeries;
import org.tridas.schema.TridasEntity;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.formats.TridasDoc;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaEntityAssociatedResource;

/**
 * @author Lucas Madar
 *
 */
public class SeriesResource extends CorinaEntityAssociatedResource<Sample> {

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
	public SeriesResource(TridasEntity entity, String parentEntityID,
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
		List<BaseSeries> tridasSeries = ListUtil.subListOfType(content, BaseSeries.class);
		List<BaseSample> samples = new ArrayList<BaseSample>();
		
		// init our loader
		TridasDoc doc = new TridasDoc();
		
		try {
			// load the tridas object tree first
			for(TridasObject obj : tridasObjects) {
				doc.loadFromObject(obj, samples, true);
			}
			
			// now load any base series lying around (usually derived series here)
			for(BaseSeries series : tridasSeries) {
				BaseSample bs = doc.loadFromBaseSeries(series);
				samples.add(bs);
			}
			
		} catch(IOException ioe) {
			throw new ResourceException("Couldn't load series: " + ioe.toString());
		}
		
		for(BaseSample s : samples) {
			// create a loader and associate it with this sample
			CorinaWsiTridasElement loader = new CorinaWsiTridasElement(
					s.getMeta(Metadata.TRIDAS_IDENTIFIER, TridasIdentifier.class));
			
			s.setLoader(loader);
			
			// don't forget to populate!
			loader.preload(s);
		}
		
		this.setAssociatedResult((Sample) samples.get(samples.size() - 1));
		
		return true;
	}
}
