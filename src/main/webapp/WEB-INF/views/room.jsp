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
<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
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
<link rel="stylesheet" type="text/css" href="static/css/room.css">
</head>
<body>
	<div id="frame">
		<div id="sidepanel">
			<div id="profile">
				<div class="wrap">
					<img id="profile-img" src="static/img/emoji/smile.png"
						class="online" alt="" />
					<p>
						&nbsp; ID:<span id="myName"></span> (<span id="myId"></span>)
					<p>
						<button type="button" class="btn btn-secondary btn-sm" id="login"
							onclick="loginProcess()" aria-hidden="true">IN</button>
					</p>
					<p>
						<button type="button" class="btn btn-secondary btn-sm" id="logout"
							onclick="disconnect();" aria-hidden="true">OUT</button>
					</p>
					<p>
						<i class="fa fa-user-plus fa-fw" id="signup"
							onclick="signup();" aria-hidden="true"></i>
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

			<div id="contacts">
				<ul>
					<li class="contact active">
						<div class="wrap">
							<span class="contact-status online"></span> <img
								src="static/img/emoji/smile.png" alt="" />
							<div class="meta">
								<p class="name">CHATNAME</p>
								<p class="preview">Preview Message</p>
							</div>
						</div>
					</li>
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
			<div class="contact-profile">
				<img src="static/img/emoji/smile.png" alt="" /> <span
					id="chat-name">CHATNAME</span>
					
				<div class="social-media">
					<i class="fa fa-times" aria-hidden="true" onclick="exitFromRoom();"></i>

				</div>

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

	<script src='https://code.jquery.com/jquery-2.2.4.min.js'></script>
	<script>
		var websocket;
		var myId;
		var myName;
		var jwt;
		var host;
		var websocketPort;

		function signup() {
			var name = prompt("가입할 이름을 입력하세요");
			if (name == null || name == "") {
				alert("다시 입력하세요");
				return;
			}
			var password = prompt("비밀번호를 입력하세요");
			if (password == null || password == "") {
				alert("다시 입력하세요");
				return;
			}
			var user = {
					"userName" : name,
					"userPw" : password
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
		function loginProcess() {
			var name = prompt("이름을 입력하세요");
			if (name == null || name == "") {
				alert("다시 입력하세요");
				return;
			}

			var user = {
				"userName" : name
			};
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
				location.reload();
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

				} else if (action == 'AllUserList') {
					//사용 안함
					var refId = getFromHeader(obj, "refId");
					var arr = JSON.parse(obj.msg);
					var target = $("#allUsers");
					target.html("");
					$.each(arr, function(idx, elem) {
						append(target, elem.name + "(" + elem.id + ")"
								+ (myId == elem.id ? " => 나" : ""), elem.id);
					});
				} else if (action == 'FriendsList') {
					var arr = JSON.parse(obj.msg);
					var target = $("#contacts ul");
					target.html("");
					$.each(arr,function(idx, elem) {
						append(target, elem.name + "(" + elem.id + ")",
								elem.id);
					});
				} else if (action == 'RoomList') {
					var arr = JSON.parse(obj.msg);
					var target = $("#contacts ul");
					target.html("");
					$.each(arr, function(idx, elem) {
						append(target, elem.name, elem.id);
					});

				} else if (action == 'UserRoomList') {
					var arr = JSON.parse(obj.msg);
					var target = $("#contacts ul");
					target.html("");
					$.each(arr, function(idx, elem) {
						append(target, elem.name, elem.id);
					});

				} else if (action == 'UserList') {
					var arr = JSON.parse(obj.msg);
					var roomId = getFromHeader(obj, "roomId");
					var target = $("#userListInRoom");
					var names = "";
					/*$.each(arr, function(idx, elem) {
					    append3(target, elem.name + "(" + elem.id + ")" +
					        (myId == elem.id ? " => 나" : ""));
					});*/
					$.each(arr, function(idx, elem) {
						names += ' ' + elem.name;
					});
					target.text(names);
				} else if (action == 'EnterToRoom') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var alreadyIn = getFromHeader(obj, "alreadyIn");

					console.log("== enter to room", roomId, refId, refName);
					if (alreadyIn === "true") {
						console.log("already in!!");
						emptyMessage(roomId);
					} else {
						var msg = refName + "(" + refId + ")이 " + roomId
								+ " 방으로 들어왔습니다."
						enterMessage(msg, roomId, refName, refId);
					}

				} else if (action == 'ExitFromRoom') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");

					if (refId == myId) {
						$(".contact.active").empty();
					} else {
						var msg = refName + "(" + refId + ")이 " + roomId
								+ " 방을 나갔습니다."
						enterMessage(msg, roomId, refName, refId);
					}

				} else if (action == 'SendMsg') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");

					enterMessage(obj.msg, roomId, refName, refId);

				} else if (action == 'MsgLog') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var arr = JSON.parse(obj.msg);

					$.each(arr, function(idx, elem) {
						enterMessage(elem.text, roomId, elem.sender);
					});
				} else if (action == 'LogIn') {
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var target = $("#allUsers");
					if (target.find("[hong-etc='" + refId + "']").length == 0) {
						append(target, refName + "(" + refId + ")", refId);
					}
					$("#login").hide();
					$("#logout").show();
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
				alert("websocket을 시작한후에 하세요.");
				return;
			}
			if (confirm(name + " 접속을 종료합니다.")) {
				websocket.close();
				initDisplay();
			}
		}

		function initDisplay() {
			$("#login").show();
			$("#logout").hide();
			$("#myId").text("");
			$("#myName").text("");
			$("#contacts").html("");
			$(".messages").html("");
			$(".contact-profile").html("");
		}

		function initMessage() {
			$(".messages").html("");
			$(".contact-profile").html("");
		}

		function updateDiv($div) {
			$div.load(window.location.href + " " + $div);
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
					me.headers.push({
						k : v
					});
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
				alert("websocket을 시작한후에 하세요.");
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
				alert("websocket을 시작한후에 하세요.");
				return;
			}
			var roomId = $("#chat-name").text();
			if (confirm(roomId + " 방을 나가시겠습니까?")) {
				var obj = new Builder().action("ExitFromRoom").header("roomId",
						roomId).finish();
				var jsonStr = JSON.stringify(obj);
				websocket.send(jsonStr);
				//initMessage();
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

		function getIdxFromRoomname(roomName) {
			var result;
			$("#room_1, #room_2, #room_3, #room_4").each(function() {
				var val = $(this).val();
				if (val == roomName) {
					result = $(this).prop("id").substring(5);
					return false;
				}
			});
			return result;
		}

		function log(msg) {
			$('<div/>').text(msg).appendTo($("#log"));
		}

		/*function append($div, msg, etcVal) {
		    var children = $div.children("div");
		    var len = children.length;
		    // 50개 이전것은 삭제
		    if (len > 50) {
		        children.slice(0, len - 50).remove();
		    }
		    var newElem = $('<div/>').text(msg);
		    if (etcVal != null) {
		        newElem.attr("hong-etc", etcVal);
		    }
		    newElem.appendTo($div);
		    $div.animate({
		        scrollTop: 100000
		    });
		}*/

		//채팅방 리스트 목록 (target, room name, room Id)
		function append($div, msg, id) {
			console.log("==append", msg, id);
			var newElem = $("<li class='contact' id='" + id + "'><div class='wrap'><span class='contact-status online'></span><img src='static/img/emoji/smiling.png' alt='' /><div class='meta'><p class='name'>"
					+ msg + "</p><p class='preview'></p></div></div></li>")

			newElem.appendTo($div);
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

		$(".messages").animate({
			scrollTop : $(document).height()
		}, "fast");

		$("#profile-img").click(function() {
			$("#status-options").toggleClass("active");
		});

		$("#contacts ul").on("click", ".contact", function() {
			//active되어있는 li를 removeclass하고 현재 선택한 li에 addClass를 한다.
			$("#contacts ul").find("li.active").removeClass("active");
			$(this).addClass("active");
			onClickRoom($(this).find("p").first().text());

		});

		/*$("#show-contact-information").click(function() {
			var x = document.getElementById("information");
			if (x.style.display == "none") {
				x.style.display = "block";
			} else {
				x.style.display = "none";
			}
		});*/

		$("#status-options ul li").click(function() {
			$("#profile-img").removeClass();
			$("#status-online").removeClass("active");
			$("#status-away").removeClass("active");
			$("#status-busy").removeClass("active");
			$("#status-offline").removeClass("active");
			$("#status-happy").removeClass("active");

			if ($("#status-online").hasClass("active")) {
				$("#profile-img").addClass("online");
				$("#profile-img").attr("src", "static/img/emoji/smile.png");
			} else if ($("#status-away").hasClass("active")) {
				$("#profile-img").addClass("away");
				$("#profile-img").attr("src", "static/img/emoji/smiling.png");
			} else if ($("#status-busy").hasClass("active")) {
				$("#profile-img").addClass("busy");
				$("#profile-img").attr("src", "static/img/emoji/serious.png");
			} else if ($("#status-offline").hasClass("active")) {
				$("#profile-img").addClass("offline");
				$("#profile-img").attr("src", "static/img/emoji/sad.png");
			} else if ($("#status-happy").hasClass("active")) {
				$("#profile-img").addClass("happy");
				$("#profile-img").attr("src", "static/img/emoji/heart.png");
			} else {
				$("#profile-img").removeClass();
			}

			$("#status-options").removeClass("active");
		});

		function onMsgKeyUp(event) {
			if (event.keyCode == 13 || event.which == 13) {
				console.log("== enter");
				var roomId = $("#chat-name").text();
				var input = $("#input-msg");
				var msg = input.val();
				input.val("");

				var obj = new Builder().action("SendMsg").header("roomId",
						roomId).msg(msg).finish();
				var jsonStr = JSON.stringify(obj);
				websocket.send(jsonStr);
			}
		}

		function emptyMessage(roomId) {
			if ($("#chat-name").text() === roomId) {
				//계속 머무르던 방에 대화를 보냄

			} else {
				$("#chat-name").text(roomId);
				var target = $("#messages ul");
				target.html("");

			}
		}
		function enterMessage(message, roomId, userName, userId) {
			console.log(message);
			$('.contact.active .preview').html('<span>You: </span>' + message);
			if ($("#chat-name").text() === roomId) {
				//계속 머무르던 방에 대화를 보냄

			} else {
				$("#chat-name").text(roomId);
				var target = $("#messages ul");
				target.html("");

			}
			if (myId == userId || userName === myName) {
				$(
						'<li class="sent"><img src="static/img/emoji/smile.png" alt="" /><p>'
								+ message + '</p></li>').appendTo(
						$('.messages ul'));

			} else {
				$(
						'<li class="replies"><div class="avatar"><img src="static/img/emoji/smiling.png" alt="" /></div><div class="name">'
								+ userName
								+ '</div><div class="text"><p>'
								+ message + '</p></div></li>').appendTo(
						$('.messages ul'));

			}

			$(".messages").animate({
				scrollTop : $(document).height()
			}, "fast");
		};

		/*function newMessage(message) {
		    var roomId = $("#chat-room").text();
		    var obj = new Builder().action("SendMsg").header("roomId", roomId)
		        .msg(msg).finish();
		    var jsonStr = JSON.stringify(obj);

		    websocket.send(jsonStr);
		}*/

		/*$('.submit').click(function() {
		    message = $(".message-input input").val();
		    if ($.trim(message) == '') {
		        return false;
		    }
		    $('.message-input input').val(null);
		    $('.contact.active .preview').html('<span>You: </span>' + message);

		    newMessage(message);
		});

		$(window).on('keydown', function(e) {
		    if (e.which == 13) {
		        newMessage();
		        return false;
		    }
		});*/

		//# sourceURL=pen.js
	</script>
</body>
</html>