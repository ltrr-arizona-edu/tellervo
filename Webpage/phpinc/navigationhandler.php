<? /** This file contains all the logic needed to handle navigation events through a recordset
It is intended that navigation is handled in a consistent generic manner for all forms
so there should be no form specific stuff in this file!

Before including this file, make sure the following vars are set appropriately:
$formTable = "tablename";
$keyField = "primarykeyfieldname";
$extraFilter = a part of a where clause (omit leading and / where) to use as an additional filter
$filterFiledArray = array of fields on which filtering should be carried out

TODO:
GAdd transaction handling!

Tim Sutton 2003*/

//
// Note make sure the dbsetup has been carried out before including this file!
//
//$debugFlag=true;
$baseQuery = "Select * from $formTable";


//Get and set main vars
$currentRecord = $HTTP_POST_VARS['currentRecord'];
//only initialise formAction if it has not been initialised prior to this include! e.g. people.php
if (!$formAction) $formAction = $HTTP_POST_VARS['formAction'];
$callingFormPKey = $HTTP_POST_VARS['callingFormPKey'];

//only allow search and filter settings to register
//if they were set on this form and 
if ($formAction=="formChange" || $formAction=="new")
{
    $useFilterFlag="false";
    $searchString="";
}
else
{
    $searchString = $HTTP_POST_VARS['txtSearchBox'];
}
//Create a record filter
if ($HTTP_POST_VARS["useFilterFlag"]=="true")
{
    if ($debugFlag)
    {
      echo "We are using filters dude!";
    }
    $filter="";
    foreach($filterFieldArray as $value)
    {
        if (!$filter) //first filter gets no 'or' in front
        {
            //ilike is a postgres specific extension that allows case insensitive searches
            $filter = "$value ilike '%$searchString%'";
        }
        else
        {
            //ilike is a postgres specific extension that allows case insensitive searches
            $filter.=" or $value ilike '%$searchString%'";
        }
    }
}
//apply any additional filters as appropriate
if (!$filter && $extraFilter != "")
{
    $filter.= $extraFilter;
}
elseif ($filter && $extraFilter != "")
{
    $filter.=" and ".$extraFilter;

}

if ($debugFlag)
{
    echo "<div class=\"box\">";
    echo "<p><b>Filter Now Set to:</b></p>\n";
    echo "$filter\n</div>\n";
}

//Check for first run of form
if (!$currentRecord && !$formAction)
{
    $formAction = "first";
}
//echo "Filter flag is: $useFilterFlag";

//determine pkey of first record in recordset using filters if necessary
$sql = $baseQuery;
$sql.=($filter)? (" where $filter "): " ";
$sql.="order by $keyField asc limit 1";
$result = pg_query ($dbconn, $sql);
$myRow = pg_fetch_array($result);
$firstRecID = $myRow[$keyField];


//determine pkey of last record in recordset using filters if necessary
$sql = $baseQuery;
$sql.=($filter)? (" where $filter "): " ";
$sql.="order by $keyField desc limit 1";
$result = pg_query ($dbconn, $sql);
$myRow = pg_fetch_array($result);
$lastRecID = $myRow[$keyField];



//if first recid is null or last recid is null, no match was found, set both first and last 
//to zero so no recs show
if (!$firstRecID || !$lastRecID)
{
    $firstRecID=0;
    $lastRecID=0;
}

//global $readonly;
//$readonly=$HTTP_SESSION_VARS["readonly"]; //the session var that tells us whether the user is allowed to change stuff
//$readonly=$myAuth->isReadOnly();
//var_dump( $readonly);
//override insert,update,save and delete requests by read only users
if ($readonly && ($formAction=="save" || $formAction=="insert" || $formAction=="delete" || $formAction=="update"))
{
    ?>
    <div class="box">
    <h1>Access Denied!</h1>
    <p>
    Changing this form is only available to users with edit privaledges!
    You are logged in as a read only user - the request to <?echo $formAction; ?> a record has been denied.
    </p>
    </div>
    <?
    $formAction="absolute";
}
if ($debugFlag)
{
    echo "<div class=\"box\">\n";
    echo "The current user readonly status is : $readonly<br/>";
    echo "The current record is $currentRecord </br>";
    echo "last current record is $currentRecord and form action is $formAction <br/>\n";
    echo "formAction = $formAction </br>\n";
    echo "Recordset first record query was : $sql </br>\n";
    echo "Recordset last record query was : $sql </br>\n";
    echo "First Record ID is $firstRecID, Last Record ID is $lastRecID</br>\n";
    echo "</div>\n";
}


//
// Now start parsing for save / insert / delete actions  - these cases _MAY_ change formaction
//
/* Event handler for inserting a new record */
if ($formAction=="save" && !$currentRecord && !$readonly)
{
    $sql= "insert into $formTable (";
    $fieldListString = "";
    $valueListString = "";
    $addCommaFlag = false;
    foreach ($HTTP_POST_VARS as $key=>$value)
    {
        //check if the post var starts with 'fld', if it does, add it to
        //our insert string
        if (substr($key,0,3)=="fld")
        {
            if ($addCommaFlag)
            {
                $fieldListString.=", ";
                $valueListString.=", ";
            }
            if ($value=="NULL" || $value=="")
            {
                $fieldListString.= substr($key,3);
                $valueListString.="NULL";
            }
            else
            {
                $fieldListString.= substr($key,3);
                $valueListString.="'".$value."'";
                //$valueListString.="'".addslashes($value)."'";
            }
            $addCommaFlag = true;
        }

    }
    $fieldListString.= ") values (";
    $valueListString.= ")";
    $sql.=$fieldListString.$valueListString;
    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    //now perform the actual insert query

    $result = pg_query ($dbconn, $sql);
    //find out the oid of the newly inserted record
    $oid = pg_last_oid($result); 
    //now get the pkey of the inserted record
    $insertedKeySQL ="select ".$keyField." from ".$formTable." where oid = ".$oid;
    @$insertedResult = pg_query($insertedKeySQL); //@ suppresses oid errors 
    if ($insertedResult)
    {
        $insertedRow =  pg_fetch_array($insertedResult);
        $currentRecord = $insertedRow[$keyField];
        //now find out the primary key value of the newly inserted record
        if ($debugFlag)
        {
            echo "<div class=\"box\">";
            echo "<p><b>SQL Generated:</b></p>\n";
            echo "$sql\n";
            echo "<p><b>SQL To Find Insert Key:</b></p>\n";
            echo "$insertedKeySQL\n";
            echo "<p><b>Insert OID :$oid, Insert ID: $currentRecord</b></p>\n</div>\n";
        }
        // now process all the cross pickers (if any exist)
        foreach ($HTTP_POST_VARS as $key=>$value)
        {
            //check if the post var starts with 'fld', if it does, add it to
            //our insert string
            if (substr($key,0,3)=="cp_")
            {
                $postVarValue=$HTTP_POST_VARS[$key];
                ProcessCrossPicker($key,$postVarValue,$keyField,$currentRecord,$dbconn);
            }
        }
    }
    else
    {
        if ($debugFlag)
        {
            echo "<div class=\"box\">";
            echo "<b> Error:</b>\n";
            echo "The record you have just inserted could not be referenced by its oid.";
            echo "if you were inserting into a view , you need to ask the database admin";
            echo "to include the base table oid field in the view.";
            echo "</div>\n";
        }
    }
    //now set the form action
    $formAction = "last";
}
/* Event handler for updating an existing record */
elseif ($formAction=="save" && $currentRecord && !$readonly)
{
    //first populate the table
    $sql= "update $formTable set ";
    $addCommaFlag = false;
    foreach ($HTTP_POST_VARS as $key=>$value)
    {
        //check if the post var starts with 'fld', if it does, add it to
        //our update string
        if (substr($key,0,3)=="fld")
        {
            //echo "$key -> $value\n";
            if ($addCommaFlag) $sql.=", ";
            if ($value=="NULL" || $value=="")
            {
                $sql.= substr($key,3)."=NULL ";
            }
            else
            {
                $sql.= substr($key,3)."='".$value."' ";
                //$sql.= substr($key,3)."='".addslashes($value)."' ";
            }
            $addCommaFlag = true;
        }

    }

    $sql.="where $keyField=".$currentRecord;
    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);

    // now process all the cross pickers (if any exist)
    foreach ($HTTP_POST_VARS as $key=>$value)
    {
        //check if the post var starts with 'fld', if it does, add it to
        //our insert string
        if (substr($key,0,3)=="cp_")
        {
            $postVarValue=$HTTP_POST_VARS[$key];
            ProcessCrossPicker($key,$postVarValue,$keyField,$currentRecord,$dbconn);
        }
    }

    //now do the standard base query
    $result = pg_query ($dbconn, "$baseQuery where $keyField=".$currentRecord." limit 1;");

}
elseif ($formAction=="delete" && !$readonly)
{
    $sql="delete from $formTable where $keyField=".$currentRecord;
    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>SQL Generated for delete:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn,$sql );
    $formAction = "previous";
}

//
// Standard Nav - these cases may _NOT_ change formaction
//

if ($formAction=="next")
{
    //build the query up, incorporating any filters that have been set
    $sql="$baseQuery where $keyField>".$currentRecord;
    $sql.= ($filter) ? (" and ($filter)") : "";
    $sql.= " order by $keyField asc limit 1;";
    if ($debugflag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>Form next query SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);
}
elseif ($formAction=="last")
{
    //build the query up, incorporating any filters that have been set
    $sql=$baseQuery;
    $sql.= ($filter) ? (" where $filter") : "";
    $sql.= " order by $keyField desc limit 1;";

    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>Form last query SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);

    $eofFlag=true;
}
elseif ($formAction=="previous")
{
    //build the query up, incorporating any filters that have been set
    $sql="$baseQuery where $keyField<".$currentRecord;
    $sql.= ($filter) ? (" and ($filter)") : "";
    $sql.= " order by $keyField desc limit 1;";

    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>Form last query SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);
}
elseif ($formAction=="first")
{
    //build the query up, incorporating any filters that have been set
    $sql=$baseQuery;
    $sql.= ($filter) ? (" where $filter") : "";
    $sql.= " order by $keyField asc limit 1;";

    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>Form first query SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);
    $bofFlag=true;
}
elseif ($formAction=="absolute") //move to a specific rec
{
    //build the query up, incorporating any filters that have been set
    $sql="$baseQuery where $keyField=".$currentRecord;
    $sql.= ($filter) ? (" and ($filter)") : "";
    $sql.= " order by $keyField desc limit 1;";

    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>Form Action : $formAction </b></p>\n";
        echo "<p><b>Form last query SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);
}
elseif ($formAction == 'formChange')
{
    //
    // Need to make these lines generic and use a cookie!
    //
    $callingForm = $HTTP_POST_VARS['callingForm'];
    $callingFormPKey = $currentRecord;
    $currentRecord = $HTTP_POST_VARS[$keyField];
    //build the query up, incorporating any filters that have been set
    $sql = "";
    if ($currentRecord)
    {
      $sql="$baseQuery where $keyField=".$currentRecord;
      $sql.= ($filter) ? (" and ($filter)") : "";
    }
    else //fallback position for if the calling form didnt have the pkey on its form
    {
      $sql="$baseQuery" ;
      $sql.= ($filter) ? (" where  $filter") : "";
    }
    $sql.= " order by $keyField asc limit 1;";
    if ($debugFlag)
    {
        echo "<div class=\"box\">";
        echo "<p><b>Form change query SQL Generated:</b></p>\n";
        echo "$sql\n</div>\n";
    }
    $result = pg_query ($dbconn, $sql);

}


//
// Need to make the above couple of lines generic and use a cookie!
//

// work out where we are in the recset - this must be done last
if ($formAction != "insert")
{
    $numrows = pg_num_rows($result);
    $row = pg_fetch_array($result);
    $currentRecord = $row[$keyField];
    //set end of recordset and beginning of recordset flags
    if ($currentRecord==$firstRecID)
    {
        $bofFlag=true;
    }
    else
    {
        $bofFlag=false;
    }

    if ($currentRecord==$lastRecID)
    {
        $eofFlag=true;
    }
    else
    {
        $eofFlag=false;
    }
}
else //formAction == 'insert'
{
    $eofFlag=true;
    $bofFlag=false;
    $currentRecord='';
}
if ($debugFlag)
{
    echo "\n<div class=\"box\">\n";
    echo "<p><b>Post Vars:</b></p>\n";
    foreach ($HTTP_POST_VARS as $key=>$value)
    {
        echo "$key ----> $value<br>\n";
    }
    echo "\n</div>\n";
}


function ProcessCrossPicker($postVarName,$postVarValue,$keyField,$currentRecord, $dbconn)
{
    $joinTable = substr($postVarName,3);
    global $debugFlag;
    if ($debugFlag)
    {
        echo "\n<div class=\"box\">\n";
        echo "<p><b>ProcessCrosspicker $postVarName,$postVarValue,$keyField,$currentRecord, $dbconn, $joinTable:</b></p>\n";
        echo "<p>".var_dump($postVarValue)."</p>";
    }
    //
    //now populate the many2many join table for roles
    //
    //first clear any related role data for this person
    $sql = "delete from $joinTable where $keyField=".$currentRecord;
    if ($debugFlag)
    {
        echo "\n<div class=\"box\">\n";
        echo "<p><b>Crosspicker deleting old values:</b></p>\n";
        echo "<p>$sql</p>";
        echo "\n</div>\n";
    }
    pg_query($dbconn,$sql);

    //get the selList string into an array
    $myArray = explode(",",$postVarValue);


    //now insert a record for each value in the selList Array
    if ($postVarValue)
    {
        foreach ($myArray as $value)
        {
            $sql="INSERT INTO $joinTable VALUES ($currentRecord,$value); ";
            //now commit the updated list to the database
            if ($debugFlag)
            {
                echo "\n<div class=\"box\">\n";
                echo "<p><b>Crosspicker query processing:</b></p>\n";
                echo "<p>$sql</p>";
                echo "\n</div>\n";
            }
            pg_query($dbconn,$sql);
        }
    }
    if ($debugFlag)
    {
        echo "\n</div>\n";
    }
    //get the current rec

}


?>
