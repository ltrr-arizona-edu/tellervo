<?php 

       function printError($msg, $httpcode="400 Not found")
        {
           global $firebug;


           $firebug->log($msg, "Error");

            header('HTTP/1.1 '.$httpcode);

            header('Content-Type: text/html');

            echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"
         \"http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd\">
        <HTML>
          <HEAD>
            <TITLE>Error</TITLE>
          </HEAD>
          <BODY><H1>$httpcode</H1>
	<p>$msg</p></BODY>
        </HTML>
        ";
            die();


        }


        function printResponse($msg, $httpcode="200 OK")
        {
           global $firebug;


           $firebug->log($msg, "Error");

            header('HTTP/1.1 '.$httpcode);

            header('Content-Type: text/xml');

	    echo "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response items=\"1\">";
	    echo "<message>$msg</message>";
            echo "</OpenRosaResponse>";

            die();


        }



?>
