package edu.cornell.dendro.cpgdb;

//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Properties;
import java.util.UUID;

public class Dispatch {

	public static PgDB_UUID GetVMeasurementResult(String VMeasurementID) throws SQLException {
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
			
			Connection sqlConnection = DriverManager.getConnection("jdbc:postgresql://negaverse.no-ip.org/corina", props);

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
