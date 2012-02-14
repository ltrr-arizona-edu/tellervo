package edu.cornell.dendro.corina.editor;

public class EWLWValue {

	private Number ewvalue;
	private Number lwvalue;
	
	public EWLWValue(Number ew, Number lw)
	{
		this.ewvalue = ew;
		this.lwvalue = lw;
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
