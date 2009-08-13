#!/usr/bin/perl

use DBI;

my $olddbname = 'corina_live';

my $sitens = '97f3d478-9693-484c-98bc-98394c81c9cf';
my $subsitens = 'e57a7261-faf7-422a-ae51-4525e7431b72';
my $treens = 'a4738364-bed5-4bf4-9f47-8f09ad41cade';
my $specimenns = '98300c41-d06d-473f-a2e7-c28a12147de9';
my $radiusns = '525fa19f-8842-4ea1-9337-e87e92ab56c0';
my $vmns = '7b3c5a4f-15e2-43e3-9f94-b60fefe01cc2';

# hash for subsiteid->siteid so we can remove subsites that are "Main"
my %mainsubsites = ();

# hashes for name->id
my %sampletypes = ();
my %complexpres = ();

# hash for specimenid -> (unmeaspre, etc)
my %specimenrmap = ();

my $dbh = DBI->connect("dbi:Pg:dbname=$olddbname", "", "") or die ("no db");

# make lookup tables...
make_lkups();

print "BEGIN TRANSACTION;\n";

copy_stuff_pre();

do_objects();
do_elements();
do_samples();
do_radii();
do_measurements();
do_vmeasurements();

copy_stuff();

print "COMMIT;\n";

sub copy_stuff_pre() {
   dumptable('tblsecuritydefault');
   dumptable('tblsecuritygroup');
   dumptable('tblsecuritygroupmembership');
   dumptable('tblsecurityuser');
   dumptable('tblsecurityusermembership');
}

sub copy_stuff() {
   dumptable('tblreading');

   keepseq('tblvmeasurementgroup_vmeasurementgroupid_seq');
}

sub keepseq($) {
   my $s = shift;

   my $sq = $dbh->prepare("SELECT last_value FROM $s");
   $sq->execute() or die("can't get seq $s");

   while(my $r = $sq->fetchrow_hashref() ) {
      $val = $r->{last_value};
      print "SELECT pg_catalog.setval('$s', $val, true);\n\n";
   }
   $sq->finish;   
}

sub dumptable($) {
   my $t = shift;
   my $newt = shift;

   print "TRUNCATE TABLE $t CASCADE;\n\n";
   my $cmd = "pg_dump -a --disable-triggers -t $t $olddbname";

   if(defined $newt) {
      $cmd .= "| sed $newt";
   }

   system($cmd);
}

sub make_lkups() {
   $complexpres{"Absent"} = 3;
   $complexpres{"Present but undateable"} = 2;
   $complexpres{"Present"} = 1;

   $sampletypes{"Section"} = 1;
   $sampletypes{"Charcoal"} = 2;
   $sampletypes{"Core"} = 3;
}

sub get_pithid($) {
   my $val = shift;

   if(!defined $val) {
      return 5; #unknown
   }

   my $rval = $complexpres{$val} or die ("No value $val");
   return $rval;
}

sub get_sampletypeid($) {
   my $val = shift;

   if(!defined $val) {
      return 'NULL';
   }

   my $rval = $sampletypes{$val} or die ("No value $val");
   return $rval;
}

sub do_objects() {
   my $sq = $dbh->prepare('SELECT * FROM tblSite ORDER BY siteID');

   starttable('tblObject');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {
      my $stmt = "INSERT INTO tblObject(ObjectID, Title, Code, createdtimestamp, lastmodifiedtimestamp, locationgeometry, objectTypeID) VALUES (";
      $stmt .= touuid($r->{siteid}, $sitens, 'site') . ', ';
      $stmt .= esc($r->{name}) . ', ';
      $stmt .= esc($r->{code}) . ', ';
      $stmt .= esc($r->{createdtimestamp}) . ', ';
      $stmt .= esc($r->{lastmodifiedtimestamp}) . ', ';
      $stmt .= esc($r->{extent}, 1);
      $stmt .= '1'; # site

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   $sq = $dbh->prepare('SELECT * FROM tblSubSite ORDER BY subSiteID');
   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {
      if($r->{name} eq "Main") {
         $mainsubsites{$r->{subsiteid}} = $r->{siteid};
         next;
      }

      my $stmt = "INSERT INTO tblObject(ObjectID, parentObjectID, Title, Code, createdtimestamp, lastmodifiedtimestamp, objectTypeID) VALUES (";
      $stmt .= touuid($r->{subsiteid}, $subsitens, 'subsite') . ', ';
      $stmt .= touuid($r->{siteid}, $sitens, 'site') . ', ';
      $stmt .= esc($r->{name}) . ', ';
      $stmt .= esc(trimsubsite($r->{name})) . ', ';
      $stmt .= esc($r->{createdtimestamp}) . ', ';
      $stmt .= esc($r->{lastmodifiedtimestamp}, 1);
      $stmt .= '2'; # subsite

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   endtable('tblObject');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub do_elements() {
   $sq = $dbh->prepare('SELECT * FROM tblTree ORDER BY treeID');

   starttable('tblElement');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {
      my $upid;

      if(defined $mainsubsites{$r->{subsiteid}}) {
         # we map to a 'main' subsite
         $upid = touuid($mainsubsites{$r->{subsiteid}}, $sitens, 'site');
      } else {
         $upid = touuid($r->{subsiteid}, $subsitens, 'subsite');
      }

      my $stmt = "INSERT INTO tblElement(elementid, objectid, taxonid, code, createdtimestamp, lastmodifiedtimestamp, islivetree, locationPrecision, locationGeometry, elementTypeID) VALUES (";
      $stmt .= touuid($r->{treeid}, $treens, 'tree') . ', ';
      $stmt .= $upid . ', ';
      $stmt .= esc($r->{taxonid}) . ', ';
      $stmt .= esc(undupename($upid, 'tree', $r->{name})) . ', ';
      $stmt .= esc($r->{createdtimestamp}) . ', ';
      $stmt .= esc($r->{lastmodifiedtimestamp}) . ', ';
      $stmt .= escbool($r->{islivetree}) . ', ';
      if(defined $r->{location}) {
         $stmt .= esc($r->{precision}) . ', ';
         $stmt .= esc($r->{location}) . ', ';
      } else {
         $stmt .= 'NULL, NULL, ';
      }
      $stmt .= '1'; # elementTypeID of Tree

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   endtable('tblElement');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub do_samples() {
   $sq = $dbh->prepare('SELECT * FROM tblSpecimen ORDER BY specimenID');

   starttable('tblSample');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {
      my $typeid = get_sampletypeid($r->{specimentype});
      my $sid = $r->{specimenid};
      
      $specimenrmap{$sid}->{sapwoodcount} = $r->{sapwoodcount};
      $specimenrmap{$sid}->{unmeaspre} = $r->{unmeaspre};
      $specimenrmap{$sid}->{unmeaspost} = $r->{unmeaspost};
      $specimenrmap{$sid}->{pithid} = get_pithid($r->{pith});
      $specimenrmap{$sid}->{terminalring} = $r->{terminalring};

      my $stmt = "INSERT INTO tblSample(sampleID, elementid, code, createdtimestamp, lastmodifiedtimestamp, samplingdate, typeid) VALUES (";
      $stmt .= touuid($r->{specimenid}, $specimenns, 'spec') . ', ';
      $stmt .= touuid($r->{treeid}, $treens, 'tree') . ', ';
      $stmt .= esc(undupename($r->{treeid}, 'specimen', $r->{name})) . ', ';
      $stmt .= esc($r->{createdtimestamp}) . ', ';
      $stmt .= esc($r->{lastmodifiedtimestamp}) . ', ';
      $stmt .= esc($r->{datecollected}) . ', ';
      $stmt .= $typeid;

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   endtable('tblSample');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub do_radii() {
   $sq = $dbh->prepare('SELECT * FROM tblRadius ORDER BY radiusID');

   starttable('tblRadius');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {
      my $sid = $r->{specimenid};

      my $sapwoodcount = $specimenrmap{$sid}->{sapwoodcount};      
      my $unmeaspre = $specimenrmap{$sid}->{unmeaspre};      
      my $unmeaspost = $specimenrmap{$sid}->{unmeaspost};      
      my $pithid = $specimenrmap{$sid}->{pithid};      
      my $terminalring = $specimenrmap{$sid}->{terminalring};

      my $stmt = "INSERT INTO tblRadius(RadiusID, SampleID, code, createdtimestamp, lastmodifiedtimestamp, pithid, " .
                 "missingHeartwoodRingsToPith, missingHeartwoodRingsToPithFoundation, missingSapwoodRingsToBark, missingSapwoodRingsToBarkFoundation, " .
                 "barkPresent, sapwoodid) VALUES (";
      $stmt .= touuid($r->{radiusid}, $radiusns, 'radius') . ', ';
      $stmt .= touuid($r->{specimenid}, $specimenns, 'spec') . ', ';
      $stmt .= esc(undupename($r->{specimenid}, 'radius', $r->{name})) . ', ';
      $stmt .= esc($r->{createdtimestamp}) . ', ';
      $stmt .= esc($r->{lastmodifiedtimestamp}) . ', ';
      $stmt .= $pithid . ', ';
      if(defined $unmeaspre) {
         $stmt .= esc($unmeaspre, 1);
         $stmt .= "'Observed but not measured', ";
      }
      else {
         $stmt .= 'NULL, NULL, ';
      }

      if(defined $unmeaspost) {
         $stmt .= esc($unmeaspost, 1);
         $stmt .= "'Observed but not measured', ";
      }
      else {
         $stmt .= 'NULL, NULL, ';
      }

      if($terminalring eq 'Bark') {
         $stmt .= 'true, ';
      } else {
         $stmt .= 'false, ';
      }

      if($terminalring eq 'Waney edge') {
         $stmt .= '1'; #complete
      } elsif ($terminalring eq 'Near edge') {
         $stmt .= '2'; #incomplete
      } else {
         $stmt .= '5'; #unknown
      }

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   endtable('tblRadius');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub do_measurements() {
   $sq = $dbh->prepare('SELECT * FROM tblMeasurement ORDER BY measurementID');

   starttable('tblMeasurement');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {

      my $stmt = "INSERT INTO tblMeasurement(measurementID, radiusID, isReconciled, startYear, isLegacyCleaned, ".
                 "importTableName, measuredByID, createdtimestamp, lastmodifiedtimestamp, datingtypeid, datingerrorpositive, datingerrornegative) VALUES (";
      $stmt .= esc($r->{measurementid}, 1);
      $stmt .= touuid($r->{radiusid}, $radiusns, 'radius') . ', ';
      $stmt .= escbool($r->{isreconciled}, 1);
      $stmt .= esc($r->{startyear}, 1);
      $stmt .= escbool($r->{islegacycleaned}, 1);
      $stmt .= esc($r->{importtablename}, 1);
      $stmt .= esc($r->{measuredbyid}, 1);
      $stmt .= esc($r->{createdtimestamp}, 1);
      $stmt .= esc($r->{lastmodifiedtimestamp}, 1);
      $stmt .= esc($r->{datingtypeid}, 1);
      $stmt .= esc($r->{datingerrorpositive}, 1);
      $stmt .= esc($r->{datingerrornegative}, false);

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   endtable('tblMeasurement');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub do_vmeasurements() {
   $sq = $dbh->prepare('SELECT * FROM tblVMeasurement ORDER BY vmeasurementID');

   starttable('tblVMeasurement');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {

      my $stmt = "INSERT INTO tblVMeasurement(vmeasurementid, measurementid, vmeasurementopid, vmeasurementopparameter, code, comments, ispublished, ".
                 "owneruserid, createdtimestamp, lastmodifiedtimestamp, isgenerating, birthdate".
                 ") VALUES (";
      $stmt .= touuid($r->{vmeasurementid}, $vmns, 'vm') . ', ';
      $stmt .= esc($r->{measurementid}, 1);
      $stmt .= esc($r->{vmeasurementopid}, 1);
      $stmt .= esc($r->{vmeasurementopparameter}, 1);
      $stmt .= esc($r->{name}, 1);
      $stmt .= esc($r->{description}, 1);
      $stmt .= escbool($r->{ispublished}, 1);
      $stmt .= esc($r->{owneruserid}, 1);
      $stmt .= esc($r->{createdtimestamp}, 1);
      $stmt .= esc($r->{lastmodifiedtimestamp}, 1);
      $stmt .= 'false, ';
      $stmt .= esc($r->{createdtimestamp}, 0);

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   do_vmeasurementgroups();

   endtable('tblVMeasurement');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub do_vmeasurementgroups() {
   $sq = $dbh->prepare('SELECT * FROM tblVMeasurementGroup ORDER BY vmeasurementID');

   starttable('tblVMeasurementGroup');

   $sq->execute() or die("bad query");
   while(my $r = $sq->fetchrow_hashref() ) {

      my $stmt = "INSERT INTO tblVMeasurementGroup(VMeasurementID, MemberVMeasurementID, VMeasurementGroupID) VALUES (";
      $stmt .= touuid($r->{vmeasurementid}, $vmns, 'vm') . ', ';
      $stmt .= touuid($r->{membervmeasurementid}, $vmns, 'vm') . ', ';
      $stmt .= esc($r->{vmeasurementgroupid}, 0);

      $stmt .= ')';
      print "$stmt;\n";
   }
   $sq->finish;

   endtable('tblVMeasurementGroup');

   die "Err: ", $sth->errstr(), "\n" if $sq->err();
}

sub touuid($$$) {
   my $id = shift;
   my $ns = shift;
   my $prefix = shift;

   return "uuid_generate_v5('$ns', '$prefix$id')";
}

sub esc($?) {
   my $val = shift;
   my $extra = shift;
   if(! defined $val) {
      if (defined $extra && $extra > 0) {
         return 'NULL, ';
      }
      return 'NULL';
   }
   if (defined $extra && $extra > 0) {
     return $dbh->quote($val) . ', ';
   }
   return $dbh->quote($val);
}

sub escbool($?) {
   my $val = shift;
   my $extra = shift;

   if(! defined $val) {
      if (defined $extra && $extra > 0) {
         return 'NULL, ';
      }
      return 'NULL';
   }

   if (defined $extra && $extra > 0) {
      return $val ? 'true, ' : 'false, ';
   }

   return $val ? 'true' : 'false';
}

my %undupe = ();
sub undupename($$$) {
   my $parentid = shift;
   my $type = shift;
   my $name = shift;

   my $key = $parentid.':'.$type.':'.$name;

   if(defined($undupe{$key})) {
      my $cnt = $undupe{$key};
      $undupe{$key} = $cnt + 1;
      return $name.'('.$cnt.')';
   }
   else {
      $undupe{$key} = 1;
      return $name;
   }
}

sub trimsubsite($) {
   my $val = shift;

   if(length($val) > 18) {
      return substr($val, 0, 16) . '...';
   }

   return $val;
}

sub starttable($) {
   my $t = shift;
   print "------------------------------------------------------------\n";
   print "-- BEGIN $t\n";
   print "------------------------------------------------------------\n\n";

   print "ALTER TABLE $t DISABLE TRIGGER ALL;\n";
   print "TRUNCATE TABLE $t CASCADE;\n\n";
}

sub endtable($) {
   my $t = shift;

   print "ALTER TABLE $t ENABLE TRIGGER ALL;\n";
   print "------------------------------------------------------------\n";
   print "-- END $t\n";
   print "------------------------------------------------------------\n\n";
}
