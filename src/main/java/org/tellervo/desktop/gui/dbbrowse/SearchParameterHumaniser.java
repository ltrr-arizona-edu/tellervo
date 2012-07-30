package org.tellervo.desktop.gui.dbbrowse;

import java.util.ArrayList;
import java.util.Arrays;

import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;

public class SearchParameterHumaniser {

	public static String getHumanisedName(SearchParameterName name)
	{
		if(name==null) return "---";
		
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
		else if (name.equals(SearchParameterName.ELEMENTHEIGHT))
			return "Dimension of element - Height";
		else if (name.equals(SearchParameterName.ELEMENTDIAMETER))
			return "Dimension of element - Diameter";
		else if (name.equals(SearchParameterName.ELEMENTWIDTH))
			return "Dimension of element - Width";		
		else if (name.equals(SearchParameterName.ELEMENTDEPTH))
			return "Dimension of element - Depth";
		else if (name.equals(SearchParameterName.ELEMENTDESCRIPTION))
			return "Element description";
		else if (name.equals(SearchParameterName.ELEMENTGENUSNAME))
			return "Taxonomy - Genus";
		else if (name.equals(SearchParameterName.ELEMENTFAMILYNAME))
			return "Taxonomy - Family";
		
		
		return name.name();
	}
	
	
	
	public static Class getDataType(SearchParameterName name)
	{
		if(name==null) return null;
		
		if       (name.equals(SearchParameterName.ANYPARENTOBJECTCODE))
			return String.class;
		else if (name.equals(SearchParameterName.ELEMENTCODE))
			return String.class;
		else if (name.equals(SearchParameterName.SAMPLECODE))
			return String.class;
		else if (name.equals(SearchParameterName.RADIUSCODE))
			return String.class;
		else if (name.equals(SearchParameterName.SERIESCODE))
			return String.class;
		else if (name.equals(SearchParameterName.RADIUSNUMBERSAPWOODRINGS))
			return Integer.class;
		else if (name.equals(SearchParameterName.ELEMENTHEIGHT))
			return Integer.class;
		else if (name.equals(SearchParameterName.ELEMENTDIAMETER))
			return Integer.class;
		else if (name.equals(SearchParameterName.ELEMENTWIDTH))
			return Integer.class;	
		else if (name.equals(SearchParameterName.ELEMENTDEPTH))
			return Integer.class;
		else if (name.equals(SearchParameterName.ELEMENTDESCRIPTION))
			return String.class;
		else if (name.equals(SearchParameterName.ELEMENTGENUSNAME))
			return String.class;
		else if (name.equals(SearchParameterName.ELEMENTFAMILYNAME))
			return String.class;
		
		
		
		
		return String.class;
		
	}
	

	
	public static ArrayList<SearchOperator> getValuesParameterType(SearchParameterName param)
	{
		if(param==null) return null;
		
		Class clazz = getDataType(param);
		ArrayList<SearchOperator> vals = new ArrayList<SearchOperator>();
		
		vals.add(SearchOperator.EQUALS);
		vals.add(SearchOperator.NOT_EQUALS);
		
		if(clazz.equals(String.class))
		{
			vals.add(SearchOperator.LIKE);
		}
		if(clazz.equals(Integer.class))
		{
			vals.add(SearchOperator.GREATER_THAN);
			vals.add(SearchOperator.LESS_THAN);
		}
		
		return vals;
		
	}
}
