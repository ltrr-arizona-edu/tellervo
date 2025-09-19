
----------------------------------------------------
-- UPGRADE vwtblsample to include userdefinedfields 
----------------------------------------------------

DROP VIEW vwipt;
DROP VIEW vwtblsample2;


CREATE VIEW vwtbluserdefinedfieldvalue AS 
 SELECT f.userdefinedfieldid,
    f.fieldname,
    f.description,
    f.attachedto,
    f.datatype,
    f.longfieldname,
    f.dictionarykey,
    u.entityid,
    u.value,
    json_build_object(f.userdefinedfieldid, u.value) AS jsonkvpair
   FROM tbluserdefinedfieldvalue u
     LEFT JOIN tlkpuserdefinedfield f ON u.userdefinedfieldid = f.userdefinedfieldid;

CREATE VIEW vwuserdefinedfieldagg AS
	SELECT vwtbluserdefinedfieldvalue.entityid,
	    json_agg(vwtbluserdefinedfieldvalue.jsonkvpair) AS userdefinedfields
	   FROM vwtbluserdefinedfieldvalue
	  GROUP BY vwtbluserdefinedfieldvalue.entityid;

-- CREATE A NEW VIEW for TBLSAMPLE WITH EXTRA FIELDS
CREATE OR REPLACE VIEW vwtblsample2 AS

 SELECT s.externalid,
    s.sampleid,
    s.code,
    s.comments,
    s.code AS title,
    s.elementid,
    s.samplingdate,
    s.samplingdateprec,
    s.createdtimestamp,
    s.lastmodifiedtimestamp,
    st.sampletypeid,
    st.sampletype,
    array_to_string(s.file, '><'::text) AS file,
    s."position",
    s.state,
    s.knots,
    s.description,
    array_to_string(s.userdefinedfielddata, '><'::text) AS userdefinedfielddata,
    dc.datecertainty,
    s.boxid,
    lc.objectcode,
    lc.objectid,
    lc.elementcode,
    c.curationstatusid,
    c.curationstatus,
    s.samplingmonth,
    s.samplingyear,
    s.samplestatusid,
    s.dendrochronologist,
    ss.samplestatus,
    udf.userdefinedfields
   FROM tblsample s
     LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
     LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN vwtblcurationeventmostrecent c ON s.sampleid = c.sampleid
     LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid
     LEFT JOIN vwuserdefinedfieldagg udf ON s.sampleid = udf.entityid;
     
     
     
----------------------------------
-- CREATE VIEW FOR IPT
---------------------------------- 



DROP VIEW IF EXISTS vwipt;
CREATE OR REPLACE VIEW vwipt AS

SELECT 
p.projectid AS projectid,
p.title AS ptitle,
p.createdtimestamp AS pcreatedtimestamp,
p.lastmodifiedtimestamp AS plastmodifiedtimestamp,
p.comments AS pcomments,
p.description AS pdescription,
p.file AS pfile,
p.projectcategory AS projectcategory,
p.investigator AS recordedby,
p.period AS pperiod,
p.reference AS projectreference,


o1.objectid AS o1objectid,
o1.title AS o1title,
o1.code AS o1code,

e.elementid AS elementid,
e.code AS ecode,
e.taxonid AS acceptednameusageid,

ST_X(ST_Centroid(l.locationgeometry)) AS decimallongitude,
ST_Y(ST_Centroid(l.locationgeometry)) AS decimallatitude,
e.locationprecision AS coordinateUncertaintyinmeters,
e.locationcomment AS locationremarks,
e.locationcountry AS country,
e.locationstateprovinceregion AS stateprovince,
e.altitude AS verbatimElevation,
 

t.kingdom, t.phylum, t.class, t.txorder AS "order", t.family, t.subfamily, t.genus, t.subgenus, t.section AS infragenericepithet, t.species, t.subspecies AS infraspecificepithet, variety as cultivarepithet,
td.label AS scientificname,
td.parenttaxonid AS parentnameusageid,
tr.taxonrank AS taxonrank,
'accepted' AS taxonomicstatus,
'icn' AS nomenclaturalcode,

s.sampleid AS occurrenceid,
s.externalid AS recordnumber,
s.dendrochronologist AS identifiedby,
cpgdb.getdateday(s.samplingdate, s.samplingdateprec) AS day,
cpgdb.getdatemonth(s.samplingdate, s.samplingdateprec) AS month,
cpgdb.getdateyear(s.samplingdate, s.samplingdateprec) AS year,
cpgdb.getdatestring(s.samplingdate, s.samplingdateprec) AS eventdate,

'Physical object'::varchar AS "type",
to_char(s.lastmodifiedtimestamp, 'YYYY-MM-DD"T"HH24:MI:SSTZHTZM')  AS "modified",
'en'::varchar AS "language",
'Arizona Board of Regents'::varchar AS "rightsholder",
'http://grscicoll.org/cool/mhy6-8hem'::varchar AS institutionid,
'LTRR'::varchar AS institutioncode,
'Laboratory of Tree-Ring Research Natural History Collection'::varchar AS datasetName,
'PreservedSpecimen'::varchar AS basisofrecord,
'1'::varchar AS individualcount,
'1'::varchar AS organismQuantity,
'individuals'::varchar AS organismQuantityType,
'present'::varchar AS occurrenceStatus,
'wood specimen'::varchar AS preparations,
'WGS84'::varchar AS geodeticdatum

FROM 
vwtblproject p 
LEFT JOIN vwtblobject o1 ON p.projectid=o1.projectid
LEFT JOIN vwtblelement e ON e.objectid=o1.objectid
LEFT JOIN stblflattenedtaxonomy t ON e.taxonid=t.taxonid
LEFT JOIN tlkptaxon td ON e.taxonid=td.taxonid
LEFT JOIN tlkptaxonrank tr ON tr.taxonrankid=td.taxonrankid
LEFT JOIN vwtblsample2 s ON s.elementid=e.elementid
LEFT JOIN vwlocation l ON l.entityid = e.bestgeometryid

WHERE s.sampleid IS NOT NULL;

DROP TABLE IF EXISTS staticvwipt;
CREATE TABLE staticvwipt AS SELECT * FROM vwipt;
GRANT ALL ON staticvwipt TO tellervo;
     
    