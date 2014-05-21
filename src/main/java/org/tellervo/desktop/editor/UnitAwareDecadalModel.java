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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.SafeIntYear;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.DatingSuffix;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.NormalTridasUnit;


public class UnitAwareDecadalModel extends DecadalModel {

	private final static Logger log = LoggerFactory.getLogger(UnitAwareDecadalModel.class);

	private static final long serialVersionUID = 1L;
	private NormalTridasUnit displayUnits = NormalTridasUnit.MICROMETRES;
	private DatingSuffix datingSuffix = DatingSuffix.AD;
	private ITridasSeries series;
	
	public UnitAwareDecadalModel() {
		setDisplayUnits(null);
		setDatingSuffix(null);
		series = s.getSeries();

	}

	public UnitAwareDecadalModel(Sample s) {
		super(s);
		setDisplayUnits(null);
		series = s.getSeries();
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
	
	
	public void setDatingSuffix(DatingSuffix ds)
	{
		if(ds==null)
		{
			try{
				String strds = App.prefs.getPref(PrefKey.DISPLAY_DATING_SUFFIX, DatingSuffix.AD.name().toString());
				ds = TridasUtils.getDatingSuffixFromName(strds);
			} catch (Exception e)
			{
				ds = DatingSuffix.AD;
			}
		}

		datingSuffix = ds;	
		this.fireTableDataChanged();
	}
	
	public DatingSuffix getDisplayDatingSuffix()
	{
		return datingSuffix;
	}
	
	public Object getValueAt(int row, int col) 
	{
		
		Object obj = super.getValueAt(row, col);
		
		if(obj==null) {
			return obj;
		}
			
		if(col==0)
		{
			NormalTridasDatingType datingtype = null;

			try{
				datingtype = s.getSeries().getInterpretation().getDating().getType();
			} catch (Exception e)
			{
				
			}

			SafeIntYear y = new SafeIntYear(obj.toString());
			return y.formattedYear(datingtype, datingSuffix);


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
				
				Integer ewval = null; 
				Integer lwval = null;
				
				try{
					ewval = ((EWLWValue) obj).getEarlywoodValue().intValue();
					lwval = ((EWLWValue) obj).getLatewoodValue().intValue();
				} catch (Exception e)
				{
					return null;
				}
				
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
			log.debug("UnitAwareDecadalModel.setValueAt() called with null value");
			return;
		}
		
		log.debug("UnitAwareDecadalModel.setValueAt() called for a value of type "+value.getClass());
		log.debug("Value was: "+value.toString());
		
		if (col==0 || col==11)
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
				if(value instanceof String)
				{
					try{
						EWLWValue ewlw = new EWLWValue((String)value);
					
						super.setValueAt(ewlw, row, col);
						return;
					} catch (NumberFormatException ex){};
						
				}
				
				// Not a number so forget about units and just use the super class
				super.setValueAt(value, row, col);
				return;
			}
			
			if(val!=null) 
			{
				val = convertToMicrons(displayUnits, val);
				super.setValueAt(val, row, col);

			}
			else
			{
				super.setValueAt(value, row, col);
			}
		}
		
	}
	
	public static Number convertToMicrons(NormalTridasUnit displayUnits, Number val)
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
		
		return val;
	}
}
