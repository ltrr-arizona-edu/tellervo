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
package org.tellervo.desktop.editor;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasUnit;


public class UnitAwareDecadalModel extends DecadalModel {


	private static final long serialVersionUID = 1L;
	private NormalTridasUnit displayUnits = NormalTridasUnit.MICROMETRES;
	
	public UnitAwareDecadalModel() {
		setDisplayUnits(null);
	}

	public UnitAwareDecadalModel(Sample s) {
		super(s);
		setDisplayUnits(null);
	}

	/**
	 * Set the display units.  If unit=null then the units from
	 * the preferences will be used defaulting to microns if 
	 * no preference is set.
	 * 
	 * @param unit
	 */
	public void setDisplayUnits(NormalTridasUnit unit)
	{
		if(unit==null)
		{
			try{
				String strunit = App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.name().toString());
				unit = TridasUtils.getUnitFromName(strunit);
			} catch (Exception e)
			{
				unit = NormalTridasUnit.MICROMETRES;
			}
		}

		displayUnits = unit;	
		this.fireTableDataChanged();
	}
	
	public NormalTridasUnit getDisplayUnits()
	{
		return displayUnits;
	}
	
	public Object getValueAt(int row, int col) 
	{
		
		Object obj = super.getValueAt(row, col);
		
		if(obj==null) {
			return obj;
		}
				
		if (col==0 || col==11)
		{
			// Year or count column so just return
			return obj;
		}
		else
		{
			// HANDLE ALL PLAIN NUMBERS
			
			if(obj instanceof Number)
			{
				Number val = (((Number) (obj)).intValue());
				
				if(val!=null) 
				{
					if (s.getTridasUnits()==null)
					{
						// No units so leave values as they are
					}
					else if (displayUnits.equals(NormalTridasUnit.MILLIMETRES))
					{
						val = val.doubleValue() / 1000;
					}
					else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
					{
						val = val.intValue() / 100;
					}
					else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
					{
						val = val.intValue() / 50;
					}
					else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
					{
						val = val.intValue() / 20;
					}
					else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
					{
						val = val.intValue() / 10;
					}
					else if (displayUnits.equals(NormalTridasUnit.MICROMETRES))
					{
						// do nothing as microns is the internal default units
					}
					else
					{
						System.out.println("Unsupported display units. Ignoring and defaulting to microns");
					}
					return val;
	
				}
				else
				{
					return obj;
				}
			}
			
			// HANDLE EWLW VALUES
			
			else if (obj instanceof EWLWValue)
			{
				Integer ewval = ((EWLWValue) obj).getEarlywoodValue().intValue();
				Integer lwval = ((EWLWValue) obj).getLatewoodValue().intValue();
				String strval = "";

				if (s.getTridasUnits()==null)
				{
					// No units so leave values as they are
					return obj.toString();
				}
				
				if(ewval!=null) 
				{		
					if (s.getTridasUnits()==null)
					{
						// No units so leave values as they are
					}
					else if (displayUnits.equals(NormalTridasUnit.MILLIMETRES))
					{
						strval+= ewval / 1000;
					}
					else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
					{
						strval+= ewval / 100;
					}
					else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
					{
						strval+= ewval / 50;
					}
					else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
					{
						strval+= ewval / 20;
					}
					else if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
					{
						strval+= ewval / 10;
					}
					else if (displayUnits.equals(NormalTridasUnit.MICROMETRES))
					{
						// do nothing as microns is the internal default units
						strval+= ewval;
					}
					else
					{
						System.out.println("Unsupported display units. Ignoring and defaulting to microns");
						strval+= ewval;
					}
					
				}
				
				strval+="/";
					
				if(lwval!=null) 
				{	
					if (s.getTridasUnits()==null)
					{
						// No units so leave values as they are
					}
					else if (displayUnits.equals(NormalTridasUnit.MILLIMETRES))
					{
						strval+= lwval / 1000;
					}
					else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
					{
						strval+= lwval / 100;
					}
					else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
					{
						strval+= lwval / 50;
					}
					else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
					{
						strval+= lwval / 20;
					}
					if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
					{
						strval+= lwval / 10;
					}
					else if (displayUnits.equals(NormalTridasUnit.MICROMETRES))
					{
						// do nothing as microns is the internal default units
						strval+= lwval;
					}
					else
					{
						System.out.println("Unsupported display units. Ignoring and defaulting to microns");
						strval+= lwval;
					}					
				}
				
				return strval;

			}
		}
		return null;
	}
	
	public void setValueAt(Object value, int row, int col) {
	
		if (value==null)
		{
			// Ignore null values
			return;
		}
		
		else if (col==0 || col==11)
		{
			// First and last columns don't need to be unit handled
			super.setValueAt(value, row, col);
		}
		
		else
		{
			Number val = null;
			try{
				
				val = (Number) value;
			} catch (Exception e)
			{
				// Not a number so forget about units and just use the super class
				super.setValueAt(value, row, col);
			}
			
			if(val!=null) 
			{
				if (displayUnits.equals(NormalTridasUnit.MILLIMETRES))
				{
					val = val.doubleValue() * 1000;
				}
				else if (displayUnits.equals(NormalTridasUnit.TENTH_MM))
				{
					val = val.doubleValue() * 100;
				}
				else if (displayUnits.equals(NormalTridasUnit.TWENTIETH_MM))
				{
					val = val.doubleValue() * 50;
				}
				else if (displayUnits.equals(NormalTridasUnit.FIFTIETH_MM))
				{
					val = val.doubleValue() * 20;
				}
				
				if (displayUnits.equals(NormalTridasUnit.HUNDREDTH_MM))
				{
					val = val.doubleValue() * 10;
				}
				else if (displayUnits.equals(NormalTridasUnit.MICROMETRES))
				{
					// do nothing as microns is the internal default units
				}
				else
				{
					System.out.println("Unsupported display units. Ignoring and defaulting to microns");
				}
				super.setValueAt(val, row, col);

			}
			else
			{
				super.setValueAt(value, row, col);
			}
		}
		
	}
	
}
