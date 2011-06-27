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
	package edu.cornell.dendro.corina.tridasv2;

import java.util.List;

import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;

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
	@SuppressWarnings("unused")
	private String siteDelimiter;
	private String unavailableValue;
	private String codeFormat;
	
	private final static LabCodeFormatter cornellLabCodeFormatter = new LabCodeFormatter("C-%SITES%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%");
	private final static LabCodeFormatter cornellSeriesPrefixFormatter = new LabCodeFormatter("C-%SITES%-%ELEMENT%-%SAMPLE%-%RADIUS%");
	private final static LabCodeFormatter cornellRadiusPrefixFormatter = new LabCodeFormatter("C-%SITE%-%ELEMENT%-%SAMPLE%");
	private final static LabCodeFormatter cornellSamplePrefixFormatter = new LabCodeFormatter("C-%SITES%-%ELEMENT%");
	private final static LabCodeFormatter cornellElementPrefixFormatter = new LabCodeFormatter("C-%SITES%");

	
	public static LabCodeFormatter getDefaultFormatter() {
		return cornellLabCodeFormatter;
	}
		
	public static LabCodeFormatter getSeriesPrefixFormatter(){
		return cornellSeriesPrefixFormatter;
	}
	
	public static LabCodeFormatter getRadiusPrefixFormatter(){
		return cornellRadiusPrefixFormatter;
	}
	
	public static LabCodeFormatter getSamplePrefixFormatter(){
		return cornellSamplePrefixFormatter;
	}
	
	public static LabCodeFormatter getElementPrefixFormatter(){
		return cornellElementPrefixFormatter;
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
		
		// handle completely empty lab codes
		if(labCode.isEmptyCode()) {
			if((val = labCode.getSeriesCode()) != null)
				return val;
			else
				return unavailableValue;
		}
		
		if(objects.size() > 0) {
			StringBuffer objectList = new StringBuffer();
			
			// PWB - Ignoring subobjects in lab code
			// So that style is always the C-ABC-1-A-A-A 
			// that is expected.
			/*
			for(String s : objects) {
				if(objectList.length() > 0)
					objectList.append(siteDelimiter);
				objectList.append(s);
			}*/	
			objectList.append(objects.get(0));
			
			
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
