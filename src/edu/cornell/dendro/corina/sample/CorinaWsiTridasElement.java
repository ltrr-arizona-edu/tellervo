package edu.cornell.dendro.corina.sample;

import java.io.IOException;
import java.util.List;

import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasLinkSeries;
import org.tridas.schema.TridasRadius;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.util.ListUtil;
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
 *  	identifier = NewTridasIdentifier
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

	/**
	 * Make a new CorinaWsiTridasElement and attach it to this sample
	 * 
	 * TODO: Make this work for different domains!
	 * 
	 * @param s
	 * @return the CorinaWsiTridasElement (same as s.getLoader())
	 */
	public static CorinaWsiTridasElement attachNewSample(Sample s) {
		TridasIdentifier newid = NewTridasIdentifier.getInstance("unknown");
		CorinaWsiTridasElement element = new CorinaWsiTridasElement(newid);
		
		s.setLoader(element);
		s.getSeries().setIdentifier(newid);
		
		return element;
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
		BaseSample newSample = bslist.get(bslist.size() - 1);
		
		// Copy its values over
		Sample.copy((Sample) newSample, s);
		preload(s);
		
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
			
			// do some basic error checking
			if(derived.isSetLinkSeries()) {
				if(derived.getLinkSeries().size() == 0)
					throw new IllegalArgumentException("Derived series specified with empty link series");
				
				int nSeries = countLinkSeries(derived);
				if(s.getSampleType() == SampleType.SUM) {
					if(nSeries < 2)
						throw new IllegalArgumentException("Sum must have more than 1 linked series");
				}
				else if(nSeries != 1) 
					throw new IllegalArgumentException("Series must be derived from exactly one other series");
			}
			else if(NewTridasIdentifier.isNew(seriesIdentifier))
				throw new IllegalArgumentException("Derived series created with no link series!");
			
			// set series to our derived copy
			series = derived;
		}
		
		// creating a new series?
		if(NewTridasIdentifier.isNew(seriesIdentifier)) {
			// ok, this is a new series
			
			if(!NewTridasIdentifier.isNew(identifier))
				throw new IllegalArgumentException("Creating a new series must have both identifiers as new");
			
			if(series instanceof ITridasDerivedSeries) {			
				// create a new derived series - parent is null
				return new SeriesResource(series, null, CorinaRequestType.CREATE);
			}
			
			// ok, series isn't derived; make sure it's got the right information in radius
			TridasRadius radius;
			if ((radius = s.getMeta(Metadata.RADIUS, TridasRadius.class)) == null
					|| !radius.isSetIdentifier()
					|| NewTridasIdentifier.isNew(radius.getIdentifier()))
				throw new IllegalArgumentException("Creating a new series without a radius that has an identifier?");
			
			return new SeriesResource(series, radius.getIdentifier().getValue(), CorinaRequestType.CREATE);
		}

		// we're just updating
		return new SeriesResource(series, null, CorinaRequestType.UPDATE);
	}
	
	private int countLinkSeries(ITridasDerivedSeries series) {
		List<TridasLinkSeries> links = series.getLinkSeries();
		int nlinks = 0;
		
		for(TridasLinkSeries link : links) {
			List<TridasIdentifier> identifiers = ListUtil.subListOfType(link.getIdRevesAndXLinksAndIdentifiers(), 
					TridasIdentifier.class);
			
			nlinks += identifiers.size();
		}
		
		return nlinks;
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
