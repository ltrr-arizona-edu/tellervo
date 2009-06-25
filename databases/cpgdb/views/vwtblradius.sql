DROP VIEW vwtblradius;

CREATE OR REPLACE VIEW vwtblradius AS 
 SELECT tblradius.radiusid, tblradius.sampleid, tblradius.code AS radiuscode, tblradius.azimuth, tblradius.createdtimestamp AS radiuscreated, tblradius.lastmodifiedtimestamp AS radiuslastmodified, tblradius.numberofsapwoodrings, tblradius.barkpresent, tblradius.lastringunderbark, tblradius.missingheartwoodringstopith, tblradius.missingheartwoodringstopithfoundation, tblradius.missingsapwoodringstobark, tblradius.missingsapwoodringstobarkfoundation, spw.complexpresenceabsenceid as sapwoodid, spw.complexpresenceabsence as sapwood, hwd. complexpresenceabsenceid as heartwoodid, hwd. complexpresenceabsence as heartwood, pth.complexpresenceabsenceid as pithid, pth.complexpresenceabsence as pith
   FROM tblradius
   LEFT JOIN tlkpcomplexpresenceabsence spw ON tblradius.sapwoodid = spw.complexpresenceabsenceid
   LEFT JOIN tlkpcomplexpresenceabsence hwd ON tblradius.heartwoodid = hwd. complexpresenceabsenceid
   LEFT JOIN tlkpcomplexpresenceabsence pth ON tblradius.pithid = pth. complexpresenceabsenceid;

GRANT ALL on vwtblradius to "Webgroup";
