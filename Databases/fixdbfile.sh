#!/bin/sh

#
# This script removes any sort of absolute references from the db dump
# Useful for migrating to another system!
#

echo "Removing absolute \$libdir references..."
sed -i_pre_fix_backup 's/\/usr\/lib\/postgresql\/8.2\/lib/$libdir/' pg_dump_corina.sql

echo "Removing absolute liblwgeom versioning..."
sed -i 's/liblwgeom.so.1.[0-9]/liblwgeom.so.1/' pg_dump_corina.sql
