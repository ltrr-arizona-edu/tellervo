package edu.cornell.dendro.corina.tridas;

import java.util.List;

/**
 * Lab code formatter
 * 
 * Pass a format string; replaces the following tokens
 * %SITES% a siteDelimiter separated list of site codes (see constructor to change this, defaults to /)
 * %SITE% the last site code
 * %ELEMENT% the element code
 * %SAMPLE% the sample code
 * %RADIUS% the radius code
 * %SERIES% the series code
 * 
 * @author lucasm
 *
 */

public class LabCodeFormatter {
	private String siteDelimiter;
	private String unavailableValue;
	private String codeFormat;
	
	private static LabCodeFormatter cornellLabCodeFormatter = new LabCodeFormatter("C-%SITES%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%");
	public static LabCodeFormatter getDefaultFormatter() {
		return cornellLabCodeFormatter;
	}
	
	public LabCodeFormatter(String format) {
		this(format, "/", "*NA*");
	}
	
	public LabCodeFormatter(String format, String siteDelimiter, String unavailableValue) {
		this.siteDelimiter = siteDelimiter;
		this.unavailableValue = unavailableValue;
		this.codeFormat = format;
	}

	/**
	 * Replace in place!
	 * @param sb
	 * @param target
	 * @param replacement
	 */
	private void replace(StringBuffer sb, String target, String replacement) {
		int idx;
		
		while((idx = sb.indexOf(target)) != -1) 
			sb.replace(idx, idx + target.length(), replacement);
	}
	
	public String format(LabCode labCode) {
		StringBuffer format = new StringBuffer(codeFormat);
		List<String> objects = labCode.getSiteCodes();
		String val;
		StringBuffer sb;
		
		if(objects.size() > 0) {
			StringBuffer objectList = new StringBuffer();
			
			for(String s : objects) {
				if(objectList.length() > 0)
					objectList.append(siteDelimiter);
				objectList.append(s);
			}
					
			replace(format, "%SITES%", objectList.toString());
			replace(format, "%SITE%", objects.get(objects.size() - 1));
		}
		else {
			replace(format, "%SITES%", unavailableValue);
			replace(format, "%SITE%", unavailableValue);
		}
		
		if((val = labCode.getElementCode()) != null)
			replace(format, "%ELEMENT%", val);
		else
			replace(format, "%ELEMENT%", unavailableValue);

		if((val = labCode.getSampleCode()) != null)
			replace(format, "%SAMPLE%", val);
		else
			replace(format, "%SAMPLE%", unavailableValue);

		if((val = labCode.getRadiusCode()) != null)
			replace(format, "%RADIUS%", val);
		else
			replace(format, "%RADIUS%", unavailableValue);

		if((val = labCode.getSeriesCode()) != null)
			replace(format, "%SERIES%", val);
		else
			replace(format, "%SERIES%", unavailableValue);

		return format.toString();
	}
}
