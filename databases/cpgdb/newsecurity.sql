CREATE OR REPLACE FUNCTION cpgdb.AndPermissionSets(origset typPermissionSet, newset typPermissionSet) 
RETURNS typPermissionSet AS $$
DECLARE
   res typPermissionSet;
BEGIN
   -- if we're denied, it all ends here
   IF newset.denied THEN
      res.denied := true;
      res.canCreate := false;
      res.canRead := false;
      res.canUpdate := false;
      res.canDelete := false;
      res.decidedBy := newset.denied;
      
      RETURN res;
   ELSIF origset.denied THEN
      res.denied := true;
      res.canCreate := false;
      res.canRead := false;
      res.canUpdate := false;
      res.canDelete := false;
      res.decidedBy := origset.denied;
      
      RETURN res;
   END IF;

   res.denied := false;
   res.canCreate := origset.canCreate AND newset.canCreate;
   res.canRead := origset.canRead AND newset.canRead;
   res.canUpdate := origset.canUpdate AND newset.canUpdate;
   res.canDelete := origset.canDelete AND newset.canDelete;
   res.decidedBy := origset.decidedBy || ', ' || newset.decidedBy;

   RETURN res;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- 1 = array of groups
-- 2 = type (vmeasurement, tree, site, default)
-- 3 = id (VMeasurementID, TreeID, SiteID, or 0 for default)
CREATE OR REPLACE FUNCTION cpgdb.GetGroupPermissionSet(_groupIDs integer[], _permtype varchar, _pid integer) 
RETURNS typPermissionSet AS $$
DECLARE
   query varchar;
   whereClause varchar;
   --i integer;
   objid integer;
   res refcursor;
   perm varchar;
   vstype varchar;
   perms typPermissionSet;
   childPerms typPermissionSet;
   setSize integer;

   stypes varchar[] := array['vmeasurement','tree','site','default'];
BEGIN
   -- Invalid type specified?
   IF NOT (_permtype = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, tree, site, default (case matters!).', _permtype;
   END IF;

   -- Build our query
   IF _permtype = 'default' THEN
      whereClause := '';
   ELSE
      whereClause := ' WHERE ' || _permType || 'Id = ' || _pid;
   END IF;
   
   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || _permType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID' || whereClause;

   --RAISE NOTICE 'secQuery: % for %', _permtype, _pid;

   -- Open the query, and execute to see if we get any results
   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   -- No results?
   IF NOT FOUND THEN
      CLOSE res;
      
      IF _permtype = 'vmeasurement' THEN
         -- This obnoxious query joins direct vss to trees, so we don't have to do another query
         SELECT op.Name,tr.TreeID INTO vstype,objid FROM tblVMeasurement vs 
            INNER JOIN tlkpVMeasurementOp op ON vs.VMeasurementOpID = op.VMeasurementOpID
            LEFT JOIN tblMeasurement t1 ON vs.MeasurementID = t1.MeasurementID 
            LEFT JOIN tblRadius t2 ON t2.RadiusID = t1.RadiusID 
            LEFT JOIN tblSpecimen t3 on t3.SpecimenID = t2.SpecimenID 
            LEFT JOIN tblTree tr ON tr.TreeID = t3.TreeID
            WHERE vs.VMeasurementID = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: vmeasurement % -> tree does not exist', _pid;
         END IF;

         IF vstype = 'Direct' THEN
            -- We hit a direct VMeasurement. Move down to tree...
            perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'tree', objid);
	    RETURN perms;
         ELSE
            -- Start with a completely empty permission set
            setSize := 0;

            -- For each child VMeasurement, AND our sets together...
            FOR objid IN SELECT MemberVMeasurementID FROM tblVMeasurementGroup WHERE VMeasurementID = _pid LOOP
               childPerms := cpgdb.GetGroupPermissionSet(_groupIDs, 'vmeasurement', objid);

               -- Start out with our first childperms; continue with anding them.               
               IF setSize = 0 THEN
                  perms := childperms;
               ELSE
                  perms := cpgdb.AndPermissionSets(perms, childPerms);
               END IF;

               setSize := setSize + 1;
               
            END LOOP;
            RETURN perms;
         END IF;
         
      ELSIF _permType = 'tree' THEN
         -- Get the siteID of this tree
         SELECT tblSubsite.siteID INTO objid FROM tblTree, tblSubsite
                WHERE tblTree.subsiteID=tblSubsite.SubsiteID
                AND tblTree.treeid = _pid;

         IF NOT FOUND THEN
            RAISE EXCEPTION 'Could not determine security: tree % -> site does not exist', _pid;
         END IF;
         
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'site', objid);
         RETURN perms;
         
      ELSIF _permType = 'site' THEN
         -- Get default permissions
         perms := cpgdb.GetGroupPermissionSet(_groupIDs, 'default', 0);
         RETURN perms;

      ELSE
	 -- No defaults?!?! Ahh!
	 perms.denied := true;
	 perms.canRead := false;
	 perms.canDelete := false;
	 perms.canUpdate := false;
	 perms.canCreate := false;
	 perms.decidedBy := 'No defaults';
	 RETURN perms;

      END IF;

   -- Results found?
   ELSE
      -- initialize our perms...
      perms.decidedBy := _permType || ' ' || _pid;
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

         -- Get the next permission
         FETCH res INTO perm;

         IF NOT FOUND THEN
            EXIT; -- at the end of our permission list
         END IF;
      END LOOP;
   END IF;
   
   -- Close the query and return permissions
   CLOSE res;
   RETURN perms;
END;
$$ LANGUAGE PLPGSQL VOLATILE;

CREATE OR REPLACE FUNCTION cpgdb.GetGroupMembership(integer)
RETURNS SETOF tblSecurityGroup AS $$
DECLARE
   _securityUserID ALIAS FOR $1;

   res tblSecurityGroup%ROWTYPE;
   parentRes tblSecurityGroup%ROWTYPE;
BEGIN
   -- Go through each group our user is a direct member of
   FOR res IN SELECT g.* FROM tblSecurityGroup g
              INNER JOIN tblSecurityUserMembership m ON m.securityGroupID = g.securityGroupID
              WHERE m.securityUserID = _securityUserID AND g.isActive = true
   LOOP
      -- Return this direct group
      RETURN NEXT res;

      -- Return our parent groups
      FOR parentRes IN SELECT * FROM cpgdb.recurseGetParentGroups(res.securityGroupID, 0) 
      LOOP
         RETURN NEXT parentRes;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION cpgdb.recurseGetParentGroups(integer, integer)
RETURNS SETOF tblSecurityGroup AS $$
DECLARE
   _securityGroupID ALIAS FOR $1;
   _recursionLevel ALIAS FOR $2;

   res tblSecurityGroup%ROWTYPE;
   parentRes tblSecurityGroup%ROWTYPE;
BEGIN
   IF _recursionLevel > 50 THEN
      RAISE EXCEPTION 'Infinite recursion in getParentGroups!';
   END IF;

   -- Return a list of security groups this group is a direct member of
   FOR res IN SELECT g.* FROM tblSecurityGroup g
              INNER JOIN tblSecurityGroupMembership m ON m.parentSecurityGroupID = g.securityGroupID
              WHERE m.childSecurityGroupID = _securityGroupID and g.isActive = true
   LOOP
      RETURN NEXT res;

      -- Return our parent groups
      FOR parentRes IN SELECT * FROM cpgdb.recurseGetParentGroups(res.securityGroupID, _recursionLevel + 1) 
      LOOP
         RETURN NEXT parentRes;
      END LOOP;
   END LOOP;
END;
$$ LANGUAGE PLPGSQL;

-- $1 = securityUserID
CREATE OR REPLACE FUNCTION cpgdb.GetGroupMembershipArray(integer) 
RETURNS integer[] AS $$
   SELECT ARRAY(SELECT DISTINCT SecurityGroupID FROM cpgdb.GetGroupMembership($1) ORDER BY SecurityGroupID ASC);
$$ LANGUAGE SQL IMMUTABLE;

-- 1 = securityUserID
-- 2 = type (VMeasurement, Tree, Site, Default)
-- 3 = id (VMeasurementID, TreeID, SiteID, or 0 for default)
CREATE OR REPLACE FUNCTION cpgdb.GetUserPermissionSet(integer, varchar, integer) 
RETURNS typPermissionSet AS $$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
   perms typPermissionSet;
BEGIN
   -- Get an array list of all groups this user is a member of
   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   -- If the user is in the admin group, don't bother with any security checks.
   -- Even the restrictive ones. Return a set of everything.
   IF 1 = ANY(groupMembership) THEN
      perms.denied = false;
      perms.canRead = true;
      perms.canUpdate = true;
      perms.canCreate = true;
      perms.canDelete = true;
      perms.decidedBy = 'Administrative user';
      RETURN perms;
   ELSIF array_upper(groupMembership, 1) IS NULL THEN
      perms.denied = true;
      perms.canRead = false;
      perms.canUpdate = false;
      perms.canCreate = false;
      perms.canDelete = false;
      perms.decidedBy = 'User is not a member of any groups';
      RETURN perms;      
   END IF;

   -- Ok, we've cached the groups, then... go ahead and ask there.
   perms := cpgdb.GetGroupPermissionSet(groupMembership, _ptype, _pid);
   RETURN perms;
END;
$$ LANGUAGE PLPGSQL;
