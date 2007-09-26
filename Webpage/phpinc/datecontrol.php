<?
/** This function implements a simple validating data control */
//controlname will have fld prepended to it! Dont pass it in with fld already there!
function dateControl($controlName, $defaultValue, $required, $minYear, $maxYear)
{
	global $day;
	global $month;
	global $year;
    global $debugMode;

	if ($defaultValue)
	{
		$day=substr($defaultValue,0,2);
		$month=substr($defaultValue,3,2);
		$year=substr($defaultValue,6,4);
        //echo "$day/$month/$year";
	}
        else if ($defaultValue==Null)
	{
		$day="";
		$month="";
		$year="";
	}
    	else
    	{
		$day=date(d);
        	$month=date(m);
        	$year=date(Y);
    	}

	?>
	<? 
	echo "<select name=\"".$controlName."_day\" ";
	echo "onchange=\"if ( isYear(".$controlName."_year.value, $required,$minYear,$maxYear) && ".$controlName."_month.value!='' && this.value != '')\n ";
	echo "{\n";
	//echo " alert ('Changing hidden date!');";
	echo "    fld".$controlName.".value=".$controlName."_day.value + '-' + ".$controlName."_month.value + '-' + ".$controlName."_year.value;";
	echo "}\n";
	echo "else\n";
	echo "{\n";
	//echo " alert ('NOT Changing hidden date!');";
	echo "    fld".$controlName.".value='';\n";
	echo "}\"\n";
	echo ">\n"; 
	?>
	<option>--</option>
	<option value="1" <? echo ($day==1)?"selected":""; ?>>01</option>
	<option value="2" <? echo ($day==2)?"selected":""; ?>>02</option>
	<option value="3" <? echo ($day==3)?"selected":""; ?>>03</option>
	<option value="4" <? echo ($day==4)?"selected":""; ?>>04</option>
	<option value="5" <? echo ($day==5)?"selected":""; ?>>05</option>
	<option value="6" <? echo ($day==6)?"selected":""; ?>>06</option>
	<option value="7" <? echo ($day==7)?"selected":""; ?>>07</option>
	<option value="8" <? echo ($day==8)?"selected":""; ?>>08</option>
	<option value="9" <? echo ($day==9)?"selected":""; ?>>09</option>
	<option value="10" <? echo ($day==10)?"selected":""; ?>>10</option>
	<option value="11" <? echo ($day==11)?"selected":""; ?>>11</option>
	<option value="12" <? echo ($day==12)?"selected":""; ?>>12</option>
	<option value="13" <? echo ($day==13)?"selected":""; ?>>13</option>
	<option value="14" <? echo ($day==14)?"selected":""; ?>>14</option>
	<option value="15" <? echo ($day==15)?"selected":""; ?>>15</option>
	<option value="16" <? echo ($day==16)?"selected":""; ?>>16</option>
	<option value="17" <? echo ($day==17)?"selected":""; ?>>17</option>
	<option value="18" <? echo ($day==18)?"selected":""; ?>>18</option>
	<option value="19" <? echo ($day==19)?"selected":""; ?>>19</option>
	<option value="20" <? echo ($day==20)?"selected":""; ?>>20</option>
	<option value="21" <? echo ($day==21)?"selected":""; ?>>21</option>
	<option value="22" <? echo ($day==22)?"selected":""; ?>>22</option>
	<option value="23" <? echo ($day==23)?"selected":""; ?>>23</option>
	<option value="24" <? echo ($day==24)?"selected":""; ?>>24</option>
	<option value="25" <? echo ($day==25)?"selected":""; ?>>25</option>
	<option value="26" <? echo ($day==26)?"selected":""; ?>>26</option>
	<option value="27" <? echo ($day==27)?"selected":""; ?>>27</option>
	<option value="28" <? echo ($day==28)?"selected":""; ?>>28</option>
	<option value="29" <? echo ($day==29)?"selected":""; ?>>29</option>
	<option value="30" <? echo ($day==30)?"selected":""; ?>>30</option>
	<option value="31" <? echo ($day==31)?"selected":""; ?>>31</option>
	</select>
	<? 
	echo "<select name=\"".$controlName."_month\" ";
	echo "onchange=\"if ( isYear(".$controlName."_year.value, $required,$minYear,$maxYear) && ".$controlName."_day.value!='' && this.value != '')\n ";
	echo "{\n";
	//echo " alert ('Changing hidden date!');";
	echo "    fld".$controlName.".value=".$controlName."_day.value + '-' + ".$controlName."_month.value + '-' + ".$controlName."_year.value;";
	echo "}\n";
	echo "else\n";
	echo "{\n";
	//echo " alert ('NOT Changing hidden date!');";
	echo "    fld".$controlName.".value='';\n";
	echo "}\"\n";
	echo ">\n"; 
	?>
	<option>---</option>
	<option value="1"  <? echo ($month==1) ? "selected":""; ?>>Jan</option>
	<option value="2" <? echo  ($month==2) ? "selected":""; ?>>Feb</option>
	<option value="3" <? echo  ($month==3) ? "selected":""; ?>>Mar</option>
	<option value="4" <? echo  ($month==4) ? "selected":""; ?>>Apr</option>
	<option value="5" <? echo  ($month==5) ? "selected":""; ?>>May</option>
	<option value="6" <? echo  ($month==6) ? "selected":""; ?>>Jun</option>
	<option value="7" <? echo  ($month==7) ? "selected":""; ?>>Jul</option>
	<option value="8" <? echo  ($month==8) ? "selected":""; ?>>Aug</option>
	<option value="9" <? echo  ($month==9) ? "selected":""; ?>>Sep</option>
	<option value="10" <? echo ($month==10)? "selected":""; ?>>Oct</option>
	<option value="11" <? echo ($month==11)? "selected":""; ?>>Nov</option>
	<option value="12" <? echo ($month==12)? "selected":""; ?>>Dec</option>
	</select>
	<? echo "<input type=\"text\" size=\"4\" name=\"".$controlName."_year\"\n value=\"$year\" ";
	echo "onchange=\"if (isYear(this.value, $required,$minYear,$maxYear) && ".$controlName."_day != '' && ".$controlName."_month != '')\n ";
	echo "{\n";
	echo "fld".$controlName.".value=".$controlName."_day.value + '-' + ".$controlName."_month.value + '-' + ".$controlName."_year.value;\n";
	echo "}\n";
	echo "else\n";
	echo "{\n";
	//echo " alert ('NOT Changing hidden date!');";
	echo "    fld".$controlName.".value='';\n";
	echo "}\"\n";
	echo "\/>\n";
    	if ($debugMode)
    	{
		echo "<input type=\"button\" onclick=\"alert (fld".$controlName.".value);\"/>\n";
	}
	
	echo "<input type=\"hidden\" name=\"fld".$controlName."\" id=\"fld".$controlName."\" value=\"".$defaultValue."\" />\n";
        

//	echo "<input type=\"button\" onclick=\"document.myForm.".$controlName."_day.value='".date("j")."'; document.myForm.".$controlName."_month.value='".date("n")."'; document.myForm.".$controlName."_year.value='".date("Y")."';\" value=\"today\">";
}

?>

