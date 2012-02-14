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


-- 1 = array of groups
-- 2 = type (VMeasurement, Element, Object, Default)
-- 3 = id (VMeasurementID, ElementID, ObjectID, or 0 for default)
CREATE OR REPLACE FUNCTION cpgdb.GetGroupPermissions(integer[], varchar, integer) RETURNS varchar[] AS $$
DECLARE
   _groupIDs ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   permissionType varchar := lower(_ptype);
   query varchar;
   i integer;
   objectid integer;
   res refcursor;
   perm varchar;
   perms varchar[];

   stypes varchar[] := array['vmeasurement','element','object','default'];
BEGIN
   -- Invalid type specified?
   IF NOT (permissionType = ANY(stypes)) THEN
      RAISE EXCEPTION 'Invalid permission type: %. Should be one of vmeasurement, element, object, default.', _ptype;
   END IF;

   -- Invoke special defaults... 
   -- Note that this if statement MUST return!
   IF permissionType = 'default' THEN
      RAISE NOTICE 'Fell back to read only defaults; implement defaults??';
      RETURN ARRAY['Read'];
   END IF;

   -- Build our query
   query := 'SELECT DISTINCT perm.name FROM tblSecurity' || permissionType || ' obj ' ||
            'INNER JOIN ArrayToRows(ARRAY[' || array_to_string(_groupIDs, ',') || ']) AS membership ON obj.securityGroupID = membership ' ||
            'INNER JOIN tlkpSecurityPermission perm ON perm.securityPermissionID = obj.securityPermissionID ' || 
            'WHERE ' || permissionType || 'Id =' || _pid;

   -- Open the query, and execute to see if we get any results
   OPEN res FOR EXECUTE query;
   FETCH res INTO perm;

   -- No results?
   IF NOT FOUND THEN
      IF permissionType = 'vmeasurement' THEN
         -- What the hell do we return for VMeasurements?
         -- Get default permissions
         RETURN cpgdb.GetGroupPermissions(_groupIDs, 'default', 0);

      ELSEIF permissionType = 'element' THEN
         -- Get the objectID of this element
         SELECT tblSubobject.objectID INTO objectid FROM tblElement, tblSubobject 
                WHERE tblElement.subobjectID=tblSubobject.SubobjectID
                AND tblElement.elementid = _pid;

         RETURN cpgdb.GetGroupPermissions(_groupIDs, 'object', objectid);

      ELSEIF permissionType = 'object' THEN

         -- Get default permissions
         RETURN cpgdb.GetGroupPermissions(_groupIDs, 'default', 0);
      END IF;
   END IF;

   i := 0;
   LOOP
      -- If we happen upon 'no permissions,' this overrides everything. Bail out!
      IF perm = 'No permissions' THEN
         CLOSE res;
         RETURN ARRAY['No permissions'];
      END IF;

      perms[i] = perm;
      i := i + 1;

      FETCH res INTO perm;

      IF NOT FOUND THEN
         EXIT; -- We've reached the end of our list of permissions
      END IF;
   END LOOP;

   CLOSE res;
   RETURN perms;
END;
$$ LANGUAGE PLPGSQL;

-- 1 = securityUserID
-- 2 = type (VMeasurement, Element, Object, Default)
-- 3 = id (VMeasurementID, ElementID, ObjectID, or 0 for default)
CREATE OR REPLACE FUNCTION cpgdb.GetUserPermissions(integer, varchar, integer) RETURNS varchar[] AS $$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
BEGIN
   -- Get an array list of all groups this user is a member of
   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

   -- If the user is in the admin group, don't bother with any security checks.
   -- Even the restrictive ones. Return a set of everything.
   IF 1 = ANY(groupMembership) THEN
      RETURN ARRAY(SELECT name FROM tlkpSecurityPermission WHERE name <> 'No permission');
   END IF;

   -- Ok, we've cached the groups, then... go ahead and ask there.
   RETURN (SELECT * FROM cpgdb.GetGroupPermissions(groupMembership, _ptype, _pid));
END;
$$ LANGUAGE PLPGSQL;
