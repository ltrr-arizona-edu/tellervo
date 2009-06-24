/**
 * 
 */
package edu.cornell.dendro.cpgdb;

import java.sql.*;
import java.util.UUID;

import org.postgresql.pljava.ResultSetHandle;

/**
 * This class is an easier interface to VMeasurementResult (an alternative to Dispatch.GetVMeasurementResult)
 * It interfaces with pl/pgsql to return the generated row on success. 
 * Failure conditions will generated with an exception as before.
 * 
 * @author lucasm
 *
 */
public class VMeasurementResultSet implements ResultSetHandle {
        private UUID VMeasurementID;
        private Statement statement;

	/**
	 * @param VMeasurementID
	 */
	public VMeasurementResultSet(String VMeasurementID) {
		this.VMeasurementID = UUID.fromString(VMeasurementID);
	}

	/**
	 * @param VMeasurementID
	 */
	public VMeasurementResultSet(UUID VMeasurementID) {
		this.VMeasurementID = VMeasurementID;
	}
	
	public void close() throws SQLException {
		statement.close();
	}

	public ResultSet getResultSet() throws SQLException {
		VMeasurementResult result = new VMeasurementResult(VMeasurementID, false);
		String resid = result.getResult().toString();

		if(resid != null) {
			statement = DriverManager.getConnection("jdbc:default:connection").createStatement();
			return statement.executeQuery(
				"SELECT * FROM tblVMeasurementResult WHERE VMeasurementResultID = '" +
				resid + "'");
		}
		return null;
	}
	
	public static ResultSetHandle getVMeasurementResultSet(String VMeasurementID) throws SQLException {
		return new VMeasurementResultSet(VMeasurementID);
	}
}
