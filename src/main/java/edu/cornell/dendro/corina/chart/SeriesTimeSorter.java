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
package edu.cornell.dendro.corina.chart;

import java.util.Comparator;

import org.tridas.interfaces.ITridasSeries;

public class SeriesTimeSorter implements Comparator<ITridasSeries>{

	public Boolean undatedFirst = true;

	@Override
	public int compare(ITridasSeries ser1, ITridasSeries ser2) {
				
		Integer ser1firstyear = null;
		Integer ser1lastyear = null;
		Integer ser2firstyear = null;
		Integer ser2lastyear = null;
		
		
		if(ser1.isSetInterpretation())
		{
			if(ser1.getInterpretation().isSetFirstYear())
			{
				ser1firstyear = ser1.getInterpretation().getFirstYear().getValue();
			}
			if(ser1.getInterpretation().isSetLastYear())
			{
				ser1lastyear = ser1.getInterpretation().getLastYear().getValue();
			}
		}
		
		if(ser2.isSetInterpretation())
		{
			if(ser2.getInterpretation().isSetFirstYear())
			{
				ser2firstyear = ser2.getInterpretation().getFirstYear().getValue();
			}
			if(ser2.getInterpretation().isSetLastYear())
			{
				ser2lastyear = ser2.getInterpretation().getLastYear().getValue();
			}
		}
		
		if(ser1firstyear==null || ser1lastyear==null)
		{
			if (undatedFirst==true) return 1;
			else return 0;
		}
		
		if(ser2firstyear==null || ser2lastyear==null)
		{
			if (undatedFirst==true) return 0;
			else return 1;
		}
		
		if(ser1lastyear!=ser2lastyear)
		{
			return ser2lastyear.compareTo(ser1lastyear);
		}
		else
		{
			return ser2firstyear.compareTo(ser1firstyear);
		}
		
		
	}
}

