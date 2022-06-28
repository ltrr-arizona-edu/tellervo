package org.tellervo.desktop.gui.dbbrowse;

import java.util.ArrayList;

import org.tellervo.schema.SearchOperator;

public class SearchOperatorEx implements Comparable<SearchOperatorEx>{

	private final SearchOperator op;
	
	public SearchOperatorEx(SearchOperator op)
	{
		this.op = op;
	}
	
	public static ArrayList<SearchOperatorEx> getPossibleOperators()
	{
		ArrayList<SearchOperatorEx> vals = new ArrayList<SearchOperatorEx>();
		vals.add(new SearchOperatorEx(SearchOperator.EQUALS));
		vals.add(new SearchOperatorEx(SearchOperator.NOT_EQUALS));
		vals.add(new SearchOperatorEx(SearchOperator.GREATER_THAN));
		vals.add(new SearchOperatorEx(SearchOperator.LESS_THAN));
		vals.add(new SearchOperatorEx(SearchOperator.LIKE));
		return vals;
	}
	
	public String toString()
	{
		if(op==null) return null;
		
		if(op.equals(SearchOperator.EQUALS))
		{
			return "=";
		}
		else if(op.equals(SearchOperator.GREATER_THAN))
		{
			return ">";
		}
		else if(op.equals(SearchOperator.IS))
		{
			return "is";
		}
		else if(op.equals(SearchOperator.LESS_THAN))
		{
			return "<";
		}
		else if(op.equals(SearchOperator.LIKE))
		{
			return "contains";
		}
		else if(op.equals(SearchOperator.NOT_EQUALS))
		{
			return "!=";
		}
		
		return op.toString();
		
	}
	
	public SearchOperator getSearchOperator()
	{
		return op;
	}

	@Override
	public int compareTo(SearchOperatorEx o) {
		String s1 = this.op.value();
		String s2 = o.op.value();
		
		return s1.compareTo(s2);
	}
}
