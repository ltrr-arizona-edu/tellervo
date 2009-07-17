
--
-- cpgdbj.qryVMeasurementType
--
-- IN PARAMETERS
---- vmeasurementid(uuid) The VMeasurementID we are querying
-- OUT PARAMETERS
---- vmeasurementid(uuid) 
---- op(text) 
---- vmeasurementsingroup(bigint) 
---- measurementid(integer) 
---- vmeasurementopparameter(integer) 

CREATE OR REPLACE FUNCTION cpgdbj.qryVMeasurementType( 
  IN vmeasurementid uuid,
  OUT vmeasurementid uuid,
  OUT op text,
  OUT vmeasurementsingroup bigint,
  OUT measurementid integer,
  OUT vmeasurementopparameter integer
) AS $$

  SELECT tblVMeasurement.VMeasurementID, First(tlkpVMeasurementOp.Name) AS Op, Count(tblVMeasurementGroup.VMeasurementID) AS VMeasurementsInGroup, First(tblVMeasurement.MeasurementID) AS MeasurementID, First(tblVMeasurement.VMeasurementOpParameter) AS VMeasurementOpParameter 
   FROM tlkpVMeasurementOp 
   INNER JOIN (tblVMeasurement 
   LEFT JOIN tblVMeasurementGroup 
      ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID) 
      ON tlkpVMeasurementOp.VMeasurementOpID = tblVMeasurement.VMeasurementOpID 
   GROUP BY tblVMeasurement.VMeasurementID 
   HAVING tblVMeasurement.VMeasurementID=$1

$$ LANGUAGE SQL STABLE;

--
-- cpgdbj.qappVMeasurementResult
--
-- IN PARAMETERS
---- paramVMeasurementResultID(uuid) 
---- paramVMeasurementID(uuid) 
---- paramVMeasurementResultGroupID(uuid) 
---- paramVMeasurementResultMasterID(uuid) 
---- ownerUserID(integer) 
---- paramMeasurementID(integer) 
-- RETURNS void

CREATE OR REPLACE FUNCTION cpgdbj.qappVMeasurementResult( 
  IN paramVMeasurementResultID uuid,
  IN paramVMeasurementID uuid,
  IN paramVMeasurementResultGroupID uuid,
  IN paramVMeasurementResultMasterID uuid,
  IN ownerUserID integer,
  IN paramMeasurementID integer
) RETURNS void AS $$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, RadiusID, IsReconciled, StartYear, DatingTypeID, DatingErrorPositive, DatingErrorNegative, IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultGroupID, VMeasurementResultMasterID, OwnerUserID, Code, Comments, isPublished ) 
  SELECT $1 AS Expr1, $2 AS Expr2, m.RadiusID, m.IsReconciled, m.StartYear, m.DatingTypeID, m.DatingErrorPositive, m.DatingErrorNegative, m.IsLegacyCleaned, vm.CreatedTimestamp, vm.LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, $5 AS Expr7, vm.code, vm.comments, vm.isPublished 
   FROM tblMeasurement m 
   INNER JOIN tblVMeasurement AS vm 
      ON vm.MeasurementID = m.MeasurementID 
   WHERE m.MeasurementID=$6 and vm.VMeasurementID=$2

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qappVMeasurementReadingResult
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementResultID(uuid) 
---- paramMeasurementID(integer) 

CREATE OR REPLACE FUNCTION cpgdbj.qappVMeasurementReadingResult( 
  IN paramVMeasurementResultID uuid,
  IN paramMeasurementID integer
) RETURNS void AS $$

  INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID ) 
  SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID 
   FROM tblReading 
   WHERE tblReading.MeasurementID=$2

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qryVMeasurementMembers
--
-- IN PARAMETERS
---- paramVMeasurementID(uuid) 
-- OUT PARAMETERS
---- VMeasurementID(uuid) 
---- MemberVMeasurementID(uuid) 
-- RETURNS SETOF record

CREATE OR REPLACE FUNCTION cpgdbj.qryVMeasurementMembers( 
  IN paramVMeasurementID uuid,
  OUT VMeasurementID uuid,
  OUT MemberVMeasurementID uuid
) RETURNS SETOF record AS $$

  SELECT tblVMeasurement.VMeasurementID, tblVMeasurementGroup.MemberVMeasurementID 
   FROM tblVMeasurement 
   INNER JOIN tblVMeasurementGroup 
      ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
   WHERE tblVMeasurement.VMeasurementID=$1

$$ LANGUAGE SQL STABLE;

--
-- cpgdbj.qupdVMeasurementResultOpClean
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementID(uuid) 
---- paramCurrentVMeasurementResultID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpClean( 
  IN paramVMeasurementID uuid,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$

  UPDATE tblVMeasurementResult 
   SET VMeasurementID=$1 
   WHERE VMeasurementResultID=$2

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qupdVMeasurementResultOpRedate
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementID(uuid) 
---- paramRedate(integer) 
---- paramCurrentVMeasurementResultID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpRedate( 
  IN paramVMeasurementID uuid,
  IN paramRedate integer,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$

  UPDATE tblVMeasurementResult 
   SET VMeasurementID=$1, StartYear=$2 
   WHERE VMeasurementResultID=$3

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qupdVMeasurementResultOpCrossdate
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementID(uuid) 
---- paramCurrentVMeasurementResultID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultOpCrossdate( 
  IN paramVMeasurementID uuid,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$

  UPDATE tblVMeasurementResult 
   SET VMeasurementID=$1, StartYear=(
  SELECT StartYear 
   FROM tblCrossdate 
   WHERE VMeasurementID=$1) 
   WHERE VMeasurementResultID=$2

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qupdVMeasurementResultClearGroupID
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementResultGroupID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultClearGroupID( 
  IN paramVMeasurementResultGroupID uuid
) RETURNS void AS $$

  UPDATE tblVMeasurementResult 
   SET VMeasurementResultGroupID=NULL 
   WHERE VMeasurementResultGroupID=$1

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qupdVMeasurementResultAttachGroupID
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementResultGroupID(uuid) 
---- paramVMeasurementResultID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qupdVMeasurementResultAttachGroupID( 
  IN paramVMeasurementResultGroupID uuid,
  IN paramVMeasurementResultID uuid
) RETURNS void AS $$

  UPDATE tblVMeasurementResult 
   SET VMeasurementResultGroupID=$1 
   WHERE VMeasurementResultID=$2

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qdelVMeasurementResultRemoveMasterID
--
-- RETURNS void
-- IN PARAMETERS
---- paramVMeasurementResultMasterID(uuid) 
---- paramVMeasurementResultID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qdelVMeasurementResultRemoveMasterID( 
  IN paramVMeasurementResultMasterID uuid,
  IN paramVMeasurementResultID uuid
) RETURNS void AS $$

  DELETE 
   FROM tblVMeasurementResult 
   WHERE VMeasurementResultMasterID=$1 AND VMeasurementResultID<>$2

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qappVMeasurementResultOpIndex
--
-- RETURNS void
-- IN PARAMETERS
---- paramNewVMeasurementResultID(uuid) 
---- paramVMeasurementID(uuid) 
---- paramVMeasurementResultMasterID(uuid) 
---- ownerUserID(integer) 
---- paramCurrentVMeasurementResultID(uuid) 

CREATE OR REPLACE FUNCTION cpgdbj.qappVMeasurementResultOpIndex( 
  IN paramNewVMeasurementResultID uuid,
  IN paramVMeasurementID uuid,
  IN paramVMeasurementResultMasterID uuid,
  IN ownerUserID integer,
  IN paramCurrentVMeasurementResultID uuid
) RETURNS void AS $$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, RadiusID, IsReconciled, StartYear, DatingTypeID, DatingErrorPositive, DatingErrorNegative, IsLegacyCleaned, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultMasterID, OwnerUserID, Code, Comments, isPublished ) 
  SELECT $1 AS Expr1, $2 AS Expr2, r.RadiusID, r.IsReconciled, r.StartYear, r.DatingTypeID, r.DatingErrorPositive, r.DatingErrorNegative, r.IsLegacyCleaned, v.CreatedTimestamp, v.LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, v.Code, v.Comments, v.isPublished 
   FROM tblVMeasurementResult r 
   INNER JOIN tblVMeasurement AS v 
      ON v.VMeasurementID = r.VMeasurementID 
   WHERE r.VMeasurementResultID=$5

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qacqVMeasurementReadingResult
--
-- IN PARAMETERS
---- paramCurrentVMeasurementResultID(uuid) 
-- OUT PARAMETERS
---- RelYear(integer) 
---- Reading(integer) 
-- RETURNS SETOF record

CREATE OR REPLACE FUNCTION cpgdbj.qacqVMeasurementReadingResult( 
  IN paramCurrentVMeasurementResultID uuid,
  OUT RelYear integer,
  OUT Reading integer
) RETURNS SETOF record AS $$

  SELECT RelYear, Reading from tblVMeasurementReadingResult 
   WHERE VMeasurementResultID=$1 
   ORDER BY RelYear ASC

$$ LANGUAGE SQL STABLE;

--
-- cpgdbj.qappVMeasurementResultOpSum
--
-- IN PARAMETERS
---- paramNewVMeasurementResultID(uuid) 
---- paramVMeasurementID(uuid) 
---- paramVMeasurementResultMasterID(uuid) 
---- ownerUserID(integer) 
---- paramVMeasurementResultGroupID(uuid) 
-- RETURNS void

CREATE OR REPLACE FUNCTION cpgdbj.qappVMeasurementResultOpSum( 
  IN paramNewVMeasurementResultID uuid,
  IN paramVMeasurementID uuid,
  IN paramVMeasurementResultMasterID uuid,
  IN ownerUserID integer,
  IN paramVMeasurementResultGroupID uuid
) RETURNS void AS $$

  INSERT INTO tblVMeasurementResult ( VMeasurementResultID, VMeasurementID, StartYear, DatingTypeID, CreatedTimestamp, LastModifiedTimestamp, VMeasurementResultMasterID, OwnerUserID, Code) 
  SELECT $1 AS Expr1, $2 AS Expr2, Min(r.StartYear) AS MinOfStartYear, Max(r.DatingTypeID) AS MaxOfDatingTypeID, now() AS CreatedTimestamp, now() AS LastModifiedTimestamp, $3 AS Expr5, $4 AS Expr6, 'SUM' AS Code 
   FROM tblVMeasurementResult r 
   WHERE r.VMeasurementResultGroupID=$5

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qappVMeasurementResultReadingOpSum
--
-- IN PARAMETERS
---- paramNewVMeasurementResultGroupID(uuid) 
---- paramNewVMeasurementResultID(uuid) 
-- RETURNS integer

CREATE OR REPLACE FUNCTION cpgdbj.qappVMeasurementResultReadingOpSum( 
  IN paramNewVMeasurementResultGroupID uuid,
  IN paramNewVMeasurementResultID uuid
) RETURNS integer AS $$

  SELECT * from cpgdb.qappVMeasurementResultReadingOpSum($1, $2)

$$ LANGUAGE SQL VOLATILE;

--
-- cpgdbj.qappVMeasurementReadingNoteResult
--
-- IN PARAMETERS
---- paramVMeasurementResultID(uuid) 
-- RETURNS void

CREATE OR REPLACE FUNCTION cpgdbj.qappVMeasurementReadingNoteResult( 
  IN paramVMeasurementResultID uuid
) RETURNS void AS $$

  INSERT INTO tblVMeasurementReadingNoteResult ( VMeasurementResultID, RelYear, ReadingNoteID ) 
  SELECT vmrr.VMeasurementResultID, vmrr.RelYear, rrn.ReadingNoteID 
   FROM tblVMeasurementReadingResult vmrr 
   INNER JOIN tblReadingReadingNote rrn 
      ON vmrr.readingID=rrn.readingID 
   WHERE vmrr.vMeasurementResultID=$1;

$$ LANGUAGE SQL VOLATILE;
