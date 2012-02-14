/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.cpgdb;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	public VMeasurementResultSet(UUID VMeasurementID) 
	{
		this.VMeasurementID = VMeasurementID;
	}
	
	public void close() throws SQLException {
		statement.close();
	}

	public ResultSet getResultSet() throws SQLException 
	{
		VMeasurementResult result = new VMeasurementResult(VMeasurementID, false);
		String resid = result.getResult().toString();
		
		if(resid == null) return null;

		statement = DriverManager.getConnection("jdbc:default:connection").createStatement();
		return statement.executeQuery(
				"SELECT * FROM tblVMeasurementResult WHERE VMeasurementResultID = '" +
				resid + "'::uuid");

	}
	
	public static ResultSetHandle getVMeasurementResultSet(String VMeasurementID) {
		
		System.out.println("getVMeasurementResultSet called with vmid string : "+VMeasurementID);
		
		return new VMeasurementResultSet(VMeasurementID);
	}
}
