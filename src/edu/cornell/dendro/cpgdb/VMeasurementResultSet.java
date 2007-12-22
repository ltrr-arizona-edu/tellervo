/**
 * 
 */
package edu.cornell.dendro.cpgdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgresql.pljava.ResultSetHandle;

/**
 * This class is an easier interface to VMeasurementResult (an alternative to Dispatch.GetVMeasurementResult)
 * It interfaces with pl/pgsql to return the generated row on success. 
 * Failure conditions will generated with an exception as before.
 * 
 * @author lucasm
 *
 */
public class VMeasurementResultSet extends VMeasurementResult implements ResultSetHandle {
	/**
	 * @param VMeasurementID
	 * @param cleanup
	 * @throws SQLException
	 */
	public VMeasurementResultSet(int VMeasurementID)
			throws SQLException {
		super(VMeasurementID, true, false);
	}
	
	public void close() throws SQLException {
		dbq.cleanup();
	}

	public ResultSet getResultSet() throws SQLException {
		String resid = getResult();
		if(resid != null)
			return dbq.query("qGetResultRow", resid);
		return null;
	}
	
	public static ResultSetHandle getVMeasurementResultSet(int VMeasurementID) throws SQLException {
		return new VMeasurementResultSet(VMeasurementID);
	}
}
