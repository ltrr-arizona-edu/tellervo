package org.tellervo.desktop.gui.dbbrowse;

import org.tellervo.schema.SearchOperator;

public class SearchOperatorEx {

	private final SearchOperator op;
	
	public SearchOperatorEx(SearchOperator op)
	{
		this.op = op;
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
}
