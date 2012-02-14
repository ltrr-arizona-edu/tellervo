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
package org.tellervo.cpgdb;

//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Properties;
import java.util.UUID;

public class Dispatch {

	public static UUID GetVMeasurementResult(String VMeasurementID) throws SQLException {
		// Simple and clean. Pass in the ID, return the result string.
		try{
			VMeasurementResult result = new VMeasurementResult(UUID.fromString(VMeasurementID), false);
			return result.getResult();
			
		} catch (Exception e) { 
			e.printStackTrace();  
			if (e instanceof SQLException) 
				throw (SQLException) e; 
			
			throw new SQLException("Error: " + e.toString());
		}		
	}
	
	public static void main(String[] args) {
			/*
			 * The following code is for testing purposes. And doesn't currently work.
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (Exception e) {}
			
			Properties props = new Properties();
			props.setProperty("user", "testuser");
			props.setProperty("password", "t3stus3r");
			props.setProperty("loglevel", "1");
			
			Connection sqlConnection = DriverManager.getConnection("jdbc:postgresql://negaverse.no-ip.org/tellervo", props);

			System.out.println("PG DB TEST!");
			
			// don't use cleanup, because it doesn't work with the postgresql jdbc driver, only the native driver!
			VMeasurementResult vmr = new VMeasurementResult(27, false, new DBQuery(sqlConnection));
			String result = vmr.getResult();
			System.out.println("Result ID: " + result);
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
			 */
	}

}
