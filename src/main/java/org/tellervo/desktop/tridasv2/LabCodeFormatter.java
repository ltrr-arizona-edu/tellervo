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

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;


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
	
	public static String[] standardStyles = {"%OBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%",
			"%OBJECTS%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%", 
			"%BLOBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%",
			"%LABACRONYM%-%OBJECT%-%ELEMENT%-%SAMPLE%-%RADIUS%-%SERIES%"
			};

	private final static LabCodeFormatter defaultLabCodeFormatter = new LabCodeFormatter(standardStyles[0]);

	
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
	

	public static String getDefaultFormattedLabCode(TridasObject object,
			TridasElement element, TridasSample sample, 
			TridasRadius radius, ITridasSeries series) {
		
		ArrayList<TridasObject> objects = new ArrayList<TridasObject>();
		objects.add(object);
		
		return getDefaultFormattedLabCode(objects, element, sample, radius, series);
		
	}
	
	
	public static String getDefaultFormattedLabCode(List<TridasObject> objects,
			TridasElement element, TridasSample sample, 
			TridasRadius radius, ITridasSeries series) {

		
		LabCodeFormatter lf = LabCodeFormatter.getDefaultFormatter();
		LabCode lc = new LabCode();
		
		// Limit code based on depth given
		if(series==null)  lf = LabCodeFormatter.getSeriesPrefixFormatter();
		if(radius==null)  lf = LabCodeFormatter.getRadiusPrefixFormatter();
		if(sample==null)  lf = LabCodeFormatter.getSamplePrefixFormatter();
		if(element==null) lf = LabCodeFormatter.getElementPrefixFormatter();		
		
		if(objects==null || objects.size()==0)
		{
			return null;
		}
		else
		{
			for(TridasObject o : objects)
			{
				lc.appendSiteCode(TridasUtils.getGenericFieldValueByName(o,  TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE));
				lc.appendSiteTitle(o.getTitle());
			}
		}
		
		if(element!=null)
		{
			lc.setElementCode(element.getTitle());
		}
		
		if(sample!=null)
		{
			lc.setSampleCode(sample.getTitle());
		}
		
		if(radius!=null)
		{
			lc.setRadiusCode(radius.getTitle());
		}
		
		if(series!=null)
		{
			lc.setSeriesCode(series.getTitle());
		}
		
		return lf.format(lc);
		
		
	}
	
	
	public static LabCodeFormatter getSeriesPrefixFormatter(){
		String format = getDefaultFormatter().codeFormat.replace("-%SERIES%", "");
		return new LabCodeFormatter(format);
	}
	
	public static LabCodeFormatter getRadiusPrefixFormatter(){
		String format = getDefaultFormatter().codeFormat.replace("-%SERIES%", "");
		format = format.replace("-%RADIUS%", "");
		return new LabCodeFormatter(format);
	}
	
	public static LabCodeFormatter getSamplePrefixFormatter(){
		String format = getDefaultFormatter().codeFormat.replace("-%SERIES%", "");
		format = format.replace("-%RADIUS%", "");
		format = format.replace("-%SAMPLE%", "");
		return new LabCodeFormatter(format);
	}
	
	public static LabCodeFormatter getElementPrefixFormatter(){
		String format = getDefaultFormatter().codeFormat.replace("-%SERIES%", "");
		format = format.replace("-%RADIUS%", "");
		format = format.replace("-%SAMPLE%", "");
		format = format.replace("-%ELEMENT%", "");
		return new LabCodeFormatter(format);
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
				
	
		replace(format, "%LABACRONYM%", App.getLabAcronym());

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
