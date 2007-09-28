package edu.cornell.dendro.cpgdb;

import java.sql.SQLException;

public class Dispatch {

	public static String GetVMeasurementResult(int VMeasurementID) throws SQLException {
		// Simple and clean. Pass in the ID, return the result string.
		VMeasurementResult result = new VMeasurementResult(VMeasurementID);
		
		return result.getResult();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("PG DB TEST!");
			String result = GetVMeasurementResult(6);
			System.out.println("Result ID: " + result);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
