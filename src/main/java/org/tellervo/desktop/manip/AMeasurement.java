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

import org.tellervo.desktop.core.App;
import org.tridas.schema.NormalTridasUnit;

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
