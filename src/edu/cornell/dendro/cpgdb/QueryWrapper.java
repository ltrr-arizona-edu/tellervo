package edu.cornell.dendro.cpgdb;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
		
		queries = new HashMap<String, PreparedStatement>();
		
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
		 * 5 = paramMeasurementID
		 */
		addQuery("qappVMeasurementResult", 
				"INSERT INTO tblVMeasurementResult " +
				"( VMeasurementResultID, VMeasurementID, RadiusID, IsReconciled, StartYear, IsRelYear, " +
				"IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultGroupID, " +
				"VMeasurementResultMasterID, OwnerUserID ) " +
				"SELECT ? AS Expr1, ? AS Expr2, tblMeasurement.RadiusID, tblMeasurement.IsReconciled, " +
				"tblMeasurement.StartYear, tblMeasurement.IsRelYear, tblMeasurement.IsLegacyCleaned, " +
				"Now() AS Expr3, Now() AS Expr4, ? AS Expr5, ? AS Expr6, 1 AS Expr7 " +
				"FROM tblMeasurement WHERE tblMeasurement.MeasurementID=?");
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
		 * 2 = paramRedate
		 * 3 = paramCurrentVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultOpRedate",
				"UPDATE tblVMeasurementResult SET VMeasurementID = ?, " +
				"StartYear = StartYear + ?, CreatedTimestamp = Now(), " +
				"LastModifiedTimestamp = Now() " +
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

	}
		
	public PreparedStatement getQuery(String queryName, Object[] args) throws SQLException {
		PreparedStatement statement = queries.get(queryName);

		if(statement == null)
			return null;
		
		for(int i = 0; i < args.length; i++) {
			if(args[i] instanceof String)
				statement.setString(i + 1, (String) args[i]);
			else if(args[i] instanceof Integer)
				statement.setInt(i + 1, (Integer) args[i]);
			else
				statement.setObject(i + 1, args[i]);
		}
		
		System.out.println(statement.toString());
		
		return statement;
	}

	private void addQuery(String queryName, String SQLStatement) throws SQLException {
		queries.put(queryName, sql.prepareStatement(SQLStatement));
	}
	
	private HashMap<String, PreparedStatement> queries;
	private Connection sql;
}
