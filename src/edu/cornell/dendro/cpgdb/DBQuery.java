package edu.cornell.dendro.cpgdb;

import java.sql.*;
import java.util.Properties;

public class DBQuery {
	// our connection via jdbc to the server
	private Connection sqlConnection;

	// We use the querywrapper to get prepared statements.
	private QueryWrapper queries;
	
	// in debug mode, we print out a bunch of stuff to stdout.
	private boolean debug = false;
	
	public DBQuery(Connection sqlConnection) throws SQLException {
		this.sqlConnection = sqlConnection;
		this.queries = new QueryWrapper(sqlConnection);		
	}
	
	// keep around a jdbc connection
	public DBQuery() throws SQLException {
		// obtain a JDBC connection, if one isn't passed to us.
		this(DriverManager.getConnection("jdbc:default:connection"));
	}
	
	public String createUUID() throws SQLException {
		String nativeUUID = UUID.createUUID();

		if(nativeUUID != null)
			return nativeUUID;

		Statement stmt = sqlConnection.createStatement();
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
		if(debug)
			beVerbose(queryName, args);
		
		PreparedStatement q = queries.getQuery(queryName, args);
		if(q != null)
			return q.execute();
		
		throw new SQLException("Invalid Query");
	}
	
	public ResultSet query(String queryName, Object ... args) throws SQLException {
		if(debug)
			beVerbose(queryName, args);
		
		PreparedStatement q = queries.getQuery(queryName, args);
		if(q != null)
			return q.executeQuery();
		
		throw new SQLException("Invalid Query");
	}
	
	public Connection getConnection() {
		return sqlConnection;
	}
	
	/** close our connection and clean up our stored queries */
	public void cleanup() throws SQLException {
		try {
			queries.cleanup();
			sqlConnection.close();
		} catch (SQLException sqle) {
			// cleanup failed? oh well.
		}
	}
}
