package edu.cornell.dendro.corina.manip;

import org.tridas.schema.NormalTridasUnit;
import edu.cornell.dendro.corina.core.App;

// lame holder for a measurement to be listed in our table...
public class AMeasurement {	
	
	
	
	public AMeasurement(boolean enabled, Integer value, String source) {
	
		NormalTridasUnit displayUnits = NormalTridasUnit.valueOf(App.prefs.getPref("corina.displayunits", NormalTridasUnit.HUNDREDTH_MM.value().toString()));

		
		if(!source.equals("manual"))
		{
			if(displayUnits.equals(NormalTridasUnit.MICROMETRES))
			{
				this.value = value;
			}
			else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
			{
				this.value = value/10;
			}
			else
			{
				this.value = null;
			}
		}
		else
		{
			this.value = value;
		}
		

		this.enabled = enabled;
		this.source = source;
	}

	public boolean enabled;
	public Integer value;
	public String source;
}
