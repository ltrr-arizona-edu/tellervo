package edu.cornell.dendro.cpgdb;

import java.sql.*;

public class VMeasurementResult {
	// Internal values
	private enum VMeasurementOperation { DIRECT, INDEX, CLEAN, REDATE, SUM }
		
	// This string holds our result, which is a UUID returned by the DB
	private String result;
	
	// we keep this instantiated for easy access to the db
	private DBQuery dbq = new DBQuery();
	
	public VMeasurementResult(int VMeasurementID) throws SQLException {
		result = getVMeasurementResult(VMeasurementID, (String)null, (String)null, 0);
	}
	
	private String getVMeasurementResult(int VMeasurementID, String VMeasurementResultGroupID,	
			String VMeasurementResultMasterID, int recursionDepth) throws SQLException {
		
		ResultSet res;
		VMeasurementOperation op;
		int VMeasurementOpParameter;
				
		// break out of the whole mess if we have an infinite loop
		if(recursionDepth > 50)
			throw new SQLException("VMeasurementResult: Infinite recursion detected!");
		
		if(VMeasurementResultMasterID == null)
			VMeasurementResultMasterID = dbq.createUUID();
		
		// Figure out what kind of VMeasurement we have to deal with
		res = dbq.query("qryVMeasurementType", VMeasurementID);
		if(res.next()) {
			op = getOp(res.getString("Op"));
			int MeasurementID = res.getInt("MeasurementID");
			
			int VMeasurementsInGroup = res.getInt("VMeasurementsInGroup");
			// If VMeasurementOpParameter is NULL, getInt turns NULL to 0
			// This is what Kit is doing in his code, but we don't need an extra sanity check.
			VMeasurementOpParameter = res.getInt("VMeasurementOpParameter");
			
			if(op == VMeasurementOperation.DIRECT && VMeasurementsInGroup == 0 && MeasurementID != 0) {
				// Ah, the clean base case. Just drop out nicely, after we clean up.
				res.close();
				
				return doDirectCase(VMeasurementID, VMeasurementResultGroupID, VMeasurementResultMasterID, MeasurementID);
			}
			// These are now just sanity checks. If our base case failed, and it doesn't match any of these...
			else if(!(
					(op == VMeasurementOperation.INDEX && VMeasurementsInGroup == 1) ||
					(op == VMeasurementOperation.CLEAN && VMeasurementsInGroup == 1) ||
					(op == VMeasurementOperation.REDATE && VMeasurementsInGroup == 1) ||
					(op == VMeasurementOperation.SUM && VMeasurementsInGroup > 1)
				   )) 
				   throw new SQLException("Malformed VMeasurement (id:" + VMeasurementID + ")");
		}
		else
			throw new SQLException("VMeasurementResult: VMeasurementID " + VMeasurementID + " not found.");
		
		// dispose of the result set.
		res.close();
		
		/*
		 * The recursive case.
		 */

		String newVMeasurementResultID = null;
		String newVMeasurementResultGroupID = null;
		String lastWorkingVMeasurementResultID = null;
		/*
		 * The lastWorkingVMeasurementResultID (CurrentVMeasurementResultID in
		 * Kit's code) is the last VMeasurementResult returned by our recursive
		 * function. It's only meaningful for things that have less than one
		 * member (which, at this point, is everything that's not a sum)
		 */


		switch (op) {
		case SUM:
			/*
			 * For a sum, all the VMeasurementResults generated in this instance
			 * and all direct children will share the same Group ID. Once we get
			 * back here after the recursion, we sum up everything with this
			 * group ID.
			 */
			if (VMeasurementResultGroupID == null)
				newVMeasurementResultGroupID = dbq.createUUID();
			else
				newVMeasurementResultGroupID = VMeasurementResultGroupID;

			break;

		case CLEAN:
		case REDATE:
		case INDEX:
			/*
			 * For everything else, we make a new Group ID. If we're a redated
			 * sample underneath a sum, this is so we don't include the original
			 * sample in the sum as well.
			 */
			newVMeasurementResultGroupID = dbq.createUUID();

			break;

		default:
			// this should be unreachable.
			break;
		}

		// Get the members of each VMeasurement
		res = dbq.query("qryVMeasurementMembers", VMeasurementID);

		while (res.next()) {
			lastWorkingVMeasurementResultID = getVMeasurementResult(res.getInt("MemberVMeasurementID"),
					newVMeasurementResultGroupID, VMeasurementResultMasterID,
					recursionDepth + 1);
		}
		res.close();
		
		/*
		 * Now, we have to perform whatever evil operation we were intending to do.
		 */
		
		switch(op) {
		case SUM:
		case INDEX:
			// TODO: Implement these. :)
			throw new SQLException("Sorry, these functions aren't implemented yet. :'(");
			
		case CLEAN:
			/*
			 * In the clean case, no changes are made to the measurement results themselves.
			 * All we are doing is "cutting off" any information below us.
			 */
			newVMeasurementResultID = lastWorkingVMeasurementResultID;
			break;
			
		case REDATE:
			/* 
			 * "As we are updating a record, not appending a new one, use the current ID as the new one."
			 */
			newVMeasurementResultID = lastWorkingVMeasurementResultID;
			dbq.execute("qupdVMeasurementResultOpRedate", VMeasurementID, VMeasurementOpParameter, lastWorkingVMeasurementResultID);
			break;
		}
		
		/*
		 * Now, we clean the data for explicit cases (CLEAN)
		 * and implicit cases (REDATE and INDEX).
		 * 
		 * TODO: Someone document what this means, it makes my head hurt.
		 */
		
		switch(op) {
		case INDEX:
		case REDATE:
		case CLEAN:
			// Clear away the group ID and change it to our parent?
			dbq.execute("qupdVMeasurementResultClearGroupID", newVMeasurementResultGroupID);
			dbq.execute("qupdVMeasurementResultAttachGroupID", VMeasurementResultGroupID, lastWorkingVMeasurementResultID);
			break;
		}
		
		if(recursionDepth == 0) {
			// TODO: Warn about duplicate direct VMeasurements!

			// remove all our child results...
			dbq.execute("qdelVMeasurementResultRemoveMasterID", VMeasurementResultMasterID, newVMeasurementResultID);
		}
		
		return newVMeasurementResultID;
	}
		
	private VMeasurementOperation getOp(String strOp) throws SQLException {
		if(strOp.equals("Direct"))
			return VMeasurementOperation.DIRECT;
		if(strOp.equals("Index"))
			return VMeasurementOperation.INDEX;
		if(strOp.equals("Sum"))
			return VMeasurementOperation.SUM;
		if(strOp.equals("Redate"))
			return VMeasurementOperation.REDATE;
		if(strOp.equals("Clean"))
			return VMeasurementOperation.CLEAN;
		
		throw new SQLException("Invalid VMeasurement Operation: " + strOp);
	}

	/*
	 * The base case is easy!
	 * 
	 * VmeasurementID identifies a VMeasurement
	 * Copy measurement into tblVMeasurementResult and tblVMeasurementReadingResult
	 * Place the result (newVMeasurementResultID) in the result class variable.
	 */
	private String doDirectCase(int VMeasurementID, String VMeasurementResultGroupID, 
			String VMeasurementResultMasterID, int MeasurementID) throws SQLException {
		
		String newVMeasurementResultID = dbq.createUUID();
		
		// Create a new VMeasurementResult
		dbq.execute("qappVMeasurementResult", 
				newVMeasurementResultID, VMeasurementID, VMeasurementResultGroupID, VMeasurementResultMasterID, MeasurementID);
		
		// Create new VMeasurementReadingResults...
		dbq.execute("qappVMeasurementReadingResult", newVMeasurementResultID, MeasurementID);
		
		return newVMeasurementResultID;		
	}

	public String getResult() { return result; }
}
