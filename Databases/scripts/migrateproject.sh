#!/bin/bash
#
# Simple script for copying all projects, objects, elements, samples and radii from one database
# to another
#


if [ "$#" -ne 3 ]; then
	
	printf "\nUseage:\n";
        printf "migrateproject.sh sourcedatabase targetdatabase projectuuid\n\n"
        printf "   sourcedatabase : Database from which you are copying records\n"
        printf "   targetdatabase : Database to which you are copying records\n"
        printf "   projectuuid    : ID of the project you'd like to copy\n"
	printf "\n\n";
	exit 0;
fi


function doMigration(){
	# Projects
	printf '\n\n\n**************************************************\n';
	printf 'Migrating projects from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblproject where projectid='$3') TO stdout" | psql $2 -c "COPY tblproject FROM stdin ON CONFLICT (projectid) do nothing"

	# Top level objects
	printf '\n\n\n**************************************************\n';
	printf 'Migrating top level objects from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblobject where projectid='$3') TO stdout" | psql $2 -c "COPY tblobject FROM stdin"

	# Sub-objects
	printf '\n\n\n**************************************************\n';
	printf 'Migrating sub objects from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblobject where parentobjectid IN (SELECT objectid from tblobject WHERE projectid='$3')) TO stdout" | psql $2 -c "COPY tblobject FROM stdin"

	# Elements
	printf '\n\n\n**************************************************\n';
	printf 'Migrating elements from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblelement where objectid IN (SELECT objectid from tblobject WHERE projectid='$3' OR projectid='$3')) TO stdout" | psql $2 -c "COPY tblelement FROM stdin"

	# Boxes
	printf '\n\n\n**************************************************\n';
	printf 'Migrating boxes from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblbox where boxid IN (SELECT boxid from tblsample WHERE elementid IN (SELECT elementid from tblelement WHERE objectid IN (SELECT objectid from tblobject where projectid='$3' OR projectid='$3')))) TO stdout" | psql $2 -c "COPY tblbox FROM stdin"

	# Samples
	printf '\n\n\n**************************************************\n';
	printf 'Migrating samples from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblsample where elementid IN (SELECT elementid from tblelement WHERE objectid IN (SELECT objectid from tblobject where projectid='$3' OR projectid='$3'))) TO stdout" | psql $2 -c "COPY tblsample FROM stdin"

	# Radii
	printf '\n\n\n**************************************************\n';
	printf 'Migrating radii from '$1' to '$2;
	printf '\n**************************************************\n';
	psql $1 -c "COPY (select * from tblradius where sampleid IN (SELECT sampleid from tblsample WHERE elementid IN (SELECT elementid from tblelement WHERE objectid IN (SELECT objectid from tblobject where projectid='$3' OR projectid='$3')))) TO stdout" | psql $2 -c "COPY tblradius FROM stdin"

}



printf '\n******************************************************';
printf '\nMigrating Tellervo data between databases:\n';
if [ "$1" != "" ]; then
    printf '\nSource database:  '$1;
fi

if [ "$2" != "" ]; then
    printf '\nTarget database:  '$2;
fi

if [ "$3" != "" ]; then
    printf '\nProject ID     :  '$3;
fi
printf '\n******************************************************\n\n';


read -p "Are you sure you want to continue? (y/N)" -n 1 -r
echo    # (optional) move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
    doMigration $1 $2 $3;
fi

