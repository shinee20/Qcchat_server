<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class=''>
<head>
<title>QCCHAT</title>
<meta charset='UTF-8'>
<meta name="robots" content="noindex">
<link rel="shortcut icon" type="chat/ico" href="static/img/chat.ico" />
<link rel="canonical"
	href="https://codepen.io/emilcarlsson/pen/ZOQZaV?limit=all&page=74&q=contact+" />
<link
	href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,300'
	rel='stylesheet' type='text/css'>
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<style>
#myModel {
	text-align: center;
}

@media screen and (min-width: 768px) {
	#myModel:before {
		display: inline-block;
		vertical-align: middle;
		content: " ";
		height: 100%;
	}
}

.modal-dialog {
	margin-left: -20%;
	display: inline-block;
	text-align: left;
	vertical-align: middle;
}

.modal-header, h4, .close {
	background-color: #5cb85c;
	color: white !important;
	text-align: center;
	font-size: 30px;
}

.modal-footer {
	background-color: #f9f9f9;
}
</style>
<script src="https://use.typekit.net/hoy3lrg.js"></script>
<script>
	try {
		Typekit.load({
			async : true
		});
	} catch (e) {
	}
</script>
<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css'>
<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.2/css/font-awesome.min.css'>
<link rel="stylesheet" type="text/css" href="static/css/room.css?ver=1">

</head>
<body>
	<!-- Modal -->
	<div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header" style="padding: 35px 50px;">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4>
						<span class="glyphicon glyphicon-lock" id="modal-name"></span>
					</h4>
				</div>
				<div class="modal-body" style="padding: 40px 50px;">
					<form role="form">
						<div class="form-group">
							<label for="usrname"><span
								class="glyphicon glyphicon-user"></span> Username</label> <input
								type="text" class="form-control" id="username"
								placeholder="Enter user name">
						</div>
						<div class="form-group">
							<label for="psw"><span
								class="glyphicon glyphicon-eye-open"></span> Password</label> <input
								type="password" class="form-control" id="passwd"
								placeholder="Enter password">
						</div>
						<button type="button" class="btn btn-success btn-block"
							id="closeModal">
							<span class="glyphicon glyphicon-off"></span> OK
						</button>
					</form>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-danger btn-default pull-left"
						data-dismiss="modal">
						<span class="glyphicon glyphicon-remove"></span> Cancel
					</button>
				</div>
			</div>
		</div>
	</div>
	<div id="frame">
		<div id="sidepanel">
			<div id="profile">
				<div class="wrap">
					<img id="profile-img" src="static/img/emoji/smile.png"
						class="online" alt="" />
					<p>
						<span id="myName" style="font-weight: bold; font-size: 1.3em;"></span>
					<p>
						<i class="fa fa-user icon fa-fw" id="login" aria-hidden="true"
							title="로그인"></i>
					</p>

					<p>
						<i class="fa fa-user-plus fa-fw" id="signup" aria-hidden="true"
							title="회원가입"></i>
					</p>
					<p>
						<i class="fa fa-sign-out fa-fw" id="logout"
							onclick="disconnect();" aria-hidden="true" title="로그아웃"></i>
					</p>
					<!--  <i class="fa fa-chevron-down expand-button" aria-hidden="true"></i>-->

					<div id="status-options">
						<ul>
							<li id="status-online" class="active"><span
								class="status-circle"></span>
								<p>Smile</p></li>
							<li id="status-away"><span class="status-circle"></span>
								<p>Enjoy</p></li>
							<li id="status-busy"><span class="status-circle"></span>
								<p>Serious</p></li>
							<li id="status-offline"><span class="status-circle"></span>
								<p>Sad</p></li>
							<li id="status-happy"><span class="status-circle"></span>
								<p>Happy</p></li>
						</ul>
					</div>
					<div id="expanded">
						<!-- friends list -->

					</div>
				</div>
			</div>
			<div id="search">
				<label for=""><i class="fa fa-search" aria-hidden="true"></i></label>
				<input type="text" id="searchId" placeholder="Search contacts..." />
			</div>
			<div id="contacts">
				<ul>

				</ul>
			</div>
			<div id="bottom-bar">
				<button id="addcontact" onclick="createRoom();">
					<i class="fa fa-plus-circle fa-fw" aria-hidden="true"></i> <span>채팅방
						추가</span>
				</button>
				<button id="friends" onClick="requestFriendsList();">
					<i class="fa fa-user fa-fw" aria-hidden="true"></i> <span>
						친구 목록 보기</span>
				</button>
				<button id="showallroom" onClick="requestAllRoomList();">
					<i class="fa fa-comments-o fa-fw" aria-hidden="true"></i> <span>모든
						채팅방 보기</span>
				</button>
				<button id="showallroom" onClick="requestUserRoomList();">
					<i class="fa fa-commenting-o fa-fw" aria-hidden="true"></i> <span>참여
						채팅방 보기</span>
				</button>
			</div>
		</div>

		<div class="content" id="content">
			<div class="contact-profile" id="chat-header">
				<img src="static/img/avatar/Member001.jpg" alt=""
					id="chat-header-img" /> <span
					style="width: 80%; margin-top: -7px; font-weight: bold;"
					id="chat-name"></span>
				<div class="social-media">
					<i class="fa fa-times" aria-hidden="true" onclick="exitFromRoom();"></i>
				</div>
				<span id="userListInRoom"></span>
			</div>
			<div class="messages" id="messages">
				<ul>

				</ul>
			</div>

			<div class="message-input">
				<div class="wrap">
					<input type="text" placeholder="Write your message..."
						onkeyup="onMsgKeyUp(event);" id="input-msg" /> <i
						class="fa fa-paperclip attachment" aria-hidden="true"></i>
					<button class="submit">
						<i class="fa fa-paper-plane" aria-hidden="true"></i>
					</button>
				</div>
			</div>
		</div>
	</div>

	<script>
		var websocket;
		var myId;
		var myName;
		var jwt;
		var host;
		var websocketPort;
		var status;
		var roomCnt;

		$("#signup").on("click", function() {
			$("#username").val("");
			$("#passwd").val("");
			$("#modal-name").text("SIGNUP");
			$("#myModal").modal();

		});
		$("#login").on("click", function() {
			$("#username").val("");
			$("#passwd").val("");
			$("#modal-name").text("LOGIN");
			$("#myModal").modal();

		});
		$("#myModal").on('shown.bs.modal', function() {
			$("#username").focus();
		});
		$('#closeModal').on('click', function() {
			var howuse = $("#modal-name").text();

			var name = $("#username").val();
			var password = $("#passwd").val();

			if (name == "" || password == "")
				return;

			if (howuse === "LOGIN") {
				loginProcess(name, password);
			} else {
				signup(name, password);
			}
			$("#myModal").modal('hide');
		});
		function signup(name, password) {
			//폼으로 바꾸기
			/*var name = prompt("가입할 이름을 입력하세요");
			if (name == null || name == "") {
				alert("다시 입력하세요");
				return;
			}
			var password = prompt("비밀번호를 입력하세요");
			if (password == null || password == "") {
				alert("다시 입력하세요");
				return;
			}*/
			var user = {
				userName : name,
				userPw : password
			};
			$.ajax({
				type : 'POST',
				url : 'signup',
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(user),
				async : false,
				success : function(data) {
					if (data.status == 201) {
						console.log(data);
						alert("회원 가입을 완료하였습니다.");
						return;
					}
				},
				error : function() {
					alert("이미 사용중인 이름입니다. 다른 이름을 입력해주세요.");
					return;
				}

			});

		}
		function loginProcess(name, password) {

			var user = {
				userName : name,
				userPw : password
			};

			console.log(JSON.stringify(user));
			$.ajax({
				type : 'POST',
				url : 'login',
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(user),
				async : false,
				success : function(data) {
					if (data.status == 200) {
						console.log(data);
						jwt = data.data.token.token;
						host = data.data.host;
						websocketPort = data.data.websocketPort;
						startWebsocket();
					} else {
						alert("가입되어있지 않는 사용자입니다.");
						return;
					}
				},
				error : function(data) {
					if (data.status == 500) {
						alert("틀린 비밀번호입니다.");
						return;
					}

				}
			});
		}

		function startWebsocket() {
			websocket = new WebSocket("ws://" + host + ":" + websocketPort
					+ "/websocket");

			websocket.onerror = function(event) {
				console.log("== error", event);
				alert("== Error : " + event.code + " , " + event.reason);
			};

			websocket.onopen = function(event) {
				console.log("== onopen");
				loginConfirm();
			};

			websocket.onclose = function(event) {
				console.log("== websocket closed");
				websocket = null;

			};

			websocket.onmessage = function(event) {
				console.log(event.data);
				var obj = JSON.parse(event.data);
				var action = obj.action;
				if (action == 'LoginConfirmed') {
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");

					myId = refId;
					myName = refName;

					$("#myId").text(myId);
					$("#myName").text(myName);

				} else if (action == 'FriendsList') {
					var arr = JSON.parse(obj.msg);
					var target = $("#contacts ul");
					target.empty();
					$.each(arr,
							function(idx, elem) {
								append(target, elem.name + "(" + elem.id + ")",
										elem.id);
							});
				} else if (action == 'RoomList') {
					var arr = JSON.parse(obj.msg);

					var target = $("#contacts ul");
					target.empty();
					$.each(arr, function(idx, elem) {
						append3(target, elem.name, elem.id, elem.mens, idx);
					});

				} else if (action == 'UserRoomList') {
					var arr = JSON.parse(obj.msg);

					var target = $("#contacts ul");
					target.empty();
					$.each(arr, function(idx, elem) {
						append3(target, elem.name, elem.id, elem.mens, idx);
					});

				} else if (action == 'FindUserByName') {
					var arr = JSON.parse(obj.msg);
					console.log(arr);
					var target = $("#contacts ul");
					target.empty();
					append2(target, arr.userName, arr.userId);

				} else if (action == 'UserList') {
					var arr = JSON.parse(obj.msg);
					var roomId = getFromHeader(obj, "roomId");
					var target = $("#userListInRoom");
					var names = "";

					$.each(arr, function(idx, elem) {
						names += elem.name;
						if (idx < arr.length - 1) {
							names += ", ";
						}
					});
					target.text(names);
				} else if (action == 'InviteFriend') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var alreadyIn = getFromHeader(obj, "alreadyIn");

					console.log("== enter to room", roomId, refId, refName,
							alreadyIn);
					if (refId === myId) {
						$("#chat-name").text(roomId);
						var imgsrc = $('.contact[id^=' + roomId + '] img')
								.attr("src");
						var src = $('#chat-header-img').attr("src", imgsrc);
						$(".messages ul").empty();

					} else {
						if (alreadyIn === "true") {
						} else {
							var msg = refName + "(" + refId + ")이 " + roomId
									+ " 방으로 들어왔습니다."
							enterMessage(msg, roomId, refName, refId);
						}
					}

				} else if (action == 'EnterToRoom') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var alreadyIn = getFromHeader(obj, "alreadyIn");
					var status = getFromHeader(obj, "status");
					
					console.log("== enter to room", roomId, refId, refName,
							alreadyIn);
					if (refId === myId) {
						$("#chat-name").text(roomId);
						var imgsrc = $('.contact[id^=' + roomId + '] img')
								.attr("src");
						var src = $('#chat-header-img').attr("src", imgsrc);
						$(".messages ul").empty();

					} else {
						if (alreadyIn === "true") {
						} else {
							var msg = refName + "(" + refId + ")이 " + roomId
									+ " 방으로 들어왔습니다."
							enterMessage(msg, roomId, "","",status);
						}
					}

				} else if (action == 'ExitFromRoom') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");

					if (refId == myId) {
						$(".contact").find(roomId).empty();
						initDisplay();
					} else {
						var msg = refName + "(" + refId + ")이 " + roomId
								+ " 방을 나갔습니다."
						enterMessage(msg, roomId, refName, refId);
						//현재 이 방에 있는 사람들에게만 화면에 띄워야 한다. 다른 채팅방에 있는 사람들까지 화면 전환할 필요 없음
					}

				} else if (action == 'SendMsg') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var status = getFromHeader(obj, "status");
					
					enterMessage(obj.msg, roomId, refName, refId, status);

				} else if (action == 'MsgLog') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var arr = JSON.parse(obj.msg);

					$.each(arr, function(idx, elem) {
						enterMessage(elem.text, roomId, elem.sender,"",elem.status);
					});
				} else if (action == 'LogIn') {
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var target = $("#allUsers");
					if (target.find("[hong-etc='" + refId + "']").length == 0) {
						append(target, refName + "(" + refId + ")", refId);
					}
					$("#login").hide();
					$("#signup").hide();
					$("#logout").show();
				} else if (action == 'Invalid') {
					alert(obj.msg);
				}

			};
		}

		function getFromHeader(obj, key) {
			var headers = obj.headers;
			if (headers != null) {
				return headers[key];
			} else {
				return null;
			}
		}

		function loginConfirm() {
			console.log("auth", jwt);
			var obj = new Builder().action("LogIn").header("auth", jwt)
					.finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function disconnect() {
			if (websocket == null) {
				alert("로그인하십시오.");
				return;
			}
			if (confirm(name + " 접속을 종료합니다.")) {
				websocket.close();
				location.reload();
			}
		}

		function initDisplay() {
			$("#chat-name").text("");
			$(".messages ul").empty();
			$("#userListInRoom").empty();

		}

		function Builder() {
			var me = {};

			function action(a) {
				me.action = a;
				return this;
			}

			function header(k, v) {
				if (me.headers == null) {
					me.headers = {};
					me.headers[k] = v;
				} else {
					me.headers[k] = v;
				}
				return this;
			}

			function msg(m) {
				me.msg = m;
				return this;
			}

			function finish() {
				return me;
			}
			return {
				action : action,
				header : header,
				msg : msg,
				finish : finish
			};
		}

		function createRoom() {
			if (websocket == null) {
				alert("로그인하십시오.");
				return;
			}
			var roomName = prompt("방명을 입력해주세요. ");
			console.log("== roomName", roomName);
			if (roomName == null || roomName == "") {
				alert("방명을 입력하세요.");
				return;
			}

			var obj = new Builder().action("CreateRoom").header("roomId",
					roomName).finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		//사용자가 방에서 exit 할 때  reload 필요 (html("") 한 후에 다시 append 하기 위해 )
		function exitFromRoom() {
			if (websocket == null) {
				alert("로그인하십시오.");
				return;
			}
			var roomId = $("#chat-name").text();
			if (confirm(roomId + " 방을 나가시겠습니까?")) {
				var obj = new Builder().action("ExitFromRoom").header("roomId",
						roomId).finish();
				var jsonStr = JSON.stringify(obj);
				websocket.send(jsonStr);

			}
		}

		function requestAllUserList() {
			var obj = new Builder().action("AllUserList").finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function requestFriendsList() {
			var obj = new Builder().action("FriendsList").finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		//친 리스트 목록 (target, room name, room Id)
		function append($div, msg, id) {
			console.log("==append", msg, id);
			var newElem = $("<li class='contact friends' id='" + id + "'><div class='wrap'><span class='contact-status online'></span><img src='static/img/emoji/smiling.png' alt='' /><div class='meta'><p class='name'>"
					+ msg + "</p><p class='preview'></p></div></div></li>")

			newElem.appendTo($div);
			$div.animate({
				scrollTop : 100000
			});
		}

		//친구 찾기 
		function append2($div, msg, id) {
			console.log("==append2", msg, id);
			var newElem = $("<li class='contact friends' id='" + id + "'><div class='wrap'><span class='contact-status online'></span><img src='static/img/emoji/smiling.png' alt='' /><div class='meta'><p class='name'>"
					+ msg
					+ "<i class='fa fa-plus-circle fa-fw' aria-hidden='true' style='float:right;' id='addFriendBtn'></i></p></div></div></li>")

			newElem.appendTo($div);
			$div.animate({
				scrollTop : 100000
			});
		}

		//채팅방 리스트 목록 (target, room name, room Id)
		function append3($div, msg, id, number, idx) {
			console.log("==append", msg, id, number);
			console.log("== size of room", roomCnt);
			if (number > 2) {
				var newElem = $("<li class='contact room' id='" + id + "'><div class='wrap'><img src='static/img/avatar/Group01.jpg' alt='' /><div class='meta'><p class='name'>"
						+ msg + "</p><p class='preview'></p></div></div></li>")

			} else {
				var num = (idx + 1) % 10;
				console.log(num);
				var newElem = $("<li class='contact room' id='" + id + "'><div class='wrap'><span class='contact-status online'></span><img src='static/img/avatar/Member00"+num+".jpg' alt='' /><div class='meta'><p class='name'>"
						+ msg + "</p><p class='preview'></p></div></div></li>")
			}
			newElem.appendTo($div);
			$('.contact[id^=' + $("#chat-name").text() + ']').addClass(
					".active");
			$div.animate({
				scrollTop : 100000
			});
		}

		function onClickRoom(name) {
			if (confirm(name + " 방에 들어가시겠습니까?")) {
				var obj = new Builder().action("EnterToRoom").header("roomId",
						name).finish();
				var jsonStr = JSON.stringify(obj);
				websocket.send(jsonStr);
			}
		}

		//대화 로그가 존재한다면 같이 보내줘야 한다. 
		function requestMessageLog(roomId) {
			var obj = new Builder().action("MsgLog").header("roomId", roomId)
					.finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function requestAllRoomList() {
			var obj = new Builder().action("RoomList").finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function requestUserRoomList() {
			var obj = new Builder().action("UserRoomList").finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function addFriendToRoom(friendId, roomId) {
			console.log("==invite friends", friendId, roomId);
			var obj = new Builder().action("InviteFriend").header("userName",
					friendId).header("roomId", roomId).finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		$(".messages").animate({
			scrollTop : $(document).height()
		}, "fast");

		$("#profile-img").click(function() {
			$("#status-options").toggleClass("active");
		});

		$("#contacts ul").on("click", ".contact.room", function() {
			//active되어있는 li를 removeclass하고 현재 선택한 li에 addClass를 한다.

			$("#contacts ul").find("li.active").removeClass("active");
			$(this).addClass("active");
			onClickRoom($(this).find("p").first().text());

		});

		$("#contacts ul")
				.on(
						"click",
						".contact.friends",
						function() {
							//active되어있는 li를 removeclass하고 현재 선택한 li에 addClass를 한다.
							var name = $("#searchId").val();
							$("#searchId").val("");
							if (name === "" || name == null) {
								//친구를 채팅방에 초대하기 
								var userId = $(this).find("p").first().text();
								var userName = userId.substring(0,
										userId.length - 3);
								var roomId = prompt(userName
										+ " 을(를) 초대할 방명을 입력해주세요. ");
								if (roomId === "" || name == null) {
									alert("방명을 입력하세요.");
									return;
								}
								addFriendToRoom(userName, roomId);

							} else {
								if (confirm(name + " 을 친구 리스트에 추가하시겠습니까?")) {
									var obj = new Builder().action("AddFriend")
											.header("userName", name).finish();
									var jsonStr = JSON.stringify(obj);
									websocket.send(jsonStr);

								}
							}

						});

		$("#status-options ul li").click(
				function() {
					$("#profile-img").removeClass();
					$("#status-online").removeClass("active");
					$("#status-away").removeClass("active");
					$("#status-busy").removeClass("active");
					$("#status-offline").removeClass("active");
					$("#status-happy").removeClass("active");

					$(this).addClass("active");

					var changeStatus = "online";

					if ($("#status-online").hasClass("active")) {
						$("#profile-img").addClass("online");
						$("#profile-img").attr("src",
								"static/img/emoji/smile.png");
						changeStatus = "online";
					} else if ($("#status-away").hasClass("active")) {
						$("#profile-img").addClass("away");
						$("#profile-img").attr("src",
								"static/img/emoji/smiling.png");
						changeStatus = "away";
					} else if ($("#status-busy").hasClass("active")) {
						$("#profile-img").addClass("busy");
						$("#profile-img").attr("src",
								"static/img/emoji/serious.png");
						changeStatus = "busy";
					} else if ($("#status-offline").hasClass("active")) {
						$("#profile-img").addClass("offline");
						$("#profile-img").attr("src",
								"static/img/emoji/sad.png");
						changeStatus = "offline";
					} else if ($("#status-happy").hasClass("active")) {
						$("#profile-img").addClass("happy");
						$("#profile-img").attr("src",
								"static/img/emoji/heart.png");
						changeStatus = "happy";
					} else {
						$("#profile-img").removeClass();
					}

					$("#status-options").removeClass("active");

					console.log("change-status ", changeStatus);

					var obj = new Builder().action("ChangeStatus").header("status",changeStatus).finish();
					var jsonStr = JSON.stringify(obj);
					websocket.send(jsonStr);

				});

		$('#searchId')
				.keypress(
						function(event) {
							var keycode = (event.keyCode ? event.keyCode
									: event.which);
							if (keycode == '13') {
								var searchName = $("#searchId").val();
								console.log(searchName);
								var obj = new Builder()
										.action("FindUserByName").header(
												"userName", searchName)
										.finish();
								var jsonStr = JSON.stringify(obj);
								websocket.send(jsonStr);
							}
						});

		
		function onMsgKeyUp(event) {
			if (event.keyCode == 13 || event.which == 13) {

				console.log("== enter");
				var roomId = $("#chat-name").text();
				var input = $("#input-msg");
				var msg = input.val();
				if (msg == null || msg === "")
					return;
				input.val("");

				var status = $("#profile-img").attr("class");
				console.log("profile img class = " , status);
				var obj = new Builder().action("SendMsg").header("roomId",
						roomId).header("status",status).msg(msg).finish();
				var jsonStr = JSON.stringify(obj);
				websocket.send(jsonStr);
			}
		}

		function enterMessage(message, roomId, userName, userId, status) {
			console.log(message);

			if (userName == null || userName === "") {}
			else {
				$('.contact[id^=' + roomId + '] .preview').html(
					'<span>' + userName + ': </span>' + message);
			}

			var imgName;

			if (status === "online") {
				imgName="smile";
				} else if (status === "away") {
				imgName="smiling";
				} else if (status === "busy") {
				imgName="serious";
				} else if (status === "offline") {
				imgName="sad";
				} else if (status === "happy") {
				imgName="heart";
				}
			
			
			if ($("#chat-name").text() === roomId) {
				//계속 머무르던 방에 대화를 보냄
				if (myId === userId || userName === myName) {
					$(
							'<li class="sent"><img src="static/img/emoji/'+imgName+'.png" alt="" /><p>'
									+ message + '</p></li>').appendTo(
							$('.messages ul'));

				} else {
					$(
							'<li class="replies"><div class="avatar"><img src="static/img/emoji/'+imgName+'.png" alt="" /><div class="name" style="float:right;margin-top:-15px">'
									+ userName
									+ '</div></div><div class="text"><p>'
									+ message + '</p></div></li>').appendTo(
							$('.messages ul'));

				}

				$(".messages").animate({
					scrollTop : $(document).height()
				}, "fast");
			}

		};

		//# sourceURL=pen.js
	</script>
</body>
</html>
