<?php

//Set up database connection
$conn_string = "host=dendro.cornell.edu port=5432 dbname=corina_test user=webuser password=treesarecool";
$dbconn = pg_connect ($conn_string);

//Date format
$sql = "set datestyle to 'Postgres, European'";
pg_query($dbconn, $sql);

?>
