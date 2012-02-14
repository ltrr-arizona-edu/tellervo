/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.cpgdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for loading in a VMeasurementReadingResult.
 * It's intended to be used for indexing, but I've made a base class
 * in case anyone needs to extend it for other datatypes in the future.
 * 
 * It is to be used with the qacqVMeasurementReadingResult query (acq = acquire)
 * 
 * @author Lucas Madar
 */
public class ReadingResultHolder {
	protected List<Integer> relYear;
	protected List<Integer> reading;
	protected List<? extends Number> output;
	
	public ReadingResultHolder(ResultSet readingResults) throws SQLException {
		relYear = new ArrayList<Integer>();
		reading = new ArrayList<Integer>();
			
		populate(readingResults);
	}
	
	private void populate(ResultSet res) throws SQLException {
		relYear.clear();
		reading.clear();
		
		while(res.next()) {
			relYear.add(res.getInt(1));
			reading.add(res.getInt(2));
		}
	}
	
	public void operate() throws SQLException {
		// do something useful?
		output = reading;
	}	
}
