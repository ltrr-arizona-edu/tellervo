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
package org.tellervo.desktop.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.editor.EditorFactory.BarcodeDialogResult;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.schema.WSISecurityUser;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.NormalTridasMeasuringMethod;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasBark;
import org.tridas.schema.TridasDating;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasHeartwood;
import org.tridas.schema.TridasInterpretation;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasPith;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasSapwood;
import org.tridas.schema.TridasWoodCompleteness;


/**
 * This class provides convenience methods for extracting data out of old-format Corina samples
 * (or maybe even imported Heidelberg, etc samples).
 * 
 * @author lucasm
 *
 */
public class LegacySampleExtractor {
	private Sample s;
	
	public LegacySampleExtractor(Sample s) {
		//if(!(s.getLoader() instanceof FileElement))
			//throw new UnsupportedOperationException("Legacy samples must be file-based samples");
		
		this.s = s;
		this.barcodeSupplied = false;
		extractFromFilename();		
	}
	
	/**
	 * For use when you have a barcode to set the object, element and sample
	 * @param s
	 * @param result
	 */
	public LegacySampleExtractor(Sample s, BarcodeDialogResult result){
	//	if(!(s.getLoader() instanceof FileElement))
		//	throw new UnsupportedOperationException("Legacy samples must be file-based samples");
		
		
		this.barcodeSupplied = true;
		result.populateCorinaSample(s);
		this.s = s;
		extractFromFilename();		
		
	}

	private String objectCode;
	private String elementName;
	private String sampleName;
	private String radiusName;
	private String measurementName;
	private Boolean barcodeSupplied;

	
	public void extractFromFilename() {
	
		String filename = (String) s.getMeta("filename");
		File f = new File(filename);
		String basename = f.getName();
		
		Pattern patterns[] = new Pattern[7];
		
		// default some stuff to A
		if (barcodeSupplied==false) sampleName = "A";
		radiusName = "A";
		measurementName = "A";
		
        // Site code, specimen/tree, radius, I for index
        patterns[0]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z])I\\.");
        // Site code, specimen/tree, radius, measurement
        patterns[1]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z]{2,3})\\.");
        // Site code, specimen/tree, radius, measurement, I for index
        patterns[2]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z]{2,3})I\\.");
        // Site code, specimen/tree, measurement
        patterns[3]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z])\\.");
        // Site code, first specimen, second specimen (SUM)
        patterns[4]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)\\&([0-9]*)\\.");
        // Site code, Three digits representing number of specimens in this sum
        patterns[5]  = Pattern.compile("([a-zA-Z]{3})[000|111|222|333|444|555|666|777|888|999]\\.");
        // Modern Corina TRiDaS code C-ABC-1-A-A-A 
        patterns[6]  = Pattern.compile(App.getLabCodePrefix()+"([a-zA-Z]{3})-([0-9]*)-([a-zA-Z]{1})-([a-zA-Z]{1})-([a-zA-Z]{1})\\.");
        
        for(int i = 0; i < patterns.length; i++) {
        	Matcher matcher = patterns[i].matcher(basename);
        	
        	// no match
        	if(!matcher.find())
        		continue;
        	
        	// off by one
        	for(int j = 1; j <= matcher.groupCount(); j++) {
        		String val = matcher.group(j);
        		
        		switch (j) {
        		case 1:
        			if (barcodeSupplied==false) objectCode = val.toUpperCase();
        			break;
        		case 2:
        			if (barcodeSupplied==false) elementName = val.toUpperCase();
        			break;
        		case 3:
        			switch(i) {
        			case 0:
        				radiusName = val.toUpperCase();
        				break;
        			case 1:
        			case 2:
        				if(val.length() == 2) {
        					radiusName = val.substring(0, 1).toUpperCase();
        					measurementName = val.substring(1, 2).toUpperCase();
        				}
        				else if(val.length() == 3) {
        					if (barcodeSupplied==false) sampleName = val.substring(0, 1).toUpperCase();
        				    radiusName = val.substring(1, 2).toUpperCase();
        					measurementName = val.substring(2, 3).toUpperCase();
        				}
        				break;
        			case 3:
        				measurementName = val.toUpperCase();
        				break;
        			// should we implement case 4? it's not useful, I think
        			case 4:
        				break;
        			case 6:
        				if (barcodeSupplied==false) sampleName = val.toUpperCase();
    					break;
        			default:
        				break;
        			}
        			break;
     					
				case 4:
					radiusName = val.toUpperCase();
					break;
				case 5:
					measurementName = val.toUpperCase();
    				break;
        		}
        	}
        	
        	// we found a match, break out
        	break;
        }
	}

	public String getObjectCode() {
		return objectCode;
	}
	
	public String getElementTitle() {
		return elementName;
	}

	public String getSampleTitle() {
		return sampleName;
	}

	public String getRadiusTitle() {
		return radiusName;
	}
	
	public String getMeasurementTitle() {
		return measurementName;
	}
	
	private ControlledVoc getControlledVocForName(String name, String dictionaryName) {
		List<?> dictionary = Dictionary.getDictionary(dictionaryName);
		List<ControlledVoc> vocab = ListUtil.subListOfType(dictionary, ControlledVoc.class);
		
		for(ControlledVoc voc : vocab) {
			if(name.equalsIgnoreCase(voc.getNormal()))
				return voc;
		}
		
		return null;
	}
	
	public void populateObject(TridasObject object) {
		if(objectCode != null)
			object.setTitle(objectCode);
		
		// does it have a forest in the name?
		String fn = (String) s.getMeta("filename");
		if(fn.toLowerCase().contains("forest"))
			object.setType(getControlledVocForName("Forest", "objectTypeDictionary"));
		
		// ok, then default to site
		if(!object.isSetType())
			object.setType(getControlledVocForName("Site", "objectTypeDictionary"));
	}
	
	public void populateElement(TridasElement element) {
		if(elementName != null)
			element.setTitle(elementName);
		
		element.setTaxon(getControlledVocForName("Plantae", "taxonDictionary"));
		element.setType(getControlledVocForName("Tree", "elementTypeDictionary"));
		
		// try for a taxon...
		if(s.hasMeta("species")) {
			String myTaxon = s.getMetaString("species").toLowerCase();
			boolean foundTaxon = false;
		
			List<?> dictionary = Dictionary.getDictionary("taxonDictionary");
			List<ControlledVoc> taxa = ListUtil.subListOfType(dictionary, ControlledVoc.class);
		
			for(ControlledVoc taxon : taxa) {
				if(taxon.getNormal().equalsIgnoreCase(myTaxon)) {
					element.setTaxon(taxon);
					foundTaxon = true;
					break;
				}
			}
			
			// ok, try starts with...
			if(!foundTaxon) {
				for(ControlledVoc taxon : taxa) {
					if(taxon.getNormal().toLowerCase().startsWith(myTaxon)) {
						element.setTaxon(taxon);
						break;
					}
				}				
			}
		}
	}

	public void populateSample(TridasSample sample) {
		if(sampleName != null)
			sample.setTitle(sampleName);

		String type = s.hasMeta("type") 
				? getLegacyMapping("type", s.getMetaString("type")) 
				: "Section";
				
		sample.setType(getControlledVocForName(type, "sampleTypeDictionary"));
	}
	
	public void populateRadius(TridasRadius radius) {
		if(radiusName != null)
			radius.setTitle(radiusName);
		
		// add a wood completeness if it's not there
		if(!radius.isSetWoodCompleteness())
			radius.setWoodCompleteness(new TridasWoodCompleteness());
		
		// ok, now modify it...
		TridasWoodCompleteness wood = radius.getWoodCompleteness();

		// have to add a bunch of stuff...
		TridasPith pith = new TridasPith();
		pith.setPresence(ComplexPresenceAbsence.UNKNOWN);
		wood.setPith(pith);
		
		TridasHeartwood heartwood = new TridasHeartwood();
		heartwood.setPresence(ComplexPresenceAbsence.UNKNOWN);
		wood.setHeartwood(heartwood);
		
		TridasSapwood sapwood = new TridasSapwood();
		sapwood.setPresence(ComplexPresenceAbsence.UNKNOWN);
		wood.setSapwood(sapwood);
		
		TridasBark bark = new TridasBark();
		bark.setPresence(PresenceAbsence.ABSENT); // default to this, we look again later...
		wood.setBark(bark);

		String val;

		if((val = s.getMetaString("pith")) != null) {
			val = getLegacyMapping("pith", val);
			
			pith.setPresence(ComplexPresenceAbsence.fromValue(val));
		}
		
		if((val = s.getMetaString("unmeas_pre")) != null) {
			try{
			heartwood.setMissingHeartwoodRingsToPith(Integer.valueOf(val));
			heartwood.setMissingHeartwoodRingsToPithFoundation("Observed but not measured");
			} catch (NumberFormatException e){}
		}

		if((val = s.getMetaString("unmeas_post")) != null) {
			try{
			sapwood.setMissingSapwoodRingsToBark(Integer.valueOf(val));
			sapwood.setMissingSapwoodRingsToBarkFoundation("Observed but not measured");
			} catch (NumberFormatException e){}
		}
		
		if((val = s.getMetaString("sapwood")) != null) {	
			try {
				sapwood.setNrOfSapwoodRings(Integer.parseInt(val));
			} catch (Exception e) {
				// well fine, ignore it
			}
		}
		
		if((val = s.getMetaString("terminal")) != null) {
			val = getLegacyMapping("terminal", val);
			
			if("bark".equals(val)) {
				bark.setPresence(PresenceAbsence.PRESENT);
			}
			else {					
				if("waney edge".equals(val))
					sapwood.setPresence(ComplexPresenceAbsence.COMPLETE);
				else if("near edge".equals(val))
					sapwood.setPresence(ComplexPresenceAbsence.INCOMPLETE);
				else
					sapwood.setPresence(ComplexPresenceAbsence.UNKNOWN);
			}
		}
	}

	public void populateMeasurement(TridasMeasurementSeries series) {
		if(measurementName == null) {
			String filename = (String) s.getMeta("filename");
			File f = new File(filename);
			String basename = f.getName();
			
			series.setTitle(basename);
		}
		else
			series.setTitle(measurementName);
		
		// measuring method...
		TridasMeasuringMethod method = new TridasMeasuringMethod();
		method.setNormalTridas(NormalTridasMeasuringMethod.MEASURING_PLATFORM);
		series.setMeasuringMethod(method);
		
		// interpretation
		TridasInterpretation interpretation = new TridasInterpretation();
		series.setInterpretation(interpretation);
		
		// first year
		interpretation.setFirstYear(s.getRange().getStart().tridasYearValue());
		
		// dating type...
		TridasDating dating = new TridasDating();
		interpretation.setDating(dating);
		
		if(s.hasMeta("dating")) {
			String val = getLegacyMapping("dating", s.getMetaString("dating"));
			dating.setType(NormalTridasDatingType.fromValue(val));
		}
		else
			dating.setType(NormalTridasDatingType.RELATIVE);

		// comments are a little more complicated...
		StringBuffer sb = new StringBuffer();
		if(s.hasMeta("title"))
			sb.append(s.getMetaString("title"));
		
		if(sb.length() > 0)
			sb.append("; ");
		
		if(s.hasMeta("comments")) {
			sb.append(s.getMetaString("comments"));
			sb.append("; ");
		}		
	
		sb.append('[');
		sb.append(getFilename());
		sb.append(']');
		
		series.setComments(sb.toString());

		// author... hey, guess!
		if(s.hasMeta("author")) {
			String author = s.getMetaString("author");
			WSISecurityUser user = findUser(author);
			
			if(user != null) {
				series.setAnalyst(user.getFirstName() + " " + user.getLastName());
				series.setDendrochronologist(user.getFirstName() + " " + user.getLastName());
				
				GenericFieldUtils.addField(series, "corina.analystID", user.getId());
				GenericFieldUtils.addField(series, "corina.dendrochronologistID", user.getId());
			}
		}
	}
	
	private WSISecurityUser findUser(String author) {
		List<?> dictionary = Dictionary.getDictionary("securityUserDictionary");
		List<WSISecurityUser> users = ListUtil.subListOfType(dictionary, WSISecurityUser.class);
		
		for(WSISecurityUser user : users) {
			String name = user.getFirstName() + " " + user.getLastName();
			if(name.equalsIgnoreCase(author))
				return user;
			
			name = user.getLastName() + " " + user.getFirstName();
			if(name.equalsIgnoreCase(author))
				return user;
			
			name = user.getLastName() + ", " + user.getFirstName();
			if(name.equalsIgnoreCase(author))
				return user;
			
			if(name.toLowerCase().contains(author.toLowerCase()))
				return user;
		}
		
		return null;
	}
	
	
	/**
	 * Return a sanitized filename
	 * @return
	 */
	public String getFilename() {
		String fn = (String) s.getMeta("filename");
		ArrayList<String> segments = new ArrayList<String>();
		File f = new File(fn);
		int depth = 0;
		
		fn = f.getName();
		do {
			segments.add(fn);
			
			f = f.getParentFile();
			fn = f.getName();
			depth++;
		} while((fn.length() == 3 || fn.equalsIgnoreCase("forest") || depth == 1) && fn.toLowerCase().indexOf("data") == -1);
		
		StringBuffer sb = new StringBuffer();
		for(int i = segments.size() - 1; i >= 0; i--) {
			if(sb.length() != 0)
				sb.append(File.separator);
			sb.append(segments.get(i));
		}
		
		return sb.toString();
	}
	
	public String asHTML() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<span style=\"color: black; font-family: sans-serif; font-size: 12pt;\">");

		// data that we've guessed at
		if(objectCode != null || elementName != null || sampleName != null || radiusName != null || measurementName != null) {
			sb.append("<b><u>Guessed Data</u></b><br>");
			if (objectCode != null)
				sb.append("<b>Object</b>: " + objectCode + "<br>");
			if (elementName != null)
				sb.append("<b>Element</b>: " + elementName + "<br>");
			if (sampleName != null)
				sb.append("<b>Sample</b>: " + sampleName + "<br>");
			if (radiusName != null)
				sb.append("<b>Radius</b>: " + radiusName + "<br>");
			if (measurementName != null)
				sb.append("<b>Measurement</b>: " + measurementName
						+ "<br>");
			
			sb.append("<br>");
		}

		sb.append("<b><u>Metadata</u></b><br>");

		Map<String, Object> meta = s.getMetadata();
		for(Map.Entry<String, Object> e : meta.entrySet()) {
			Object v = e.getValue();

			// ignore 'machine' data
			if(e.getKey().startsWith(":"))
				continue;
			
			// kludge together something acceptable for files
			if(e.getKey().equalsIgnoreCase("filename")) {
				v = getFilename();
			}
			
			sb.append("<b>");
			sb.append(e.getKey());
			sb.append("</b>: ");
			sb.append(v == null ? "[empty]" : v.toString());
			
			// append useful information?
			if(v != null) {
				String mapVal = getLegacyMapping(e.getKey(), v.toString());
				if(mapVal != null) {
					sb.append(" (");
					sb.append(mapVal);
					sb.append(')');
				}				
			}
			
			sb.append("<br>");
		}

		sb.append("</span>");
		return sb.toString();
	}
	
	/**
	 * Maps old-style metadata to new-style metadata!
	 *
	 * 
	 * @param oldKey (e.g., dating)
	 * @param value (e.g., A)
	 * @return the new metadata key (e.g., Absolute) or null if it's bad.
	 */
	public static String getLegacyMapping(String oldKey, String value) {
		return legacyMetadataMap.get(oldKey + "_" + value);
	}
	
	private final static HashMap<String, String> legacyMetadataMap = new HashMap<String, String>();
	static {
		// dating
		legacyMetadataMap.put("dating_R", "Relative");
		legacyMetadataMap.put("dating_A", "Absolute");
		// specimen type
		legacyMetadataMap.put("type_S", "Section");
		legacyMetadataMap.put("type_H", "Charcoal");
		legacyMetadataMap.put("type_C", "Core");
		// pith
		legacyMetadataMap.put("pith_+", "complete"); // older corina
		legacyMetadataMap.put("pith_P", "complete"); // newer corina
		legacyMetadataMap.put("pith_*", "incomplete");
		legacyMetadataMap.put("pith_N", "absent");
		// terminal ring
		legacyMetadataMap.put("terminal_v", "unknown");
		legacyMetadataMap.put("terminal_vv", "near edge");
		legacyMetadataMap.put("terminal_B", "bark");
		legacyMetadataMap.put("terminal_W", "waney edge");
		// specimen continuity
		legacyMetadataMap.put("continuous_C", "Continuous");
		legacyMetadataMap.put("continuous_R", "Partially continuous (>50%)");
		legacyMetadataMap.put("continuous_N", "Not continuous");
	}
}
