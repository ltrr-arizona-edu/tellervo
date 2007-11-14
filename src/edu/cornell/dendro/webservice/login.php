<?php
?>
<html>
<head>
<script type="text/javascript" src="jscript/md5.js">
</script>
<script type="text/javascript">

  function setHash()
  {
    document.getElementById('hash').value = hex_md5(document.secureform.username.value+':'+hex_md5(document.getElementById('plainpassword2').value)+':'+document.getElementById('snonce').value +':'+document.getElementById('nonce').value);
    document.getElementById('prehash').value = document.secureform.username.value+':'+hex_md5(document.getElementById('plainpassword2').value)+':'+document.getElementById('snonce').value; +':'+document.getElementById('nonce').valu
    document.getElementById('snonce').value='';
    document.getElementById('password').value='';
  }
</script>
</head>

<body>
<h1> Basic login</h1>
<form name="myform" method="post" action="authenticate.php">
Username: <input type="text" name="username" value="" id="username"><br/>
Password: <input type="password" name="password" value="" id="password"><br/>
<input type="hidden" name="mode" id="mode" value="plainlogin">
<input type="submit" value ="submit">
</form>

<h1> Secure login</h1>
<form name="secureform" method="post" action="authenticate.php">
Server Nonce: <input type="text" name="snonce" id="snonce"><br/>
Client Nonce: <input type="text" name="nonce" id="nonce" ><br/>
Username: <input type="text" name="username" value="" id="username"><br/>
Password: <input type="password" name="plainpassword2" value="" id="plainpassword2"><br/>
<input type="hidden" name="mode" id="mode" value="securelogin">
<input type="hidden" size=100 name="hash" id="hash" value="">
<input type="hidden" name="prehash" id="prehash" value="">
<input type="button" value="submit" onclick="setHash();document.secureform.submit();">
</form>

<form name="logoutform" method="post" action="authenticate.php">
<input type="hidden" name="mode" id="mode" value="logout">
<input type="button" value="logout" onclick="document.logoutform.submit();">
</form>

</body>
</html>
