package edu.cornell.dendro.corina.site;

public class GenericSummary {
	private String labPrefix;
	private String labCode;
	
	public String getLabCode() {
		return labCode;
	}
	public String getLabPrefix() {
		return labPrefix;
	}
	
	public GenericSummary setLabCode(String labCode) {
		this.labCode = labCode;
		return this;
	}
	
	public GenericSummary setLabPrefix(String labPrefix) {
		this.labPrefix = labPrefix;
		return this;
	}
	
	public static GenericSummary fromXML(org.jdom.Element summary) {
		GenericSummary ss = new GenericSummary();
		
		fromXMLBase(summary, ss);
		
		return ss;
	}
	
	protected static void fromXMLBase(org.jdom.Element summary, GenericSummary ss) {
		ss.setLabCode(summary.getChildTextNormalize("fullLabCode"));
		ss.setLabPrefix(summary.getChildTextNormalize("labPrefix"));		
	}
}
