

CREATE OR REPLACE FUNCTION securitygrouppermissivedefault3(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandentityid AS
$BODY$
SELECT tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
FROM tblsecurityuser 
inner join ((tblsecuritydefault 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecuritydefault.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritydefault.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouppermissivedefault2(securitypermission integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandentityid AS
$BODY$select tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritydefault on tblsecuritygroup_1.securitygroupid = tblsecuritydefault.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritydefault.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;  
  
CREATE OR REPLACE FUNCTION securitygrouppermissivedefault1(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandentityid AS
$BODY$SELECT tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecuritydefault ON tblsecuritygroup.securitygroupid = tblsecuritydefault.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecuritydefault.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecuritydefault.securitydefaultid
HAVING (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouppermissivedefaultcombined(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandentityid AS
$BODY$SELECT * from securitygrouppermissivedefault1($1,$2)
UNION
SELECT * from securitygrouppermissivedefault2($1,$2)
UNION SELECT * from securitygrouppermissivedefault3($1,$2);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouppermissiveobject3(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$
SELECT tblsecurityuser.securityuserid, tblsecurityobject.objectid
FROM tblsecurityuser 
inner join ((tblsecurityobject 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouppermissiveobject2(securitypermission integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouppermissiveobject1(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$SELECT tblsecurityuser.securityuserid, tblsecurityobject.objectid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecurityobject ON tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityobject.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityobject.objectid
HAVING (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouppermissiveobjectcombined(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$SELECT * from securitygrouppermissiveobject1($1,$2)
UNION
SELECT * from securitygrouppermissiveobject2($1,$2)
UNION SELECT * from securitygrouppermissiveobject3($1,$2);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  CREATE OR REPLACE FUNCTION securitygrouppermissiveelement3(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser 
inner join ((tblsecurityelement 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityelement.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityelement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  CREATE OR REPLACE FUNCTION securitygrouppermissiveelement2(securitypermission integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$
select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join ((tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecurityelement on tblsecuritygroup_1.securitygroupid = tblsecurityelement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityelement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  

CREATE OR REPLACE FUNCTION securitygrouppermissiveelement1(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$SELECT tblsecurityuser.securityuserid, tblsecurityelement.elementid
FROM tblsecurityuser 
INNER JOIN ((tblsecuritygroup 
INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
INNER JOIN tblsecurityelement ON tblsecuritygroup.securitygroupid = tblsecurityelement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityelement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityelement.elementid
HAVING (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouppermissiveelementcombined(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$SELECT * from securitygrouppermissiveelement1($1,$2)
UNION
SELECT * from securitygrouppermissiveelement2($1,$2)
UNION SELECT * from securitygrouppermissiveelement3($1,$2);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

  
CREATE OR REPLACE FUNCTION securitygrouppermissivevmeasurement3(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser 
inner join ((tblsecurityvmeasurement 
inner join (tblsecuritygroup 
inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) 
inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouppermissivevmeasurement2(securitypermission integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouppermissivevmeasurement1(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$SELECT tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN tblsecurityusermembership ON tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) INNER JOIN tblsecurityvmeasurement ON tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) ON tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
WHERE (((tblsecurityvmeasurement.securitypermissionid)=$1) AND (tblsecurityuser.isactive=True) AND (tblsecuritygroup.isactive=True))
GROUP BY tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
HAVING (((tblsecurityuser.securityuserid)=$2));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouppermissivevmeasurementcombined(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$SELECT * from securitygrouppermissivevmeasurement1($1,$2)
UNION
SELECT * from securitygrouppermissivevmeasurement2($1,$2)
UNION SELECT * from securitygrouppermissivevmeasurement3($1,$2);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

  


CREATE OR REPLACE FUNCTION securitygrouprestrictiveobject3(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecurityobject inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityobject.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictiveobject2(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityobject on tblsecuritygroup_1.securitygroupid = tblsecurityobject.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictiveobject1(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityobject.objectid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityobject on tblsecuritygroup.securitygroupid = tblsecurityobject.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityobject.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityobject.objectid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictiveobjectcombined(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select * from securitygrouprestrictiveobject1($1)
UNION
select * from securitygrouprestrictiveobject2($1)
UNION SELECT * from securitygrouprestrictiveobject3($1);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  
CREATE OR REPLACE FUNCTION securitygroupobjectmaster(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select securitygrouppermissiveobjectcombined.* 
from securitygrouppermissiveobjectcombined($1, $2) left join securitygrouprestrictiveobjectcombined($2) on securitygrouppermissiveobjectcombined.objectid = securitygrouprestrictiveobjectcombined.objectid 
where (((securitygrouprestrictiveobjectcombined.objectid) is null));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  
CREATE OR REPLACE FUNCTION securitygrouprestrictiveelement3(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecurityelement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityelement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictiveelement2(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityelement on tblsecuritygroup_1.securitygroupid = tblsecurityelement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictiveelement1(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityelement.elementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityelement on tblsecuritygroup.securitygroupid = tblsecurityelement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityelement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityelement.elementid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictiveelementcombined(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select * from securitygrouprestrictiveelement1($1)
UNION
select * from securitygrouprestrictiveelement2($1)
UNION SELECT * from securitygrouprestrictiveelement3($1);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouprestrictivevmeasurement3(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecurityvmeasurement inner join (tblsecuritygroup inner join ((tblsecuritygroupmembership as tblsecuritygroupmembership_1 inner join (tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecuritygroup as tblsecuritygroup_2 on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) on tblsecurityvmeasurement.securitygroupid = tblsecuritygroup_2.securitygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouprestrictivevmeasurement2(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join ((tblsecuritygroupmembership inner join tblsecuritygroup as tblsecuritygroup_1 on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup_1.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygrouprestrictivevmeasurement1(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
from tblsecurityuser inner join ((tblsecuritygroup inner join tblsecurityusermembership on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) inner join tblsecurityvmeasurement on tblsecuritygroup.securitygroupid = tblsecurityvmeasurement.securitygroupid) on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityvmeasurement.securitypermissionid)=6))
group by tblsecurityuser.securityuserid, tblsecurityvmeasurement.vmeasurementid
having (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygrouprestrictivevmeasurementcombined(securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select * from securitygrouprestrictivevmeasurement1($1)
UNION
select * from securitygrouprestrictivevmeasurement2($1)
UNION SELECT * from securitygrouprestrictivevmeasurement3($1);$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

  CREATE OR REPLACE FUNCTION securitygroupsbyuser3(securityuserid uuid)
  RETURNS SETOF integer AS
$BODY$select tblsecuritygroup_2.securitygroupid 
from tblsecurityuser 
inner join ((tblsecuritygroup 
inner join
((tblsecuritygroupmembership as tblsecuritygroupmembership_1 
inner join (tblsecuritygroupmembership 
inner join tblsecuritygroup as tblsecuritygroup_1 
on tblsecuritygroupmembership.parentsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
on tblsecuritygroupmembership_1.childsecuritygroupid = tblsecuritygroup_1.securitygroupid) 
inner join tblsecuritygroup as tblsecuritygroup_2 
on tblsecuritygroupmembership_1.parentsecuritygroupid = tblsecuritygroup_2.securitygroupid) 
on tblsecuritygroup.securitygroupid = tblsecuritygroupmembership.childsecuritygroupid) 
inner join tblsecurityusermembership 
on tblsecuritygroup.securitygroupid = tblsecurityusermembership.securitygroupid) 
on tblsecurityuser.securityuserid = tblsecurityusermembership.securityuserid
where (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
  CREATE OR REPLACE FUNCTION securitygroupsbyuser2(securityuserid uuid)
  RETURNS SETOF integer AS
$BODY$SELECT tblSecurityGroup_1.SecurityGroupID
FROM tblsecurityuser INNER JOIN ((tblsecuritygroup INNER JOIN
(tblsecuritygroupmembership INNER JOIN tblsecuritygroup AS
tblsecuritygroup_1 ON tblsecuritygroupmembership.parentsecuritygroupid=tblsecuritygroup_1.securitygroupid) ON
tblsecuritygroup.securitygroupid=tblsecuritygroupmembership.childsecuritygroupid) INNER JOIN tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid) ON 
tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygroupsbyuser1(securityuserid uuid)
  RETURNS SETOF integer AS
$BODY$SELECT tblsecuritygroup.securitygroupid
FROM tblsecurityuser INNER JOIN (tblsecuritygroup INNER JOIN
tblsecurityusermembership ON
tblsecuritygroup.securitygroupid=tblsecurityusermembership.securitygroupid)
ON tblsecurityuser.securityuserid=tblsecurityusermembership.securityuserid
WHERE (((tblsecurityuser.securityuserid)=$1));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygroupsbyuser(securityuserid uuid)
  RETURNS SETOF integer AS
$BODY$SELECT * from securitygroupsbyuser1($1)
UNION
SELECT * from securitygroupsbyuser2($1)
UNION SELECT * from securitygroupsbyuser3($1)$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitygroupelementmaster(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select securitygrouppermissiveelementcombined.* 
from securitygrouppermissiveelementcombined($1, $2) left join securitygrouprestrictiveelementcombined($2) on securitygrouppermissiveelementcombined.objectid = securitygrouprestrictiveelementcombined.objectid 
where (((securitygrouprestrictiveelementcombined.objectid) is null));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
  
CREATE OR REPLACE FUNCTION securitygroupvmeasurementmaster(securitypermissionid integer, securityuserid uuid)
  RETURNS SETOF securityuseruuidandobjectuuid AS
$BODY$select securitygrouppermissivevmeasurementcombined.* 
from securitygrouppermissivevmeasurementcombined($1, $2) left join securitygrouprestrictivevmeasurementcombined($2) on securitygrouppermissivevmeasurementcombined.objectid = securitygrouprestrictivevmeasurementcombined.objectid 
where (((securitygrouprestrictivevmeasurementcombined.objectid) is null));$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

CREATE OR REPLACE FUNCTION securitypermselement(securityuserid uuid, securitypermissionid integer, elementid integer)
  RETURNS boolean AS
$BODY$DECLARE
  myelementid integer;
  myobjectid integer;
  mybin integer;
  myboolbin boolean;
BEGIN
	-- Check if user is in admin group
	SELECT isadmin into myboolbin from isadmin($1) where isadmin=true;
	IF NOT FOUND THEN
		-- User not in admin group so continue checking permissions
		-- Next check that a 'no permission' record isn't overiding
		 SELECT objectid INTO mybin from securitygrouprestrictiveelementcombined($1) where objectid=$3;

		 IF NOT FOUND THEN
			-- There is no 'no permission' record so continue checking permissions
			-- Check for specified permission, on specified tree, for specified user
			SELECT objectid 
			INTO myelementid 
			FROM securitygroupelementmaster($2, $1) 
			WHERE objectid=$3;

			IF NOT FOUND THEN
				-- No tree permissions specified so move up to object to check permissions
				SELECT objectid
				INTO myobjectid
				FROM tblsubobject, tblelement
				WHERE tblelement.subobjectid=tblsubobject.subobjectid 
				AND tblelement.elementid=$3;
				-- Return true/false for permission from object 
				return securitypermsobject($1, $2, myobjectid);
			ELSE
				-- Tree permission record found so return true 
				return true;
			END IF;
		 ELSE
			-- 'No permission' specified for tree so return false
			return false;
		 END IF;
	ELSE
		-- User is in admin group so return true
		return true;
	END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
    
CREATE OR REPLACE FUNCTION cpgdb.isadmin(securityuserid uuid)
  RETURNS boolean AS
$BODY$select 
case when securitygroupsbyuser=1 
then true 
else null 
end 
as isadmin 
from securitygroupsbyuser($1) 
where securitygroupsbyuser=1;$BODY$
  LANGUAGE sql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION securitypermsobject(securityuserid uuid, securitypermissionid integer, securityobjectid integer)
  RETURNS boolean AS
$BODY$DECLARE
  myobjectid integer;
  mydatabaseid integer;
  mybin integer;
  myboolbin boolean;
BEGIN

 SELECT isadmin into myboolbin from isadmin($1) where isadmin=true;
 IF NOT FOUND THEN
	-- User not in admin group so continue checking permissions
	-- Next check that a 'no permission' record isn't overiding
	 SELECT objectid INTO mybin from securitygrouprestrictiveobjectcombined($1) where objectid=$3;

	 IF NOT FOUND THEN
		-- No 'no permssion' record for the object so continue checking permissions
		-- Check for specified permission, on specified object, for specified user
		SELECT objectid 
		INTO myobjectid 
		FROM securitygroupobjectmaster($2, $1) 
		WHERE objectid=$3;

		IF NOT FOUND THEN
			-- -- No permissions specified so move up to database level to check default permissions
			-- SELECT databaseid
			-- INTO mydatabaseid
			-- FROM tblobject
			-- WHERE tblobject.objectid=myobjectid
			-- -- Return true/false for permission from object 
			-- return securitypermsobject($1, $2, mydatabaseid); 
			return false;
		 ELSE
			-- Site permission record found so return true
			return true;
		 END IF;
	 ELSE
		-- 'No permission' specified for tree
		return false;
	 END IF;
ELSE
	-- User is in admin group so return true;
	return true;
END IF;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
CREATE OR REPLACE FUNCTION cpgdb.getuserpermissionset(uuid, character varying, uuid)
  RETURNS typpermissionset AS
$BODY$
DECLARE
   _securityUserID ALIAS FOR $1;
   _ptype ALIAS FOR $2;
   _pid ALIAS FOR $3;

   groupMembership int[];
   perms typPermissionSet;
BEGIN

   SELECT * INTO groupMembership FROM cpgdb.GetGroupMembershipArray($1);

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

   perms := cpgdb.GetGroupPermissionSet(groupMembership, _ptype, _pid);
   RETURN perms;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;  
  
-- 
-- Force users to upgrade to Tellervo desktop version 1.0.3 
--
UPDATE tblsupportedclient SET minversion='1.0.3' WHERE client='Tellervo WSI';