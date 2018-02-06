#!/bin/bash
 
function showHelp {
  echo ""
  echo "Utility to merge Tellervo databases together."
  echo ""
  echo "  Useage:  mergedbs -d todbname -f fromdbname"
  echo ""
  echo "  Arguments:"
  echo "    -d     Name of database into which to merge"
  echo "    -f     Name of database to merge from"
  echo "    -h     Show this help"
  echo ""
} 

safeRunCommand() {
  typeset cmnd="$*"
  typeset ret_code

  eval $cmnd
  ret_code=$?
  if [ $ret_code != 0 ]; then
    printf "   Error when executing command: '$cmnd'" $ret_code
    echo ""
    echo ""
    echo "MERGE FAILED!!!"
    exit $ret_code
  fi
}

insertSQL = "-- 
-- SQL CODE FOR INSERTING RECORDS INTO THE CURRENT DATABASE FROM A SECOND DATABASE
-- THE SECOND DATABASE MUST ALREADY HAVE BEEN RESTORED INTO THIS DATABASE INTO A SEPARATE
-- SCHEMA CALLED 'MERGEFROM'.
--

INSERT INTO tblsecurityuser SELECT * FROM mergefrom.tblsecurityuser ON CONFLICT DO NOTHING;
INSERT INTO tbltag SELECT * FROM mergefrom.tbltag ON CONFLICT DO NOTHING;
INSERT INTO tblbox SELECT * FROM mergefrom.tblbox ON CONFLICT DO NOTHING;
INSERT INTO tbllaboratory SELECT * FROM mergefrom.tbllaboratory ON CONFLICT DO NOTHING;


INSERT INTO tblproject SELECT * FROM mergefrom.tblproject ON CONFLICT DO NOTHING;
INSERT INTO tblprojectprojecttype SELECT * FROM mergefrom.tblprojectprojecttype ON CONFLICT DO NOTHING;

INSERT INTO tblobject (
 objectid                     ,
 title                        ,
 code                         ,
 createdtimestamp             ,
 lastmodifiedtimestamp        ,
 locationgeometry             ,
 locationtypeid               ,
 locationprecision            ,
 locationcomment              ,
 file                         ,
 creator                      ,
 owner                        ,
 parentobjectid               ,
 description                  ,
 objecttypeid                 ,
 coveragetemporalid           ,
 coveragetemporalfoundationid ,
 comments                     ,
 coveragetemporal             ,
 coveragetemporalfoundation   ,
 locationaddressline1         ,
 locationaddressline2         ,
 locationcityortown           ,
 locationstateprovinceregion  ,
 locationpostalcode           ,
 locationcountry              ,
 vegetationtype               ,
 domainid                     ,
 projectid     )  
select  objectid                     ,
 title                        ,
 code                         ,
 createdtimestamp             ,
 lastmodifiedtimestamp        ,
 public.st_geomfromewkt(mergefrom.st_asewkt(locationgeometry))          ,
 locationtypeid               ,
 locationprecision            ,
 locationcomment              ,
 file                         ,
 creator                      ,
 owner                        ,
 parentobjectid               ,
 description                  ,
 objecttypeid                 ,
 coveragetemporalid           ,
 coveragetemporalfoundationid ,
 comments                     ,
 coveragetemporal             ,
 coveragetemporalfoundation   ,
 locationaddressline1         ,
 locationaddressline2         ,
 locationcityortown           ,
 locationstateprovinceregion  ,
 locationpostalcode           ,
 locationcountry              ,
 vegetationtype               ,
 domainid                     ,
 projectid  
FROM mergefrom.tblobject 
ON CONFLICT DO NOTHING
;




INSERT INTO tblelement (
elementid                   ,
 taxonid                     ,
 locationprecision           ,
 code                        ,
 createdtimestamp            ,
 lastmodifiedtimestamp       ,
 locationgeometry            ,
 islivetree                  ,
 originaltaxonname           ,
 locationtypeid              ,
 locationcomment             ,
 file                        ,
 description                 ,
 processing                  ,
 marks                       ,
 diameter                    ,
 width                       ,
 height                      ,
 depth                       ,
 unsupportedxml              ,
 unitsold                    ,
 objectid                    ,
 elementtypeid               ,
 elementauthenticityid       ,
 elementshapeid              ,
 altitudeint                 ,
 slopeangle                  ,
 slopeazimuth                ,
 soildescription             ,
 soildepth                   ,
 bedrockdescription          ,
 comments                    ,
 locationaddressline2        ,
 locationcityortown          ,
 locationstateprovinceregion ,
 locationpostalcode          ,
 locationcountry             ,
 locationaddressline1        ,
 altitude                    ,
 gispkey                     ,
 units                       ,
 authenticity                ,
 domainid                    
)
SELECT 
 elementid                   ,
 taxonid                     ,
 locationprecision           ,
 code                        ,
 createdtimestamp            ,
 lastmodifiedtimestamp       ,
 public.st_geomfromewkt(mergefrom.st_asewkt(locationgeometry))          ,
 islivetree                  ,
 originaltaxonname           ,
 locationtypeid              ,
 locationcomment             ,
 file                        ,
 description                 ,
 processing                  ,
 marks                       ,
 diameter                    ,
 width                       ,
 height                      ,
 depth                       ,
 unsupportedxml              ,
 unitsold                    ,
 objectid                    ,
 elementtypeid               ,
 elementauthenticityid       ,
 elementshapeid              ,
 altitudeint                 ,
 slopeangle                  ,
 slopeazimuth                ,
 soildescription             ,
 soildepth                   ,
 bedrockdescription          ,
 comments                    ,
 locationaddressline2        ,
 locationcityortown          ,
 locationstateprovinceregion ,
 locationpostalcode          ,
 locationcountry             ,
 locationaddressline1        ,
 altitude                    ,
 gispkey                     ,
 units                       ,
 authenticity                ,
 domainid                    
 FROM mergefrom.tblelement
ON CONFLICT DO NOTHING
;

INSERT INTO tblsample SELECT * FROM mergefrom.tblsample ON CONFLICT DO NOTHING;
INSERT INTO tblradius SELECT * FROM mergefrom.tblradius ON CONFLICT DO NOTHING;
INSERT INTO tblmeasurement SELECT * FROM mergefrom.tblmeasurement ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurement SELECT * FROM mergefrom.tblvmeasurement ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurementtotag SELECT * FROM mergefrom.tblvmeasurementtotag ON CONFLICT DO NOTHING;
INSERT INTO tblreading SELECT * FROM mergefrom.tblreading ON CONFLICT DO NOTHING;
INSERT INTO tblreadingreadingnote SELECT * FROM mergefrom.tblreadingreadingnote ON CONFLICT DO NOTHING;
INSERT INTO tblcrossdate SELECT * FROM mergefrom.tblcrossdate ON CONFLICT DO NOTHING;
INSERT INTO tblredate SELECT * FROM mergefrom.tblredate ON CONFLICT DO NOTHING;
INSERT INTO tbltruncate SELECT * FROM mergefrom.tbltruncate ON CONFLICT DO NOTHING;
INSERT INTO tblcuration SELECT * FROM mergefrom.tblcuration ON CONFLICT DO NOTHING;
INSERT INTO tblcustomvocabterm SELECT * FROM mergefrom.tblcustomvocabterm ON CONFLICT DO NOTHING;
INSERT INTO tlkpuserdefinedfield SELECT * FROM mergefrom.tlkpuserdefinedfield ON CONFLICT DO NOTHING;
INSERT INTO tlkpuserdefinedterm SELECT * FROM mergefrom.tlkpuserdefinedterm ON CONFLICT DO NOTHING;
INSERT INTO tbluserdefinedfieldvalue SELECT * FROM mergefrom.tbluserdefinedfieldvalue ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurementderivedcache SELECT * FROM mergefrom.tblvmeasurementderivedcache ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurementgroup SELECT * FROM mergefrom.tblvmeasurementgroup ON CONFLICT DO NOTHING;

INSERT INTO tblvmeasurementmetacache (
vmeasurementid,
  startyear,
  readingcount,
  measurementcount,
  vmextent,
  vmeasurementmetacacheid,
  objectcode,
  objectcount,
  commontaxonname,
  taxoncount,
  prefix,
  datingtypeid)
SELECT 
vmeasurementid,
  startyear,
  readingcount,
  measurementcount,
  public.st_geomfromewkt(mergefrom.st_asewkt(vmextent)) ,
  vmeasurementmetacacheid,
  objectcode,
  objectcount,
  commontaxonname,
  taxoncount,
  prefix,
  datingtypeid
FROM mergefrom.tblvmeasurementmetacache 
 ON CONFLICT DO NOTHING;

INSERT INTO tblvmeasurementreadingnoteresult SELECT * FROM mergefrom.tblvmeasurementreadingnoteresult ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurementreadingresult SELECT * FROM mergefrom.tblvmeasurementreadingresult ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurementrelyearreadingnote SELECT * FROM mergefrom.tblvmeasurementrelyearreadingnote ON CONFLICT DO NOTHING;
INSERT INTO tblvmeasurementresult SELECT * FROM mergefrom.tblvmeasurementresult ON CONFLICT DO NOTHING;
"
 
while getopts "d:f:h" opt; do
  case $opt in
    d)
      todbname=$OPTARG
      doneflag1=true;
      ;;
      
    f)
      fromdbname=$OPTARG
      doneflag2=true;
      ;;      
    h)
      showHelp
      exit 0
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      exit 1
      ;;
    \?)
      showHelp
      exit 0
  esac
done

if [ -z "$todbname" ]
then
    echo "Invalid options:"
    echo "  -d (name of database to merge TO) must be included " >&2
    
    exit 1
fi

if [ -z "$fromdbname" ]
then
    echo "Invalid options:"
    echo "  -f (name of the database to merge FROM) must be included" >&2
    showHelp
    exit 1
fi

echo ""
echo "You have requested to merge data from '$fromdbname' to '$todbname'.  The merge is not revertable, so you should ensure you have a backup of your data before you proceed."
echo ""
read -r -p "Are you sure you want to continue? [y/N] " response
if [[ "$response" =~ ^([yY][eE][sS]|[yY])+$ ]]
then
    echo "Merging data from '$fromdbname' to '$todbname'"
    echo "Please wait..."
else
    exit 0
fi


safeRunCommand "psql --dbname=$fromdbname -U tellervo -c \"ALTER SCHEMA public RENAME TO mergefrom\""
safeRunCommand "pg_dump -Fc -U tellervo --schema=mergefrom --dbname=$fromdbname | pg_restore -U tellervo --dbname=$todbname"
safeRunCommand "psql --dbname=$todbname -U tellervo $insertSQL"
safeRunCommand "psql --dbname=$fromdbname -U tellervo -c \"ALTER SCHEMA mergefrom RENAME TO public\""

exit 0
