package org.tellervo.desktop.bulkdataentry.model;

import org.tridas.schema.TridasElement;

public class TridasElementOrPlaceholder {

	
	private TridasElement elem;
	private String code;
	
	public TridasElementOrPlaceholder(TridasElement elem)
	{
		this.elem = elem;
	}
	
	public TridasElementOrPlaceholder(String code)
	{
		this.code = code;
	}
	
	
	public TridasElement getTridasElement()
	{
		return elem;
	}
	
	public String getCode()
	{
		if(elem!=null)
		{
			
			return elem.getTitle();
			
		}
		else
		{
			return code;
		}
	}
	
	public String toString()
	{
		return getCode();
	}
	
}
