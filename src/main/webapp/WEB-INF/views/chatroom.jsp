<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chatting Room</title>
</head>
<body>
	<link
		href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
		rel="stylesheet" id="bootstrap-css">
	<script
		src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
	<!------ Include the above in your HEAD tag ---------->

	<div class="container">
		<div class="row">
			<div class="col-sm-4">
				<a href="?msg=15" class="chatperson"> <span class="chatimg">
						<img src="http://via.placeholder.com/50x50?text=A" alt="" />
				</span>
					<div class="namechat">
						<div class="pname">Aperson name here</div>
						<div class="lastmsg">sdk nskdfjnlssdf sdf ss fsdaf kjlsadjf
							nkksaddbhk jasddl sdf kjsdfoashf89f sdbfoi nkksaddbhk jasddl</div>
					</div>
				</a><a href="?msg=16" class="chatperson"> <span class="chatimg">
						<img src="http://via.placeholder.com/50x50?text=D" alt="" />
				</span>
					<div class="namechat">
						<div class="pname">D personname</div>
						<div class="lastmsg">sdk nskdfjnlssdf sdf ss fsdaf kjlsadjf
							nkksaddbhk jasddl sdf kjsdfoashf89f sdbfoi nkksaddbhk jasddl</div>
					</div>
				</a><a href="?msg=17" class="chatperson"> <span class="chatimg">
						<img src="http://via.placeholder.com/50x50?text=W" alt="" />
				</span>
					<div class="namechat">
						<div class="pname">WWW</div>
						<div class="lastmsg">sdk nskdfjnlssdf sdf ss fsdaf kjlsadjf
							nkksaddbhk jasddl sdf kjsdfoashf89f sdbfoi nkksaddbhk jasddl</div>
					</div>
				</a>
			</div>
			<div class="col-sm-8">
				<div class="chatbody">

					<table class="table">
						<tr>
							<td><img src="http://via.placeholder.com/50x50?text=A" /></td>
							<td>sdk fnsdkjnsadjnlk anflkjasdnflk kjasndlkfjnsdfs adfjksd
								ofh saddf lsdnflksnadl fjnasdfl ajsndflkja nlnfsdjlnf aslnfd as</td>
							<td>15:20</td>
						</tr>

						<tr>
							<td><img src="http://via.placeholder.com/50x50?text=B" /></td>
							<td>sdk fnsdkjnsadjnlk adfjksd ofh saddf lsdnflksnadl
								fjnasdfl ajsndflkja nlnfsdjlnf aslnfd as</td>
							<td>15:20</td>
						</tr>

						<tr>
							<td><img src="http://via.placeholder.com/50x50?text=A" /></td>
							<td>sdk fnsdkjnsadjnlk anflkjasdnflk kjasndl sdflksnfl
								jsdkfjsafkjsnd fnsalddf sdjdknkfjnsdfs adfjksd ofh saddf
								lsdnflksnadl fjnasdfl ajsndflkja nlnfsdjlnf aslnfd as</td>
							<td>15:20</td>
						</tr>

						<tr>
							<td><img src="http://via.placeholder.com/50x50?text=B" /></td>
							<td>sdk sf s aslnfd as</td>
							<td>15:20</td>
						</tr>

						<tr>
							<td><img src="http://via.placeholder.com/50x50?text=A" /></td>
							<td>sdk fnsdkjnsadjnlk anflkjasdnflk c vlsndl v slkdknf
								sdkmnfkfsndf kasndfllk asnddlflkn sadfn sadn sandf sadf sajdf
								sdf sdflksnfl jsdkfjsafkjsnd fnsalddf sdjdknkfjnsdfs adfjksd ofh
								saddf lsdnflksnadl fjnasdfl ajsndflkja nlnfsdjlnf aslnfd as</td>
							<td>15:20</td>
						</tr>
					</table>

				</div>

				<div class="row">
					<div class="col-xs-9">
						<input type="text" placeholder="Yozing..." class="form-control" />
					</div>
					<div class="col-xs-3">
						<button class="btn btn-info btn-block">Send</button>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>