package edu.cornell.dendro.cpgdb;

import java.sql.*;

public class VMeasurementResult {
	// Internal hard values
	private static final int BASE_CASE = 1;
	private static final int RECURSIVE_CASE = 2;
	
	// This string holds our result, which is a UUID returned by the DB
	private String result;
	
	// we keep this instantiated for easy access to the db
	private DBQuery dbq = new DBQuery();
	
	public VMeasurementResult(String VMeasurementID) throws SQLException {
		GetVMeasurementResult(VMeasurementID, (String)null, (String)null, 0);
	}
	
	private void GetVMeasurementResult(String VMeasurementID, String VMeasurementResultGroupID,	
			String VMeasurementResultMasterID, int recursionDepth) throws SQLException {
		
		// break out of the whole mess if we have an infinite loop
		if(recursionDepth > 50)
			throw new SQLException("VMeasurementResult: Infinite recursion detected!");
		
		if(VMeasurementResultMasterID == null) {
			// TODO: Do we create a GUID string here? In Kit's VB code, we do
			VMeasurementResultMasterID = new String("ABCDEF-123456-A1B2C3");
		}
			
		ResultSet rst = dbq.query("qryVMeasurementType", VMeasurementID);
		if(!rst.next())
			throw new SQLException("VMeasurementResult: VMeasurementID " + VMeasurementID + "not found.");
		
		
		
	}
	
	public String getResult() { return result; }
}
