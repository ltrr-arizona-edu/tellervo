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
	package org.tellervo.desktop.tridasv2;

import java.util.List;

import org.jfree.util.Log;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;


/**
 * Lab code formatter
 * 
 * Pass a format string; replaces the following tokens
 * %LABACRONYM% acronym of lab
 * %OBJECTS% a siteDelimiter separated list of site codes (see constructor to change this, defaults to /)
 * %OBJECT% the top level site code
 * %BLOBJECT% the bottom level site code (i.e. site immediately attached to element)
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
	

	private final static LabCodeFormatter subsitesLabCodeFormatter = new LabCodeFormatter("%LABACRONYM%-%OBJECTS%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%");
	private final static LabCodeFormatter immediateParentObjectLabCodeFormatter = new LabCodeFormatter("%BLOBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%");
	private final static LabCodeFormatter cornellLabCodeFormatter = new LabCodeFormatter("%LABACRONYM%-%OBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%");
	private final static LabCodeFormatter cornellSeriesPrefixFormatter = new LabCodeFormatter("%LABACRONYM%-%OBJECTS%-%ELEMENT%-%SAMPLE%-%RADIUS%");
	private final static LabCodeFormatter cornellRadiusPrefixFormatter = new LabCodeFormatter("%LABACRONYM%-%OBJECTS%-%ELEMENT%-%SAMPLE%");
	private final static LabCodeFormatter cornellSamplePrefixFormatter = new LabCodeFormatter("%LABACRONYM%-%OBJECTS%-%ELEMENT%");
	private final static LabCodeFormatter cornellElementPrefixFormatter = new LabCodeFormatter("%LABACRONYM%-%OBJECTS%");
	private final static LabCodeFormatter defaultLabCodeFormatter = immediateParentObjectLabCodeFormatter;

	
	public static LabCodeFormatter getDefaultFormatter() {
		
		String style = App.prefs.getPref(PrefKey.LABCODE_STYLE, null);
		
		if(style==null)
		{
			return defaultLabCodeFormatter;
		}
		
		try{
			LabCodeFormatter lcf = new LabCodeFormatter(style);
			if(lcf!=null) return lcf;
		} catch (Exception e)
		{
		}
		
		return defaultLabCodeFormatter;
	}
	
	public static LabCodeFormatter getSubSitesLabCodeFormatter(){
		return subsitesLabCodeFormatter;
	}
	
	public static LabCodeFormatter getCornellLabCodeFormatter(){
		return cornellLabCodeFormatter;	
	}
	
	public static LabCodeFormatter getimmediateParentObjectLabCodeFormatter(){
		return immediateParentObjectLabCodeFormatter;
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

	public static LabCodeFormatter getSubSitesFormatter(){
		return subsitesLabCodeFormatter;
	}
	
	public String getCodeFormat()
	{
		return this.codeFormat;
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
				
		replace(format, "%LABACRONYM%", App.prefs.getPref(PrefKey.LABCODE_STYLE, ""));

		// handle completely empty lab codes
		if(labCode.isEmptyCode()) {
			if((val = labCode.getSeriesCode()) != null)
				return val;
			else
				return unavailableValue;
		}
		
		if(objects.size() > 0) {
			StringBuffer objectList = new StringBuffer();
			

			for(String s : objects) {
				if(objectList.length() > 0)
					objectList.append(siteDelimiter);
				objectList.append(s);
			}
			
			replace(format, "%OBJECTS%", objectList.toString());
			replace(format, "%OBJECT%", objects.get(0));
			replace(format, "%BLOBJECT%", objects.get(objects.size()-1));
		}
		else {
			replace(format, "%OBJECTS%", unavailableValue);
			replace(format, "%OBJECT%", unavailableValue);
			replace(format, "%BLOBJECT%", unavailableValue);
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
