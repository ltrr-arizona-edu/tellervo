select sqlj.replace_jar('file:///tmp/cpgdb.jar', 'cpgdb_jar', false);
select sqlj.replace_jar('file:///tmp/corina-indexing.jar', 'indexing_jar', false);
select sqlj.set_classpath('cpgdb', 'cpgdb_jar:indexing_jar');
