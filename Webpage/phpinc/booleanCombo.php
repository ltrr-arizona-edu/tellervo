<?
/** Function to construct a boolean combo box dynamically */
function booleanCombo($boundFieldName, $boundFieldValue, $indent="      ")
{
  $myString = $indent."<select id=\"$boundFieldName\" name=\"$boundFieldName\" class=\"booleanCombo\">\n";
  $myString.=$indent."<option value=\"NULL\"";
  $myString.=($boundFieldValue=="NULL")?" selected=\"selected\"":"";
  $myString.=">[ Select ]</option>\n";
  $myString.=$indent."<option value=\"t\"";
  $myString.=($boundFieldValue=="t")?" selected=\"selected\"":"";
  $myString.=">Yes</option>\n";
  $myString.=$indent."<option value=\"n\"";
  $myString.=($boundFieldValue=="f")?" selected=\"selected\"":"";
  $myString.=">No</option>\n";
  $myString.=$indent."</select>\n";
  return $myString;
}
?>
