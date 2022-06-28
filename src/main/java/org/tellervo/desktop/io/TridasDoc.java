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
package org.tellervo.desktop.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.util.MultidimensionalCounter.Iterator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.tridasv2.TridasIdentifierMap;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TridasGenericFieldMap;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;


public class TridasDoc implements Filetype {	
	private final static Logger log = LoggerFactory.getLogger(TridasDoc.class);

	@Override
	public String toString() {
		return I18n.getText("format.tridas") + " (*"+ getDefaultExtension()+")";
	}
	
	public String getDefaultExtension() {
		return ".xml";
	}
	
	private void breakUpTridasLinks(TridasProject p) {
		// first, disassociate any children of child objects
		if(p.isSetObjects()) {
			
			
			for(TridasObject o : p.getObjects())
			{
				breakUpTridasLinks(o);
			}
		}
	}
	
	private void breakUpTridasLinks(TridasObject obj) {
		// first, disassociate any children of child objects
		if(obj.isSetObjects()) {
			for(TridasObject childObj : obj.getObjects())				
				breakUpTridasLinks(childObj);
			
			obj.unsetObjects();
		}
		
		// now, disassociate everything else
		for(TridasElement element : obj.getElements()) {
			for(TridasSample sample : element.getSamples()) {
				for(TridasRadius radius : sample.getRadiuses()) {
					//for(TridasMeasurementSeries series : radius.getMeasurementSeries()) {
						// do we need to do anything in here?
					//}
					radius.unsetMeasurementSeries();
				}
				sample.unsetRadiuses();
			}
			element.unsetSamples();
		}
		obj.unsetElements();
	}
	
	
	private void loadProjectMeasurementsIntoList(TridasProject p, 
			List<BaseSample> samples,
			TridasIdentifierMap<BaseSample> references,
			List<TridasObject> objectHierarchy) throws IOException {
		
		
		for(TridasObject o : p.getObjects())
		{
			loadObjectMeasurementsIntoList(o, samples, references, objectHierarchy);
		}
		
	}
	
	/**
	 * Load an object tree!
	 * 
	 * @param obj
	 * 		the base object from which to start loading
	 * @param samples
	 * 		a list of basesamples we've loaded
	 * @param references
	 * 		a map from identifier->sample for samples we've already loaded
	 * @param objectHierarchy
	 * 		the hierarchy of objects at this depth (starts as an empty list)
	 * @throws IOException
	 */
	private void loadObjectMeasurementsIntoList(TridasObject obj, 
			List<BaseSample> samples,
			TridasIdentifierMap<BaseSample> references,
			List<TridasObject> objectHierarchy) throws IOException {

		// add myself to the hierarchy
		objectHierarchy.add(obj);
		
		// create an array of the hierarchy so far (not modified by next recursive call)
		TridasObject[] objArray = objectHierarchy.toArray(new TridasObject[0]);
		
		// do child objects first
		for(TridasObject child : obj.getObjects())
			loadObjectMeasurementsIntoList(child, samples, references, objectHierarchy);
		
		// ok, now load some samples from this tree!
		for(TridasElement element : obj.getElements()) {
			for(TridasSample sample : element.getSamples()) {
				for(TridasRadius radius : sample.getRadiuses()) {
					for(ITridasSeries series : radius.getMeasurementSeries()) {
						BaseSample s = loadFromBaseSeries(series, references);
												
						s.setMeta(Metadata.OBJECT, obj);
						s.setMeta(Metadata.OBJECT_ARRAY, objArray);
						s.setMeta(Metadata.ELEMENT, element);
						s.setMeta(Metadata.SAMPLE, sample);
						s.setMeta(Metadata.RADIUS, radius);
						// series is set in loadFromBaseSeries()

						// (re-)populate lab code
						LabCode labcode = (LabCode) s.getMeta(Metadata.LABCODE);
						if(labcode == null)
							labcode = new LabCode();
						
						// object codes are more obnoxious
						labcode.clearSites();
						for(TridasObject object : objArray) {
							for(TridasGenericField f : object.getGenericFields()) {
								if(TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE.equals(f.getName())) {
									labcode.appendSiteCode(f.getValue());
								}
							}
							
							labcode.appendSiteTitle(object.getTitle());
						}
						
						// this stuff is easier
						labcode.setElementCode(element.getTitle());
						labcode.setSampleCode(sample.getTitle());
						labcode.setRadiusCode(radius.getTitle());
						labcode.setSeriesCode(series.getTitle());

						// populate new metadata
						s.setMeta(Metadata.LABCODE, labcode);
						s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));

						// add it to our list
						samples.add(s);
					}
				}
			}
		}
	}
	
	/**
	 * Returns an array of BaseSamples derived from this object
	 * 
	 * @param obj The base tridas object
	 * @param appendSamples the list to append new samples onto (must not be null)
	 * @param references a map from identifier->sample for each already loaded object
	 * @param disassociate if true, takes the object list given by 'obj' and breaks all
	 * 		parent->child links (to prevent one sample from holding on to another sample 
	 * 		in garbage collection, for instance)
	 * @return
	 */
	public List<BaseSample> loadFromObject(TridasObject obj, 
			List<BaseSample> appendSamples, 
			TridasIdentifierMap<BaseSample> references,
			boolean disassociate) throws IOException {
		
		loadObjectMeasurementsIntoList(obj, appendSamples, references, new ArrayList<TridasObject>());
		
		if(disassociate)
			breakUpTridasLinks(obj);
		
		return appendSamples;
	}
	
	public List<BaseSample> loadFromProject(TridasProject p, 
			List<BaseSample> appendSamples, 
			TridasIdentifierMap<BaseSample> references,
			boolean disassociate) throws IOException {
		
		loadProjectMeasurementsIntoList(p, appendSamples, references, new ArrayList<TridasObject>());
		
		if(disassociate)
			breakUpTridasLinks(p);
		
		return appendSamples;
	}
	
	/**
	 * @see loadFromObject(TridasObject, List, boolean)
	 * @param obj
	 * @param disassociate
	 * @return
	 */
	public List<BaseSample> loadFromObject(TridasObject obj, boolean disassociate) throws IOException {
		return loadFromObject(obj, new ArrayList<BaseSample>(), 
				new TridasIdentifierMap<BaseSample>(), disassociate);
	}

	/**
	 * "Finishes" a derived sample
	 * - Loads elements into a sample for a derived series
	 * - Creates labcodes where applicable
	 * @param s
	 * @param series
	 * @param references
	 */
	public void finishDerivedSample(Sample s, 
			TridasIdentifierMap<BaseSample> references) {
		
		if(!(s.getSeries() instanceof ITridasDerivedSeries))
			throw new IllegalArgumentException("loadReferences requires derived series!");
				
		// only works with a derived series
		ITridasDerivedSeries series = (ITridasDerivedSeries) s.getSeries();
		
		// the list of elements
		ElementList elements = new ElementList();
		
		// go through the linkseries and use the identifiers
		List<TridasIdentifier> identifiers = SeriesLinkUtil.getIdentifiers(series.getLinkSeries());
			
		for(TridasIdentifier identifier : identifiers) {
			BaseSample ref = references.get(identifier);
			
			if(ref != null) {
				// easy enough, found the reference
				elements.add(new CachedElement(ref));
			}
			else {
				log.error("Sample " + s + " references unknown element: [" + identifier.getDomain()+"] "+identifier.getValue());
				log.debug("References list has "+references.size()+" items in it");
				for (TridasIdentifier key : references.keySet()) {
					log.debug("   - ["+key.getDomain()+"] "+key.getValue());
				}
			}
		}
		
		s.setElements(elements);
		
		LabCode labcode = null;
		
		// if it's derived from one thing, try to get its lab code and change it for our purposes
		if(elements.size() == 1) {
			try {
				BaseSample ref = elements.get(0).loadBasic();
				if(ref.hasMeta(Metadata.LABCODE)) {
					labcode = new LabCode(ref.getMeta(Metadata.LABCODE, LabCode.class));
					labcode.setSeriesCode(series.getTitle());
				}
			} catch (IOException e) {
				// oh well
			}
		}
		
		// no lab code? make a sad one
		if(labcode == null) {
			labcode = new LabCode();
			labcode.setSeriesCode(series.getTitle());
		}
		
		s.setMeta(Metadata.LABCODE, labcode);
		s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
	}
	
	/**
	 * Given a derivedSeries or measurementSeries, load into a BaseSample 
	 * (or a sample, if the series has values)
	 * 
	 * @param series The series to generate this BaseSample/Sample from
	 * @param references A list of samples appearing before this in xml which this sample can reference in loading (can be null)
	 * @return a BaseSample or Sample encapsulating this series
	 * @throws IOException
	 */
	public BaseSample loadFromBaseSeries(ITridasSeries series, 
			TridasIdentifierMap<BaseSample> references) throws IOException {
		BaseSample s;
		
		// if it has values, it's a sample. Otherwise, it's a basesample.
		if(series.isSetValues())
			s = new Sample(series);
		else
			s = new BaseSample(series);

		// add to references
		references.put(s);
		
		// Start with a basic title
		s.setMeta(Metadata.TITLE, series.isSetTitle() 
				? series.getTitle() 
				: series.getIdentifier().toString());
		
		// set up SampleType
		if(series instanceof ITridasDerivedSeries) {
			ITridasDerivedSeries derived = (ITridasDerivedSeries) series;
			s.setSampleType(SampleType.fromString(derived.getType().getValue()));
		}
		else
			s.setSampleType(SampleType.DIRECT);
		
		// prep generic fields
		TridasGenericFieldMap genericFields = new TridasGenericFieldMap(series.getGenericFields());
		
		// easy metadata bits
		s.setMeta(Metadata.TRIDAS_IDENTIFIER, series.getIdentifier());
		s.setMeta(Metadata.CREATED_TIMESTAMP, series.getCreatedTimestamp().
				getValue().toGregorianCalendar().getTime());
		s.setMeta(Metadata.MODIFIED_TIMESTAMP, series.getLastModifiedTimestamp().
				getValue().toGregorianCalendar().getTime());
		
		// count of direct children
		s.setMeta(Metadata.CHILD_COUNT, genericFields.getInteger("tellervo.directChildCount", 0));
		
		// reconciled only works on Direct VMs
		if(s.getSampleType() == SampleType.DIRECT) {
			// set it to the value of reconciled, or false if it's not present
			if(genericFields.containsKey("tellervo.isReconciled"))
				s.setMeta(Metadata.RECONCILED, genericFields.getBoolean("tellervo.isReconciled"));
			else
				s.setMeta(Metadata.RECONCILED, Boolean.FALSE);
		}
		
		// translate start year
		Year firstYear;
		if(series.isSetInterpretation() && series.getInterpretation().isSetFirstYear()) {
			org.tridas.schema.Year tridasFirstYear = series.getInterpretation().getFirstYear();
			
			int y = tridasFirstYear.getValue().intValue();
			
			switch(tridasFirstYear.getSuffix()) {
			case AD:
				firstYear = new Year(y);
				break;
			case BC:
				firstYear = new Year(-y);
				break;
			case BP: // years before 1950
				firstYear = new Year(1950 - y);
				break;
			default:
				throw new InvalidDataException("Invalid year data: Suffix of unknown type.");
			}
		}
		else
			firstYear = new Year(); // default to standard year
		
		// this has values: it's a standard/comprehensive VM
		if(series.isSetValues()) {
			for(TridasValues valuesElement : series.getValues()) {
				TridasVariable variable = valuesElement.getVariable();

				// seek out tridas normal "ring width," and use the size of that to create our range
				if(variable.isSetNormalTridas()) {
					if(variable.getNormalTridas() == NormalTridasVariable.RING_WIDTH) {
						// compute our range!
						s.setRange(new Range(firstYear, valuesElement.getValues().size()));
						s.setMeta(Metadata.UNITS, valuesElement.getUnit());
					}
				}				
			}			
		}
		// no values: summary VM
		else {
			// make up our range...
			s.setRange(new Range(firstYear, genericFields.getInteger("tellervo.readingCount", 0)));
			
			// ok, build lab code...
			LabCode labcode = new LabCode();
			
			for(int objectIdx = 1;;objectIdx++) {
				String objectCode = genericFields.getString("tellervo.objectCode." + objectIdx);
				String objectTitle = genericFields.getString("tellervo.objectTitle." + objectIdx);
				
				// we're done here!
				if(objectCode == null || objectTitle == null)
					break;
				
				labcode.appendSiteCode(objectCode);
				labcode.appendSiteTitle(objectTitle);
			}
			
			// ok if these are null
			labcode.setElementCode(genericFields.getString("tellervo.elementTitle"));
			labcode.setRadiusCode(genericFields.getString("tellervo.radiusTitle"));
			labcode.setSampleCode(genericFields.getString("tellervo.sampleTitle"));
			labcode.setSeriesCode(series.getTitle());
			
			s.setMeta(Metadata.LABCODE, labcode);			
			s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
			
			// set up summary metadata
			if(genericFields.containsKey("tellervo.seriesCount") && s.getSampleType() == SampleType.SUM)
				s.setMeta(Metadata.SUMMARY_SUM_CONSTITUENT_COUNT, genericFields.getInteger("tellervo.seriesCount"));
			if(genericFields.containsKey("tellervo.summaryTaxonName"))
				s.setMeta(Metadata.SUMMARY_MUTUAL_TAXON, genericFields.getString("tellervo.summaryTaxonName"));
			if(genericFields.containsKey("tellervo.summaryTaxonCount"))
				s.setMeta(Metadata.SUMMARY_MUTUAL_TAXON_COUNT, genericFields.getInteger("tellervo.summaryTaxonCount"));
		
			// Version
			if(series instanceof ITridasDerivedSeries) {
				s.setMeta(Metadata.VERSION, ((ITridasDerivedSeries)series).getVersion());
			}
		}
		
		return s;
	}
	
	
	public void loadSeries(Sample s, Element root) throws IOException {
		
	}

	public Sample load(BufferedReader r) throws IOException,
			WrongFiletypeException {
		// quickly check: am I an XML document?
		quickVerify(r);
		
		Sample s = new Sample();
		Document doc;
		
		try {
			doc = new SAXBuilder().build(r);
		} catch (JDOMException jdome) {
			log.error(jdome.getLocalizedMessage());
			throw new WrongFiletypeException();
		}
		
		Element root = doc.getRootElement();
		
		// no root element??
		if(root == null)
			throw new WrongFiletypeException();
		
		if(root.getName().equals("measurementSeries") || root.getName().equals("derivedSeries")) {
			loadSeries(s, root);
		}
		else
			throw new WrongFiletypeException();

		return s;
	}

	public void save(Sample s, BufferedWriter w) throws IOException {
		

		
		

	}


	
	/**
	 * Quickly check to see if it's an XML document
	 * @param r
	 * @throws IOException
	 */
	private void quickVerify(BufferedReader r) throws IOException {
		r.mark(4096);

		String firstLine = r.readLine();
		if(firstLine == null || !firstLine.startsWith("<?xml"))
			throw new WrongFiletypeException();
		
		r.reset();
	}

	public Boolean isPackedFileCapable() {
		return true;
	}

	public String getDeficiencyDescription() {
		return null;
	}

	public Boolean isLossless() {
		return true;
	}


}
