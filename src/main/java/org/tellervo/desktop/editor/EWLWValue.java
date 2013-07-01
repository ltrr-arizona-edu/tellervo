package org.tellervo.desktop.editor;

public class EWLWValue {

	private Number ewvalue;
	private Number lwvalue;
	
	public EWLWValue(Number ew, Number lw)
	{
		this.ewvalue = ew;
		this.lwvalue = lw;
	}
	
	public EWLWValue(String value) throws NumberFormatException
	{
		String[] arr = ((String)value).split("/");
    	
    	if(arr.length==2)
    	{
	    		Number ew = Integer.parseInt(arr[0]);
	    		Number lw = Integer.parseInt(arr[1]);
	    		this.ewvalue = ew;
	    		this.lwvalue = lw;	
    	}
    	else
    	{
    		throw new NumberFormatException("String value provided does not split into early and late wood numbers");
    	}
	}
	
	public Number getEarlywoodValue()
	{
		return ewvalue;
	}
	
	public Number getLatewoodValue()
	{
		return lwvalue;
	}
	
	
	public String toString()
	{
		return ewvalue + "/"+lwvalue;
	}
}
