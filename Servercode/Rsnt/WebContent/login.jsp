<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>

<base target="_parent">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

<title>Kyobee Login</title>
<script src="https://kyobee.com/wp-content/themes/koybee/js/css3-mediaqueries.js" type="text/javascript"></script>
<link href="https://kyobee.com/wp-content/themes/koybee/css/login.css" rel="stylesheet" type="text/css" />
<link rel="icon" type="image/png" href="http://kyobee.com/wp-content/themes/koybee/images/favicon2.png"></link>
</head>

<body>
<script>

	function openForgotPassPage(){
		 var serverPath = window.location.href;
	     var serverPathIndex = serverPath.indexOf("/views");
       	 window.open(serverPath.substring(0, serverPathIndex)+"/external/forgotPassword.seam");
       
	}


	
	</script>
	
<div class="login-sec">
	<form action="j_security_check" method="post" <%if(request.getParameter("auth")== null || !(request.getParameter("auth").equalsIgnoreCase("fail"))){request.getParameter("auth");%> <%} %>>
 		
 		<%
    	if(request.getParameter("auth")!= null && request.getParameter("auth").equalsIgnoreCase("fail")){
   
    	%>
			<div class="loginerror">&nbsp;&nbsp;&nbsp;Invalid Username or Password</div> <%
    	}
    	%>
       <img src="../img/logo.png" />	
       <h2 class="logintitle">Welcome to Your Waitlist System </h2>
       <input type="text" name="j_username" class="text-field" name="text" placeholder="Username">
       <input type="password" name="j_password" class="text-field" name="text" placeholder="Password">
       <input type="submit" value="Login" class="login" name="Login">
       
       <div class="sec3">
        	<a class="forget" onclick="openForgotPassPage();" href="javascript:void(0);">Forgot Password?</a>
        </div>
       
  </form>
</div>
</body>
</html>