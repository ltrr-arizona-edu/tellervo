package edu.cornell.dendro.corina.sample;

import java.io.IOException;

import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.tridasv2.TridasMeasurementSeriesEx;
import edu.cornell.dendro.corina.wsi.corina.CorinaAssociatedResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesResource;

/**
 * An implementation of SampleLoader
 * 
 * Loads a TRiDaS document from a Corina-style resource
 * 
 * @author Lucas Madar
 *
 */

public class CorinaWsiTridasElement extends AbstractCorinaGUISampleLoader {
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
	protected Sample doLoad(CorinaResource resource,
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
		
		CorinaAssociatedResource<Sample> rsrc = (CorinaAssociatedResource<Sample>)resource;
		
		// make sure we load our special info
		preload(rsrc.getAssociatedResult());
		
		return rsrc.getAssociatedResult();
	}

	@Override
	protected boolean doSave(Sample s, CorinaResource resource,
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
		else
			return true;
	}

	@Override
	protected CorinaResource getResource() {
		return new SeriesResource(identifier, EntityType.MEASUREMENT_SERIES, CorinaRequestType.READ);
	}

	@Override
	protected CorinaResource getResource(Sample s) {
		TridasMeasurementSeries series = new TridasMeasurementSeriesEx(s);
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
