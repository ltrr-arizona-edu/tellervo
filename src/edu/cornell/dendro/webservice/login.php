<?php
?>
<html>
<head>
<script type="text/javascript" src="jscript/md5.js">
</script>
<script type="text/javascript">
  document.getElementById('nonce').value = 
  function setHash()
  {
    document.getElementById('hash').value = hex_md5(document.getElementById('username').value+':'+hex_md5(document.getElementById('plainpassword2').value)+':'+document.getElementById('snonce').value +':'+document.getElementById('nonce').value);
    document.getElementById('snonce').value='';
    document.getElementById('password').value='';
  }
</script>
</head>

<body>
<h1> Basic login</h1>
<form name="myform" method="post" action="authenticate.php">
Username: <input type="text" name="username" value="" id="username"><br/>
Password: <input type="text" name="plainpassword" value="" id="plainpassword"><br/>
<input type="hidden" name="mode" id="mode" value="plainlogin">
<input type="hidden" name="password" id="password">
<input type="button" value ="submit" onclick="document.getElementById('password').value= hex_md5(document.getElementById('plainpassword').value); document.getElementById('plainpassword').value='';document.myform.submit();">
</form>

<h1> Secure login</h1>
<form name="secureform" method="post" action="authenticate.php">
Server Nonce: <input type="text" name="snonce" id="snonce"><br/>
Client Nonce: <input type="text" name="nonce" id="nonce" value=""><br/>
Username: <input type="text" name="username" value="" id="username"><br/>
Password: <input type="text" name="plainpassword2" value="" id="plainpassword2"><br/>
<input type="hidden" name="mode" id="mode" value="securelogin">
<input type="hidden" size=100 name="hash" id="hash" value="">
<input type="button" value="submit" onclick="setHash();document.secureform.submit();">
</form>
</body>
</html>
