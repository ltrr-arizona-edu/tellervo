/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.sample;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.SeriesLink;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasRadius;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;


/**
 * An implementation of SampleLoader
 * 
 * Loads or saves a TRiDaS document from a Tellervo-style resource
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

public class TellervoWSILoader extends AbstractTellervoGUIDeletableSampleLoader<SeriesResource> implements Cloneable {
	
	private final static Logger log = LoggerFactory.getLogger(TellervoWSILoader.class);

	private String shortName;
	private String name;
	private TridasIdentifier identifier;
	private SampleType type = SampleType.UNKNOWN;
	
	/**
	 * Create a new TellervoWsiTridasElement from this identifier
	 * @param identifier
	 */
	public TellervoWSILoader(TridasIdentifier identifier) {
		this.identifier = identifier;
		name = shortName = identifier.toString();
	}
	
	/**
	 * Create a copy of this TellervoWsiTridasElement
	 * @param src
	 */
	public TellervoWSILoader(TellervoWSILoader src) {
		this.identifier = (TridasIdentifier) src.identifier.clone();
		src.identifier.copyTo(this.identifier);
		
		this.name = src.name;
		this.shortName = src.shortName;
		this.type = src.type;
	}

	/**
	 * Make a new TellervoWsiTridasElement and attach it to this sample
	 * 
	 * TODO: Make this work for different domains!
	 * 
	 * @param s
	 * @return the TellervoWsiTridasElement (same as s.getLoader())
	 */
	public static TellervoWSILoader attachNewSample(Sample s) {
		TridasIdentifier newid = NewTridasIdentifier.getInstance("unknown");
		TellervoWSILoader element = new TellervoWSILoader(newid);
		
		s.setLoader(element);
		s.getSeries().setIdentifier(newid);
		
		return element;
	}
	
	public TridasIdentifier getTridasIdentifier() {
		return identifier;
	}
	
	@Override
	protected Sample doLoad(SeriesResource resource,
			TellervoResourceAccessDialog dialog) throws IOException {

		log.debug("Loading sample");
		
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
		
		log.debug("Queried server successfully");
		
		log.debug("Building a Sample with Tridas Identifier: "+getTridasIdentifier());

		Sample s = resource.getSample(getTridasIdentifier());

		if(s == null) {
			throw new IllegalStateException("doLoad() failed: sample not found");
		}
		
		// make sure we load our special info
		preload(s);
		
		return s;
	}

	@Override
	protected boolean doSave(Sample s, SeriesResource resource,
			TellervoResourceAccessDialog dialog) throws IOException {

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
	protected SeriesResource getResource(Map<String, ? extends Object> properties) {
		SeriesResource resource = new SeriesResource(identifier,
				EntityType.MEASUREMENT_SERIES, TellervoRequestType.READ);
				
		// set resource properties
		if(properties != null && !properties.isEmpty())
			resource.setProperties(properties);
		
		return resource;
	}

	@Override
	protected SeriesResource getResource(Sample s, Map<String, ? extends Object> properties) {
		SeriesResource resource = null;
		ITridasSeries series = s.getSeries();
		
		TridasIdentifier seriesIdentifier = s.getSeries().getIdentifier();
				
		if(seriesIdentifier == null)
		{
			log.warn("Expecting the identifier to not be null.  Proceeding is if saving a new series");
			seriesIdentifier = NewTridasIdentifier.getInstance(App.domain);
			s.getSeries().setIdentifier(seriesIdentifier);		
			Alert.message("Provisional Fix", "A workaround to a bug has just been applied.  Please check your series saved successfully and let the developers know if not!");
			
			//throw new NullPointerException("Series identifier must not be null for save; use new or existing");
		}

		// if we're creating a derived series, we have to be careful here and copy it
		// this is because we don't want to send along any values!
		if(series instanceof ITridasDerivedSeries) {
			// TODO Check this refactor is ok - PWB 2010-08-23
			ITridasDerivedSeries derived = (ITridasDerivedSeries) ((TridasDerivedSeries) series).clone();
			series.copyTo(derived);
			
			derived.unsetValues();
			
			// do some basic error checking
			if(derived.isSetLinkSeries()) {			
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

		try {
			// creating a new series?
			if (NewTridasIdentifier.isNew(seriesIdentifier)) {
				// ok, this is a new series

				if (!NewTridasIdentifier.isNew(identifier))
					throw new IllegalArgumentException(
							"Creating a new series must have both identifiers as new");

				if (series instanceof ITridasDerivedSeries) {
					// create a new derived series - parent is null
					resource = new SeriesResource(series, null,
							TellervoRequestType.CREATE);
					return resource;
				}

				// ok, series isn't derived; make sure it's got the right
				// information in radius
				TridasRadius radius;
				if ((radius = s.getMeta(Metadata.RADIUS, TridasRadius.class)) == null
						|| !radius.isSetIdentifier()
						|| NewTridasIdentifier.isNew(radius.getIdentifier()))
					throw new IllegalArgumentException(
							"Creating a new series without a radius that has an identifier?");

				resource = new SeriesResource(series, radius.getIdentifier()
						.getValue(), TellervoRequestType.CREATE);
				return resource;
			}

			// we're just updating
			resource = new SeriesResource(series, null, TellervoRequestType.UPDATE);
			return resource;
			
		} finally {
			// set save properties, if we're returning a resource
			if (resource != null && properties != null && !properties.isEmpty())
				resource.setProperties(properties); 
		}
	}
	
	private int countLinkSeries(ITridasDerivedSeries series) {
		int count = 0;

		// only allow identifiers here?
		// XXX: Is this really what we want?
		for(SeriesLink link : series.getLinkSeries().getSeries()) {
			if(link.isSetIdentifier())
				count++;
		}

		return count;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public void preload(BaseSample bs) {
		shortName = name = bs.getMetaString(Metadata.TITLE);
		type = bs.getSampleType();
	}

	@Override
	protected boolean doDelete(SeriesResource resource,
			TellervoResourceAccessDialog dialog) throws IOException {
		
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
		
		return true;
	}

	@Override
	protected SeriesResource getDeletionResource() {
		return new SeriesResource(identifier, EntityType.MEASUREMENT_SERIES, TellervoRequestType.DELETE);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof TellervoWSILoader) {
			TellervoWSILoader o2 = (TellervoWSILoader) o;

			return identifier.equals(o2.identifier);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
	@Override
	public Object clone() {
		return new TellervoWSILoader(this);
	}

	public SampleType getSampleType() {
		return type;
	}
}
