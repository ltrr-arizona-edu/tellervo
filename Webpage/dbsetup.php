<?php

//Set up database connection
$conn_string = "host=localhost port=5432 dbname=dendro user=aps03pwb password=codatanl";
$dbconn = pg_coonnect ($conn_string);

//Date format
$sql = "set datestyle to 'Postgres, European'";
pg_query($dbconn, $sql);

?>