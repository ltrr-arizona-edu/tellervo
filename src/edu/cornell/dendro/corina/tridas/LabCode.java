package edu.cornell.dendro.corina.tridas;

import java.util.ArrayList;
import java.util.List;

public class LabCode {
	private List<String> siteCodes;
	private String elementCode;
	private String sampleCode;
	private String radiusCode;
	private String seriesCode;
	
	public LabCode() {
		siteCodes = new ArrayList<String>(1);
		elementCode = sampleCode = radiusCode = seriesCode = null;
	}

	/**
	 * @return the siteCodes
	 */
	public List<String> getSiteCodes() {
		return siteCodes;
	}

	/**
	 * @param siteCode a siteCode to append
	 */
	public void appendSiteCode(String siteCode) {
		this.siteCodes.add(siteCode);
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
		this.seriesCode = seriesCode;
	}
		
}
