package org.tellervo.desktop.gui.dbbrowse;

import org.tellervo.schema.SearchParameterName;

public class SearchParameterHumaniser {

	public static String getHumanisedName(SearchParameterName name)
	{
		
		if       (name.equals(SearchParameterName.ANYPARENTOBJECTCODE))
			return "Object code";
		else if (name.equals(SearchParameterName.ELEMENTCODE))
			return "Element code";
		else if (name.equals(SearchParameterName.SAMPLECODE))
			return "Sample code";
		else if (name.equals(SearchParameterName.RADIUSCODE))
			return "Radius code";
		else if (name.equals(SearchParameterName.SERIESCODE))
			return "Series code";
		else if (name.equals(SearchParameterName.RADIUSNUMBERSAPWOODRINGS))
			return "Number of sapwood rings";
		
		
		return name.name();
	}
	
	
	
	public static Class getDataType(SearchParameterName name)
	{
		return String.class;
	}
	

}
