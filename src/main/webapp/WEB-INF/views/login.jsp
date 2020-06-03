<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>untitled</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="Transparent Sign In Form Responsive Widget,Login form widgets, Sign up Web forms , Login signup Responsive web form,Flat Pricing table,Flat Drop downs,Registration Forms,News letter Forms,Elements" />
    <script type="application/x-javascript"> 
        addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
        function hideURLbar(){ window.scrollTo(0,1); } 
    </script>
    <script src="static/js/common/jquery-1.9.1.min.js"></script>
    <link rel="icon" href="static/img/chat.ico" type="image/x-icon" />
    <link rel="stylesheet" href="static/css/common/font-awesome.css"/> <!-- Font-Awesome-Icons-CSS -->
    
</head>
<body class="background">
		<div class="header-w3l">
			<h1>채팅방</h1>
		</div>
		<div class="main-content-agile">
			<div class="sub-main-w3">	
				<h2>로그인</h2>
				<form>
					
					<div class="icon1">
						<input placeholder="user name" id="username" type="text"/>
					</div>
					
					 
						<div class="clear"></div>
					<input type="button" value="Login" onclick="login()"/>
				</form>
			</div>
		</div>
		<div class="footer">
			<p>&copy; 2020 Qucell System SW myseo, All Rights Reserved </p>
		</div>
</body>
<script type="text/javascript">
    function login() {
    	//do not send request if exists access token in cookie ->auto login 
        $.ajax({
                type : 'POST',
                url : 'users/login',
                dataType: 'application/json',
                data : {
                    username: $("#username").val()
                },
                async : false,
                success: function(data) {
                	if (data.status == 200) {
                        window.location.href="chatroom";
                    } else {
                        alert(data.message);
                    }
                }
            });
    }
    
</script>
</html>
