package edu.cornell.dendro.corina.formats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridas.LabCode;
import edu.cornell.dendro.corina.tridas.LabCodeFormatter;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.webdbi.CorinaXML;
import edu.cornell.dendro.corina.wsi.corina.TridasGenericFieldMap;

public class TridasDoc implements Filetype {	
	/**
	 * Load the data in the 'data' list into wjinc and wjdec
	 * 
	 * @param data
	 * @param wjinc
	 * @param wjdec
	 * @throws IOException
	 */
	private void loadWeiserjahre(List<TridasValue> data, List<Integer> wjinc, List<Integer> wjdec) throws IOException {
		for(TridasValue v : data) {
			String strvalue = v.getValue();
			int slashPos;
		
			if(strvalue == null || (slashPos = strvalue.indexOf('/')) < 1)
				throw new InvalidDataException("Invalid Weiserjahre Data Format: " + v.toString());
			
			try {
				Integer inc = Integer.valueOf(strvalue.substring(0, slashPos));
				Integer dec = Integer.valueOf(strvalue.substring(slashPos + 1));
			} catch (NumberFormatException nfpe) {
				throw new InvalidDataException("Invalid Weiserjahre data: " + v.toString());
			}
		}
				
	}
	
	/**
	 * Load the data in the 'data' list as Integers in the values<object> list, and
	 * the counts in the counts list. Unsets the counts if it's all one.
	 * @param data
	 * @param clearCountAllOnes if true, unsets count if it's all ones (should only be true if it's not a sum)
	 * @param values
	 * @param counts
	 * @throws IOException
	 */
	private void loadIntegerObjectAndCountList(List<TridasValue> data, boolean clearCountAllOnes,
			List<Integer> values, List<Integer> counts) 
		throws IOException 
	{
		boolean countAllOnes = true;
		
		// loop through and simply get the data
		for(TridasValue v : data) {
			String strval = v.getValue();
			Integer countval = v.isSetCount() ? v.getCount().intValue() : 1;
			Integer ival;

			try {
				ival = Integer.valueOf(strval);
			} catch (NumberFormatException nfe) {
				throw new InvalidDataException("Invalid ring width data: " + strval + " : " + v.toString());
			}
			
			values.add(ival);
			counts.add(countval);

			// the count isn't just all ones
			if(countval != 1)
				countAllOnes = false;
		}
		
		// all ones? clear!
		if(countAllOnes && clearCountAllOnes)
			counts.clear();
	}
	
	/**
	 * Load values into a particular sample
	 * 
	 * @param values
	 * @param s
	 * @return true if values loaded, false if ignored
	 * @throws IOException
	 */
	private boolean loadValuesIntoSample(TridasValues values, Sample s) throws IOException {
		TridasVariable variable = values.getVariable();
		boolean unitless = values.isSetUnitless();
		TridasUnit units = unitless ? null : values.getUnit();
		List<TridasValue> dataValues = values.getValues();
		
		if(variable.isSetNormalTridas()) {
			switch(variable.getNormalTridas()) {
			case RING_WIDTH: {
				List<Integer> ringwidths = new ArrayList<Integer>(dataValues.size());
				List<Integer> counts = new ArrayList<Integer>(dataValues.size());
				
				loadIntegerObjectAndCountList(dataValues, s.getSampleType() != SampleType.SUM, 
						ringwidths, counts);
				
				// set our sample data
				s.setData(ringwidths);
				
				// now set counts
				if(!counts.isEmpty())
					s.setCount(counts);

				return true;
			}
				
			default:
				System.out.println("Not handling tridas variable " + variable.getNormalTridas().value());
				return false;
			}
		}
		// a 'normal' standard is one that's not created
		else if(variable.isSetNormalStd()) {
			String standard = variable.getNormalStd();
			
			// a corina standard?
			if("corina".equals(standard)) {
				if("weiserjahre".equals(variable.getNormal())) {
					List<Integer> wjinc = new ArrayList<Integer>(dataValues.size());
					List<Integer> wjdec = new ArrayList<Integer>(dataValues.size());
					
					loadWeiserjahre(dataValues, wjinc, wjdec);
					
					s.setWJIncr(wjinc);
					s.setWJDecr(wjdec);
					
					return true;
				}
				else {
					System.out.println("Unknown corina standard " + variable.getNormal());
					return false;
				}
			}
			else {
				System.out.println("Unknown standard " + standard);
				return false;
			}
			
		} 
		else {
			System.err.println("Don't know how to deal with values: " + values.toString());
			return false;
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
					for(TridasMeasurementSeries series : radius.getMeasurementSeries()) {
						// do we need to do anything in here?
					}
					radius.unsetMeasurementSeries();
				}
				sample.unsetRadiuses();
			}
			element.unsetSamples();
		}
		obj.unsetElements();
	}

	private void populateLabCode(BaseSample s) {
		
	}
	
	/**
	 * Load an object tree!
	 * 
	 * @param obj
	 * @param samples
	 * @param objectHierarchy
	 * @throws IOException
	 */
	private void loadObjectMeasurementsIntoList(TridasObject obj, List<BaseSample> samples, 
			List<TridasObject> objectHierarchy) throws IOException {

		// add myself to the hierarchy
		objectHierarchy.add(obj);
		
		// create an array of the hierarchy so far (not modified by next recursive call)
		TridasObject[] objArray = new TridasObject[objectHierarchy.size()];
		objArray = objectHierarchy.toArray(objArray);
		
		// do child objects first
		for(TridasObject child : obj.getObjects())
			loadObjectMeasurementsIntoList(child, samples, objectHierarchy);
		
		// ok, now load some samples from this tree!
		for(TridasElement element : obj.getElements()) {
			for(TridasSample sample : element.getSamples()) {
				for(TridasRadius radius : sample.getRadiuses()) {
					for(ITridasSeries series : radius.getMeasurementSeries()) {
						BaseSample s = loadFromBaseSeries(series);
						
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
								if("corina.objectLabCode".equals(f.getName())) {
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
	 * @param disassociate if true, takes the object list given by 'obj' and breaks all
	 * 		parent->child links (to prevent one sample from holding on to another sample 
	 * 		in garbage collection, for instance)
	 * @return
	 */
	public List<BaseSample> loadFromObject(TridasObject obj, List<BaseSample> appendSamples, 
			boolean disassociate) throws IOException {
		loadObjectMeasurementsIntoList(obj, appendSamples, new ArrayList<TridasObject>());
		
		if(disassociate)
			breakUpTridasLinks(obj);
		
		return appendSamples;
	}
	
	/**
	 * @see loadFromObject(TridasObject, List, boolean)
	 * @param obj
	 * @param disassociate
	 * @return
	 */
	public List<BaseSample> loadFromObject(TridasObject obj, boolean disassociate) throws IOException {
		return loadFromObject(obj, new ArrayList<BaseSample>(), disassociate);
	}

	
	/**
	 * Given a derivedSeries or measurementSeries, load into a BaseSample 
	 * (or a sample, if the series has values)
	 * 
	 * @param series
	 * @return
	 * @throws IOException
	 */
	public BaseSample loadFromBaseSeries(ITridasSeries series) throws IOException {
		BaseSample s;
		
		// if it has values, it's a sample. Otherwise, it's a basesample.
		if(series.isSetValues())
			s = new Sample();
		else
			s = new BaseSample();
		
		// Start with a basic title
		s.setMeta(Metadata.TITLE, series.getIdentifier().toString());
		
		// set up SampleType
		if(series instanceof TridasDerivedSeries) {
			TridasDerivedSeries derived = (TridasDerivedSeries) series;
			s.setSampleType(SampleType.fromString(derived.getType().getValue()));
		}
		else
			s.setSampleType(SampleType.DIRECT);
		
		// prep generic fields
		TridasGenericFieldMap genericFields = new TridasGenericFieldMap(series.getGenericFields());
		
		// easy metadata bits
		s.setMeta(Metadata.NAME, series.getTitle());
		s.setMeta(Metadata.TRIDAS_IDENTIFIER, series.getIdentifier());
		s.setMeta(Metadata.CREATED_TIMESTAMP, series.getCreatedTimestamp().
				getValue().toGregorianCalendar().getTime());
		s.setMeta(Metadata.MODIFIED_TIMESTAMP, series.getLastModifiedTimestamp().
				getValue().toGregorianCalendar().getTime());
		s.setMeta(Metadata.SERIES, series); // hold on to this for later!

		// reconciled only works on Direct VMs
		if(genericFields.containsKey("corina.isReconciled") && s.getSampleType() == SampleType.DIRECT)
			s.setMeta(Metadata.RECONCILED, genericFields.getBoolean("corina.isReconciled"));
		
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
			Sample fs = (Sample) s;
			List<TridasValues> removeValues = new ArrayList<TridasValues>();
			
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
				
				// load the values into the actual sample
				// keep a list of things we used (true return)
				if(loadValuesIntoSample(valuesElement, fs))
					removeValues.add(valuesElement);
			}
			
			// remove everything that we used
			series.getValues().removeAll(removeValues);
		}
		// no values: summary VM
		else {
			// make up our range...
			s.setRange(new Range(firstYear, genericFields.getInteger("corina.readingCount", 0)));
			
			// ok, build lab code...
			LabCode labcode = new LabCode();
			
			for(int objectIdx = 1;;objectIdx++) {
				String objectCode = genericFields.getString("corina.objectCode." + objectIdx);
				String objectTitle = genericFields.getString("corina.objectTitle." + objectIdx);
				
				// we're done here!
				if(objectCode == null || objectTitle == null)
					break;
				
				labcode.appendSiteCode(objectCode);
				labcode.appendSiteTitle(objectTitle);
			}
			
			// ok if these are null
			labcode.setElementCode(genericFields.getString("corina.elementTitle"));
			labcode.setRadiusCode(genericFields.getString("corina.radiusTitle"));
			labcode.setSampleCode(genericFields.getString("corina.sampleTitle"));
			labcode.setSeriesCode(series.getTitle());
			
			s.setMeta(Metadata.LABCODE, labcode);			
			s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
			
			// set up summary metadata
			if(genericFields.containsKey("corina.seriesCount") && s.getSampleType() == SampleType.SUM)
				s.setMeta(Metadata.SUMMARY_SUM_CONSTITUENT_COUNT, genericFields.getInteger("corina.seriesCount"));
			if(genericFields.containsKey("corina.summaryTaxonName"))
				s.setMeta(Metadata.SUMMARY_MUTUAL_TAXON, genericFields.getString("corina.summaryTaxonName"));
			if(genericFields.containsKey("corina.summaryTaxonCount"))
				s.setMeta(Metadata.SUMMARY_MUTUAL_TAXON_COUNT, genericFields.getInteger("corina.summaryTaxonCount"));
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
			System.out.println("JDOME: " + jdome);
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

	public String getDefaultExtension() {
		return I18n.getText("format.corinaxml");
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
}
