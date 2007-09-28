package edu.cornell.dendro.cpgdb;

import java.sql.*;
import java.util.Properties;

public class DBQuery {
	// our connection via jdbc to the server
	private Connection sql;

	// We use the querywrapper to get prepared statements.
	private QueryWrapper queries;	
	
	// keep around a jdbc connection
	public DBQuery() throws SQLException {
		/*
		 * The following code is for testing purposes.
		 */
		
		/**/
		try {
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {}
		
		Properties props = new Properties();
		props.setProperty("user", "testuser");
		props.setProperty("password", "t3stus3r");
		props.setProperty("loglevel", "1");
		
		sql = DriverManager.getConnection("jdbc:postgresql://negaverse.no-ip.org/corina", props);
		/**/
		
		/*
		 * In production, this will be how we connect to the server...
		 * sql = DriverManager.getConnection("jdbc:default:connection");
		 */
		
		queries = new QueryWrapper(sql);
	}
	
	public String createUUID() throws SQLException {
		Statement stmt = sql.createStatement();
		ResultSet res = stmt.executeQuery("SELECT uuid()");
		
		// perhaps some better error checking is in order,
		// but this will throw an SQLException if things go bad...
		res.next();
		String ret = res.getString(1);
		res.close();
		
		return ret;
	}
	
	private void beVerbose(String queryName, Object[] args) {
		System.out.print("Executing " + queryName + ": ");
		for(Object arg:args) {
			if(arg == null)
				System.out.print("<null>, ");
			else
				System.out.print(arg.toString() + ", ");
		}
		System.out.println(".");
	}
	
	public boolean execute(String queryName, Object ... args) throws SQLException {
		beVerbose(queryName, args);
		
		PreparedStatement q = queries.getQuery(queryName, args);
		if(q != null)
			return q.execute();
		
		throw new SQLException("Invalid Query");
	}
	
	public ResultSet query(String queryName, Object ... args) throws SQLException {
		beVerbose(queryName, args);
		
		PreparedStatement q = queries.getQuery(queryName, args);
		if(q != null)
			return q.executeQuery();
		
		throw new SQLException("Invalid Query");
	}
}
