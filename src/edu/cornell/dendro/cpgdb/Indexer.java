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
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cornell.dendro.corina_indexing.*;

/**
 * @author Lucas Madar
 *
 */
public class Indexer extends ReadingResultHolder implements Indexable {

	private final int indexType;
	
	/**
	 * @param readingResults
	 * @param indexType the type of index. See the function getIndexFunction() for further information.
	 * @throws SQLException
	 */
	public Indexer(ResultSet readingResults, Integer indexType) throws SQLException {
		super(readingResults);
		
		if(indexType == null)
			throw new NullPointerException("indexType must be specified as VMeasurementOpParam");
		
		this.indexType = indexType;
	}

	/**
	 * @see Indexable
	 */
	public List<? extends Number> getData() {
		return reading;
	}

	@Override
	public void operate() throws SQLException {
		IndexFunction func;
		
		func = getIndexFunction();
		func.index();
		
		List<? extends Number> indexedData = func.getOutput();
		if(indexedData.size() != reading.size())
			throw new IllegalArgumentException("output and input are not the same size!");
		
		/*
		 * Traverse the indexed data and create our output.
		 */
		int len = indexedData.size();
		List<Integer> output = new ArrayList<Integer>(len);
		for(int i = 0; i < len; i++) {
			double ind = ((Number) indexedData.get(i)).doubleValue();
			double raw = ((Number) reading.get(i)).doubleValue();
			double ratio = raw / ind;
			int val = (int) Math.round(ratio * 1000.0d);			
			
			output.add(val);
		}
		
		this.output = output;
	}
	
	private IndexFunction getIndexFunction() throws SQLException  {
		
		Logger logger = Logger.getAnonymousLogger();
		
		if(logger.isLoggable(Level.FINE)) {
			String msg = new ParamStringBuilder()
				.append("indexType", indexType)
				.toString();
			
			logger.fine("getIndexFunction(): " + msg );
		}
		
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
			b.append(relYear.get(i));
			b.append(',');
			b.append(output.get(i).intValue());
			b.append(")");
			s.addBatch(b.toString());
		}
	}

	public void batchAddStatements(PreparedStatement s, Object newVMeasurementResultID) throws SQLException {
		int len = output.size();

		for(int i = 0; i < len; i++) {
			s.setObject(1, newVMeasurementResultID);
			s.setInt(2, relYear.get(i));
			s.setInt(3, output.get(i).intValue());
		
			s.addBatch();
		}
	}
}
