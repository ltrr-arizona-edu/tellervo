package edu.cornell.dendro.cpgdb;

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

public class QueryGenerator {
	private enum PD { IN, OUT, RETURN };
	private enum PT { uuid, text, integer, bigint };
	private enum Stability { STABLE, IMMUTABLE, VOLATILE };

	private static class PP {
		public PD direction;
		public PT type;
		public String varname;
		public String desc;
		
		public static PP gen(PD direction, String varname, PT type, String desc) {
			PP p = new PP();
			
			p.direction = direction;
			p.varname = varname;
			p.type = type;
			p.desc = desc;
			
			return p;
		}
		
		public static PP gen(PD direction, String varname, PT type) {
			return PP.gen(direction, varname, type, null);
		}

		public static PP in(String varname, PT type) {
			return PP.gen(PD.IN, varname, type, null);
		}
		
		public static PP out(String varname, PT type) {
			return PP.gen(PD.OUT, varname, type, null);
		}
		
		public static PP voidreturn() {
			return PP.gen(PD.RETURN, "void", null);
		}
	}
	
	public QueryGenerator() {
		
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
				"GROUP BY tblVMeasurement.VMeasurementID HAVING tblVMeasurement.VMeasurementID=$1",
				Stability.STABLE,
				PP.gen(PD.IN, "vmeasurementid", PT.uuid, "The VMeasurementID we are querying"),
				PP.out("vmeasurementid", PT.uuid),
				PP.out("op", PT.text),
				PP.out("vmeasurementsingroup", PT.bigint),
				PP.out("measurementid", PT.integer),
				PP.out("vmeasurementopparameter", PT.integer)
				);
		/*
				"(IN vmeasurementid uuid, OUT vmeasurementid uuid, OUT op text, OUT vmeasurementsingroup integer, "+
				 "OUT measurementid integer, OUT vmeasurementopparameter integer)");
				 */
		
		/*
		 * NOTE: 2 and 7 are dupes, FIXED!
		 * BE SURE TO FIX IN ACTUAL QUERY!
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
				"VMeasurementResultMasterID, OwnerUserID, Code, Comments, isPublished ) " +				
				"SELECT $1 AS Expr1, $2 AS Expr2, m.RadiusID, m.IsReconciled, " +
				"m.StartYear, m.DatingTypeID, m.DatingErrorPositive, " +
				"m.DatingErrorNegative, m.IsLegacyCleaned, " +
				"vm.CreatedTimestamp, " +
				"vm.LastModifiedTimestamp, " + 
				"$3 AS Expr5, $4 AS Expr6, $5 AS Expr7, " + 
				"vm.code, vm.comments, vm.isPublished " +
				"FROM tblMeasurement m " +
				"INNER JOIN tblVMeasurement AS vm ON vm.MeasurementID = m.MeasurementID " +
				"WHERE m.MeasurementID=$6 and vm.VMeasurementID=$2",
				Stability.VOLATILE,
				PP.in("paramVMeasurementResultID", PT.uuid),
				PP.in("paramVMeasurementID", PT.uuid),
				PP.in("paramVMeasurementResultGroupID", PT.uuid),
				PP.in("paramVMeasurementResultMasterID", PT.uuid),
				PP.in("ownerUserID", PT.integer),
				PP.in("paramMeasurementID", PT.integer),
				PP.voidreturn()
				);
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
				"INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID ) " +
				"SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID " +
				"FROM tblReading " +
				"WHERE tblReading.MeasurementID=$2",
				Stability.VOLATILE,
				PP.gen(PD.RETURN, "void", null),
				PP.in("paramVMeasurementResultID", PT.uuid),
				PP.in("paramMeasurementID", PT.integer)
				);
		
		/*
		 * 1 = paramVMeasurementID
		 */
		addQuery("qryVMeasurementMembers",
				"SELECT tblVMeasurement.VMeasurementID, tblVMeasurementGroup.MemberVMeasurementID " +
				"FROM tblVMeasurement INNER JOIN tblVMeasurementGroup ON " +
				"tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID "+
				"WHERE tblVMeasurement.VMeasurementID=$1",
				Stability.STABLE,
				PP.in("paramVMeasurementID", PT.uuid),
				PP.out("VMeasurementID", PT.uuid),
				PP.out("MemberVMeasurementID", PT.uuid),
				PP.gen(PD.RETURN, "SETOF record", null)
				);
		
		/*
		 * 1 = paramVMeasurementID
		 * 2 = paramCurrentVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultOpClean",
				"UPDATE tblVMeasurementResult SET VMeasurementID=$1 " +
				"WHERE VMeasurementResultID=$2",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramVMeasurementID", PT.uuid),
				PP.in("paramCurrentVMeasurementResultID", PT.uuid)
				);

		/*
		 * 1 = paramVMeasurementID
		 * 2 = paramRedate
		 * 3 = paramCurrentVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultOpRedate",
				"UPDATE tblVMeasurementResult SET VMeasurementID=$1, " +
				"StartYear=$2 " +
				"WHERE VMeasurementResultID=$3",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramVMeasurementID", PT.uuid),
				PP.in("paramRedate", PT.integer),
				PP.in("paramCurrentVMeasurementResultID", PT.uuid)
				);
	
		/*
		 * NOTE: DUPE PARAMS AGAIN!
		 * 1 = paramVMeasurementID
		 * 2 = paramVMeasurementID
		 * 3 = paramCurrentVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultOpCrossdate",
				"UPDATE tblVMeasurementResult SET VMeasurementID=$1, " +
				"StartYear=(SELECT StartYear FROM tblCrossdate WHERE VMeasurementID=$1) " +
				"WHERE VMeasurementResultID=$2",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramVMeasurementID", PT.uuid),
				PP.in("paramCurrentVMeasurementResultID", PT.uuid)
				);
	
		/*
		 * 1 = paramVMeasurementResultGroupID
		 */
		addQuery("qupdVMeasurementResultClearGroupID", 
				"UPDATE tblVMeasurementResult SET VMeasurementResultGroupID=NULL " +
				"WHERE VMeasurementResultGroupID=$1",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramVMeasurementResultGroupID", PT.uuid));
		
		/* 
		 * 1 = paramVMeasurementResultGroupID
		 * 2 = paramVMeasurementResultID
		 */
		addQuery("qupdVMeasurementResultAttachGroupID", 
				"UPDATE tblVMeasurementResult SET VMeasurementResultGroupID=$1 " +
				"WHERE VMeasurementResultID=$2",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramVMeasurementResultGroupID", PT.uuid),
				PP.in("paramVMeasurementResultID", PT.uuid)
				);
		
		/*
		 * 1 = paramVMeasurementResultMasterID
		 * 2 = paramVMeasurementResultID
		 */
		addQuery("qdelVMeasurementResultRemoveMasterID",
				"DELETE FROM tblVMeasurementResult " +
				"WHERE VMeasurementResultMasterID=$1 AND " +
				"VMeasurementResultID<>$2",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramVMeasurementResultMasterID", PT.uuid),
				PP.in("paramVMeasurementResultID", PT.uuid)
				);

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
				"OwnerUserID, Code, Comments, isPublished ) " +
				"SELECT $1 AS Expr1, $2 AS Expr2, " +
				"r.RadiusID, r.IsReconciled, r.StartYear, " +
				"r.DatingTypeID, r.DatingErrorPositive, " +
				"r.DatingErrorNegative, r.IsLegacyCleaned, v.CreatedTimestamp, " +
				"v.LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, " +
				"v.Code, v.Comments, v.isPublished " +
				"FROM tblVMeasurementResult r " +
				"INNER JOIN tblVMeasurement AS v ON v.VMeasurementID = r.VMeasurementID " +
				"WHERE r.VMeasurementResultID=$5",
				Stability.VOLATILE,
				PP.voidreturn(),
				PP.in("paramNewVMeasurementResultID", PT.uuid),
				PP.in("paramVMeasurementID", PT.uuid),
				PP.in("paramVMeasurementResultMasterID", PT.uuid),
				PP.in("ownerUserID", PT.integer),
				PP.in("paramCurrentVMeasurementResultID", PT.uuid)
				);
		
		/*
		 * 1 = paramCurrentVMeasurementResultID
		 */
		addQuery("qacqVMeasurementReadingResult", 
				"SELECT RelYear, Reading from tblVMeasurementReadingResult WHERE VMeasurementResultID=$1 ORDER BY RelYear ASC",
				Stability.STABLE,
				PP.in("paramCurrentVMeasurementResultID", PT.uuid),
				PP.out("RelYear", PT.integer),
				PP.out("Reading", PT.integer),
				PP.gen(PD.RETURN, "SETOF record", null)
				);
		
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
				"Code) " +
				"SELECT $1 AS Expr1, $2 AS Expr2, " +
				"Min(r.StartYear) AS MinOfStartYear, " +
				"Max(r.DatingTypeID) AS MaxOfDatingTypeID, " +
				"now() AS CreatedTimestamp, now() AS LastModifiedTimestamp, " +
				"$3 AS Expr5, $4 AS Expr6, 'SUM' AS Code " + 
				"FROM tblVMeasurementResult r " +
				"WHERE r.VMeasurementResultGroupID=$5",
				Stability.VOLATILE,
				PP.in("paramNewVMeasurementResultID", PT.uuid),
				PP.in("paramVMeasurementID", PT.uuid),
				PP.in("paramVMeasurementResultMasterID", PT.uuid),
				PP.in("ownerUserID", PT.integer),
				PP.in("paramVMeasurementResultGroupID", PT.uuid),
				PP.voidreturn()
				);
		
		/*
		 * 1 = strNewVMeasurementResultGroupID
		 * 2 = strNewVMeasurementResultID
		 */
		addQuery("qappVMeasurementResultReadingOpSum",
				"SELECT * from cpgdb.qappVMeasurementResultReadingOpSum($1, $2)",
				Stability.VOLATILE,
				PP.in("paramNewVMeasurementResultGroupID", PT.uuid),
				PP.in("paramNewVMeasurementResultID", PT.uuid),
				PP.gen(PD.RETURN, "integer", null)
				);
		
		
		/*
		 * 1 = VMeasurementResultID
		 */
		/* Wrapper???
		addQuery("qupdVMeasurementResultInfo", 
				"SELECT * from cpgdb.qupdVMeasurementResultInfo($1)",
				Stability.VOLATILE,
				PP.in("paramVMeasurementResultID", PT.uuid),
				PP.gen(PD.RETURN, "boolean", null)
				);		
		*/
		
		/*
		 * New queries for dealing with NOTES
		 */
		// Copy notes over from a DIRECT measurement
		addQuery("qappVMeasurementReadingNoteResult",
				"INSERT INTO tblVMeasurementReadingNoteResult ( VMeasurementResultID, RelYear, ReadingNoteID ) " +
				"SELECT vmrr.VMeasurementResultID, vmrr.RelYear, rrn.ReadingNoteID " +
				"FROM tblVMeasurementReadingResult vmrr " +
				"INNER JOIN tblReadingReadingNote rrn ON vmrr.readingID=rrn.readingID " +
				"WHERE vmrr.vMeasurementResultID=$1;",
				Stability.VOLATILE,
				PP.in("paramVMeasurementResultID", PT.uuid),
				PP.voidreturn()
				);
	}
	
	private void addQuery(String queryName, String SQLStatement, Stability stability, PP ... paramlist) {
		queryName = "cpgdbj." + queryName;
		boolean showedIn = false, showedOut = false;
		StringBuffer sb = new StringBuffer();
		String returnval = "";

		System.out.println("\n--\n-- " + queryName + "\n--");
		
		for (PP p : paramlist) {
			if (p.direction == PD.IN && !showedIn) {
				System.out.println("-- IN PARAMETERS");
				showedIn = true;
			}
			if (p.direction == PD.OUT && !showedOut) {
				System.out.println("-- OUT PARAMETERS");
				showedOut = true;
			}
			
			if(p.direction == PD.RETURN) {
				System.out.println("-- RETURNS " + p.varname);
				if(p.desc != null)
					System.out.println("--- " + p.desc);
				
				returnval = "RETURNS " + p.varname + " ";
				continue;
			}
				
			System.out.println("---- " + p.varname + "(" + p.type + ") "
					+ (p.desc == null ? "" : p.desc));
			if(sb.length() != 0)
				sb.append(",\n");
			
			sb.append("  " + p.direction + " " + p.varname + " " + p.type);
		}
		
		System.out.println("\nCREATE OR REPLACE FUNCTION " + queryName + "( ");
		System.out.println(sb.toString());
		System.out.println(") " + returnval + "AS $$");
		System.out.println(indentSQL(SQLStatement));
		System.out.println("\n$$ LANGUAGE SQL " + stability + ";");
	}
	
	private String indentSQL(String str) {
		String x = str;
		
		x = x.replaceAll("LEFT JOIN", "\n   LEFT JOIN");
		x = x.replaceAll("RIGHT JOIN", "\n   RIGHT JOIN");
		x = x.replaceAll("INNER JOIN", "\n   INNER JOIN");
		x = x.replaceAll("WHERE", "\n   WHERE");
		x = x.replaceAll("SET", "\n   SET");
		x = x.replaceAll("FROM", "\n   FROM");
		x = x.replaceAll("HAVING", "\n   HAVING");
		x = x.replaceAll("GROUP BY", "\n   GROUP BY");
		x = x.replaceAll("ORDER BY", "\n   ORDER BY");
		x = x.replaceAll("ON", "\n      ON");
		x = x.replaceAll("SELECT", "\n  SELECT");
		x = x.replaceAll("INSERT", "\n  INSERT");
		x = x.replaceAll("UPDATE", "\n  UPDATE");
		x = x.replaceAll("DELETE", "\n  DELETE");
		
		return x;
	}
	
	public static void main(String args[]) {
		new QueryGenerator();
	}
}
