<?php

function login($username, $password)
{
     $request="<corina><request type=\"plainlogin\"> <authenticate username=\"$username\" password=\"$password\" /></request></corina>";
     $response = postData($request, "authenticate.php");
     return $response;
}

function postData($request, $page)
{
   
    $url ="https://dendro.cornell.edu/".$page;
    $postData = array();
    $postData['xmlrequest'] = $request;

    $o="";
    foreach ($postData as $k=>$v)
    {
        $o.= "$k=".utf8_encode($v)."&";
    }
    $postData=substr($o,0,-1);


    //$postData = encodeQueryString($postData);

    $header[] = "MIME-Version: 1.0";
    $header[] = "Accept: text/xml";
    $header[] = "Cache-Control: no-cache";
    //$header[] = "Connection: close \r\n";
    //$header[] = $postData;
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($ch, CURLOPT_COOKIEJAR, '/tmp/cookie.txt');
    curl_setopt($ch, CURLOPT_COOKIEFILE, '/tmp/cookie.txt');
    curl_setopt($ch, CURLOPT_URL,$url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_TIMEOUT, 4);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $postData );
    //curl_setopt($ch, CURLOPT_POSTFIELDS, "var1=value&var2=value" );
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST,'POST');
    curl_setopt($ch, CURLOPT_HTTPHEADER, $header);


    $data = curl_exec($ch);       
    if (curl_errno($ch)) 
    {
        print curl_error($ch);
        return false;
    } 
    else 
    {
        curl_close($ch);
        return $data;
    }
}

//this function is passed an array, and returns a 
//url encoded query string.  
function encodeQueryString($array)
{
    if(!is_array($array))
    {
        die("Encode Post Array expects an array");
    }    

    $retValue = '';

    for(reset($array);$key=key($array);next($array))
    {
        $retValue .= $key."=".urlencode($array[$key])."&";
    }

    //zap last &
    $retValue = substr($retValue, 0, -1);      
    return $retValue;

}

?>
