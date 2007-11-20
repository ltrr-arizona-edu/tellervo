<?php

class dendroSpecimen
{
    var $theFileName = NULL;
    var $theFileStyle = NULL;
    var $theMIME = NULL;
    var $theSiteCode = NULL;
    var $theFileExtension = NULL;
    var $theSampleNumber = NULL;
    var $theRadius = NULL;
    var $theReading = NULL;
    
    var $theUnsuitableFlag = FALSE;
    var $theElements = NULL;
    var $theTaxonString = NULL;
    var $reconciledFlag = FALSE;
    var $theDescription = NULL;
    var $theComments = NULL;
    var $theDating = NULL;
    var $theUnmeasuredBegin = NULL;
    var $theUnmeasuredEnd = NULL;
    var $theSampleType = NULL;
    var $theFormat = NULL;
    var $theIndexMethod = NULL;
    var $theSapwoodCount = NULL;
    var $thePithPresence = NULL;
    var $theTerminalRing = NULL;
    var $theContinuousStyle = NULL;
    var $theQuality = NULL;
    var $theAuthor = NULL;
    var $theLastModifiedDate = NULL;


    function dendroSpecimen()
    {
    
    }



    function readFile($fileName)
    {
        // Set meta data
        $this->theFileName=$fileName;
       
        // Check the file is readable
        if(!(is_readable($this->theFileName)))
        {
            $this->theUnsuitableFlag = TRUE;
            $this->logUnusable("Not readable");
        }
        else
        {
            // File is readable so continue

            // Check the file is a text file (this is Linux specific code)
            $command = "file -i -b \"".$this->theFileName."\"";
            exec($command, $result);
            $this->theMIME=$result[0];
            if(substr($this->theMIME, 0, 11)!="text/plain;")
            {
                $this->theUnsuitableFlag = TRUE;
                $this->logUnusable("Not a text file");
            }
            else
            {
                // File is a text file so continue 
                $this->setFileNameConvention();
                $this->extractDataFromFileName();
                $this->setElements();
                
                $this->theTaxonString  = $this->getStringValueFromKey("SPECIES");
                $this->theReconciledFlag  = $this->getBoolValueFromKey("RECONCILED");
                $this->theDescription  = $this->getStringValueFromKey("NAME");
                $this->theComments  = $this->getStringValueFromKey("COMMENTS").$this->getStringValueFromKey['COMMENTS2'];
                $this->theDating  = $this->getStringValueFromKey("DATING");
                $this->theUnmeasuredBegin  = $this->getStringValueFromKey("UNMEAS_PRE");
                $this->theUnmeasuredEnd  = $this->getStringValueFromKey("UNMEAS_POST");
                $this->theSampleType  = $this->getStringValueFromKey("TYPE");
                $this->theFormat  = $this->getStringValueFromKey("FORMAT");
                $this->theIndexMethod  = $this->getStringValueFromKey("INDEX_TYPE");
                $this->theSapwoodCount  = $this->getStringValueFromKey("SAPWOOD");
                $this->thePithPresence  = $this->getBoolValueFromKey("PITH");
                $this->theTerminalRing  = $this->getStringValueFromKey("TERMINAL");
                $this->theContinuousStyle  = $this->getStringValueFromKey("CONTINUOUS");
                $this->theQuality  = $this->getStringValueFromKey("QUALITY");
                $this->theAuthor  = $this->getStringValueFromKey("~", "");
                $this->theLastModifiedDate = date(DATE_ATOM, filemtime($this->theFileName));
            }
        }
    }


   function getBoolValueFromKey($key, $delimiter=";")
   {
        $lines = file($this->theFileName);

        $keyLength = strlen($key)+2;

        $regex = "/".$delimiter.$key." [0-9a-zA-Z:\\, .:]*[\n\t\r;]/"; 
        foreach ($lines as $line_num => $line) 
        {
            if (preg_match($regex, $line, $matches))
            {
                if((substr($matches[0], $keyLength, -1)=="Y")||(substr($matches[0], $keyLength, -1)=="y"))
                {
                    $result=TRUE;
                }  
                elseif((substr($matches[0], $keyLength, -1)=="N")||(substr($matches[0], $keyLength, -1)=="n"))
                {
                    $result=FALSE;
                }
            }
        }

        if(empty($result))
        {
            return NULL;
        }
        else
        {
            return $result;
        }

   }

   function getStringValueFromKey($key, $delimiter=";")
   {
        $lines = file($this->theFileName);

        $keyLength = strlen($key)+strlen($delimiter)+1;

        $regex = "/".$delimiter.$key." [0-9a-zA-Z:\\, .:]*[\n\t\r;]/"; 
        foreach ($lines as $line_num => $line) 
        {
            if (preg_match($regex, $line, $matches))
            {
                $result = substr($matches[0], $keyLength, -1);  
            }
        }

        if(empty($result))
        {
            return NULL;
        }
        else
        {
            return $result;
        }

   }


    function setFileNameConvention()
    {
        //
        // Create an array of regexes to try and determine the file convention
        //

        // Site code, sample, radius/core, I for index
        $regexArray['AAA1xAI']  = "/^[a-zA-Z]{3}[0-9]*[a-zA-Z]I\./";
        // Site code, sample, radius/core, reading
        $regexArray['AAA1xAA']  = "/^[a-zA-Z]{3}[0-9]*[a-zA-Z]{2}\./";
        // Site code, sample, radius/core, reading, I for index
        $regexArray['AAA1xAAI']  = "/^[a-zA-Z]{3}[0-9]*[a-zA-Z]{2}I\./";
        // Site code, sample, reading
        $regexArray['AAA1xA']   =  "/^[a-zA-Z]{3}[0-9]*[a-zA-Z]\./";
        // Site code, first sample, second sample
        $regexArray['AAA1x&1x'] = "/^[a-zA-Z]{3}[0-9]*\&[0-9]*\./";
        // Site code, Three digits (number of samples)
        $regexArray['AAA111']   = "/^[a-zA-Z]{3}[000|111|222|333|444|555|666|777|888|999]\./";

        // Run each regex on file name stopping when we find a match
        foreach($regexArray as $regexKey => $regexValue)
        {
            if(preg_match($regexValue, basename($this->theFileName)))
            {
                $this->theFileStyle=$regexKey;
                return true;
            }
        }

        // Last attempt - if file name os in format AAA?x and it is a sum,
        // then we will assume that the rest of the filename includes info
        // that we can derive such as taxon, period etc.
        if(preg_match("/^[a-zA-Z]{3}.*\./", basename($this->theFileName), $matches) &&
            $this->setElements() )
        {
            $this->theFileStyle="AAA?x";
            return true;
        }

        // None of our regexes have matched so the file convention is unknown 
        $this->theFileStyle="Unknown";
        $this->theUnsuitableFlag = TRUE;
        $this->logUnusable("File convention unknown");
        return false;
    }

   // pathinfo improved
   function pathinfo_im($path) 
   {
        $tab = pathinfo($path);
         
        $tab["basenameWE"] = substr($tab["basename"],0,strlen($tab["basename"]) - (strlen($tab["extension"]) + 1) );
                         
        return $tab;
   }


    function extractDataFromFileName()
    {
        // Explode path into components
        $pathParts = $this->pathinfo_im($this->theFileName);

        // Set file extension in Class
        $this->theFileExtension = $pathParts['extension'];

        // Only attempt to read site code if the filename is in supported format
        if(substr($this->theFileStyle, 0, 3)=="AAA")
        {
            // Get site code from first 3 chars of file name
            $siteFromFile = substr($pathParts['basename'], 0, 3);

            // Get site code from first 3 chars of last folder name
            //$siteFromFolder = substr(substr(strrchr($pathParts['dirname'], "/"), 1), 0, 3);

            // Check that they both agree and if so set it in class
            //if($siteFromFile==$siteFromFolder) 
            //{
                $this->theSiteCode= $siteFromFile;
            //}
            //else
            //{
            //    $this->theSiteCode= FALSE;
            //}
        }

        //
        // Extract varous info from file name depending on the file convention used
        // 

        if($this->theFileStyle=="AAA1xAA" || $this->theFileStyle=="AAA1xA")
        {
            //Extract sample number 
            $regex = "/[0-9][0-9]*/";
            if(preg_match($regex, $pathParts['basenameWE'], $matches))
            {
                $this->theSampleNumber = $matches[0];
            }

            //Extract reading number
            $this->theReading = substr($pathParts['basenameWE'], -1);
        }


        if($this->theFileStyle=="AAA1xAA")
        {
            //Extract the core or radius number if file convention permits 
            $this->theRadius = substr(substr($pathParts['basenameWE'], 0, -1), -1);
        }


    }

    function setElements($swapBaseDir=true)
    {
        
        // Read in file to string
        $fileContents = file_get_contents($this->theFileName);

        if($swapBaseDir)
        {
            // Replace the base dir with the correct one for this file system
            $oldBaseDir = "G:\\DATA\\";
            $newBaseDir = "/tmp/files/";
            $fileContents = str_replace($oldBaseDir, $newBaseDir, $fileContents);
        }

        // Locate elements if present and write to Class
        $regex = "/;ELEMENTS[^;]*/";
        if(preg_match($regex, $fileContents, $matches))
        {
            $elementArray = explode("\n", trim(substr($matches[0], 12)));
            $this->theElements = $elementArray;
            return TRUE;
        }
        else
        {
            return FALSE;
        }
    }

    
    function writeToDB()
    {

        if (!$this->theUnsuitableFlag)
        {
            //Set up database connection
            $conn_string = "host=localhost port=5432 dbname=dendro user=aps03pwb password=codatanl";
            $dbconn = pg_connect ($conn_string);

            // Compile insert statement
            $sql = "insert into tblimport (";
            if($this->theSampleNumber!=NULL) $sql.="samplenumber, ";
            if($this->theUnmeasuredBegin!=NULL) $sql.="unmeasuredbegin, ";
            if($this->theUnmeasurediEnd!=NULL) $sql.="unmeasuredend, ";
            if($this->theIndexMethod!=NULL) $sql.="indexmethod, ";
            if($this->theSapwoodCount!=NULL) $sql.="sapwoodcount, ";
            $sql .="reconciledflag,
                    filename, 
                    filestyle,
                    mime,
                    sitecode,
                    fileextension,
                    radius,
                    reading,
                    taxon,
                    description,
                    comments,
                    dating,
                    sampletype,
                    format,
                    pithpresence,
                    terminalring,
                    continuousstyle,
                    quality,
                    author,
                    lastmodified
                ) 
                values 
                (";
            if($this->theSampleNumber!=NULL)    $sql.= $this->theSampleNumber   .", ";
            if($this->theUnmeasuredBegin!=NULL) $sql.= $this->theUnmeasuredBegin.", ";
            if($this->theUnmeasurediEnd!=NULL)  $sql.= $this->theUnmeasuredEnd  .", ";
            if($this->theIndexMethod!=NULL)     $sql.= $this->theIndexMethod    .", ";
            if($this->theSapwoodCount!=NULL)    $sql.= $this->theSapwoodCount   .", ";
            if($this->theReconciledFlag)
            {
                $sql.= " 't', ";
            }
            else
            {
                $sql.= " 'f', ";
            }

            $sql.=   "  '".trim(addslashes(basename($this->theFileName)))."' "
                    .", '".trim(addslashes($this->theFileStyle))."' "
                    .", '".trim(addslashes($this->theMIME))."' "
                    .", '".trim(addslashes($this->theSiteCode))."' "
                    .", '".trim(addslashes($this->theFileExtension))."' "
                    .", '".trim(addslashes($this->theRadius))."' "
                    .", '".trim(addslashes($this->theReading))."' "
                    .", '".trim(addslashes($this->theTaxonString))."' "
                    .", '".trim(addslashes($this->theDescription))."' "
                    .", '".trim(addslashes($this->theComments))."' "
                    .", '".trim(addslashes($this->theDating))."' "
                    .", '".trim(addslashes($this->theSampleType))."' "
                    .", '".trim(addslashes($this->theFormat))."' "
                    .", '".trim(addslashes($this->thePithPresence))."' "
                    .", '".trim(addslashes($this->theTerminalRing))."' "
                    .", '".trim(addslashes($this->theContinuousStyle))."' "
                    .", '".trim(addslashes($this->theQuality))."' "
                    .", '".trim(addslashes($this->theAuthor))."' "
                    .", '".trim(addslashes($this->theLastModifiedDate))."' "
                .") ";

            // Rplace all empty entries with NULL values
            $sql = str_replace("''", "NULL", $sql);

            // Perform SQL insert
            pg_query($dbconn, $sql);
        }
    }


    function logUnusable($reason)
    {
        $logFile = "/tmp/badfiles.log";

        if (is_writeable($logFile))
        {
            if(!$handle = fopen($logFile, 'a'))
            {
                echo "Cannot open log file $logFile";
                exit;
            }

            if (fwrite($handle, $this->theFileName."  -- ".$reason." \n") === FALSE)
            {
                echo "Cannot write to file $logFile";
                exit;
            }

            fclose($handle);
        }
        else
        {
            echo "Log file $logFile is not writeable";
        }
        
        //Set up database connection
        $conn_string = "host=localhost port=5432 dbname=dendro user=aps03pwb password=codatanl";
        $dbconn = pg_connect ($conn_string);

        // Compile insert statement
        $sql = "insert into tblfailures (
            filename,
            reason,
            author,
            description,
            lastmodified)
            values
            (";

        $sql.=   "'". trim(addslashes(basename($this->theFileName)))."' "
              .", '". trim(addslashes($reason))."' "
              .", '". trim(addslashes($this->theAuthor))."' "
              .", '". trim(addslashes($this->theDescription))."' "
              .", '". trim(addslashes($this->theLastModifiedDate))."'"
              .") ";

        // Rplace all empty entries with NULL values
        $sql = str_replace("''", "NULL", $sql);

        // Perform SQL insert
        pg_query($dbconn, $sql);
    }


}


?>
