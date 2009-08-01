/**
 * 
 */
package edu.cornell.dendro.cpgdb;

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
		
		operate();
	}
	
	private void populate(ResultSet res) throws SQLException {
		relYear.clear();
		reading.clear();
		
		while(res.next()) {
			relYear.add(res.getInt(1));
			reading.add(res.getInt(2));
		}
	}
	
	protected void operate() throws SQLException {
		// do something useful?
		output = reading;
	}	
}
