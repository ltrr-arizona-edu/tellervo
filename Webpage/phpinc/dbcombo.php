<?


function dbCombo($dbConn, $sourceTable, $indexField, $displayField, $boundFieldName, $boundFieldValue)
{

    global $debugFlag;
        $myString ="";
        //execute SQL statement
        $mySql = "Select $indexField, $displayField from $sourceTable order by $displayField;";
        if ($debugFlag) 
        {
            echo $mySql;
        }
        $result = pg_query ($dbConn, $mySql);

        // check for errors
        if ($result=="Fail") { return "SQLList query error!
            ".pg_error(); }
        else
        {
            //create the combo control
            $myString="\n<!-- dbCombo Begins -->\n";
            $myString.="<select name=\"fld" .$boundFieldName."\"  size=1 class=\"dbcombo\">\n";
            // grab results
			$myString.="<option value=\"NULL\">[----- Select -----]</option>\n";
            while ($myRow = pg_fetch_array($result))
            {
                $cboValue = $myRow[$indexField];
                $cboDisplay = $myRow[$displayField];
		$cboDisplay = substr($cboDisplay, 0, 45);
                if ($cboValue==$boundFieldValue)
                {
                    $myString.="<option value=\"$cboValue\" selected=\"selected\">".$cboDisplay."</option>\n";
                }
                else
                {
                    $myString.="<option value=\"$cboValue\">".$cboDisplay."</option>\n";
                }

             }
            //end the combo setup
            $myString.="</select>\n";
            $myString.="<!-- dbCombo End  -->\n\n";
            return $myString;
        }

}


?>
