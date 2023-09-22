#!/usr/bin/php
<?php 
if (!isset($argc))
{
	echo "This file should be called from the command line only";
	die();
	
}

// Help text
if ($argc != 2 || in_array($argv[1], array('--help', '-help', '-h', '-?'))) {
	?>
		
This is a command line PHP script for extracting site location data from a Tellervo v1.1 site database file.

  Usage:
  <?php echo $argv[0]; ?> <infile>

  <infile> if the Tellervo sitedb file that you want to process
  
  With the --help, -help, -h,
  or -? options, you can get this help.

<?php
	die();
}

// Put args into nice variables
$filename = $argv[1]; 


// Load site db XML file
$doc = new DOMDocument();
$doc->load($filename);

if($doc!=null)
{
	echo "Loaded $filename successfully\n";
}
else
{
	echo "Loading $filename failed!\n";
	die();
}




$children = $doc->documentElement->childNodes;

foreach($children as $site)
{
	if($site->nodeType != XML_ELEMENT_NODE) continue;
	
	if($site->tagName=="site")
	{
		$sitetags = $site->childNodes;
		$code = NULL;
		$name = NULL;
		$srid=4326;
		$location = new isodate();
					
		foreach($sitetags as $tag)
		{
			if($tag->nodeType != XML_ELEMENT_NODE) continue;
			
			switch($tag->tagName)
			{
				case "code": 		$code = $tag->nodeValue; break;	
				case "name": 		$name = $tag->nodeValue; break;
				case "location": 	
					$location->setDate($tag->nodeValue); 
					break;
			}	
		}
		
		if($code && $location->asISODate()!=NULL)
		{
			
			if($location->getDDLat()!=0 && $location->getDDLong()!=0)
			{
				//echo "Location of $code - $name is: \n Original string = ".$location->asISODate()."\n Latitude  = ".$location->getDDLat()."\n Longitude = ".$location->getDDLong()."\n";
				echo "update tblobject set locationgeometry=setsrid(makepoint(".sprintf("%1.8f",pg_escape_string($dbconn, $location->getDDLong())).", "
				.sprintf("%1.8f",pg_escape_string($dbconn, $location->getDDLat()))."), "
				.pg_escape_string($dbconn, $srid).") where tblobject.code='".$code."';\n";
			}

		}
		else
		{
			//echo "Skip $code\n\n";
		}
		
	}
}

echo "Done!\n";








class isodate
{
	private $date;
	
	function __construct()
	{
	}
	
	function setDate($isodate)
	{
		$this->date = $isodate;	
	}
	
	function getLatitude($part)
	{
		$sign = substr($this->date, 0, 1);
		$degrees = (integer) substr($this->date, 1, 2);
		$minutes = (integer) substr($this->date, 3, 2);
		$seconds = (integer) substr($this->date, 5, 2);
		
		switch($part)
		{
			case "sign": return $sign;
			case "degrees": return $degrees;
			case "minutes": return $minutes;
			case "seconds": return $seconds;
			return null;
		}
	}
	
	function getLongitude($part)
	{
		$sign = substr($this->date, 7, 1);
		$degrees = (integer) substr($this->date, 8, 3);
		$minutes = (integer) substr($this->date, 11, 2);
		$seconds = (integer) substr($this->date, 13, 2);		

		switch($part)
		{
			case "sign": return $sign;
			case "degrees": return $degrees;
			case "minutes": return $minutes;
			case "seconds": return $seconds;
			return null;
		}
	}
	
	function getLatDegrees()
	{
		$sign = $this->getLatitude("sign");
		
		if($sign=="-")
		{
			return 0 - $this->getLatitude("degrees");
		}
		else
		{
			return $this->getLatitude("degrees");
		}
	}
	
	function getLatMinutes()
	{
		return $this->getLatitude("minutes"); 
	}
	
	function getLatSeconds()
	{
		return $this->getLatitude("seconds");
	}
	
	
	function getDDLat()
	{
		$ddlat = $this->getLatDegrees();
		
		if($ddlat<0)
		{
			$ddlat = $ddlat - ($this->getLatMinutes()/60) - (($this->getLatSeconds()/60)/60);
		}
		else
		{
			$ddlat = $ddlat + ($this->getLatMinutes()/60) + (($this->getLatSeconds()/60)/60);
		}
		
		return $ddlat;
	}
	
	function getLongDegrees()
	{
		$sign = $this->getLongitude("sign");
		
		if($sign=="-")
		{
			return 0 - $this->getLongitude("degrees");
		}
		else
		{
			return $this->getLongitude("degrees");
		}
	}
	
	function getLongMinutes()
	{
		return $this->getLongitude("minutes"); 
	}
	
	function getLongSeconds()
	{
		return $this->getLongitude("seconds");
	}
	
	
	function getDDLong()
	{
		$ddlong = $this->getLongDegrees();
		
		if($ddlat<0)
		{
			$ddong = $ddlong - ($this->getLongMinutes()/60) - (($this->getLongSeconds()/60)/60);
		}
		else
		{
			$ddlong = $ddlong + ($this->getLongMinutes()/60) + (($this->getLongSeconds()/60)/60);
		}
		
		return $ddlong;
	}
	
	
	
	function asString()
	{
		return "Lat = ".$this->getDDLat();
	}
	
	function asISODate()
	{
		return $this->date;
	}
	
}


?>