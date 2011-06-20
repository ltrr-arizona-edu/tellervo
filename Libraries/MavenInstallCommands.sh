# The Jars within this folder are those not found in standard Maven repositories.  They have been
# placed in the org.tridas.maven repository but are included here in case this repository becomes
# unavailable.  The jars can be installed with the following commands:

mvn install:install-file -DgroupId=com.l2prod.common   -DartifactId=l2fprod-common-sheet       -Dversion=6.9.1    -Dpackaging=jar -Dfile=l2fprod-common-sheet.jar
mvn install:install-file -DgroupId=com.google.code     -DartifactId=jsyntaxpane                -Dversion=0.9.5    -Dpackaging=jar -Dfile=jsyntaxpane-0.9.5-b17.jar
mvn install:install-file -DgroupId=gov.nasa.worldwind  -DartifactId=worldwind                  -Dversion=0.6.851  -Dpackaging=jar -Dfile=worldwind-0.6.570.13645.jar
mvn install:install-file -DgroupId=org.netbeans.api    -DartifactId=org-netbeans-swing-outline -Dversion=1.0	  -Dpackaging=jar -Dfile=org-netbeans-swing-outline.jar

