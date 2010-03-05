/**
 * 
 */
package edu.cornell.dendro.cpgdb;

import java.sql.*;
import java.util.UUID;

import javax.sql.rowset.CachedRowSet;

import org.postgresql.pljava.ResultSetHandle;

import com.sun.rowset.CachedRowSetImpl;

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
	}

	public ResultSet getResultSet() throws SQLException {
		VMeasurementResult result = new VMeasurementResult(VMeasurementID, false);
		String resid = result.getResult().toString();

		Connection connection = null;
		Statement statement = null;
		ResultSet res = null;
		if(resid != null) {
			try {
				connection = DriverManager.getConnection("jdbc:default:connection");
				statement = connection.createStatement();
				res = statement.executeQuery(
						"SELECT * FROM tblVMeasurementResult WHERE VMeasurementResultID = '" +
						resid + "'");
			
				// cache the result set, so we can close all open SQL handles
				CachedRowSet rows = new CachedRowSetImpl();
				rows.populate(res);
				
				return rows;
			}
			finally {
				if(res != null)
					res.close();
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			}
		}
		return null;
	}
	
	public static ResultSetHandle getVMeasurementResultSet(String VMeasurementID) throws SQLException {
		return new VMeasurementResultSet(VMeasurementID);
	}
}
