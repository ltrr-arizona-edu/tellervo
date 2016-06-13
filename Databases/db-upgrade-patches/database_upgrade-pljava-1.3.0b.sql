DELETE FROM sqlj.jar_repository where jarname='tellervo_jar';
SELECT sqlj.install_jar('file:///usr/share/tellervo-server/tellervo-pljava.jar', 'tellervo_jar', false);
SELECT sqlj.set_classpath('cpgdb', 'tellervo_jar');
UPDATE sqlj.jar_repository SET jarowner='tellervo';

UPDATE tblconfig SET value = '1.3.0' WHERE key = 'wsversion';