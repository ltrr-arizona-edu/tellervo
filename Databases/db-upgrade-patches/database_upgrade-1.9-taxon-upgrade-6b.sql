
CREATE OR REPLACE FUNCTION cpgdb.getvmeasurementsummaryinfo(uuid) RETURNS public.typvmeasurementsummaryinfo
    LANGUAGE plpgsql STABLE
    AS $_$
DECLARE
   VMID ALIAS FOR $1;
   numrows integer;
   taxondepth integer;

   curtf text;
   prevtf text;

   rec record;
   curtaxa typfulltaxonomy;
   prevtaxa typfulltaxonomy;

   ret typVMeasurementSummaryInfo;   
BEGIN
   ret.VMeasurementID := VMID;
   numrows := 0;

   FOR rec IN SELECT s.code 
	FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tblObject s on s.objectID = t.objectID
	WHERE d.VMeasurementID = VMID
	GROUP BY(s.code)   
   LOOP
      IF numrows = 0 THEN
         ret.objectCode = rec.code;
      ELSE
         ret.objectCode = ret.objectCode || ';' || rec.code;
      END IF;
      numrows := numrows + 1;
   END LOOP;
   ret.objectCount = numrows;   

   numrows := 0;
   FOR rec IN SELECT txbasic.*, cpgdb.qryTaxonomy(txbasic.taxonID) as tx FROM
 	(SELECT t.taxonID::varchar FROM tblVMeasurementDerivedCache d
	INNER JOIN tblMeasurement m ON m.MeasurementID = d.MeasurementID
	INNER JOIN tblRadius r on r.radiusID = m.radiusID
	INNER JOIN tblSample sp on sp.sampleID = r.sampleID
	INNER JOIN tblElement t on t.elementID = sp.elementID
	INNER JOIN tlkpTaxon tx on tx.taxonID = t.taxonID
	WHERE d.VMeasurementID = VMID
	GROUP BY(t.taxonID)) as txbasic
   LOOP
      curtaxa := rec.tx;

      IF numrows = 0 THEN
         taxondepth := 15;
         LOOP
            curtf := cpgdb._getTaxonForDepth(taxondepth, curtaxa);
            EXIT WHEN curtf IS NOT NULL;
            taxondepth := taxondepth - 1;
         END LOOP;
      ELSE
         LOOP
            curtf := cpgdb._getTaxonForDepth(taxondepth, curtaxa);
            prevtf := cpgdb._getTaxonForDepth(taxondepth, prevtaxa);            
            EXIT WHEN curtf = prevtf OR taxondepth = 1;
            taxondepth := taxondepth - 1;
         END LOOP;
      END IF;

      numrows := numrows + 1;
      prevtaxa := curtaxa;
   END LOOP;

   ret.taxonCount := numrows;
   ret.commonTaxonName := curtf;
   
   RETURN ret;
END;
$_$;


CREATE OR REPLACE FUNCTION cpgdb.ElementLocationChangedTrigger() RETURNS trigger AS $$
DECLARE
   vmid tblVMeasurement.VMeasurementID%TYPE;
   newextent geometry;
BEGIN
   -- no changes? don't bother!
   IF NEW.locationgeometry = OLD.locationgeometry THEN
      RETURN NEW;
   END IF;

   -- update all child vmeasurement extents
   FOR vmid IN SELECT vmeasurementID FROM cpgdb.FindChildrenOf('Element', NEW.elementID) LOOP
      -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
      SELECT st_setsrid(extent(tblelement.locationgeometry)::geometry,4326)
         INTO  newextent
         FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
         WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
         AND   tblmeasurement.radiusid=tblradius.radiusid
         AND   tblradius.sampleid=tblsample.sampleid
         AND   tblsample.elementid=tblelement.elementid
         AND   tblvmeasurement.vmeasurementid
            IN (SELECT vMeasurementid
                   FROM  cpgdb.FindVMParents(vmid, true)
                   WHERE FindVMParents.op='Direct');

      -- set the extent in the metacache
      UPDATE tblVMeasurementMetaCache SET vmextent=newextent WHERE vMeasurementID=vmid;

   END LOOP;

   RETURN NEW;
EXCEPTION
   WHEN internal_error THEN
      RAISE NOTICE 'WARNING: Failed to update child extents for %', NEW.elementID;
      RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

DROP TRIGGER update_vmeasurementcache_extents ON tblElement;
CREATE TRIGGER update_vmeasurementcache_extents AFTER UPDATE ON tblElement
   FOR EACH ROW EXECUTE PROCEDURE cpgdb.ElementLocationChangedTrigger();





CREATE OR REPLACE FUNCTION cpgdb.createmetacache(uuid)
  RETURNS tblvmeasurementmetacache AS
$BODY$
DECLARE
   vmid ALIAS FOR $1;
   op varchar;
   vmresult tblVMeasurementResult%ROWTYPE;
   ret tblVMeasurementMetaCache%ROWTYPE;
BEGIN
   -- RAISE NOTICE 'Creating metacache for %', vmid;

   -- acquire the vMeasurementresult
   BEGIN
      SELECT * INTO vmresult FROM cpgdb.GetVMeasurementResult(vmid);
   EXCEPTION WHEN OTHERS THEN
      RAISE NOTICE 'CreateMetaCache(%) failed: VMeasurement is malformed or does not exist', vmid;
      RETURN NULL;
   END;

   IF NOT FOUND THEN
      RETURN NULL;
   END IF;

   ret.VMeasurementID := vmid;
   ret.StartYear := vmresult.StartYear;
   ret.datingTypeID := vmresult.datingTypeID;

   -- Calculate number of readings
   SELECT COUNT(*) INTO ret.ReadingCount
      FROM tblVMeasurementReadingResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Calculate number of measurements
   SELECT FIRST(tlkpVMeasurementOp.Name), COUNT(tblVMeasurementGroup.VMeasurementID) 
      INTO op, ret.MeasurementCount
      FROM tblVMeasurement 
      INNER JOIN tlkpVMeasurementOp ON tblVMeasurement.VMeasurementOpID = tlkpVMeasurementOp.VMeasurementOpID 
      LEFT JOIN tblVMeasurementGroup ON tblVMeasurement.VMeasurementID = tblVMeasurementGroup.VMeasurementID 
      WHERE tblVMeasurement.VMeasurementID = vmid;

   -- For a Direct VMeasurement, force 1.
   IF op = 'Direct' THEN
      ret.MeasurementCount := 1;
   END IF;

   -- Delete and populate the cache
   DELETE FROM tblVMeasurementMetaCache WHERE VMeasurementID = vmid;
   INSERT INTO tblVMeasurementMetaCache(VMeasurementID, StartYear, ReadingCount, MeasurementCount, DatingTypeID)
      VALUES (ret.VMeasurementID, ret.StartYear, ret.ReadingCount, ret.MeasurementCount, ret.datingTypeID);

   -- Clean up
   DELETE FROM tblVMeasurementResult WHERE VMeasurementResultID = vmresult.VMeasurementResultID;

   -- Clear out our tblVMeasurementDerivedCache for this VMeasurement
   DELETE FROM tblVMeasurementDerivedCache WHERE VMeasurementID = vmid;
   -- Then, populate it.
   INSERT INTO tblVMeasurementDerivedCache(VMeasurementID,MeasurementID) 
      SELECT vmid,Measurement.MeasurementID FROM cpgdb.FindVMParentMeasurements(vmid) Measurement;

   -- Calculate extent of vmeasurement by looking up locations of all associated direct Measurements
  -- SELECT st_setsrid(extent(tblelement.locationgeometry)::geometry,4326)
   --   INTO  ret.vmextent
    --  FROM  tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
    --  WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
    --  AND   tblmeasurement.radiusid=tblradius.radiusid
    --  AND   tblradius.sampleid=tblsample.sampleid
    --  AND   tblsample.elementid=tblelement.elementid
    --  AND   tblvmeasurement.vmeasurementid
      --      IN (SELECT vMeasurementid
      --             FROM  cpgdb.FindVMParents(vmid, true)
       --            WHERE op='Direct');

   -- Calculate extent using all associated element and object geometries
   IF op = 'Direct' THEN
   RAISE NOTICE 'Getting extents from element and object.  Measurement count is %', ret.MeasurementCount;
	   SELECT st_setsrid(extent(st_collect(tblelement.locationgeometry, tblobject.locationgeometry)), 4326)
	      INTO  ret.vmextent
	      FROM  tblobject, tblelement, tblsample, tblradius, tblMeasurement, tblvmeasurement
	      WHERE tblvmeasurement.measurementid=tblmeasurement.measurementid
	      AND   tblmeasurement.radiusid=tblradius.radiusid
	      AND   tblradius.sampleid=tblsample.sampleid
	      AND   tblsample.elementid=tblelement.elementid
	      AND   tblelement.objectid=tblobject.objectid
	      AND   tblvmeasurement.vmeasurementid
		    IN (SELECT vMeasurementid
			   FROM  cpgdb.FindVMParents(vmid, true)
			   WHERE FindVMParents.op='Direct');
   
   -- Calculate extent of vmeasurement by looking up locations of all associated direct VMeasurements
   ELSE 
	RAISE NOTICE 'Getting extent from component vmeasurements';
	RAISE NOTICE 'Find all parents of %', vmid;
	RAISE NOTICE 'Number of measurements is %',ret.MeasurementCount;
	   SELECT st_setsrid(extent(tblvmeasurementmetacache.vmextent)::geometry, 4326)
	   INTO  ret.vmextent
	   FROM  tblvmeasurementmetacache
	   WHERE tblvmeasurementmetacache.vmeasurementid
		 IN (SELECT vMeasurementid
		   FROM  cpgdb.FindVMParents(vmid, false));
   END IF;


   RAISE NOTICE 'Extent is %', ret.vmextent;

   -- Store extent info
   UPDATE tblVMeasurementMetaCache SET vmextent = ret.vmextent WHERE VMeasurementID = ret.VMeasurementID;

   -- Now, get taxon and label data and update that
   SELECT INTO ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix 
       s.objectCode,s.objectCount,s.commonTaxonName,s.taxonCount,cpgdb.GetVMeasurementPrefix(vmid) AS prefix
       FROM cpgdb.getVMeasurementSummaryInfo(vmid) AS s;
   UPDATE tblVMeasurementMetaCache SET (objectCode, objectCount, commonTaxonName, taxonCount, prefix) =
       (ret.objectCode, ret.objectCount, ret.CommonTaxonName, ret.taxonCount, ret.prefix)
       WHERE VMeasurementID = vmid;

   -- Return result
   RETURN ret;
END;

$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION cpgdb.rebuildmetacacheforelement()
  RETURNS trigger AS
$BODY$DECLARE
  uid uuid;
BEGIN
FOR uid IN SELECT vmeasurementid FROM cpgdb.FindChildrenOf('Element', NEW.elementid)
  LOOP
    PERFORM cpgdb.createmetacache(uid);
  END LOOP;
RETURN new;
END;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

UPDATE tblelement SET colid = tlkptaxon.colid FROM tlkptaxon WHERE tblelement.taxonid=tlkptaxon.taxonid;
CREATE TRIGGER update_element_rebuildmetacache 
  AFTER INSERT OR UPDATE 
  ON tblelement 
  FOR EACH ROW 
  EXECUTE PROCEDURE cpgdb.rebuildmetacacheforelement();
	
	ALTER TABLE tlkptaxon DROP CONSTRAINT "pkey_taxon";
	DROP VIEW IF EXISTS vwtblelement;
	DROP VIEW IF EXISTS vwtblelement2;
	DROP VIEW IF EXISTS vwtlkptaxon;
	DROP VIEW IF EXISTS vwportalcomb; 
	DROP VIEW IF EXISTS vwportaldata1;
	DROP VIEW IF EXISTS vwportaldata2;
	
	DROP VIEW IF EXISTS portal.vwportalcomb; 
	DROP VIEW IF EXISTS portal.vwportaldata1;
	DROP VIEW IF EXISTS portal.vwportaldata2;


ALTER TABLE tlkptaxon DROP COLUMN taxonid;
ALTER TABLE tlkptaxon ADD COLUMN taxonid varchar;
UPDATE tlkptaxon SET taxonid=colid;
ALTER TABLE tlkptaxon ADD CONSTRAINT "pkey_taxon" PRIMARY KEY (taxonid);
ALTER TABLE tlkptaxon DROP COLUMN parenttaxonid;
ALTER TABLE tlkptaxon ADD COLUMN parenttaxonid VARCHAR;
UPDATE tlkptaxon SET parenttaxonid=colparentid;
UPDATE tlkptaxon SET label=newlabel;
ALTER TABLE tlkptaxon DROP COLUMN newlabel;

CREATE VIEW vwtlkptaxon AS 
SELECT taxon.taxonid, 
taxon.label AS taxonlabel,
taxon.parenttaxonid,
taxon.colid,
taxon.colparentid,
rank.taxonrank
FROM tlkptaxon taxon
JOIN tlkptaxonrank rank ON rank.taxonrankid = taxon.taxonrankid;


DROP VIEW vw_tlotoradius;
DROP VIEW vw_elementtoradius;

ALTER TABLE tblelement DROP COLUMN taxonid;
ALTER TABLE tblelement RENAME COLUMN colid TO taxonid;
UPDATE tblelement SET taxonid='P' WHERE taxonid='';
ALTER TABLE public.tblelement ADD CONSTRAINT fkey_element_taxon FOREIGN KEY (taxonid) REFERENCES public.tlkptaxon (taxonid) ON UPDATE NO ACTION ON DELETE NO ACTION;




CREATE VIEW vwtblelement AS 
 SELECT ( SELECT findobjecttoplevelancestor.code
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) findobjecttoplevelancestor(objectid, title, code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, locationcomment, creator, owner, parentobjectid, description, objecttypeid, coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, coveragetemporalfoundation, locationaddressline1, locationaddressline2, locationcityortown, locationstateprovinceregion, locationpostalcode, locationcountry, locationcountry_1, vegetationtype, domainid, projectid)) AS objectcode,
    e.comments,
    dom.domainid,
    dom.domain,
    e.elementid,
    e.locationprecision,
    e.code AS title,
    e.code,
    e.createdtimestamp,
    e.lastmodifiedtimestamp,
    e.locationgeometry,
    ( SELECT st_asgml(3, e.locationgeometry, 15, 1) AS st_asgml) AS gml,
    st_xmin(e.locationgeometry::box3d) AS longitude,
    st_ymin(e.locationgeometry::box3d) AS latitude,
    e.islivetree,
    e.originaltaxonname,
    e.locationtypeid,
    e.locationcomment,
    e.locationaddressline1,
    e.locationaddressline2,
    e.locationcityortown,
    e.locationstateprovinceregion,
    e.locationpostalcode,
    e.locationcountry,
    array_to_string(e.file, '><'::text) AS file,
    e.description,
    e.processing,
    e.marks,
    e.diameter,
    e.width,
    e.height,
    e.depth,
    e.unsupportedxml,
    e.objectid,
    e.elementtypeid,
    e.authenticity,
    e.elementshapeid,
    shape.elementshape,
    tbltype.elementtype,
    loctype.locationtype,
    e.altitude,
    e.slopeangle,
    e.slopeazimuth,
    e.soildescription,
    e.soildepth,
    e.bedrockdescription,
    vwt.taxonid,
    vwt.taxonlabel,
    vwt.parenttaxonid,
    vwt.colid,
    vwt.colparentid,
    vwt.taxonrank,
    unit.unit AS units,
    unit.unitid,
    cpgdb.getbestgeometryid(e.elementid) AS bestgeometryid
   FROM tblelement e
     LEFT JOIN tlkpdomain dom ON e.domainid = dom.domainid
     LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
     LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
     LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
     LEFT JOIN tlkpunit unit ON e.units = unit.unitid
     LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;

CREATE VIEW vwtblelement2 AS 
 SELECT ( SELECT findobjecttoplevelancestor.objectid
           FROM cpgdb.findobjecttoplevelancestor(e.objectid) 
           findobjecttoplevelancestor(objectid, title, code, createdtimestamp, 
           lastmodifiedtimestamp, locationgeometry, locationtypeid, locationprecision, 
           locationcomment, creator, owner, parentobjectid, description, objecttypeid, 
           coveragetemporalid, coveragetemporalfoundationid, comments, coveragetemporal, 
           coveragetemporalfoundation, locationaddressline1, locationaddressline2, 
           locationcityortown, locationstateprovinceregion, locationpostalcode, 
           locationcountry, locationcountry_1, vegetationtype, domainid, projectid)) AS tlobjectid,
    e.comments,
    dom.domainid,
    dom.domain,
    e.elementid,
    e.locationprecision,
    e.code AS title,
    e.code,
    e.createdtimestamp,
    e.lastmodifiedtimestamp,
    e.locationgeometry,
    ( SELECT st_asgml(3, e.locationgeometry, 15, 1) AS st_asgml) AS gml,
    st_xmin(e.locationgeometry::box3d) AS longitude,
    st_ymin(e.locationgeometry::box3d) AS latitude,
    e.islivetree,
    e.originaltaxonname,
    e.locationtypeid,
    e.locationcomment,
    e.locationaddressline1,
    e.locationaddressline2,
    e.locationcityortown,
    e.locationstateprovinceregion,
    e.locationpostalcode,
    e.locationcountry,
    array_to_string(e.file, '><'::text) AS file,
    e.description,
    e.processing,
    e.marks,
    e.diameter,
    e.width,
    e.height,
    e.depth,
    e.unsupportedxml,
    e.objectid,
    e.elementtypeid,
    e.authenticity,
    e.elementshapeid,
    shape.elementshape,
    tbltype.elementtype,
    loctype.locationtype,
    e.altitude,
    e.slopeangle,
    e.slopeazimuth,
    e.soildescription,
    e.soildepth,
    e.bedrockdescription,
    vwt.taxonid,
    vwt.taxonlabel,
    vwt.parenttaxonid,
    vwt.colid,
    vwt.colparentid,
    vwt.taxonrank,
    unit.unit AS units,
    unit.unitid,
    cpgdb.getbestgeometryid(e.elementid) AS bestgeometryid
FROM tblelement e
     LEFT JOIN tlkpdomain dom ON e.domainid = dom.domainid
     LEFT JOIN tlkpelementshape shape ON e.elementshapeid = shape.elementshapeid
     LEFT JOIN tlkpelementtype tbltype ON e.elementtypeid = tbltype.elementtypeid
     LEFT JOIN tlkplocationtype loctype ON e.locationtypeid = loctype.locationtypeid
     LEFT JOIN tlkpunit unit ON e.units = unit.unitid
     LEFT JOIN vwtlkptaxon vwt ON e.taxonid = vwt.taxonid;


CREATE VIEW vw_elementtoradius AS 
SELECT cpgdb.tloid(e.*) AS tlo_objectid,
    e.elementid AS e_elementid,
    e.taxonid AS e_taxonid,
    e.locationprecision AS e_locationprecision,
    e.code AS e_code,
    e.createdtimestamp AS e_createdtimestamp,
    e.lastmodifiedtimestamp AS e_lastmodifiedtimestamp,
    e.locationgeometry AS e_locationgeometry,
    e.islivetree AS e_islivetree,
    e.originaltaxonname AS e_originaltaxonname,
    e.locationtypeid AS e_locationtypeid,
    e.locationcomment AS e_locationcomment,
    e.file AS e_file,
    e.description AS e_description,
    e.processing AS e_processing,
    e.marks AS e_marks,
    e.diameter AS e_diameter,
    e.width AS e_width,
    e.height AS e_height,
    e.depth AS e_depth,
    e.unsupportedxml AS e_unsupportedxml,
    e.unitsold AS e_units,
    e.elementtypeid AS e_elementtypeid,
    e.authenticity AS e_authenticity,
    e.elementshapeid AS e_elementshapeid,
    e.altitudeint AS e_altitudeint,
    e.slopeangle AS e_slopeangle,
    e.slopeazimuth AS e_slopeazimuth,
    e.soildescription AS e_soildescription,
    e.soildepth AS e_soildepth,
    e.bedrockdescription AS e_bedrockdescription,
    e.comments AS e_comments,
    e.locationaddressline2 AS e_locationaddressline2,
    e.locationcityortown AS e_locationcityortown,
    e.locationstateprovinceregion AS e_locationstateprovinceregion,
    e.locationpostalcode AS e_locationpostalcode,
    e.locationcountry AS e_locationcountry,
    e.locationaddressline1 AS e_locationaddressline1,
    e.altitude AS e_altitude,
    e.gispkey AS e_gispkey,
    s.sampleid AS s_sampleid,
    s.code AS s_code,
    s.samplingdate AS s_samplingdate,
    s.createdtimestamp AS s_createdtimestamp,
    s.lastmodifiedtimestamp AS s_lastmodifiedtimestamp,
    s.type AS s_type,
    s.identifierdomain AS s_identifierdomain,
    s.file AS s_file,
    s."position" AS s_position,
    s.state AS s_state,
    s.knots AS s_knots,
    s.description AS s_description,
    s.datecertaintyid AS s_datecertaintyid,
    s.typeid AS s_typeid,
    s.boxid AS s_boxid,
    s.comments AS s_comments,
    r.radiusid AS r_radiusid,
    r.code AS r_code,
    r.createdtimestamp AS r_createdtimestamp,
    r.lastmodifiedtimestamp AS r_lastmodifiedtimestamp,
    r.numberofsapwoodrings AS r_numberofsapwoodrings,
    r.pithid AS r_pithid,
    r.barkpresent AS r_barkpresent,
    r.lastringunderbark AS r_lastringunderbark,
    r.missingheartwoodringstopith AS r_missingheartwoodringstopith,
    r.missingheartwoodringstopithfoundation AS r_missingheartwoodringstopithfoundation,
    r.missingsapwoodringstobark AS r_missingsapwoodringstobark,
    r.missingsapwoodringstobarkfoundation AS r_missingsapwoodringstobarkfoundation,
    r.sapwoodid AS r_sapwoodid,
    r.heartwoodid AS r_heartwoodid,
    r.azimuth AS r_azimuth,
    r.comments AS r_comments,
    r.lastringunderbarkpresent AS r_lastringunderbarkpresent,
    r.nrofunmeasuredinnerrings AS r_nrofunmeasuredinnerrings,
    r.nrofunmeasuredouterrings AS r_nrofunmeasuredouterrings
   FROM tblelement e
LEFT JOIN tblsample s ON e.elementid = s.elementid
LEFT JOIN tblradius r ON s.sampleid = r.sampleid;



CREATE VIEW vw_tlotoradius AS 
SELECT tlo.title AS tlo_title,
    tlo.code AS tlo_code,
    tlo.createdtimestamp AS tlo_createdtimestamp,
    tlo.lastmodifiedtimestamp AS tlo_lastmodifiedtimestamp,
    tlo.locationgeometry AS tlo_locationgeometry,
    tlo.locationtypeid AS tlo_locationtypeid,
    tlo.locationprecision AS tlo_locationprecision,
    tlo.locationcomment AS tlo_locationcomment,
    tlo.file AS tlo_file,
    tlo.creator AS tlo_creator,
    tlo.owner AS tlo_owner,
    tlo.parentobjectid AS tlo_parentobjectid,
    tlo.description AS tlo_description,
    tlo.objecttypeid AS tlo_objecttypeid,
    tlo.coveragetemporalid AS tlo_coveragetemporalid,
    tlo.coveragetemporalfoundationid AS tlo_coveragetemporalfoundationid,
    tlo.comments AS tlo_comments,
    tlo.coveragetemporal AS tlo_coveragetemporal,
    tlo.coveragetemporalfoundation AS tlo_coveragetemporalfoundation,
    tlo.locationaddressline1 AS tlo_locationaddressline1,
    tlo.locationaddressline2 AS tlo_locationaddressline2,
    tlo.locationcityortown AS tlo_locationcityortown,
    tlo.locationstateprovinceregion AS tlo_locationstateprovinceregion,
    tlo.locationpostalcode AS tlo_locationpostalcode,
    tlo.locationcountry AS tlo_locationcountry,
    others.tlo_objectid,
    others.e_elementid,
    others.e_taxonid,
    others.e_locationprecision,
    others.e_code,
    others.e_createdtimestamp,
    others.e_lastmodifiedtimestamp,
    others.e_locationgeometry,
    others.e_islivetree,
    others.e_originaltaxonname,
    others.e_locationtypeid,
    others.e_locationcomment,
    others.e_file,
    others.e_description,
    others.e_processing,
    others.e_marks,
    others.e_diameter,
    others.e_width,
others.e_height,
    others.e_depth,
    others.e_unsupportedxml,
    others.e_units,
    others.e_elementtypeid,
    others.e_authenticity,
    others.e_elementshapeid,
    others.e_altitudeint,
    others.e_slopeangle,
    others.e_slopeazimuth,
    others.e_soildescription,
    others.e_soildepth,
    others.e_bedrockdescription,
    others.e_comments,
    others.e_locationaddressline2,
    others.e_locationcityortown,
    others.e_locationstateprovinceregion,
    others.e_locationpostalcode,
    others.e_locationcountry,
    others.e_locationaddressline1,
    others.e_altitude,
    others.e_gispkey,
    others.s_sampleid,
    others.s_code,
    others.s_samplingdate,
    others.s_createdtimestamp,
    others.s_lastmodifiedtimestamp,
    others.s_type,
    others.s_identifierdomain,
    others.s_file,
    others.s_position,
    others.s_state,
    others.s_knots,
    others.s_description,
    others.s_datecertaintyid,
    others.s_typeid,
    others.s_boxid,
    others.s_comments,
    others.r_radiusid,
    others.r_code,
    others.r_createdtimestamp,
    others.r_lastmodifiedtimestamp,
   others.r_numberofsapwoodrings,
    others.r_pithid,
    others.r_barkpresent,
    others.r_lastringunderbark,
    others.r_missingheartwoodringstopith,
    others.r_missingheartwoodringstopithfoundation,
    others.r_missingsapwoodringstobark,
    others.r_missingsapwoodringstobarkfoundation,
    others.r_sapwoodid,
    others.r_heartwoodid,
    others.r_azimuth,
    others.r_comments,
    others.r_lastringunderbarkpresent,
    others.r_nrofunmeasuredinnerrings,
    others.r_nrofunmeasuredouterrings
   FROM tblobject tlo
LEFT JOIN vw_elementtoradius others ON others.tlo_objectid = tlo.objectid;

DROP FUNCTION cpgdb.qrytaxonflat1;
DROP FUNCTION cpgdb.qrytaxonflat2;
DROP FUNCTION cpgdb.qrytaxonomy;
DROP FUNCTION cpgdb._gettaxonfordepth(integer, typfulltaxonomy);


DROP TYPE typtaxonrankname;
DROP TYPE typtaxonflat2;
DROP TYPE typfulltaxonomy;

CREATE TYPE typtaxonrankname AS (
    taxonid varchar,
    taxonrankid integer,
    taxonname character varying(128)
);


CREATE TYPE typtaxonflat2 AS (
	taxonid varchar,
	taxonrankid integer,
	taxonname character varying(128),
	taxonrank character varying(30),
	rankorder double precision
);

CREATE TYPE typfulltaxonomy AS (
	taxonid varchar,
	kingdom character varying(128),
	subkingdom character varying(128),
	phylum character varying(128),
	division character varying(128),
	class character varying(128),
	txorder character varying(128),
	family character varying(128),
	subfamily character varying(128),
	genus character varying(128),
	subgenus character varying(128),
	section character varying(128),
	subsection character varying(128),
	species character varying(128),
	subspecies character varying(128),
	race character varying(128),
	variety character varying(128),
	subvariety character varying(128),
	form character varying(128),
	subform character varying(128)
);


CREATE OR REPLACE FUNCTION cpgdb._gettaxonfordepth(integer, typfulltaxonomy)
  RETURNS text AS
$BODY$
   SELECT CASE $1
     WHEN 1 THEN $2.kingdom 
     WHEN 2 THEN $2.subkingdom
     WHEN 3 THEN $2.phylum
     WHEN 4 THEN $2.division
     WHEN 5 THEN $2.class
     WHEN 6 THEN $2.txorder
     WHEN 7 THEN $2.family
     WHEN 8 THEN $2.subfamily
     WHEN 9 THEN $2.genus
     WHEN 10 THEN $2.subgenus
     WHEN 11 THEN $2.section
     WHEN 12 THEN $2.subsection
     WHEN 13 THEN $2.species
     WHEN 14 THEN $2.subspecies
     WHEN 15 THEN $2.race
     WHEN 16 THEN $2.variety
     WHEN 17 THEN $2.subvariety
     WHEN 18 THEN $2.form
     WHEN 19 THEN $2.subform
     ELSE 'invalid'
   END;
$BODY$
  LANGUAGE sql IMMUTABLE
  COST 100;


CREATE OR REPLACE FUNCTION cpgdb.qrytaxonflat1(taxonid varchar)
  RETURNS SETOF typtaxonrankname AS
$BODY$SELECT $1 as taxonid, tlkptaxon.taxonrankid, tlkptaxon.label AS col0
FROM ((((((((((tlkptaxon 
                                        LEFT JOIN tlkptaxon AS tlkptaxon_1 ON tlkptaxon.taxonid = tlkptaxon_1.parenttaxonid) 
                                    LEFT JOIN tlkptaxon AS tlkptaxon_2 ON tlkptaxon_1.taxonid = tlkptaxon_2.parenttaxonid) 
                                LEFT JOIN tlkptaxon AS tlkptaxon_3 ON tlkptaxon_2.taxonid = tlkptaxon_3.parenttaxonid) 
                            LEFT JOIN tlkptaxon AS tlkptaxon_4 ON tlkptaxon_3.taxonid = tlkptaxon_4.parenttaxonid) 
                        LEFT JOIN tlkptaxon AS tlkptaxon_5 ON tlkptaxon_4.taxonid = tlkptaxon_5.parenttaxonid) 
                    LEFT JOIN tlkptaxon AS tlkptaxon_6 ON tlkptaxon_5.taxonid = tlkptaxon_6.parenttaxonid) 
                LEFT JOIN tlkptaxon AS tlkptaxon_7 ON tlkptaxon_6.taxonid = tlkptaxon_7.parenttaxonid) 
            LEFT JOIN tlkptaxon AS tlkptaxon_8 ON tlkptaxon_7.taxonid = tlkptaxon_8.parenttaxonid) 
        LEFT JOIN tlkptaxon AS tlkptaxon_9 ON tlkptaxon_8.taxonid = tlkptaxon_9.parenttaxonid) 
    LEFT JOIN tlkptaxon AS tlkptaxon_10 ON tlkptaxon_9.taxonid = tlkptaxon_10.parenttaxonid) 
LEFT JOIN tlkptaxon AS tlkptaxon_11 ON tlkptaxon_10.taxonid = tlkptaxon_11.parenttaxonid
WHERE 
   (((tlkptaxon.taxonid)=$1)) 
OR (((tlkptaxon_1.taxonid)=$1)) 
OR (((tlkptaxon_2.taxonid)=$1)) 
OR (((tlkptaxon_3.taxonid)=$1)) 
OR (((tlkptaxon_4.taxonid)=$1)) 
OR (((tlkptaxon_5.taxonid)=$1)) 
OR (((tlkptaxon_6.taxonid)=$1)) 
OR (((tlkptaxon_7.taxonid)=$1)) 
OR (((tlkptaxon_8.taxonid)=$1)) 
OR (((tlkptaxon_9.taxonid)=$1)) 
OR (((tlkptaxon_10.taxonid)=$1)) 
OR (((tlkptaxon_11.taxonid)=$1))
$BODY$
  LANGUAGE 'sql' VOLATILE;



CREATE OR REPLACE FUNCTION cpgdb.qrytaxonflat2(taxonid varchar)
  RETURNS SETOF typtaxonflat2 AS
$BODY$SELECT qrytaxonflat1.taxonid, 
qrytaxonflat1.taxonrankid, 
qrytaxonflat1.taxonname, 
tlkptaxonrank.taxonrank, 
tlkptaxonrank.rankorder
FROM cpgdb.qrytaxonflat1($1) qrytaxonflat1 
RIGHT JOIN tlkptaxonrank ON qrytaxonflat1.taxonrankid = tlkptaxonrank.taxonrankid 
ORDER BY tlkptaxonrank.rankorder ASC;$BODY$
  LANGUAGE 'sql' VOLATILE;


CREATE OR REPLACE FUNCTION cpgdb.qrytaxonomy(taxonid varchar)
  RETURNS typfulltaxonomy AS
$BODY$
SELECT * FROM crosstab(
    'select qrytaxonflat2.taxonid, qrytaxonflat2.taxonrank, qrytaxonflat2.taxonname 
    from cpgdb.qrytaxonflat2('''||$1||''') 
    order by taxonrank
    ', 
    'select taxonrank
    from tlkptaxonrank 
    order by rankorder asc'
) 
as 
(taxonid text, 
    kingdom text, 
    subkingdom text, 
    phylum text, 
    division text, 
    class text, 
    txorder text, 
    family text, 
    subfamily text,
    genus text,
    subgenus text, 
    section text,
    subsection text,
    species text, 
    subspecies text, 
    race text, 
    variety text, 
    subvariety text, 
    form text, 
    subform text);$BODY$
  LANGUAGE 'sql' VOLATILE;

COMMENT ON FUNCTION cpgdb.qrytaxonomy(taxonid varchar) IS 'This is a cross tab query that builds on qrytaxonflat1 and 2 to flatten out the entire taxonomic element for a given taxonid. ';

CREATE OR REPLACE VIEW vwfirstyear AS 
SELECT tbluserdefinedfieldvalue.entityid,
    tbluserdefinedfieldvalue.value
   FROM tbluserdefinedfieldvalue,
    tlkpuserdefinedfield
  WHERE tbluserdefinedfieldvalue.userdefinedfieldid = tlkpuserdefinedfield.userdefinedfieldid AND tlkpuserdefinedfield.userdefinedfieldid = '454f7ae1-cbdf-43c9-bf64-92ebd7842632'::uuid AND tbluserdefinedfieldvalue.value IS NOT NULL AND bit_length(tbluserdefinedfieldvalue.value::text) > 0;

CREATE OR REPLACE VIEW vwlastyear AS 
 SELECT tbluserdefinedfieldvalue.entityid,
    tbluserdefinedfieldvalue.value
   FROM tbluserdefinedfieldvalue,
    tlkpuserdefinedfield
  WHERE tbluserdefinedfieldvalue.userdefinedfieldid = tlkpuserdefinedfield.userdefinedfieldid AND tlkpuserdefinedfield.userdefinedfieldid = '41aba235-a06c-45f0-a07d-8e2000a93a5f'::uuid AND tbluserdefinedfieldvalue.value IS NOT NULL AND bit_length(tbluserdefinedfieldvalue.value::text) > 0;

CREATE OR REPLACE FUNCTION convert_to_integer(varchar) RETURNS INTEGER AS $$
DECLARE
   v_int_value INTEGER DEFAULT NULL;
BEGIN
	BEGIN 
    	v_int_value := v_input::INTEGER; 
    EXCEPTION WHEN OTHERS THEN           
    	RETURN NULL;          
    END; 
    
RETURN v_int_value;    
END;
$$ LANGUAGE PLPGSQL VOLATILE;


CREATE OR REPLACE VIEW portal.vwportaldata1 AS 
 SELECT p.projectid,
    p.lastmodifiedtimestamp AS projectlastmodifiedtimestamp,
    p.title AS projecttitle,
    p.investigator,
    o.objectid,
    o.lastmodifiedtimestamp AS objectlastmodifiedtimestamp,
    o.title AS objecttitle,
    o.code AS objectcode,
    xmin(o.locationgeometry::box3d) AS objectlongitude,
    ymin(o.locationgeometry::box3d) AS objectlatitude,
    o.locationprecision AS objectlocationprecision,
    NULL::uuid AS object2id,
    NULL::timestamp with time zone AS object2lastmodifiedtimestamp,
    NULL::character varying AS object2title,
    NULL::character varying AS object2code,
    NULL::double precision AS object2longitude,
    NULL::double precision AS object2latitude,
    NULL::integer AS object2locationprecision,
    e.elementid,
    e.lastmodifiedtimestamp AS elementlastmodifiedtimestamp,
    e.code AS elementcode,
    e.taxonid,
    t.label AS taxonlabel,
    t.htmllabel AS taxonlabelhtml,
    xmin(e.locationgeometry::box3d) AS elementlongitude,
    ymin(e.locationgeometry::box3d) AS elementlatitude,
    e.locationprecision AS elementlocationprecision,
    s.sampleid,
    s.lastmodifiedtimestamp AS samplelastmodifiedtimestamp,
    s.code AS samplecode,
    s.externalid,
    s.samplingdate,
    s.samplingyear,
    s.samplingmonth,
    s.samplingdateprec,
    st.sampletype,
    convert_to_integer(fy.value::text) AS firstyear,
    convert_to_integer(ly.value::text) AS lastyear,
    s.dendrochronologist,
    b.boxid,
    b.title AS boxtitle,
    b.curationlocation,
    b.trackinglocation,
    cpgdb.preferreddouble(xmin(e.locationgeometry::box3d), NULL::double precision, xmin(o.locationgeometry::box3d)) AS preferredlongitude,
    cpgdb.preferreddouble(ymin(e.locationgeometry::box3d), NULL::double precision, ymin(o.locationgeometry::box3d)) AS preferredlatitude,
    cpgdb.preferreddouble(e.locationprecision, NULL::double precision, o.locationprecision::double precision) AS preferredlocationprecision
   FROM tblproject p
     LEFT JOIN tblobject o ON p.projectid = o.projectid
     LEFT JOIN tblelement e ON o.objectid = e.objectid
     LEFT JOIN tlkptaxon t ON e.taxonid::text = t.taxonid::text
     LEFT JOIN tblsample s ON e.elementid = s.elementid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN tblbox b ON s.boxid = b.boxid
     LEFT JOIN vwfirstyear fy ON s.sampleid = fy.entityid
     LEFT JOIN vwlastyear ly ON s.sampleid = ly.entityid
  WHERE o.parentobjectid IS NULL AND s.sampleid IS NOT NULL;

CREATE OR REPLACE VIEW portal.vwportaldata2 AS 
 SELECT p.projectid,
    p.lastmodifiedtimestamp AS projectlastmodifiedtimestamp,
    p.title AS projecttitle,
    p.investigator,
    o.objectid,
    o.lastmodifiedtimestamp AS objectlastmodifiedtimestamp,
    o.title AS objecttitle,
    o.code AS objectcode,
    xmin(o.locationgeometry::box3d) AS objectlongitude,
    ymin(o.locationgeometry::box3d) AS objectlatitude,
    o.locationprecision AS objectlocationprecision,
    o2.objectid AS object2id,
    o2.lastmodifiedtimestamp AS object2lastmodifiedtimestamp,
    o2.title AS object2title,
    o2.code AS object2code,
    xmin(o2.locationgeometry::box3d) AS object2longitude,
    ymin(o2.locationgeometry::box3d) AS object2latitude,
    o2.locationprecision AS object2locationprecision,
    e.elementid,
    e.lastmodifiedtimestamp AS elementlastmodifiedtimestamp,
    e.code AS elementcode,
    e.taxonid,
    t.label AS taxonlabel,
    t.htmllabel AS taxonlabelhtml,
    xmin(e.locationgeometry::box3d) AS elementlongitude,
    ymin(e.locationgeometry::box3d) AS elementlatitude,
    e.locationprecision AS elementlocationprecision,
    s.sampleid,
    s.lastmodifiedtimestamp AS samplelastmodifiedtimestamp,
    s.code AS samplecode,
    s.externalid,
    s.samplingdate,
    s.samplingyear,
    s.samplingmonth,
    s.samplingdateprec,
    st.sampletype,
    convert_to_integer(fy.value::text) AS firstyear,
    convert_to_integer(ly.value::text) AS lastyear,
    s.dendrochronologist,
    b.boxid,
    b.title AS boxtitle,
    b.curationlocation,
    b.trackinglocation,
    cpgdb.preferreddouble(xmin(e.locationgeometry::box3d), xmin(o2.locationgeometry::box3d), xmin(o.locationgeometry::box3d)) AS preferredlongitude,
    cpgdb.preferreddouble(ymin(e.locationgeometry::box3d), ymin(o2.locationgeometry::box3d), ymin(o.locationgeometry::box3d)) AS preferredlatitude,
    cpgdb.preferreddouble(e.locationprecision, o2.locationprecision::double precision, o.locationprecision::double precision) AS preferredlocationprecision
   FROM tblproject p
     LEFT JOIN tblobject o ON p.projectid = o.projectid
     LEFT JOIN tblobject o2 ON o.objectid = o2.parentobjectid
     LEFT JOIN tblelement e ON o.objectid = e.objectid
     LEFT JOIN tlkptaxon t ON e.taxonid::text = t.taxonid::text
     LEFT JOIN tblsample s ON e.elementid = s.elementid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN tblbox b ON s.boxid = b.boxid
     LEFT JOIN vwfirstyear fy ON s.sampleid = fy.entityid
     LEFT JOIN vwlastyear ly ON s.sampleid = ly.entityid
  WHERE o2.parentobjectid IS NOT NULL AND s.sampleid IS NOT NULL;


CREATE OR REPLACE VIEW portal.vwportalcomb AS 
 SELECT vwportaldata1.projectid,
    vwportaldata1.projectlastmodifiedtimestamp,
    vwportaldata1.projecttitle,
    vwportaldata1.investigator,
    vwportaldata1.objectid,
    vwportaldata1.objectlastmodifiedtimestamp,
    vwportaldata1.objecttitle,
    vwportaldata1.objectcode,
    vwportaldata1.objectlongitude,
    vwportaldata1.objectlatitude,
    vwportaldata1.objectlocationprecision,
    vwportaldata1.object2id,
    vwportaldata1.object2lastmodifiedtimestamp,
    vwportaldata1.object2title,
    vwportaldata1.object2code,
    vwportaldata1.object2longitude,
    vwportaldata1.object2latitude,
    vwportaldata1.object2locationprecision,
    vwportaldata1.elementid,
    vwportaldata1.elementlastmodifiedtimestamp,
    vwportaldata1.elementcode,
    vwportaldata1.taxonid,
    vwportaldata1.taxonlabel,
    vwportaldata1.taxonlabelhtml,    
    vwportaldata1.elementlongitude,
    vwportaldata1.elementlatitude,
    vwportaldata1.elementlocationprecision,
    vwportaldata1.sampleid,
    vwportaldata1.samplelastmodifiedtimestamp,
    vwportaldata1.samplecode,
    vwportaldata1.externalid,
    vwportaldata1.samplingdate,
    vwportaldata1.samplingyear,
    vwportaldata1.samplingmonth,
    vwportaldata1.samplingdateprec,
    vwportaldata1.sampletype,
    vwportaldata1.firstyear,
    vwportaldata1.lastyear,
    vwportaldata1.dendrochronologist,
    vwportaldata1.boxid,
    vwportaldata1.boxtitle,
    vwportaldata1.curationlocation,
    vwportaldata1.trackinglocation,
    vwportaldata1.preferredlongitude,
    vwportaldata1.preferredlatitude,
    vwportaldata1.preferredlocationprecision
   FROM portal.vwportaldata1 vwportaldata1
UNION
 SELECT vwportaldata2.projectid,
    vwportaldata2.projectlastmodifiedtimestamp,
    vwportaldata2.projecttitle,
    vwportaldata2.investigator,
    vwportaldata2.objectid,
    vwportaldata2.objectlastmodifiedtimestamp,
    vwportaldata2.objecttitle,
    vwportaldata2.objectcode,
    vwportaldata2.objectlongitude,
    vwportaldata2.objectlatitude,
    vwportaldata2.objectlocationprecision,
    vwportaldata2.object2id,
    vwportaldata2.object2lastmodifiedtimestamp,
    vwportaldata2.object2title,
    vwportaldata2.object2code,
    vwportaldata2.object2longitude,
    vwportaldata2.object2latitude,
    vwportaldata2.object2locationprecision,
    vwportaldata2.elementid,
    vwportaldata2.elementlastmodifiedtimestamp,
    vwportaldata2.elementcode,
    vwportaldata2.taxonid,
    vwportaldata2.taxonlabel,
    vwportaldata2.taxonlabelhtml,
    vwportaldata2.elementlongitude,
    vwportaldata2.elementlatitude,
    vwportaldata2.elementlocationprecision,
    vwportaldata2.sampleid,
    vwportaldata2.samplelastmodifiedtimestamp,
    vwportaldata2.samplecode,
    vwportaldata2.externalid,
    vwportaldata2.samplingdate,
    vwportaldata2.samplingyear,
    vwportaldata2.samplingmonth,
    vwportaldata2.samplingdateprec,
    vwportaldata2.sampletype,
    vwportaldata2.firstyear,
    vwportaldata2.lastyear,
    vwportaldata2.dendrochronologist,
    vwportaldata2.boxid,
    vwportaldata2.boxtitle,
    vwportaldata2.curationlocation,
    vwportaldata2.trackinglocation,
    vwportaldata2.preferredlongitude,
    vwportaldata2.preferredlatitude,
    vwportaldata2.preferredlocationprecision
   FROM portal.vwportaldata2 vwportaldata2;


 

