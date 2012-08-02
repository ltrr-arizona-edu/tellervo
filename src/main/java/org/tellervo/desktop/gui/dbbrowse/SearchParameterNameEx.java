package org.tellervo.desktop.gui.dbbrowse;

import java.util.ArrayList;

import org.tellervo.desktop.ui.I18n;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;

public class SearchParameterNameEx implements Comparable<SearchParameterNameEx>{

	private final SearchParameterName name;
	
	public SearchParameterNameEx(SearchParameterName n)
	{
		name = n;
	}
	
	public String toString()
	{
		if(name==null) return "---";
		
		String value = name.name();
		try{
			String key = "tridas.searchparametername."+name.name();
			value = I18n.getText(key);
			if(value.equals(key)) value = name.name();
		} catch (Exception e){}
		
		return value;
	}
	
	public SearchParameterName getSearchParameterName()
	{
		return name;
	}

	public Class getDataType()
	{
		if(name==null) return null;
		
		if ((name.equals(SearchParameterName.RADIUSNUMBERSAPWOODRINGS)) || 
		    (name.equals(SearchParameterName.ELEMENTHEIGHT)) || 
		    (name.equals(SearchParameterName.ELEMENTDIAMETER)) ||
		    (name.equals(SearchParameterName.ELEMENTDIAMETER)) ||
		    (name.equals(SearchParameterName.ELEMENTWIDTH)) ||
		    (name.equals(SearchParameterName.ELEMENTDEPTH)) ||
		    (name.equals(SearchParameterName.SERIESFIRSTYEAR)) ||
		    (name.equals(SearchParameterName.SERIESVALUECOUNT))
		   )
			return Integer.class;
		
		// Default to string
		return String.class;
		
	}
	
	public ArrayList<SearchOperatorEx> getPossibleOperators()
	{
		if(name==null) return null;
		
		Class clazz = getDataType();
		ArrayList<SearchOperatorEx> vals = new ArrayList<SearchOperatorEx>();
		
		vals.add(new SearchOperatorEx(SearchOperator.EQUALS));
		vals.add(new SearchOperatorEx(SearchOperator.NOT_EQUALS));
		
		if(clazz.equals(String.class))
		{
			vals.add(new SearchOperatorEx(SearchOperator.LIKE));
		}
		if(clazz.equals(Integer.class))
		{
			vals.add(new SearchOperatorEx(SearchOperator.GREATER_THAN));
			vals.add(new SearchOperatorEx(SearchOperator.LESS_THAN));
		}
		
		return vals;
		
	}
	
	
	@Override
	public int compareTo(SearchParameterNameEx o) {
		
		String s1 = this.toString();
		String s2 = o.toString();
		
		return s1.compareTo(s2);
	}
}
