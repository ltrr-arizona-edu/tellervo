: Add early and late wood fields to tables

ALTER TABLE tblvmeasurementreadingresult ADD COLUMN ewwidth INTEGER;
ALTER TABLE tblvmeasurementreadingresult ADD COLUMN lwwidth INTEGER;
ALTER TABLE tblreading ADD COLUMN ewwidth INTEGER;
ALTER TABLE tblreading ADD COLUMN lwwidth INTEGER;



: Modify function to include the early and late wood fields in generated tables

DROP FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer);
CREATE OR REPLACE FUNCTION cpgdbj.qappvmeasurementreadingresult(paramvmeasurementresultid uuid, parammeasurementid integer)
  RETURNS void AS
$BODY$

  INSERT INTO tblVMeasurementReadingResult ( RelYear, Reading, VMeasurementResultID, ReadingID, ewwidth, lwwidth ) 
  SELECT tblReading.RelYear, tblReading.Reading, $1 AS Expr1, tblReading.readingID, tblReading.ewwidth, tblReading.lwwidth
   FROM tblReading 
   WHERE tblReading.MeasurementID=$2

$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer) OWNER TO lucasm;
GRANT EXECUTE ON FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer) TO lucasm;
GRANT EXECUTE ON FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer) TO public;
GRANT EXECUTE ON FUNCTION cpgdbj.qappvmeasurementreadingresult(uuid, integer) TO "Webgroup";


DROP VIEW vwjsonnotedreadingresult;
CREATE OR REPLACE VIEW vwjsonnotedreadingresult AS 
 SELECT res.vmeasurementreadingresultid, res.vmeasurementresultid, res.relyear, res.reading, res.wjinc, res.wjdec, res.count, res.readingid, res.ewwidth, res.lwwidth, cpgdb.resultnotestojson(notes.noteids, notes.inheritedcounts) AS jsonnotes
   FROM tblvmeasurementreadingresult res
   LEFT JOIN vwresultnotesasarray notes ON res.vmeasurementresultid = notes.vmeasurementresultid AND res.relyear = notes.relyear;

ALTER TABLE vwjsonnotedreadingresult OWNER TO lucasm;
GRANT ALL ON TABLE vwjsonnotedreadingresult TO lucasm;
GRANT ALL ON TABLE vwjsonnotedreadingresult TO "Webgroup";

: Add new variables to lookup table

INSERT INTO tlkpmeasurementvariable (measurementvariableid, measurementvariable) values (2, 'earlywood width');
INSERT INTO tlkpmeasurementvariable (measurementvariableid, measurementvariable) values (3, 'latewood width');



:
: Update the the minimum version of the Corina client that this server supports
:
BEGIN;
DELETE FROM tblsupportedclient where client='Corina WSI';
INSERT INTO tblsupportedclient (client, minversion) VALUES ('Corina WSI', '2.13');
COMMIT;
