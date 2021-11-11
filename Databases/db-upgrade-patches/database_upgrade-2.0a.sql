UPDATE tblconfig SET value='2.0' WHERE key='wsversion';

ALTER TABLE tblinventory ADD COLUMN percentfilled integer;
ALTER TABLE tblinventory ADD COLUMN weight integer;
ALTER TABLE tblinventory ADD COLUMN comments varchar;

 
