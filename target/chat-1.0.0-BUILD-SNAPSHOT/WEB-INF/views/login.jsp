<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>

<link rel="shortcut icon" type="image/x-icon"
	href="//production-assets.codepen.io/assets/favicon/favicon-8ea04875e70c4b0bb41da869e81236e54394d63638a1ef12fa558a4a835f1164.ico" />
<link rel="mask-icon" type=""
	href="//production-assets.codepen.io/assets/favicon/logo-pin-f2d2b6d2c61838f7e76325261b7195c27224080bc099486ddd6dccb469b8e8e6.svg"
	color="#111" />
<link rel="canonical" href="https://codepen.io/frytyler/pen/EGdtg" />
<link
	href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,300'
	rel='stylesheet' type='text/css'>
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css'>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/prefixfree/1.0.7/prefixfree.min.js'></script>
<link rel="stylesheet" type="text/css" href="static/css/login.css">
</head>
<body>
	<div class="login">
		<h1>Login</h1>
		<form>
			<input placeholder="username" id="userName" type="text"/> <input placeholder="password" id="userPw" type="password"/>
			<button class="btn btn-primary btn-block btn-large" onclick="login()">LOGIN</button>
		</form>
	</div>

</body>
<script type="text/javascript">
    function login() {
    	
    	var user = {
    			userName: $("#userName").val(),
    			userPw: $("#userPw").val()
            };
        $.ajax({
                type : 'POST',
                url : 'login',
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data : JSON.stringify(user),
                async : false,
                success: function(data) {
                	
                    if (data.status == 200) {
                       $.ajax({
                    	   type:'GET',
                    	   url:'room',
                    	   dataType:'jsp',
                    	   headers :{
                    		   "authorization":data.data
                    	   },
                    	   async:false,
                    	   success:function(data) {
                    		   
                    	   }
                       });
                      
                    }
                    else {
                    	alert(data.msg);
                    }
                    
                }
            });
    }
    
</script>
</html>