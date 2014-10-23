# The Jars within this folder are those not found in standard Maven repositories.  
# The following commands will install them to the TRiDaS Maven repository so that 
# they are available to others without hassles.  Alternatively you can install 
# them localling using the MavenInstallCommands.sh script

mvn deploy:deploy-file -DgroupId=com.l2prod.common   -DartifactId=l2fprod-common-sheet       -Dversion=6.9.1         -Dpackaging=jar -Dfile=l2fprod-common-sheet.jar        -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file -DgroupId=com.google.code     -DartifactId=jsyntaxpane                -Dversion=0.9.5         -Dpackaging=jar -Dfile=jsyntaxpane-0.9.5-b17.jar       -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file -DgroupId=org.netbeans.api    -DartifactId=org-netbeans-swing-outline -Dversion=1.0	         -Dpackaging=jar -Dfile=org-netbeans-swing-outline.jar  -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases

# These are for WorldWindJava.  They were taken from the WWJ zip file.  We have no idea of the correct version numbers therefore cannot use from elsewhere
mvn deploy:deploy-file -DgroupId=gov.nasa.worldwind  -DartifactId=worldwind                  -Dversion=0.6.874.15796 -Dpackaging=jar -Dfile=worldwindjava-0.6.874.15796.jar -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file -DgroupId=org.osgeo           -DartifactId=gdal                       -Dversion=0.1	         -Dpackaging=jar -Dfile=gdal.jar                        -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file -DgroupId=org.processing      -DartifactId=gluegen-rt                  -Dversion=1.5.r0196	 -Dpackaging=jar -Dfile=gluegen-rt-1.5.r0196.jar        -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file -DgroupId=org.processing      -DartifactId=jogl                       -Dversion=1.5.r0196     -Dpackaging=jar -Dfile=jogl-1.5.r0196.jar              -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases

# Native JOGL libraries wrapper.  Original repository (maven.iscpif.fr - http://maven.iscpif.fr/snapshots) is currently broken
#mvn deploy:deploy-file -DgroupId=fr.iscpif           -DartifactId=jogl-wrapper               -Dversion=1.0	       -Dpackaging=jar -Dfile=jogl-wrapper-1.0.jar              -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases

# Postgres lib
mvn deploy:deploy-file  -DgroupId=postgresql          -DartifactId=pljava-public              -Dversion=1.4.2         -Dpackaging=jar -Dfile=pljava.jar                     -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases

# Date picker
#mvn deploy:deploy-file -DgroupId=com.michaelbaranov.microba -DartifactId=microba            -Dversion=0.4.4.3       -Dpackaging=jar -Dfile=microba-0.4.4.3.jar             -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases

mvn deploy:deploy-file  -DgroupId=com.jhlabs         -DartifactId=jmapprojlib                -Dversion=1.2.0	     -Dpackaging=jar -Dfile=jmapprojlib-1.2.0.jar		    -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases

mvn deploy:deploy-file  -DgroupId=org.jvnet.jaxb2_commons    -DartifactId=xjc-if-ins                -Dversion=0.5.2	     -Dpackaging=jar -Dfile=xjc-if-ins-0.5.2.jar		    -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file  -DgroupId=org.tridas.schema    -DartifactId=tridasaandi                -Dversion=1.0	     -Dpackaging=jar -Dfile=tridasaandi-1.0.jar		    -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file  -DgroupId=org.tridas.schema    -DartifactId=tridas-annotations                -Dversion=1.0	     -Dpackaging=jar -Dfile=tridas-annotations-1.0.jar		    -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases
mvn deploy:deploy-file  -DgroupId=com.sun.tools.xjc    -DartifactId=collection-setter-injector                -Dversion=0.1	     -Dpackaging=jar -Dfile=collection-setter-injector-0.1.jar		    -DrepositoryId=tridas-releases  -Durl=http://maven.tridas.org/repository/tridas-releases