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
package edu.cornell.dendro.corina.wsi.corina.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.io.Metadata;
import edu.cornell.dendro.corina.io.TridasDoc;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.TridasIdentifierMap;
import edu.cornell.dendro.corina.tridasv2.support.VersionUtil;
import edu.cornell.dendro.corina.util.ListUtil;
import edu.cornell.dendro.corina.wsi.ResourceException;
import edu.cornell.dendro.corina.wsi.corina.CorinaEntityAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.WebInterfaceCode;
import edu.cornell.dendro.corina.wsi.corina.WebInterfaceException;

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
		List<WSIBox> boxes = ListUtil.subListOfType(content, WSIBox.class);
		List<TridasObject> tridasObjects = ListUtil.subListOfType(content, TridasObject.class);
		List<ITridasSeries> tridasSeries = ListUtil.subListOfType(content, ITridasSeries.class);
		List<BaseSample> samples = new ArrayList<BaseSample>();

		// create a map for references
		TridasIdentifierMap<BaseSample> refmap = new TridasIdentifierMap<BaseSample>();
		
		// create a map for boxes (box identifier->box)
		TridasIdentifierMap<WSIBox> boxmap = new TridasIdentifierMap<WSIBox>(boxes);
		
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
		
		// we might overwrite this a bunch, so keep it around...
		TridasIdentifier boxIdentifier = new TridasIdentifier();
		
		for(BaseSample s : samples) {
			ITridasSeries series = s.getSeries();
						
			// populate references if it's a derived sample
			if(s instanceof Sample && s.getSampleType().isDerived()) {
				doc.finishDerivedSample((Sample) s, refmap);
			}
			
			// if it has a sample, try to get a box
			if(s.hasMeta(Metadata.SAMPLE)) {
				TridasSample sample = s.getMeta(Metadata.SAMPLE, TridasSample.class);				
				TridasGenericField field = GenericFieldUtils.findField(sample, "corina.boxID");
				
				// if we have a boxID...
				if(field != null) {
					// make the boxIdentifier represent our boxID...
					boxIdentifier.setDomain(sample.getIdentifier().getDomain());
					boxIdentifier.setValue(field.getValue());
					
					WSIBox box = boxmap.get(boxIdentifier);
					
					// if we have a box, attach it to the Corina sample
					if(box != null)
						s.setMeta(Metadata.BOX, box);
				}
			}

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
	
	/* (non-Javadoc)
	 * @see edu.cornell.dendro.corina.wsi.corina.CorinaResource#preprocessQuery(edu.cornell.dendro.corina.schema.WSIRootElement)
	 * 
	 * We need to handle a 'version already exists' response
	 */
	@Override
	protected PreprocessResult preprocessQuery(WSIRootElement object)
			throws ResourceException, UserCancelledException {
		
		try {
			// just try what we normally do
			return super.preprocessQuery(object);
		} catch (WebInterfaceException wie) {
			
			// ok, if it's anything other than a dupe version, pretend we didn't intercept it
			if(wie.getMessageCode() != WebInterfaceCode.VERSION_ALREADY_EXISTS)
				throw wie;
			
			// if we're not in a CREATE or UPDATE, this doesn't make any sense. 
			if(!(getQueryType() == CorinaRequestType.CREATE || getQueryType() == CorinaRequestType.UPDATE))
				throw wie;
			
			ITridas entity = getCreateOrUpdateEntity();
			
			// doesn't make sense if it's not a derived series...
			if(!(entity instanceof ITridasDerivedSeries))
				throw wie;
			
			ITridasDerivedSeries derived = (ITridasDerivedSeries) entity;
			String oldVersion = derived.isSetVersion() ? derived.getVersion() : "<no version>";
			String version = VersionUtil.nextVersion(derived.getVersion());

			// prompt the user for input...
			Object ret = JOptionPane.showInputDialog(getOwnerWindow(),
					"The version '" + oldVersion + "' is already in use.\n" +
					"Accept the following suggested\n"+
					"version number or choose your own.",
					"Version already exists",
					JOptionPane.ERROR_MESSAGE,
					null,
					null,
					version);
			
			// on success...
			if(ret != null) {
				derived.setVersion(ret.toString());
				return PreprocessResult.TRY_AGAIN;
			}
			
			// on failure (the user cancelled)
			return PreprocessResult.FAILURE_CANCELLED;
		}
	}

}
