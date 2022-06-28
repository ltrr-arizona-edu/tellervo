-- Allow more relaxed definition of permission type 

CREATE OR REPLACE FUNCTION cpgdb.getgrouppermissionset(_groupids integer[], _permtype character varying, _pid uuid)
  RETURNS typpermissionset AS
$BODY$
DECLARE
   query varchar;
   whereClause varchar;
   objid uuid;
   res refcursor;
   perm varchar;
   vstype varchar;
   perms typPermissionSet;
   childPerms typPermissionSet;
   setSize integer;

   stypes varchar[] := array['measurementSeries', 'derivedSeries', 'vmeasurement','element','object','default'];
BEGIN


   IF NOT (_permtype = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, element, object, default (case matters!).', _permtype;
   END IF;

   IF _permtype = 'measurementSeries' OR _permtype = 'derivedSeries' THEN
      _permtype = 'vmeasurement';
   END IF;


   IF _permtype = 'default' THEN
      whereClause := '';
   ELSE
      whereClause := ' WHERE ' || _permType || 'Id = ' ||  '''' || _pid ||'''' ;
   END IF;
   
   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || _permType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID' || whereClause;


   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   IF NOT FOUND THEN
      CLOSE res;
      
      IF _permtype = 'vmeasurement' OR _permtype='measurementSeries' OR _permtype='derivedSeries'  THEN
         SELECT op.Name,tr.ElementID INTO vstype,objid FROM tblVMeasurement vs 
            INNER JOIN tlkpVMeasurementOp op ON vs.VMeasurementOpID = op.VMeasurementOpID
            LEFT JOIN tblMeasurement t1 ON vs.MeasurementID = t1.MeasurementID 
            LEFT JOIN tblRadius t2 ON t2.RadiusID = t1.RadiusID 
            LEFT JOIN tblSample t3 on t3.SampleID = t2.SampleID 
            LEFT JOIN tblElement tr ON tr.ElementID = t3.ElementID
            WHERE vs.VMeasurementID =   _pid  ;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: vmeasurement % -> element does not exist', _pid;
         END IF;

         IF vstype = 'Direct' THEN
            perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'element', objid);
    RETURN perms;
         ELSE
            setSize := 0;

            FOR objid IN SELECT MemberVMeasurementID FROM tblVMeasurementGroup WHERE VMeasurementID = _pid  LOOP
               childPerms := cpgdb.GetGroupPermissionSet(_groupIDs, 'vmeasurement', objid);

               IF setSize = 0 THEN
                  perms := childperms;
               ELSE
                  perms := cpgdb.AndPermissionSets(perms, childPerms);
               END IF;

               setSize := setSize + 1;
               
            END LOOP;
            RETURN perms;
         END IF;
         
      ELSIF _permType = 'element' THEN
         SELECT tblelement.objectID INTO objid FROM tblElement
                WHERE tblElement.elementid = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: element % -> object does not exist', _pid;
         END IF;
         
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'object', objid);
         RETURN perms;
         
      ELSIF _permType = 'object' THEN
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'default', 'ae68d6d2-2294-11e1-9c20-4ffbb19115a7'::uuid);
         RETURN perms;

      ELSE
 perms.denied := true;
 perms.canRead := false;
 perms.canDelete := false;
 perms.canUpdate := false;
 perms.canCreate := false;
 perms.decidedBy := 'No defaults';
 RETURN perms;

      END IF;

   ELSE

      IF _permType = 'default' THEN
         perms.decidedBy := 'Database defaults';
      ELSE
         perms.decidedBy := 'Permissions overriden at the ' || _permType || ' level';
      END IF;
   
      
      perms.denied := false;
      perms.canRead := false;
      perms.canDelete := false;
      perms.canUpdate := false;
      perms.canCreate := false;

      LOOP
         IF perm = 'No permission' THEN
     perms.denied = true;
         ELSIF perm = 'Read' THEN
            perms.canRead := true;
         ELSIF perm = 'Create' THEN
            perms.canCreate := true;
         ELSIF perm = 'Update' THEN
            perms.canUpdate := true;
         ELSIF perm = 'Delete' THEN
            perms.canDelete := true;
         END IF; 

         FETCH res INTO perm;

         IF NOT FOUND THEN
            EXIT; -- at the end of our permission list
         END IF;
      END LOOP;
   END IF;
   
   CLOSE res;
   RETURN perms;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION cpgdb.getgrouppermissionset(integer[], character varying, uuid)
  OWNER TO postgres;
