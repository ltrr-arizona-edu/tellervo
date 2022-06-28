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

# Postgres lib
mvn install:install-file -DgroupId=postgresql          -DartifactId=pljava-public              -Dversion=1.4.2         -Dpackaging=jar -Dfile=pljava.jar

# Date picker
#mvn install:install-file -DgroupId=com.michaelbaranov.microba -DartifactId=microba             -Dversion=0.4.4.3       -Dpackaging=jar -Dfile=microba-0.4.4.3.jar

mvn install:install-file  -DgroupId=org.jhlabs          	   -DartifactId=jmapprojlib         -Dversion=1.2.0	        -Dpackaging=jar -Dfile=jmapprojlib-1.2.0.jar

mvn install:install-file -DgroupId=org.jvnet.jaxb2_commons    -DartifactId=xjc-if-ins                -Dversion=0.5.2	     -Dpackaging=jar -Dfile=xjc-if-ins-0.5.2.jar	
mvn install:install-file  -DgroupId=org.tridas.schema    -DartifactId=tridasaandi                -Dversion=1.0	          -Dpackaging=jar -Dfile=tridasaandi-1.0.jar		    
mvn install:install-file  -DgroupId=org.tridas.schema    -DartifactId=tridas-annotations         -Dversion=1.0	          -Dpackaging=jar -Dfile=tridas-annotations-1.0.jar		   
mvn install:install-file  -DgroupId=com.sun.tools.xjc    -DartifactId=collection-setter-injector  -Dversion=0.1	           -Dpackaging=jar -Dfile=collection-setter-injector-0.1.jar		    
mvn install:install-file  -DgroupId=jpedal                -DartifactId=jpedal                     -Dversion=4.45-b-105     -Dpackaging=jar -Dfile=jpedal-4.45-b-105.ja

mvn install:install-file -DgroupId=org.osgeo          -DartifactId=gdal                       -Dversion=0.2	       -Dpackaging=jar -Dfile=gdal.jar

mvn install:install-file -DgroupId=org.rxtx         -DartifactId=rxtx                       -Dversion=2.2-20081207	       -Dpackaging=jar -Dfile=RXTXcomm.jar
