/**
 * 
 */
package edu.cornell.dendro.corina.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.Tree;

/**
 * This class provides convenience methods for extracting data out of old-format Corina samples
 * (or maybe even imported Heidelberg, etc samples).
 * 
 * @author lucasm
 *
 */
public class LegacySampleExtractor {
	private Sample sample;
	
	public LegacySampleExtractor(Sample s) {
		if(!(s.getLoader() instanceof FileElement))
			throw new UnsupportedOperationException("Legacy samples must be file-based samples");
		
		this.sample = s;
		
		extractFromFilename();
		
		// populate the site because this is easy!
		if(siteName != null)
			associatedSite = App.tridasObjects.findSite(siteName);
	}
	
	private String siteName;
	private String specimenOrTreeName;
	private String radiusName;
	private String measurementName;
	
	private Site associatedSite;
	
	/**
	 * Get the site object!
	 * 
	 * @return
	 */
	public Site asSite() {
		return associatedSite;
	}
	
	/**
	 * Get the 'main' subsite, if a site was found
	 * @return
	 */
	public Subsite asSubsite() {
		if(associatedSite != null) {
			List<Subsite> subsites = associatedSite.getSubsites();
			
			for(Subsite s : subsites)
				if(s.toString().equalsIgnoreCase("main"))
					return s;
		}
		
		return null;
	}
	
	/**
	 * Tree- 
	 * Populates: name, species->originalTaxonName
	 * @return
	 */
	public Tree asTree() {
		Tree tree = new Tree(Tree.ID_NEW, (specimenOrTreeName == null) ? 
				Tree.NAME_INVALID : specimenOrTreeName);
		String val;
		
		if((val = (String) sample.getMeta("species")) != null)
			tree.setOriginalTaxonName(val);
		
		return tree;
	}
	
	/**
	 * Populates a specimen
	 * unmeasuredpre
	 * unmeasuredpost
	 * sapwood
	 * specimentype
	 * terminalring
	 * specimencontinuity
	 * @return
	 */
	public Specimen asSpecimen() {
		Specimen spec = new Specimen(Specimen.ID_NEW, (specimenOrTreeName == null) ? 
				Specimen.NAME_INVALID : specimenOrTreeName);
		String val;
		Object oval;
		
		if((oval = sample.getMeta("unmeas_pre")) != null) {
			try {
				spec.setUnmeasuredPre(Integer.parseInt(oval.toString()));
			} catch (NumberFormatException nfe) {
				// ignore
			}
		}

		if((oval = sample.getMeta("unmeas_post")) != null) {
			try {
				spec.setUnmeasuredPost(Integer.parseInt(oval.toString()));
			} catch (NumberFormatException nfe) {
				// ignore
			}
		}

		if((oval = sample.getMeta("sapwood")) != null) {
			try {
				spec.setSapwoodCount(Integer.parseInt(oval.toString()));
			} catch (NumberFormatException nfe) {
				// ignore
			}
		}

		if((val = (String) sample.getMeta("type")) != null)
			spec.setSpecimenType(getLegacyMapping("type", val));
		if((val = (String) sample.getMeta("pith")) != null)
			spec.setPith(getLegacyMapping("pith", val));
		if((val = (String) sample.getMeta("terminal")) != null)
			spec.setTerminalRing(getLegacyMapping("terminal", val));
		if((val = (String) sample.getMeta("continuous")) != null)
			spec.setSpecimenContinuity(getLegacyMapping("continuous", val));
// this doesn't exist?
//		if((val = (String) sample.getMeta("quality")) != null)
//			spec.setSpecimenQuality(getLegacyMapping("quality", val));

		return spec;
	}

	
	/**
	 * Radius
	 * name
	 * @return
	 */
	public Radius asRadius() {
		Radius radius = new Radius(Radius.ID_NEW, (radiusName == null) ? 
				Radius.NAME_INVALID : radiusName);
		return radius;
	}

	/**
	 * Actually, just the original sample.
	 * @return
	 */
	public Sample asMeasurement() {
		if(measurementName != null)
			sample.setMeta("guessedmeasurementname", measurementName);

		return sample;
	}
	
	private void extractFromFilename() {
		String filename = (String) sample.getMeta("filename");
		File f = new File(filename);
		String basename = f.getName();
		
		Pattern patterns[] = new Pattern[6];
		
        // Site code, specimen/tree, radius, I for index
        patterns[0]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z])I\\.");
        // Site code, specimen/tree, radius, measurement
        patterns[1]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z]{2})\\.");
        // Site code, specimen/tree, radius, measurement, I for index
        patterns[2]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z]{2})I\\.");
        // Site code, specimen/tree, measurement
        patterns[3]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)([a-zA-Z])\\.");
        // Site code, first specimen, second specimen (SUM)
        patterns[4]  = Pattern.compile("([a-zA-Z]{3})([0-9]*)\\&([0-9]*)\\.");
        // Site code, Three digits representing number of specimens in this sum
        patterns[5]  = Pattern.compile("([a-zA-Z]{3})[000|111|222|333|444|555|666|777|888|999]\\.");

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
        			siteName = val.toUpperCase();
        			break;
        		case 2:
        			specimenOrTreeName = val.toUpperCase();
        			break;
        		case 3:
        			switch(i) {
        			case 0:
        				radiusName = val.toUpperCase();
        				break;
        			case 1:
        			case 2:
        				radiusName = val.substring(0, 1).toUpperCase();
        				measurementName = val.substring(1, 2).toUpperCase();
        				break;
        			case 3:
        				measurementName = val.toUpperCase();
        				break;
        			// should we implement case 4? it's not useful, I think
        			case 4:
        				break;
        			}
        		}
        	}
        	
        	// we found a match, break out
        	break;
        }
	}
	
	/**
	 * Return a sanitized filename
	 * @return
	 */
	public String getFilename() {
		String fn = (String) sample.getMeta("filename");
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
		
		sb.append("<span style=\"color: white; font-family: sans-serif; font-size: 12pt;\">");

		// data that we've guessed at
		if(siteName != null || specimenOrTreeName != null || radiusName != null || measurementName != null) {
			sb.append("<b><u>Guessed Data</u></b><br>");
			if (siteName != null)
				sb.append("<b>Site</b>: " + siteName + "<br>");
			if (specimenOrTreeName != null)
				sb.append("<b>Specimen/tree</b>: " + specimenOrTreeName
						+ "<br>");
			if (radiusName != null)
				sb.append("<b>Radius</b>: " + radiusName + "<br>");
			if (measurementName != null)
				sb.append("<b>Measurement</b>: " + measurementName
						+ "<br>");
			
			sb.append("<br>");
		}

		sb.append("<b><u>Metadata</u></b><br>");

		Map<String, Object> meta = sample.getMetadata();
		for(Map.Entry<String, Object> e : meta.entrySet()) {
			Object v = e.getValue();
			
			// ignore our kludge! :)
			if(e.getKey().equals("guessedmeasurementname"))
				continue;
			
			// kludge together something acceptable for files
			if(e.getKey().equalsIgnoreCase("filename")) {
				v = getFilename();
			}
			
			sb.append("<b>");
			sb.append(e.getKey());
			sb.append("</b>: ");
			sb.append(v == null ? "[empty]" : v.toString());
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
		legacyMetadataMap.put("pith_+", "Present"); // older corina
		legacyMetadataMap.put("pith_P", "Present"); // newer corina
		legacyMetadataMap.put("pith_*", "Present but undateable");
		legacyMetadataMap.put("pith_N", "Absent");
		// terminal ring
		legacyMetadataMap.put("terminal_v", "Unknown");
		legacyMetadataMap.put("terminal_vv", "Near edge");
		legacyMetadataMap.put("terminal_B", "Bark");
		legacyMetadataMap.put("terminal_W", "Waney edge");
		// specimen continuity
		legacyMetadataMap.put("continuous_C", "Continuous");
		legacyMetadataMap.put("continuous_R", "Partially continuous (>50%)");
		legacyMetadataMap.put("continuous_N", "Not continuous");
		// specimen quality
		legacyMetadataMap.put("quality_+", "One unmeasured ring");
		legacyMetadataMap.put("quality_++", "More than one unmeasured rings");
		// reconciled
		legacyMetadataMap.put("reconciled_Y", "true");
		legacyMetadataMap.put("reconciled_N", "false");
	}
}
