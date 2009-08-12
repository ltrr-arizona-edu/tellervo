
DROP VIEW vwtblbox;

CREATE VIEW vwtblbox AS

SELECT b.boxid, b.title, b.curationlocation, b.trackinglocation, b.createdtimestamp, b.lastmodifiedtimestamp, b.comments, count(s.sampleid) as samplecount
FROM tblbox b
LEFT JOIN tblsample s ON b.boxid = s.boxid
GROUP BY b.boxid, b.title, b.curationlocation, b.trackinglocation, b.createdtimestamp, b.lastmodifiedtimestamp, b.comments

GRANT ALL on vwtblbox to "Webgroup";