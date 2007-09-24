package edu.cornell.dendro.cpgdb;

import java.sql.*;

public class DBQuery {
	// our connection via jdbc to the server
	private Connection conn;
	
	// keep around a jdbc connection
	public DBQuery() throws SQLException {
		//conn = DriverManager.getConnection("jdbc:default:connection");
	}
	
	
	public ResultSet query(String queryName, Object ... args) {
		System.out.print("Executing " + queryName + ": ");
		for(Object arg:args) {
			System.out.print(arg.toString() + ", ");
		}
		System.out.println(".");

		return null;
	}
}
