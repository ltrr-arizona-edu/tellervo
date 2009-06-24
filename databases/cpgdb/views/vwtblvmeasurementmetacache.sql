DROP VIEW vwtblvmeasurementmetacache;

CREATE VIEW vwtblvmeasurementmetacache AS
 SELECT tblvmeasurementmetacache.vmeasurementmetacacheid, tblvmeasurementmetacache.vmeasurementid, tblvmeasurementmetacache.startyear, tblvmeasurementmetacache.readingcount, 
        tblvmeasurementmetacache.measurementcount, ymin(tblvmeasurementmetacache.vmextent::box3d) AS measurementymin, ymax(tblvmeasurementmetacache.vmextent::box3d) AS measurementymax, 
        xmin(tblvmeasurementmetacache.vmextent::box3d) AS measurementxmin, ymax(tblvmeasurementmetacache.vmextent::box3d) AS measurementxmax, 
        x(centroid(tblvmeasurementmetacache.vmextent::box3d::geometry)) AS measurementxcentroid, y(centroid(tblvmeasurementmetacache.vmextent::box3d::geometry)) AS measurementycentroid, 
        tblvmeasurementmetacache.vmextent AS measurementextent
   FROM tblvmeasurementmetacache;

