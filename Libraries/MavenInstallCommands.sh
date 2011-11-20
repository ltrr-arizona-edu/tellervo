# The Jars within this folder are those not found in standard Maven repositories.  They have been
# placed in the org.tridas.maven repository but are included here in case this repository becomes
# unavailable.  The jars can be installed with the following commands:

mvn install:install-file -DgroupId=com.l2prod.common   -DartifactId=l2fprod-common-sheet       -Dversion=6.9.1         -Dpackaging=jar -Dfile=l2fprod-common-sheet.jar
mvn install:install-file -DgroupId=com.google.code     -DartifactId=jsyntaxpane                -Dversion=0.9.5         -Dpackaging=jar -Dfile=jsyntaxpane-0.9.5-b17.jar
mvn install:install-file -DgroupId=org.netbeans.api    -DartifactId=org-netbeans-swing-outline -Dversion=1.0	       -Dpackaging=jar -Dfile=org-netbeans-swing-outline.jar

# These are for WorldWindJava.  They were taken from the WWJ zip file.  We have no idea of the correct version numbers therefore cannot use from elsewhere
mvn install:install-file -DgroupId=gov.nasa.worldwind  -DartifactId=worldwind                  -Dversion=0.6.874.15796 -Dpackaging=jar -Dfile=worldwindjava-0.6.874.15796.jar
mvn install:install-file -DgroupId=org.tridas          -DartifactId=gdal                       -Dversion=0.1	       -Dpackaging=jar -Dfile=gdal.jar
mvn install:install-file -DgroupId=org.tridas          -DartifactId=glugen                     -Dversion=0.1	       -Dpackaging=jar -Dfile=gluegen-rt.jar
mvn install:install-file -DgroupId=org.tridas          -DartifactId=jogl                       -Dversion=0.1	       -Dpackaging=jar -Dfile=jogl.jar

# Native JOGL libraries wrapper.  Original repository (maven.iscpif.fr - http://maven.iscpif.fr/snapshots) is currently broken
mvn install:install-file -DgroupId=fr.iscpif           -DartifactId=jogl-wrapper               -Dversion=1.0	       -Dpackaging=jar -Dfile=jogl-wrapper-1.0.jar

mvn install:install-file -DgroupId=postgresql          -DartifactId=pljava-public              -Dversion=1.4.2         -Dpackaging=jar -Dfile=pljava.jar