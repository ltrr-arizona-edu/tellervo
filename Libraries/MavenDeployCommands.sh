# The Jars within this folder are those not found in standard Maven repositories.  
# The following commands will install them to the TRiDaS Maven repository so that 
# they are available to others without hassles.  Alternatively you can install 
# them localling using the MavenInstallCommands.sh script

mvn deploy:deploy-file -DgroupId=com.l2prod.common   -DartifactId=l2fprod-common-sheet       -Dversion=6.9.1         -Dpackaging=jar -Dfile=l2fprod-common-sheet.jar        -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
mvn deploy:deploy-file -DgroupId=com.google.code     -DartifactId=jsyntaxpane                -Dversion=0.9.5         -Dpackaging=jar -Dfile=jsyntaxpane-0.9.5-b17.jar       -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
mvn deploy:deploy-file -DgroupId=org.netbeans.api    -DartifactId=org-netbeans-swing-outline -Dversion=1.0	         -Dpackaging=jar -Dfile=org-netbeans-swing-outline.jar  -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases

# Postgres lib
mvn deploy:deploy-file  -DgroupId=postgresql          -DartifactId=pljava-public              -Dversion=1.4.2         -Dpackaging=jar -Dfile=pljava.jar                     -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases

# Date picker
#mvn deploy:deploy-file -DgroupId=com.michaelbaranov.microba -DartifactId=microba            -Dversion=0.4.4.3       -Dpackaging=jar -Dfile=microba-0.4.4.3.jar             -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases

mvn deploy:deploy-file  -DgroupId=com.jhlabs         -DartifactId=jmapprojlib                -Dversion=1.2.0	     -Dpackaging=jar -Dfile=jmapprojlib-1.2.0.jar		    -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases

mvn deploy:deploy-file  -DgroupId=org.jvnet.jaxb2_commons    -DartifactId=xjc-if-ins                -Dversion=0.5.2	     -Dpackaging=jar -Dfile=xjc-if-ins-0.5.2.jar		    -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
mvn deploy:deploy-file  -DgroupId=org.tridas.schema    -DartifactId=tridasaandi                -Dversion=1.0	     -Dpackaging=jar -Dfile=tridasaandi-1.0.jar		    -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
mvn deploy:deploy-file  -DgroupId=org.tridas.schema    -DartifactId=tridas-annotations                -Dversion=1.0	     -Dpackaging=jar -Dfile=tridas-annotations-1.0.jar		    -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
mvn deploy:deploy-file  -DgroupId=com.sun.tools.xjc    -DartifactId=collection-setter-injector                -Dversion=0.1	     -Dpackaging=jar -Dfile=collection-setter-injector-0.1.jar		    -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
mvn deploy:deploy-file  -DgroupId=jpedal    -DartifactId=jpedal                -Dversion=4.45-b-105     -Dpackaging=jar -Dfile=jpedal-4.45-b-105.jar		    -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases


## WorldWindJava 2
## WWJ package in Maven central is fubar so this is a manual build with the correct dependencies specified.  This should be able to be 
## replaced with an official build some time in the future
mvn deploy:deploy-file -Durl=https://repo.ltrr.arizona.edu/ltrr-releases  -DrepositoryId=ltrr-releases  -Dfile=worldwindjava-tellervo-2.0.0.jar     -DpomFile=../worldwindjava-pom.xml
mvn deploy:deploy-file  -DgroupId=org.osgeo          -DartifactId=gdal              -Dversion=0.2         -Dpackaging=jar -Dfile=gdal.jar                     -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases

mvn deploy:deploy-file -DgroupId=org.rxtx         -DartifactId=rxtx                       -Dversion=2.2-20081207	       -Dpackaging=jar -Dfile=RXTXcomm.jar -DrepositoryId=ltrr-releases  -Durl=https://repo.ltrr.arizona.edu/ltrr-releases
