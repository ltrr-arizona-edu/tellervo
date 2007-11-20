<?php
include_once('specimen.php');

    function listDir($start_dir) 
    {

        $files = array();
        if (is_dir($start_dir)) 
        {
            $fh = opendir($start_dir);
            while (($file = readdir($fh)) !== false) 
            {
                # loop through the files, skipping . and .., and recursing if necessary
                if (strcmp($file, '.')==0 || strcmp($file, '..')==0) continue;
                $filepath = $start_dir . '/' . $file;
                if ( is_dir($filepath) )
                $files = array_merge($files, listDir($filepath));
                else
                array_push($files, $filepath);
            }
            closedir($fh);
        } 
        else 
        {
            # false if the function was called with an invalid non-directory argument
            $files = false;
        }
                    
        return $files;
                   
    }

$fileList = listDir("/tmp/files/");

        //Set up database connection
        $conn_string = "host=localhost port=5432 dbname=dendro user=aps03pwb password=codatanl";
        $dbconn = pg_connect ($conn_string);

        // Empty table
        $sql = "delete from tblimport";
        pg_query($dbconn, $sql);
        $sql = "delete from tblfailures";
        pg_query($dbconn, $sql);


$i = 1;
set_time_limit(0);
foreach ($fileList as $value)
{
    $mySpecimen = new dendroSpecimen();
    $mySpecimen->readFile($value);    
    
    if(!$mySpecimen->theUnsuitableFlag)
    {
        $mySpecimen->writeToDB();
    }
    $i++;
    //if($i > 200) break;
}
?>
