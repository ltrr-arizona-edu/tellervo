
--- Fix for bug #54 - Last modifed timestamp not updating
DROP TRIGGER IF EXISTS update_vmeasurement_lastmodifiedtimestamp_trigger ON tblvmeasurement;
CREATE TRIGGER update_vmeasurement_lastmodifiedtimestamp_trigger BEFORE UPDATE ON tblvmeasurement FOR EACH ROW EXECUTE PROCEDURE update_lastmodifiedtimestamp();