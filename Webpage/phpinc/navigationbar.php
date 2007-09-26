<?

?>
<!--<div id="navbar">-->
<a href="#">Navigate Records: </a>
<?


  if (!$bofFlag)
  {
?>
      <a href="#" accesskey="f" id="first" class="recordnav" name="first" onclick="document.getElementById('formAction').value='first';document.getElementById('myForm').submit();">&lt;&lt;</a>
      <a href="#" accesskey="p" id="previous" class="recordnav" name="previous" onclick="document.getElementById('formAction').value='previous';document.getElementById('myForm').submit();">&lt;</a>
<?
  }
  else
  {
   ?> 
      <a href="#" id="first" class="recordnav">&lt;&lt;</a> 
      <a href="#" id="previous" class="recordnav">&lt;</a>
   <?
  }
  if (!$eofFlag)
  {
?>
      <a href="#" accesskey="n" id="next" class="recordnav" name="next" onclick="document.getElementById('formAction').value='next';document.getElementById('myForm').submit();">&gt;</a>
      <a href="#" accesskey="l" id="last" class="recordnav" name="last" onclick="document.getElementById('formAction').value='last';document.getElementById('myForm').submit();">&gt;&gt;</a>
  <?
  }
  else
  {
   ?> 
   <a href="#" id="next" class="recordnav">&gt;</a> 
   <a href="#" id="last" class="recordnav">&gt;&gt;</a>
   <?
  }
?>
      <a href="#" accesskey="s" id="save" class="recordnav" name="save" onclick="document.getElementById('formAction').value='save';document.getElementById('myForm').submit();">Save</a>
<? 
  $insertsDisabled=false; // place holder so we can parameterise this in future
  if ($insertsDisabled)
  {
    //insert button disabled
  }
  else
  {
    //insert button available
    ?>
      <a href="#" accesskey="i" id="insert" class="recordnav" name="insert" onclick="document.getElementById('formAction').value='insert';document.getElementById('myForm').submit();">Insert</a>
    <?
  }
  if ($HTTP_POST_VARS["formAction"]=="new")
  {
    //delete button disabled
  }
  else
  {
    //delete button enabled
?>
  <a href="#" accesskey="d" id="delete" class="recordnav" name="delete" onclick="document.getElementById('formAction').value='delete';document.getElementById('myForm').submit();">Delete</a>
<?
  }
    if ($HTTP_POST_VARS["formAction"]=="insert" || $HTTP_POST_VARS["formAction"]=="new")
    {
      //echo "    New Record";
    }
    else
    {
      //echo "<a href=\"#\">".calculateStats($formTable, $filter, $keyField, $currentRecord)."</a>";
      //echo "\n";
    }
  
?>
      <!--<input type="text" id="txtSearchBox" name="txtSearchBox" value="<? //echo $searchString ?>"/>-->


    <input type="hidden" id="useFilterFlag" name="useFilterFlag" value = "<? echo $useFilterFlag;?>"/>
    <input type="hidden" value="" id="formAction" name="formAction"/>
    <input type="hidden" value="<?echo $currentRecord; ?>" id="currentRecord" name="currentRecord"/>

    <?
      if ($searchString > "")
      {
	?>
	<a href="#"  class="recordnav" id="cmdUseFilter" onclick="useFilterFlag.value=false; txtSearchBox.value =''; formAction.value = 'first'; document.myForm.submit();">Clear</a>
        
	<?
      }
      else
      {
	?>
        
	<a href="#"  class="recordnav" id="cmdUseFilter" onclick="useFilterFlag.value=true; formAction.value = 'first'; document.myForm.submit();">Find</a>
        
	<?
      }
    
?>


    <!--End of nav bar-->    


<?

////// 
////// Some additional helper functions:
//////


function calculateStats ($baseTable, $filter, $pkeyField, $pkeyVal)
{
  global $debugFlag, $dbconn;
  //caculate basic stats in the form of 
  //     Rec 10 of 100 (filtered)
  // or
  //     Rec 2 of 150 
  // This is not the most cpu efficient way to do this - but it will ensure the 
  // counts are always up to date (which can be guaranteed when in a multi-user
  // app using offset calculations). Besides the database is pretty small so I dont expect 
  // too much cpu overhead.
  
  $totalCountSQL = "";
  $posCountSQL = "";

  if ($filter) 
  {
    $totalCountSQL = "select count($pkeyField) from $baseTable where $filter;";
    $posCountSQL = "select count($pkeyField) from $baseTable where ($filter) and ($pkeyField <= '$pkeyVal');";
  }
  else
  {
    $totalCountSQL = "select count($pkeyField) from $baseTable;";
    $posCountSQL = "select count($pkeyField) from $baseTable where $pkeyField <= '$pkeyVal';";

  }
        if ($debugFlag)
        {
         echo "\n<div class=\"box\">\n";
           echo "<p><b>totalCountSQL: </b> $totalCountSQL</p>\n";
           echo "<p><b>posCountSQL: </b> $posCountSQL</p>\n";
  }
  
  $result = @pg_query($dbconn,$totalCountSQL);
  $numrows = @pg_num_rows($result); //should be only 1
  //assume 0 returns
  $totalRecs=0;
  if ($numrows==0)
  {
    //an error occurred with the query - leave record count to 0
  }
  else
  {
    $row = @pg_fetch_array($result);
    $totalRecs = $row[0];
  }
  
  $result = @pg_query($dbconn,$posCountSQL);
  $numrows = @pg_num_rows($result); //should be only 1
  //assume 0 unless query worked
  $currentRecNo=0;
  if ($numrows==0)
  {
          //an error occurred leave current rec at 0
  }
  else
  {
    $row = @pg_fetch_array($result);
    $currentRecNo = $row[0];
  }
  if ($debugFlag)
  {
     echo "<p><b>totalCount result: </b> $totalRecs</p>\n";
     echo "<p><b>posCount result: </b> $currentRecNo</p>\n";
     echo "</div>\n";
  }
    
  if ($filter) 
  {
    return "Record $currentRecNo of $totalRecs (filtered)";
  }
  else
  {
      return "<span id=\"recordPos\">Record $currentRecNo of $totalRecs</span>";
  }

}
?>
