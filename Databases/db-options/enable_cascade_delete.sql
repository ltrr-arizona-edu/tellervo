ALTER TABLE tblelement
DROP CONSTRAINT "tblelement_objectid_fkey",
ADD CONSTRAINT "tblelement_objectid_fkey" FOREIGN KEY (objectid) REFERENCES tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblsample
DROP CONSTRAINT "fkey_sample-tree",
ADD CONSTRAINT "fkey_sample-tree" FOREIGN KEY (elementid) REFERENCES tblelement(elementid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblradius
DROP CONSTRAINT "fkey_radius-sample",
ADD CONSTRAINT "fkey_radius-sample" FOREIGN KEY (sampleid) REFERENCES tblsample(sampleid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblmeasurement
DROP CONSTRAINT "fkey_measurement-radius",
ADD CONSTRAINT "fkey_measurement-radius" FOREIGN KEY (radiusid) REFERENCES tblradius(radiusid) ON UPDATE CASCADE ON DELETE CASCADE;
