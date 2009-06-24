DROP VIEW vwtblradius;

CREATE VIEW vwtblradius AS
 SELECT tblradius.radiusid, tblradius.sampleid, tblradius.code AS radiuscode, tblradius.azimuth, 
        tblradius.createdtimestamp AS radiuscreated, tblradius.lastmodifiedtimestamp AS radiuslastmodified, 
         tblradius.numberofsapwoodrings, tblradius.pithpresent, tblradius.barkpresent, 
         tblradius.lastringunderbark, tblradius.missingheartwoodringstopith, 
         tblradius.missingheartwoodringstopithfoundation, tblradius.missingsapwoodringstobark, 
         tblradius.missingsapwoodringstobarkfoundation, 
         spw.sapwoodid, spw.sapwood, hwd.heartwoodid, hwd.heartwood
   FROM tblradius
   LEFT JOIN tlkpsapwood spw ON tblradius.sapwoodid = spw.sapwoodid
   LEFT JOIN tlkpheartwood hwd ON tblradius.heartwoodid = hwd.heartwoodid;

