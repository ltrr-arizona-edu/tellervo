/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.manip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.schema.NormalTridasUnit;

// lame holder for a measurement to be listed in our table...
public class AMeasurement {	
	
	private final static Logger log = LoggerFactory.getLogger(AMeasurement.class);
	
	public AMeasurement(boolean enabled, Integer value, String source) {
		
		

		NormalTridasUnit displayUnits;
		
		try{
			displayUnits = NormalTridasUnit.valueOf(App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.value().toString()));
		} catch (Exception e)
		{
			displayUnits = NormalTridasUnit.MICROMETRES;
		}

		
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
			else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
			{
				this.value = value/20;
			}
			else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
			{
				this.value = value/50;
			}
			else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
			{
				this.value = value/100;
			}
			else
			{
				log.error("Unsupported display units");
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
