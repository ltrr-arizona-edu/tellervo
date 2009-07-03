package edu.cornell.dendro.corina.sample;

import java.io.IOException;
import java.util.List;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesResource;

/**
 * An implementation of SampleLoader
 * 
 * Loads or saves a TRiDaS document from a Corina-style resource
 * 
 * Loading:
 * 	identifier = identifier of the series we want
 * 
 * Saving:
 *  if UPDATING
 *  	identifier = series.identifier = the identifier of the series we're changing
 *  
 *  if CREATING
 *  	identifier = parent identifier (or NewTridasIdentifier if sum)
 *  	series.identifier = NewTridasIdentifier
 * 
 * @author Lucas Madar
 *
 */

public class CorinaWsiTridasElement extends AbstractCorinaGUISampleLoader<SeriesResource> {
	private String shortName;
	private String name;
	private TridasIdentifier identifier;
	
	public CorinaWsiTridasElement(TridasIdentifier identifier) {
		this.identifier = identifier;
		
		name = shortName = identifier.toString();
	}
	
	public TridasIdentifier getTridasIdentifier() {
		return identifier;
	}
	
	@Override
	protected Sample doLoad(SeriesResource resource,
			CorinaResourceAccessDialog dialog) throws IOException {

		// start the query
		resource.query();
		dialog.setVisible(true);
		
		// ok, success?
		if(!dialog.isSuccessful()) {
			Exception e = dialog.getFailException();
			if(e instanceof IOException)
				throw (IOException) e;
			else
				throw new IOException("Error: " + e.toString());
		}

		Sample s = resource.getSample(getTridasIdentifier());
		
		// make sure we load our special info
		preload(s);
		
		return s;
	}

	@Override
	protected boolean doSave(Sample s, SeriesResource resource,
			CorinaResourceAccessDialog dialog) throws IOException {

		// start the query
		resource.query();
		dialog.setVisible(true);
		
		// ok, success?
		if(!dialog.isSuccessful()) {
			Exception e = dialog.getFailException();
			if(e instanceof IOException)
				throw (IOException) e;
			else
				throw new IOException("Error: " + e.toString());
		}
		
		List<BaseSample> bslist = resource.getAssociatedResult();

		if(bslist.size() == 0)
			throw new IllegalStateException("Save returned no samples?");
		
		// Get the last given series
		ITridasSeries series = bslist.get(bslist.size() - 1).getSeries();
		
		// assign the sample to represent this series
		s.setSeries(series);
		return true;
	}

	@Override
	protected SeriesResource getResource() {
		return new SeriesResource(identifier, EntityType.MEASUREMENT_SERIES, CorinaRequestType.READ);
	}

	@Override
	protected SeriesResource getResource(Sample s) {
		ITridasSeries series = s.getSeries();
		
		TridasIdentifier seriesIdentifier = s.getSeries().getIdentifier();
				
		if(seriesIdentifier == null)
			throw new NullPointerException("Series identifier must not be null for save; use new or existing");

		// if we're creating a derived series, we have to be careful here and copy it
		// this is because we don't want to send along any values!
		if(series instanceof ITridasDerivedSeries) {
			ITridasDerivedSeries derived = (ITridasDerivedSeries) series.createCopy();
			series.copyTo(derived);
			
			derived.unsetValues();
			
			series = derived;
		}
		
		if(NewTridasIdentifier.isNew(seriesIdentifier)) {
			// ok, this is a new series
			
			if(s.getSampleType() == SampleType.SUM) {
				if(!NewTridasIdentifier.isNew(identifier))
					throw new IllegalArgumentException("Creating a sum must have both identifiers as new");
				
				// create a new sum
				return new SeriesResource(series, null, CorinaRequestType.CREATE);
			}
			
			// create a new series with our given identifier as the parent
			return new SeriesResource(series, 
					identifier.getValue(), 
					CorinaRequestType.CREATE);
		}

		// we're just updating
		return new SeriesResource(series, null, CorinaRequestType.UPDATE);
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public void preload(BaseSample bs) {
		shortName = name = bs.getMetaString(Metadata.TITLE);
	}
}
