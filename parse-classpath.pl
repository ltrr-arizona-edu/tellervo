#!/usr/bin/perl

# Utility for parsing the .classpath file into ant/jnlp useful tidbits
# $Id$

#        <classpathentry kind="lib" path="Libraries/jfontchooser-1.0.5.jar"/>

my $names;
my $paths;
my $cnt = 0;

while(<STDIN>) {
   if (/\s*<classpathentry.* kind="lib" .*>/) {
      if(/ path="([^"]*)"( |\/>)/) {
         my $path = $1;
         my $name = $1;

         if($path =~ /^Libraries$/) {
            next;
         }

         $name =~ m/\/([^\/]+.jar)$/;
         $name = $1;
         $name =~ s/[0-9\-_\.]+(b|beta)*[0-9-_\.]*\.jar$/\.jar/;         

         $names[$cnt] = $name;
         $paths[$cnt] = $path;
         $cnt++;
      }
   }
}

my $i;

# properties file

print "---\nFor properties file\n---\n";
for($i=0; $i<$cnt; $i++) {
   my $path = $paths[$i];
   my $name = $names[$i];

   print "$name=$path\n";
}

print "---\nFor xml file 1/2\n---\n";
for($i=0; $i<$cnt; $i++) {
   my $path = $paths[$i];
   my $name = $names[$i];

   print '    <property name="'.$name.'" value="(set this in build.properties!)"/>' . "\n";
}

print "---\nFor xml file 2/2\n---\n";
for($i=0; $i<$cnt; $i++) {
   my $path = $paths[$i];
   my $name = $names[$i];

   print '    <pathelement location="${' . $name . '}"/>' . "\n";
}

print "---\nFor jnlp libraries file\n---\n";
for($i=0; $i<$cnt; $i++) {
   my $path = $paths[$i];
   my $name = $names[$i];

   my $libdir = $path;
   $libdir =~ s/Libraries/lib/;

   print '    <jar href="' .$libdir. '"/>' . "\n";
}


