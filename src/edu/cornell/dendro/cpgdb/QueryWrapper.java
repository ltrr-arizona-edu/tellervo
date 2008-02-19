package edu.cornell.dendro.cpgdb;

import java.util.HashMap;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * This class is a temporary wrapper for SQL statements.
 * 
 * In an ideal situation, SQL statements will be stored in the database.
 * For now, we store them as prepared statements because we use JDBC's native
 * string replacement.
 * 
 * queries is a hashmap of prepared statements.
 * keys are case sensitive, so all queries need to be called properly!
 * 
 * This is HORRIBLY INEFFICIENT, as all queries are generated each time the SQL
 * library is loaded. 
 */

public class QueryWrapper {
	public QueryWrapper(Connection sqlConnection) throws SQLException {
		this.sql = sqlConnection;
		
		queries = new HashMap<String, StatementQueryHolder>();
		
		addQuery("getuuid", "select uuid()");
		
		// Param order:
		// 1 = VMeasurementID
		addQuery("qryVMeasurementType", 
				"SELECT tblVMeasurement.VMeasurementID, First(tlkpVMeasurementOp.Name) AS Op, "+
				"Count(tblVMeasurementGroup.VMeasurementID) AS VMeasurementsInGroup, " +
				"First(tblVMeasurement.MeasurementID) AS MeasurementID, " +
				"First(tblVMeasurement.VMeasurementOpParameter) AS VMeasurementOpParameter " +
				"FROM tlkpVMeasurementOp " +
				"INNER JOIN (tblVMeasurement LEFT JOIN tblVMeasurementGroup ON " +
				"tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID) " +
				"ON tlkpVMeasurementOp.VMeasurementOpID = tblVMeasurement.VMeasurementOpID " +
				"GROUP BY tblVMeasurement.VMeasurementID HAVING tblVMeasurement.VMeasurementID=?");
		
		/*
		 * 1 = paramvmeasurementresultid
		 * 2 = paramvmeasurementid
		 * 3 = paramvmeasurementresultgroupid
		 * 4 = paramvmeasurementresultmasterid
		 * 5 = OwnerUserID (DEFAULT TO 1??)
		 * 6 = paramMeasurementID
		 * 7 = paramvmeasurementid
		 */
		addQuery("qappVMeasurementResult", 
				"INSERT INTO tblVMeasurementResult " +
				"( VMeasurementResultID, VMeasurementID, RadiusID, IsReconciled, StartYear, " +
				"DatingTypeID, DatingErrorPositive, DatingErrorNegative, " +
				"IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultGroupID, " +
				"VMeasurementResultMasterID, OwnerUserID, Name, Description, isPublished ) " +				
				"SELECT ? AS Expr1, ? AS Expr2, m.RadiusID, m.IsReconciled, " +
				"m.StartYear, m.DatingTypeID, m.DatingErrorPositive, " +
				"m.DatingErrorNegative, m.IsLegacyCleaned, " +
				"vm.CreatedTimestamp, " +
				"vm.LastModifiedTimestamp, " + 
				"? AS Expr5, ? AS Expr6, ? AS Expr7, " + 
				"vm.name, vm.description, vm.isPublished " +
				"FROM tblMeasurement m " +
				"INNER JOIN tblVMeasurement AS vm ON vm.MeasurementID = m.MeasurementID " +
				"WHERE m.MeasurementID=? and vm.VMeasurementID=?");
				/*
				"SELECT ? AS Expr1, ? AS Expr2, tblMeasurement.RadiusID, tblMeasurement.IsReconciled, " +
				"tblMeasurement.StartYear, tblMeasurement.DatingTypeID, tblMeasurement.DatingErrorPositive, " +
				"tblMeasurement.DatingErrorNegative, tblMeasurement.IsLegacyCleaned, " +
				"Now() AS Expr3, Now() AS Expr4, ? AS Expr5, ? AS Expr6, 1 AS Expr7 " +
				"FROM tblMeasurement WHERE tblMeasurement.MeasurementID=?");
				*/
		/*
		 * 1 = paramVMeasurementResultID
		 * 2 = paramMeasurementID
		 */
		addQuery("qappVMeasurementReadingResult",
				"INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID ) " +
				"SELECT tblReading.RelYear, tblReading.Reading, ? AS Expr1 " +
				"FROM tblReading " +
				"WHERE tblReading.MeasurementID=?");
		
		/*
		 * 1 = paramVMeasurementID
		 */
		addQuery("qryVMeasurementMembers",
				"SELECT tblVMeasurement.VMeasurementID, tblVMeasurementGroup.MemberVMeasurementID " +
				"FROM tblVMeasurement INNER JOIN tblVMeasurementGroup ON " +
				"tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID "+
				"WHERE tblVMeasurement.VMeasurementID=?");
		
		/*
		 * 1 = paramVMeasurementID
		 * 2 = paramCurrentVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultOpClean",
				"UPDATE tblVMeasurementResult SET VMeasurementID = ? " +
				"WHERE VMeasurementResultID=?");

		/*
		 * 1 = paramVMeasurementID
		 * 2 = paramRedate
		 * 3 = paramCurrentVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultOpRedate",
				"UPDATE tblVMeasurementResult SET VMeasurementID = ?, " +
				"StartYear = StartYear + ? " +
				"WHERE VMeasurementResultID=?");
	
		/*
		 * 1 = paramVMeasurementResultGroupID
		 */
		addQuery("qupdVMeasurementResultClearGroupID", 
				"UPDATE tblVMeasurementResult SET VMeasurementResultGroupID = NULL " +
				"WHERE VMeasurementResultGroupID=?");
		
		/* 
		 * 1 = paramVMeasurementResultGroupID
		 * 2 = paramVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultAttachGroupID", 
				"UPDATE tblVMeasurementResult SET VMeasurementResultGroupID = ? " +
				"WHERE VMeasurementResultID=?");
		
		/*
		 * 1 = paramVMeasurementResultMasterID
		 * 2 = paramVMeasurementResultID
		 */
		addQuery("qdelVMeasurementResultRemoveMasterID",
				"DELETE FROM tblVMeasurementResult " +
				"WHERE VMeasurementResultMasterID=? AND " +
				"VMeasurementResultID<>?");

		/*
		 * 1 = paramNewVMeasurementResultID
		 * 2 = paramVMeasurementID
		 * 3 = paramVMeasurementResultMasterID
		 * 4 = OwnerUserID
		 * 5 = paramCurrentVMeasurementResultID
		 */
		addQuery("qappVMeasurementResultOpIndex",
				"INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, RadiusID, " +
				"IsReconciled, StartYear, DatingTypeID, DatingErrorPositive, DatingErrorNegative, " +
				"IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultMasterID, " +
				"OwnerUserID, Name, Description, isPublished ) " +
				"SELECT ? AS Expr1, ? AS Expr2, " +
				"r.RadiusID, r.IsReconciled, r.StartYear, " +
				"r.DatingTypeID, r.DatingErrorPositive, " +
				"r.DatingErrorNegative, r.IsLegacyCleaned, v.CreatedTimestamp, " +
				"v.LastModifiedTimestamp, ? AS Expr5, ? AS Expr6, " +
				"v.Name, v.Description, v.isPublished " +
				"FROM tblVMeasurementResult r " +
				"INNER JOIN tblVMeasurement AS v ON v.VMeasurementID = r.VMeasurementID " +
				"WHERE r.VMeasurementResultID=?");
		
		/*
		 * 1 = paramCurrentVMeasurementResultID
		 */
		addQuery("qacqVMeasurementReadingResult", 
				"SELECT RelYear, Reading from tblVMeasurementReadingResult WHERE VMeasurementResultID=? ORDER BY RelYear ASC");
		
		/*
		 * 1 = paramNewVMeasurementResultID
		 * 2 = paramVMeasurementID
		 * 3 = paramVMeasurementResultMasterID
		 * 4 = OwnerUserID
		 * 5 = paramVMeasurementResultGroupID
		 */
		addQuery("qappVMeasurementResultOpSum",
				"INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, " +
                "StartYear, DatingTypeID, CreatedTimestamp, " +
				"LastModifiedTimestamp, VMeasurementResultMasterID, OwnerUserID, " +
				"Name) " +
				"SELECT ? AS Expr1, ? AS Expr2, " +
				"Min(r.StartYear) AS MinOfStartYear, " +
				"Max(r.DatingTypeID) AS MaxOfDatingTypeID, " +
				"now() AS CreatedTimestamp, now() AS LastModifiedTimestamp, " +
				"? AS Expr5, ? AS Expr6, 'SUM' AS Name " + 
				"FROM tblVMeasurementResult r " +
				"WHERE r.VMeasurementResultGroupID=?");
		
		/*
		 * 1 = strNewVMeasurementResultGroupID
		 * 2 = strNewVMeasurementResultID
		 */
		addQuery("qappVMeasurementResultReadingOpSum",
				"SELECT * from cpgdb.qappVMeasurementResultReadingOpSum(?, ?)");
		
		/*
		 * 1 = VMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultInfo", 
				"SELECT * from cpgdb.qupdVMeasurementResultInfo(?)");
				
		/*
		 * 1 = VMeasurementResultID
		 */
		addQuery("qGetResultRow",
				"SELECT * from tblVMeasurementResult WHERE VMeasurementResultID=?");
	}

	/**
	 * Closes all prepared statements.
	 * @throws SQLException
	 */
	public void cleanup() throws SQLException {
		Iterator i = queries.keySet().iterator();
		
		while(i.hasNext()) {
			StatementQueryHolder sq = queries.get(i.next());
			if(sq != null)
				sq.cleanup();
		}
	}
	
	public PreparedStatement getQuery(String queryName, Object[] args) throws SQLException {
		StatementQueryHolder holder = queries.get(queryName);

		// no query by this name? bzzt.
		if(holder == null)
			throw new SQLException("Invalid query name: " + queryName);
		
		PreparedStatement statement = holder.getStatement();
		
		for(int i = 0; i < args.length; i++) {
			if(args[i] == null)
				statement.setString(i + 1, (String) args[i]);
			else
				statement.setObject(i + 1, args[i]);
		}
		
		// debug output that prints out queries...
		//System.out.println(statement.toString());
		
		return statement;
	}

	private void addQuery(String queryName, String SQLStatement) throws SQLException {
		queries.put(queryName, new StatementQueryHolder(SQLStatement, sql));
	}

	/**
	 * This internal class creates prepared statements on an as-needed basis and caches them.
	 * 
	 * @author Lucas Madar
	 */
	private class StatementQueryHolder {
		private PreparedStatement preparedStatement;
		private String queryString;
		private Connection sqlConnection;

		public StatementQueryHolder(String queryString, Connection sqlConnection) {
			this.queryString = queryString;
			this.sqlConnection = sqlConnection;
			this.preparedStatement = null;
		}

		public PreparedStatement getStatement() throws SQLException {
			if(preparedStatement == null) 
				preparedStatement = sqlConnection.prepareStatement(queryString);
			
			return preparedStatement;
		}
		
		public void cleanup() throws SQLException {
			if(preparedStatement != null)
				preparedStatement.close();
		}		
	}
		
	private HashMap<String, StatementQueryHolder> queries;
	private Connection sql;
}
