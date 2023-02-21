
-- FIX QRYTAXONOMY 
DROP FUNCTION cpgdb.qrytaxonomy(varchar);
CREATE OR REPLACE FUNCTION cpgdb.qrytaxonomy(thetaxonid varchar)
  RETURNS typfulltaxonomy AS
$BODY$  
SELECT *
FROM crosstab(
'select '''||$1||'''::varchar as taxonid, taxonrank, taxonname from cpgdb.qrytaxonflat2('''||$1||''') ORDER BY taxonrank'

, 
    'select taxonrank
    from tlkptaxonrank 
    order by rankorder asc'

)
     AS taxonomylist(taxonid varchar, 
     kingdom varchar, 
    subkingdom varchar, 
    phylum varchar, 
    division varchar, 
    class varchar, 
    txorder varchar, 
    family varchar, 
    subfamily varchar,
    genus varchar,
    subgenus varchar, 
    section varchar,
    subsection varchar,
    species varchar, 
    subspecies varchar, 
    race varchar, 
    variety varchar, 
    subvariety varchar, 
    form varchar, 
    subform varchar);
    $BODY$
  LANGUAGE 'sql' VOLATILE;
  
  
-- CREATE STATIC FLATTENED TAXONOMY TABLE
DROP TABLE IF EXISTS stblflattenedtaxonomy;
CREATE TABLE stblflattenedtaxonomy as   
SELECT e.taxonid, tr.taxonrank, t.kingdom, t.subkingdom, t.phylum, t.division, t.class, t.txorder, t.family, t.subfamily, t.genus, t.subgenus, t.section, t.subsection, t.species, cpgdb.getspecificepithet(t.taxonid) as specificepithet, t.subspecies, t.race, t.variety, t.subvariety, t.form, t.subform                                         
FROM   tlkptaxon e, 
cpgdb.qrytaxonomy(e.taxonid) t, 
tlkptaxonrank tr 
WHERE tr.taxonrankid = e.taxonrankid;

-- UNION QUERY FOR ALL LOCATION FIELDS BY ENTITY
CREATE VIEW vwlocation AS 
SELECT objectid AS entityid, locationgeometry, locationprecision, locationcomment, locationcountry, locationpostalcode, locationstateprovinceregion, locationcityortown, locationaddressline2, locationaddressline1, locationtypeid FROM tblobject
UNION
SELECT elementid AS entityid, locationgeometry, locationprecision, locationcomment, locationcountry, locationpostalcode, locationstateprovinceregion, locationcityortown, locationaddressline2, locationaddressline1, locationtypeid FROM tblelement;

-- FUNCTION FOR OBTAINING THE PREFERRED/BEST GEOMETRY ID FOR THE SPECIFIED ELEMENT
-- ELEMENT > OBJECT > PARENT OBJECT
CREATE OR REPLACE FUNCTION cpgdb.getbestgeometryid(uuid) RETURNS uuid AS 
$$
DECLARE                                                           
  theelementid ALIAS FOR $1;                                      
  objectgeom geometry;                                            
  elementgeom geometry;                                           
  objectrow tblobject;                                            
  parentobjectrow tblobject;                                      
BEGIN                                                                                                               
  SELECT locationgeometry INTO elementgeom                        
  FROM tblelement e                                               
  WHERE e.elementid=theelementid;                                 
                                                                  
  IF elementgeom IS NOT NULL THEN                                 
    RETURN theelementid;                                             
  END IF;                                                         
                                                                  
  SELECT tblobject.* INTO objectrow                               
  FROM tblelement                                                 
    LEFT JOIN tblobject ON tblelement.objectid=tblobject.objectid 
  WHERE tblelement.elementid=theelementid;                        
           
  IF objectrow.locationgeometry IS NOT NULL THEN
  	RETURN objectrow.objectid;
  END IF;
                                                                        
  IF objectrow.parentobjectid IS NOT NULL THEN                    
    SELECT tblobject.* INTO parentobjectrow                         
    FROM tblobject                                                  
    WHERE tblobject.objectid=objectrow.parentobjectid;              
                                                                    
    IF objectrow.locationgeometry IS NOT NULL THEN                  
        RETURN objectrow.objectid;                              
    ELSE                                                            
        RETURN parentobjectrow.objectid;                        
    END IF;          
    
  END IF;                                                         
  RETURN objectrow.objectid;                                                                               
END;$$  
LANGUAGE PLPGSQL STABLE;        


-- FUNCTION FOR OBTAINING THE PREFERRED/BEST GEOMETRY FOR THE SPECIFIED ELEMENT
-- ELEMENT > OBJECT > PARENT OBJECT
CREATE OR REPLACE FUNCTION cpgdb.getbestgeometry(uuid) RETURNS geometry AS 
$$
DECLARE                                                           
  theelementid ALIAS FOR $1;                                      
  objectgeom geometry;                                            
  elementgeom geometry;                                           
  objectrow tblobject;                                            
  parentobjectrow tblobject;                                      
BEGIN                                                                                                               
  
  IF exists(SELECT 1 FROM tblelement WHERE elementid=theelementid) THEN
  
	  SELECT locationgeometry INTO elementgeom                        
	  FROM tblelement e                                               
	  WHERE e.elementid=theelementid;                                 
                                                   
	  IF elementgeom IS NOT NULL THEN                                 
	    RETURN elementgeom;                                             
	  END IF;
  ELSE
  	  RETURN NULL;
  END IF;                                                         
	                                                                  
  SELECT tblobject.* INTO objectrow                               
  FROM tblelement                                                 
    LEFT JOIN tblobject ON tblelement.objectid=tblobject.objectid 
  WHERE tblelement.elementid=theelementid;                        
           
  IF objectrow.locationgeometry IS NOT NULL THEN
  	RETURN objectrow.locationgeometry;
  END IF;
                                                                        
  IF objectrow.parentobjectid IS NOT NULL THEN                    
    SELECT tblobject.* INTO parentobjectrow                         
    FROM tblobject                                                  
    WHERE tblobject.objectid=objectrow.parentobjectid;              
                                                                    
    IF objectrow.locationgeometry IS NOT NULL THEN                  
        RETURN objectrow.locationgeometry;                              
    ELSE                                                            
        RETURN NULL;                        
    END IF;          
    
  END IF;                                                         
  RETURN NULL;                                                                               
END;$$  
LANGUAGE PLPGSQL STABLE;  


-- GET SPECIFIC EPITHET FROM TAXONID  
CREATE OR REPLACE FUNCTION cpgdb.getspecificepithet(varchar) RETURNS varchar AS 
$$
DECLARE                                                           
  thetaxonid ALIAS FOR $1;                                                                                
  taxonrow tlkptaxon;                                            
BEGIN                                                                                                               
  
  SELECT tlkptaxon.* INTO taxonrow FROM tlkptaxon WHERE taxonid=thetaxonid;

  IF taxonrow.taxonrankid=9 THEN  	   
  	   RETURN split_part(taxonrow.label,' ',2 );
  END IF;
  
  RETURN NULL;
                                                                           
END;$$  
LANGUAGE PLPGSQL STABLE;        




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
    ss.samplestatus
   FROM tblsample s
     LEFT JOIN tlkpsamplestatus ss ON s.samplestatusid = ss.samplestatusid
     LEFT JOIN tlkpdatecertainty dc ON s.datecertaintyid = dc.datecertaintyid
     LEFT JOIN tlkpsampletype st ON s.typeid = st.sampletypeid
     LEFT JOIN vwtblcurationeventmostrecent c ON s.sampleid = c.sampleid
     LEFT JOIN vwlabcodesforsamples lc ON s.sampleid = lc.sampleid;


CREATE OR REPLACE FUNCTION cpgdb.getdateyear(thedate date, thedateprec date_prec) RETURNS varchar AS 
$$
DECLARE                                                           
  thedate ALIAS FOR $1;                                                                                
  thedateprec ALIAS FOR $2;                                            
BEGIN                                                                                                               
  
  IF thedate IS NULL THEN
  	RETURN NULL;
  END IF;
  
  RETURN to_char(thedate, 'YYYY');
  	                                         
END;$$  
LANGUAGE PLPGSQL STABLE;        


CREATE OR REPLACE FUNCTION cpgdb.getdatemonth(thedate date, thedateprec date_prec) RETURNS varchar AS 
$$
DECLARE                                                           
  thedate ALIAS FOR $1;                                                                                
  thedateprec ALIAS FOR $2;                                            
BEGIN                                                                                                               
  
  IF thedate IS NULL THEN
  	RETURN NULL;
  END IF;
  
  IF(thedateprec = 'year') THEN 
  	RETURN NULL;
  END IF;
  	
  RETURN to_char(thedate, 'FMMM');
  	                                         
END;$$  
LANGUAGE PLPGSQL STABLE;        



CREATE OR REPLACE FUNCTION cpgdb.getdateday(thedate date, thedateprec date_prec) RETURNS varchar AS 
$$
DECLARE                                                           
  thedate ALIAS FOR $1;                                                                                
  thedateprec ALIAS FOR $2;                                            
BEGIN                                                                                                               
  
  IF thedate IS NULL THEN
  	RETURN NULL;
  END IF;
  
  IF(thedateprec = 'year' OR thedateprec = 'month') THEN 
  	RETURN NULL;
  END IF;
  	
  RETURN to_char(thedate, 'FMDD');
  	                                         
END;$$  
LANGUAGE PLPGSQL STABLE;      

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

DROP TABLE staticvwipt;
CREATE TABLE staticvwipt AS SELECT * FROM vwipt;
GRANT ALL ON staticvwipt TO tellervo;

