ALTER TABLE portal.tblprojectmanagement
DROP CONSTRAINT "fkeyprojectmanagementproject",
ADD CONSTRAINT "fkeyprojectmanagementproject" FOREIGN KEY (projectid) REFERENCES tblproject(projectid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE portal.tblprojecttaskprogress
DROP CONSTRAINT "fkeyprojecttaskprogressproject",
ADD CONSTRAINT "fkeyprojecttaskprogressproject" FOREIGN KEY (projectid) REFERENCES tblproject(projectid) ON UPDATE CASCADE ON DELETE CASCADE;
  
ALTER TABLE portal.tblprojectmessage
DROP CONSTRAINT "fkeyprojectmessageproject",
ADD CONSTRAINT "fkeyprojectmessageproject" FOREIGN KEY (projectid) REFERENCES tblproject(projectid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblobject
DROP CONSTRAINT "fkey_object-project",
ADD CONSTRAINT "fkey_object-project"  FOREIGN KEY (projectid) REFERENCES tblproject(projectid) ON UPDATE CASCADE ON DELETE CASCADE;
    
ALTER TABLE tblobject
DROP CONSTRAINT "tblobject_parentobjectid_fkey",
ADD CONSTRAINT "tblobject_parentobjectid_fkey" FOREIGN KEY (parentobjectid) REFERENCES tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblelement
DROP CONSTRAINT "tblelement_objectid_fkey",
ADD CONSTRAINT "tblelement_objectid_fkey" FOREIGN KEY (objectid) REFERENCES tblobject(objectid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblsample
DROP CONSTRAINT "fkey_sample-tree",
ADD CONSTRAINT "fkey_sample-tree" FOREIGN KEY (elementid) REFERENCES tblelement(elementid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblcurationevent
DROP CONSTRAINT "fkey_tblcuration-tblsample",
ADD CONSTRAINT "fkey_tblcuration-tblsample" FOREIGN KEY (sampleid) REFERENCES tblsample(sampleid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblradius
DROP CONSTRAINT "fkey_radius-sample",
ADD CONSTRAINT "fkey_radius-sample" FOREIGN KEY (sampleid) REFERENCES tblsample(sampleid) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE tblmeasurement
DROP CONSTRAINT "fkey_measurement-radius",
ADD CONSTRAINT "fkey_measurement-radius" FOREIGN KEY (radiusid) REFERENCES tblradius(radiusid) ON UPDATE CASCADE ON DELETE CASCADE;
