/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.cpgdb;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class DBQuery {
	// our connection via jdbc to the server
	private Connection sqlConnection;
		
	public DBQuery(Connection sqlConnection) throws SQLException {
		this.sqlConnection = sqlConnection;
	}
	
	// keep around a jdbc connection
	public DBQuery() throws SQLException {
		// obtain a JDBC connection, if one isn't passed to us.
		this(DriverManager.getConnection("jdbc:default:connection"));
	}
	
	public String createUUID() throws SQLException {
		/* Now requires uuid-ossp module to be installed in postgres */
		Statement stmt = sqlConnection.createStatement();
		ResultSet res = stmt.executeQuery("SELECT uuid_generate_v1()");
		
		// perhaps some better error checking is in order,
		// but this will throw an SQLException if things go bad...
		res.next();
		String ret = res.getString(1);
		res.close();
		
		return ret;
	}
	
	private void beVerbose(String queryName, String action, Object[] args) {
		Logger logger = Logger.getAnonymousLogger();
		
		if(logger.isLoggable(Level.FINER)) 
		{
			ParamStringBuilder params = new ParamStringBuilder(ParamStringBuilder.Mode.SQL);
			int pidx = 0;
			
			for(Object arg : args) {
				params.append("arg" + (++pidx), arg);
			}
			
			logger.log(Level.FINER, action + " " + queryName + params.toString());
		}
	}
	
	public void execute(String queryName, Object ... args) throws SQLException {
		beVerbose(queryName, "execute", args);
		
		PreparedStatement q = prepareStatement(queryName, args);

		try {
			ResultSet rs = q.executeQuery();
			rs.next();
			rs.close();
			return;
		} finally {
			q.close();
		}
	}
		
	public ResultSet query(String queryName, Object ... args) throws SQLException {
		beVerbose(queryName, "query", args);
		
		PreparedStatement q = prepareStatement(queryName, args);

		ResultSet rs = q.executeQuery();
		CachedRowSet crs = new CachedRowSetImpl();
		
		crs.populate(rs);
		
		rs.close();
		q.close();
		
		return crs;
	}
	
	public PreparedStatement prepareStatement(String queryName, Object[] args) throws SQLException {
		PreparedStatement stmt = sqlConnection.prepareStatement(prepareQueryString(queryName, args.length));

		for(int i = 0; i < args.length; i++) {
			if(args[i] == null)
				stmt.setString(i+1, null);
			else
				stmt.setObject(i+1, args[i]);
		}
		
		return stmt;
	}
	
	public String prepareQueryString(String queryName, int nargs) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT * FROM cpgdbj.");
		sb.append(queryName);
		sb.append('(');
		
		for(int i = 0; i < nargs; i++) {
			if(i > 0)
				sb.append(',');
			sb.append('?');
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	public Connection getConnection() {
		return sqlConnection;
	}
	
	/** close our connection and clean up our stored queries */
	public void cleanup() throws SQLException {
		try {
			sqlConnection.close();
		} catch (SQLException sqle) {
			// cleanup failed? oh well.
		}
	}
}
