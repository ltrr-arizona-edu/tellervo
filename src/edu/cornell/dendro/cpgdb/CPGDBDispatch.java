package edu.cornell.dendro.cpgdb;

import java.sql.SQLException;

public class CPGDBDispatch {

	public static String GetVMeasurementResult(String VMeasurementID) throws SQLException {
		// Simple and clean. Pass in the ID, return the result string.
		VMeasurementResult result = new VMeasurementResult(VMeasurementID);
		
		return result.getResult();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Moo!!!");
			GetVMeasurementResult("1");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
