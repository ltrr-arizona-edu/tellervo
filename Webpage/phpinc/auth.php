<?php

    /**
    * @author Tim Sutton
    * @version 0.1
    * @copyright Tim Sutton 2000
    */

    Class Auth

    {
        /** Current connection database backend */
        var $ConnectionID;
        var $debugMode=true;
        var $authRec = "";
        var $messageOfTheDay="The system is still under development.";

        /**This is the Constructor for creating new Auth(ects).
        *
        * @param $dbconn Set this to > 0 to establish a connection to the MSSQL backend.
        */
        function Auth($dbconn)
        {
           /*
            echo "<h1>System Down For Maintenance</h2>";
            echo "Our apologies for any invonvenience, but changes we are making to the underlying database require us to disable the service temporarily.";
            echo "Please contact pwb48@cornell.edu if you have any queries.";
            exit;
            */

            session_start();
            $this->ConnectionID = $dbconn;
            global $HTTP_POST_VARS;
            //var_dump($this->ConnectionID);
            //echo "Form Action : ".$HTTP_POST_VARS["formAction"];
            if ($HTTP_POST_VARS["formAction"]=="logout")
            {
                Auth::logout();
                return;
            }
            else
            {
                Auth::AuthenticateUser();
            }
        } // end of constructor function


        /** Use HTTP headers to Authenticate the user */
        function AuthenticateUser()
        {
            global $user_id;
            global $readonly;
            global $admin;
            global $HTTP_POST_VARS;
            global $_session;
            global $HTTP_SESSION_VARS;
            $username=$HTTP_POST_VARS['username'];
            $passwd=$HTTP_POST_VARS['passwd'];

            //$myVar=SID;
            if ($this->debugMode) echo $_SESSION['user_id']." --- Current User ID".$myVar."<br>";
            if (!$username && !$passwd && !session_is_registered ( "user_id" ))
            {
                if ($this->debugMode)  echo "Session Vars: ".var_dump($HTTP_SESSION_VARS);
                if ($this->debugMode)  echo "<br>Post Vars:".var_dump($HTTP_POST_VARS);
                if ($this->debugMode)  echo "<br>No username, no passwd, no registered user_id!";
                Auth::login();
            }
            if ($username && $passwd)
            {
                if ($this->debugMode) echo "Verifying Username and passwd<br>";
                //verify the user against the prefered backend
                $mySQL = "SELECT * FROM users WHERE username='$username' and passwd='$passwd'";
                // Execute the query and see if it returns any rows
                $result = pg_query($this->ConnectionID, $mySQL);
                $myRow = pg_fetch_array($result);
                //$myRow = $this->baseConn->GetRow();
                $usernameName=$myRow["username"];
                $usernamepasswd=$myRow["passwd"];
                if ($usernamepasswd==$passwd)
                {
                    if ($this->debugMode) echo "user_id not registered";
                    //user authorised correctly against the database
                    session_register("user_id");
                    $user_id=$myRow["user_id"];
                    session_register("username");
                    $username=$myRow["username"];
                    session_register("passwd");
                    $passwd=$myRow["passwd"];
                    //session_register("readonly");
                    if ($myRow["readonly"]=='f')
                    {
                        $readonly=false;
		    }
                    else
                    {
                        $readonly=true;
                    }
                    //temporary hack!
                    //$readonly=true;
                    //session_register("admin");
                    if ($myRow["admin"]=='t')
                    {
                        $admin=true;
                    }
                    else
                    {
                        $admin=false;
                    }
                    $this->authRec=$myRow;
                    return 1;
                }
                else
                {
                    if ($this->debugMode) echo "AT Debug authenticate against db failed";
                }
            }
            if (session_is_registered ( "user_id" ))
            {
                $user_id= $_SESSION['user_id'];
                if ($this->debugMode) echo "user_id ($user_id) is registered";
                //verify the user against the prefered backend
                $mySQL = "SELECT * FROM users WHERE user_id='$user_id'";
                // Execute the query and see if it returns any rows
                $result = pg_query($this->ConnectionID,$mySQL );
                $myRow =pg_fetch_array($result);
                if ($myRow["user_id"] == $user_id)
                {
                    //user authorised correctly against the database
                    session_register("username");
                    if ($myRow["readonly"]=='f')
                    {
                        $readonly=false;
                    }
                    else
                    {
                        $readonly=true;
                    }
                    //temporary hack!
                    //$readonly=true;
                    //session_register("admin");
                    if ($myRow["admin"]=='t')
                    {
                        $admin=true;
                    }
                    else
                    {
                        $admin=false;
                    }
		    //var_dump($_SESSION);
                    if ($this->debugMode) echo "user_id: $user_id <br> administrator: $admin\n <br>readonly : $readonly\n"; //AT uncommented for testing
                    $this->authRec=$myRow;
                    return 1;
                }
                else
                {
                    if ($this->debugMode) echo "AT Debug user_id lookup failed and user_id is '".$user_id."'";
                }

            }
            else
            {
                if ($this->debugMode) echo "AT Debug user_id is not registered";
            }

            //fallback for when there is no cookie and no username / passwd
            Auth::loginFailed();
        }//end of AuthenticateUser function

        /** Destroy the users session & log him out */

        function logout()
        {
            // if session exists, unregister all variables that exist and destroy session
            //session_unregister('user_id');
            // Initialize the session.
            // If you are using session_name("something"), don't forget it now!
            session_start();
            // Unset all of the session variables.
            session_unset();
            // Finally, destroy the session.
            session_destroy();

            Auth::loginForm("<p><center>You have been logged out.  To log in again enter your username and password.</center></p>");
            die();
        }


        function login()
        {
            Auth::loginForm("<p><center>The Catalogue of Life Metadatabase is for use by authorised personnel only.<br>  If you have been assigned a username and password,  please enter them below.  <br> To request a username please email <a href=\"mailto:sp2000@rdg.ac.uk\">sp2000@rdg.ac.uk</a></center></p><br>");

            die();
        }

        function loginFailed()
        {
            Auth::loginForm("<p><center>Your username and/or password have not been recognised.  Please try again.</center></p>");
            die();
        }


        function loginForm($theMessage)
        {
            require_once ("standardheader.php");
            ?>
            <img src="img/mdbbanner.gif"/>

            <div id="main-copy">
            <?startBox("Welcome to the Catalogue of Life Metadatabase","login","login"); 
            echo $theMessage;
            ?> 
            <label for="username">User Name: </label>
            <input type="text" name="username"><br/>

            <label for="passwd">Password: </label>
            <input type="password" name="passwd"><br/>

            <label for="submit"></label>
            <input type="submit" name="submit" value="Login"><br/>
            <?endBox();?>
            </div><?
        }

    function isAdmin()
    {
	global $admin;
	return $admin;

    }
    
    function isReadOnly()
    {
	global $readonly;
	return $readonly;

    }


    } //end of Auth class
?>
