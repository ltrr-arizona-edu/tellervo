package edu.cornell.dendro.corina.tridasv2;

import java.util.ArrayList;
import java.util.List;

public class LabCode implements Comparable<LabCode> {
	private List<String> siteCodes;
	private List<String> siteTitles;
	private String elementCode;
	private String sampleCode;
	private String radiusCode;
	private String seriesCode;
	
	public LabCode() {
		siteCodes = new ArrayList<String>(1);
		siteTitles = new ArrayList<String>(1);
		elementCode = sampleCode = radiusCode = seriesCode = null;
	}
	
	/**
	 * Copy constructor
	 * @param src
	 */
	public LabCode(LabCode src) {
		siteCodes = new ArrayList<String>(src.siteCodes);
		siteTitles = new ArrayList<String>(src.siteTitles);
		elementCode = src.elementCode;
		sampleCode = src.sampleCode;
		radiusCode = src.radiusCode;
		seriesCode = src.seriesCode;
	}
	
	/**
	 * @return true if this code is empty (not counting series code)
	 */
	public boolean isEmptyCode() {
		return siteCodes.isEmpty() && elementCode == null && sampleCode == null && radiusCode == null;
	}

	/**
	 * @return the siteCodes
	 */
	public List<String> getSiteCodes() {
		return siteCodes;
	}

	/**
	 * @return the siteTitles
	 */
	public List<String> getSiteTitles() {
		return siteTitles;
	}

	/** 
	 * Clear the list of site codes
	 */
	public void clearSites() {
		this.siteCodes.clear();
		this.siteTitles.clear();
	}

	/**
	 * @param siteCode a siteCode to append
	 */
	public void appendSiteCode(String siteCode) {
		this.siteCodes.add(siteCode);
	}

	/**
	 * @param siteTitle a siteTitle to append
	 */
	public void appendSiteTitle(String siteTitle) {
		this.siteTitles.add(siteTitle);
	}

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		if(elementCode != null && elementCode.length() == 0)
			elementCode = null;
		
		this.elementCode = elementCode;
	}

	/**
	 * @return the sampleCode
	 */
	public String getSampleCode() {
		return sampleCode;
	}

	/**
	 * @param sampleCode the sampleCode to set
	 */
	public void setSampleCode(String sampleCode) {
		if(sampleCode != null && sampleCode.length() == 0)
			sampleCode = null;
		
		this.sampleCode = sampleCode;
	}

	/**
	 * @return the radiusCode
	 */
	public String getRadiusCode() {
		return radiusCode;
	}

	/**
	 * @param radiusCode the radiusCode to set
	 */
	public void setRadiusCode(String radiusCode) {
		if(radiusCode != null && radiusCode.length() == 0)
			radiusCode = null;
		
		this.radiusCode = radiusCode;
	}

	/**
	 * @return the seriesCode
	 */
	public String getSeriesCode() {
		return seriesCode;
	}

	/**
	 * @param seriesCode the seriesCode to set
	 */
	public void setSeriesCode(String seriesCode) {
		if(seriesCode != null && seriesCode.length() == 0)
			seriesCode = null;
		
		this.seriesCode = seriesCode;
	}
	
	/**
	 * Compare two string values, but try them as integers
	 * 
	 * @param o1
	 * @param o2
	 * @return -1, 0, or 1, if o1 is less than, equal to, or greater than o2
	 */
	private int compare(String o1, String o2) {
		// nicely handle nulls
		if (o1 == null && o2 == null)
			return 0;

		// nulls go last
		if (o1 == null)
			return 1;
		if (o2 == null)
			return -1;

		Integer i1 = null, i2 = null;
		try {
			i1 = Integer.valueOf(o1);
		} catch (NumberFormatException nfe) {
		}
		
		try {
			i2 = Integer.valueOf(o2);
		} catch (NumberFormatException nfe) {
		}

		// both strings, string compare!
		if (i1 == null && i2 == null)
			return o1.compareToIgnoreCase(o2);

		// strings go last
		if (i1 == null)
			return 1;
		if (i2 == null)
			return -1;

		// flat out integers!
		return i1.compareTo(i2);
	}
		
	//
	// for comparable
	//
	
	public int compareTo(LabCode o) {
		int i = 0;
		int v;
		
		/* This code uses subobjects in code.  But at Cornell we 
		 * are only interested in the top level object so we are
		 * doing simple check of top level code instead
		 * 
		// sites are tricky...
		while(i < o.siteCodes.size() && i < this.siteCodes.size()) {
			String s1 = (i < this.siteCodes.size()) ? this.siteCodes.get(i) : null;
			String s2 = (i < o.siteCodes.size()) ? o.siteCodes.get(i) : null;
			
			v = compare(s1, s2);
			if(v != 0)
				return v;
			
			i++;
		}
		*/
		
		String s1 = (this.siteCodes.size()>0) ? this.siteCodes.get(0) : null;
		String s2 = (o.siteCodes.size()>0) ? o.siteCodes.get(0) : null;
		if((v = compare(s1, s2)) != 0)
			return v;
		
		if((v = compare(this.elementCode, o.elementCode)) != 0)
			return v;
		
		if((v = compare(this.sampleCode, o.sampleCode)) != 0)
			return v;

		if((v = compare(this.radiusCode, o.radiusCode)) != 0)
			return v;

		if((v = compare(this.seriesCode, o.seriesCode)) != 0)
			return v;
		
		// they're totally equal??
		return 0;
	}
}
