/**
 * 
 */
package edu.cornell.dendro.cpgdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import edu.cornell.dendro.corina_indexing.*;

/**
 * @author Lucas Madar
 *
 */
public class Indexer extends ReadingResultHolder implements Indexable {

	private int indexType;
	
	/**
	 * @param readingResults
	 * @param indexType the type of index. See the function getIndexFunction() for further information.
	 * @throws SQLException
	 */
	public Indexer(ResultSet readingResults, Integer indexType) throws SQLException {
		super(readingResults);
		
		if(indexType == null)
			this.indexType = 7; // exponential is the magic default.
		else
			this.indexType = indexType;
	}

	/**
	 * @see Indexable
	 */
	public List getData() {
		return reading;
	}

	@Override
	protected void operate() throws SQLException {
		IndexFunction func;
		
		func = getIndexFunction();
		func.index();
		
		List indexedData = func.getOutput();
		if(indexedData.size() != reading.size())
			throw new IllegalArgumentException("output and input are not the same size!");
		
		/*
		 * Traverse the indexed data and create our output.
		 */
		int len = indexedData.size();
		output = new ArrayList(len);
		for(int i = 0; i < len; i++) {
			double ind = ((Number) indexedData.get(i)).doubleValue();
			double raw = ((Number) reading.get(i)).doubleValue();
			double ratio = raw / ind;
			int val = (int) Math.round(ratio * 1000.0d);			
			
			output.add(val);
		}
	}
	
	private IndexFunction getIndexFunction() throws SQLException  {
		switch(indexType) {
		case 0:
			return new Horizontal(this);
			
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return new Polynomial(this, indexType); // the degree is coded into the index type. kludge! :D
			
		case 7:
			return new Exponential(this);
			
		case 8:
			return new Floating(this);
			
		case 9:
			return new HighPass(this);
			
		case 10:
			return new CubicSpline(this);
			
		default:
			throw new SQLException("Unsupported index function id " + indexType);
		}
	}
	
	/**
	 * This function actually does the SQL Insert work...
	 * Trust that our newVMeasurementID contains no ' chars?
	 */
	public void batchAddStatements(Statement s, String newVMeasurementResultID) throws SQLException {
		String header = "INSERT into tblVMeasurementReadingResult (VMeasurementResultID,RelYear,Reading) VALUES ('";
		int truncateLength = header.length();
		StringBuffer b = new StringBuffer(header);		
		
		int len = output.size();
		for(int i = 0; i < len; i++) {
			b.setLength(truncateLength);
			
			b.append(newVMeasurementResultID);
			b.append("',");
			b.append(((Number) relYear.get(i)).intValue());
			b.append(',');
			b.append(((Number) output.get(i)).intValue());
			b.append(")");
			s.addBatch(b.toString());
		}
	}

	public void batchAddStatements(PreparedStatement s, String newVMeasurementResultID) throws SQLException {
		int len = output.size();

		for(int i = 0; i < len; i++) {
			s.setString(1, newVMeasurementResultID);
			s.setInt(2, ((Number) relYear.get(i)).intValue());
			s.setInt(3, ((Number) output.get(i)).intValue());
		
			s.addBatch();
		}
	}
}
