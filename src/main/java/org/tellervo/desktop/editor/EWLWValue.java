package org.tellervo.desktop.editor;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasUnit;

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
		
    	NormalTridasUnit displayUnits;
    	try{
			String strunit = App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.name().toString());
			displayUnits = TridasUtils.getUnitFromName(strunit);
		} catch (Exception e)
		{
			displayUnits = NormalTridasUnit.MICROMETRES;
		}
    	
		
		String[] arr = ((String)value).split("/");
    	
    	if(arr.length==2)
    	{
	    		Number ew = Integer.parseInt(arr[0]);
	    		Number lw = Integer.parseInt(arr[1]);
	    		this.ewvalue = UnitAwareDecadalModel.convertToMicrons(displayUnits, ew);
	    		this.lwvalue = UnitAwareDecadalModel.convertToMicrons(displayUnits, lw);
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
