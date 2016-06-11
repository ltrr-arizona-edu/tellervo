UPDATE sqlj.jar_repository SET jarowner='tellervo';
SELECT sqlj.install_jar('file:///usr/share/tellervo-server/tellervo-pljava.jar', 'tellervo_jar', false);
SELECT sqlj.set_classpath('cpgdb', 'tellervo_jar');