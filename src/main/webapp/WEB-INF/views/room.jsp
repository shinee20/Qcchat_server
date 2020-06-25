<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class=''>
<head>

<meta charset='UTF-8'>
<meta name="robots" content="noindex">
<link rel="shortcut icon" type="image/x-icon"
	href="//production-assets.codepen.io/assets/favicon/favicon-8ea04875e70c4b0bb41da869e81236e54394d63638a1ef12fa558a4a835f1164.ico" />
<link rel="mask-icon" type=""
	href="//production-assets.codepen.io/assets/favicon/logo-pin-f2d2b6d2c61838f7e76325261b7195c27224080bc099486ddd6dccb469b8e8e6.svg"
	color="#111" />
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
	<!-- 

A concept for a chat interface. 

Try writing a new message! :)


Follow me here:
Twitter: https://twitter.com/thatguyemil
Codepen: https://codepen.io/emilcarlsson/
Website: http://emilcarlsson.se/

-->

	<div id="frame">
		<div id="sidepanel">
			<div id="profile">
				<div class="wrap">
					<img id="profile-img"
						src="http://emilcarlsson.se/assets/mikeross.png" class="online"
						alt="" /> &nbsp; ID:<span id="myId"></span> (<span id="myName"></span>)
					<button type="button" class="btn btn-secondary btn-sm"
						onclick="startWebsocket();" aria-hidden="true">IN</button>
					<button type="button" class="btn btn-secondary btn-sm"
						onclick="disconnect();" aria-hidden="true">OUT</button>

					<i class="fa fa-chevron-down expand-button" aria-hidden="true"></i>

					<div id="status-options">
						<ul>
							<li id="status-online" class="active"><span
								class="status-circle"></span>
								<p>Online</p></li>
							<li id="status-away"><span class="status-circle"></span>
								<p>Away</p></li>
							<li id="status-busy"><span class="status-circle"></span>
								<p>Busy</p></li>
							<li id="status-offline"><span class="status-circle"></span>
								<p>Offline</p></li>
						</ul>
					</div>
					<div id="expanded">
						<label for="twitter"><i class="fa fa-facebook fa-fw"
							aria-hidden="true"></i></label> <input name="twitter" type="text"
							value="mikeross" /> <label for="twitter"><i
							class="fa fa-twitter fa-fw" aria-hidden="true"></i></label> <input
							name="twitter" type="text" value="ross81" /> <label
							for="twitter"><i class="fa fa-instagram fa-fw"
							aria-hidden="true"></i></label> <input name="twitter" type="text"
							value="mike.ross" />
					</div>
				</div>
			</div>
			<div id="search">
				<label for=""><i class="fa fa-search" aria-hidden="true"></i></label>
				<input type="text" placeholder="Search contacts..." />
			</div>
			<div id="contacts">
				<ul>
					<li class="contact">
						<div class="wrap">
							<span class="contact-status online"></span> <img
								src="http://emilcarlsson.se/assets/louislitt.png" alt="" />
							<div class="meta">
								<p class="name">Louis Litt</p>
								<p class="preview">You just got LITT up, Mike.</p>
							</div>
						</div>
					</li>
					<li class="contact active">
						<div class="wrap">
							<span class="contact-status busy"></span> <img
								src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
							<div class="meta">
								<p class="name">Harvey Specter</p>
								<p class="preview">Wrong. You take the gun, or you pull out
									a bigger one. Or, you call their bluff. Or, you do any one of a
									hundred and forty six other things.</p>
							</div>
						</div>
					</li>
					
				</ul>
			</div>
			<div id="bottom-bar">
				<button id="addcontact" onclick="createRoom('1');">
					<i class="fa fa-user-plus fa-fw" aria-hidden="true"></i> <span>채팅방
						추가</span>
				</button>
				<button id="settings">
					<i class="fa fa-cog fa-fw" aria-hidden="true"></i> <span>Settings</span>
				</button>
			</div>
		</div>
		<div class="content">
			<div class="contact-profile">
				<img src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
				<span id="chat-name">
					<h1 class="font-name"></h1>
				</span>
				<div class="social-media">
					<button type="button" class="btn btn-success" onclick="exitFromRoom('1');" >나가기</button>
					<i class="fa fa-bars fa-lg" aria-hidden="true"
						id="show-contact-information"></i> <i class="fa fa-times fa-lg"
						aria-hidden="true" id="close-contact-information"></i>
				</div>
			</div>
			<div class="messages">
				<ul>
					<li class="sent"><img
						src="http://emilcarlsson.se/assets/mikeross.png" alt="" />
						<p>How the hell am I supposed to get a jury to believe you
							when I am not even sure that I do?!</p></li>
					<li class="replies"><img
						src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
						<p>When you're backed against the wall, break the god damn
							thing down.</p></li>
					<li class="replies"><img
						src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
						<p>Excuses don't win championships.</p></li>
					<li class="sent"><img
						src="http://emilcarlsson.se/assets/mikeross.png" alt="" />
						<p>Oh yeah, did Michael Jordan tell you that?</p></li>
					<li class="replies"><img
						src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
						<p>No, I told him that.</p></li>
					<li class="replies"><img
						src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
						<p>What are your choices when someone puts a gun to your head?</p>
					</li>
					<li class="sent"><img
						src="http://emilcarlsson.se/assets/mikeross.png" alt="" />
						<p>What are you talking about? You do what they say or they
							shoot you.</p></li>
					<li class="replies"><img
						src="http://emilcarlsson.se/assets/harveyspecter.png" alt="" />
						<p>Wrong. You take the gun, or you pull out a bigger one. Or,
							you call their bluff. Or, you do any one of a hundred and forty
							six other things.</p></li>
				</ul>
			</div>
			<div class="message-input">
				<div class="wrap">
					<input type="text" placeholder="Write your message..." onkeyup="onMsgKeyUp(event, '4');" id="input-msg"/> <i
						class="fa fa-paperclip attachment" aria-hidden="true"></i>
					<button class="submit">
						<i class="fa fa-paper-plane" aria-hidden="true"></i>
					</button>
				</div>
			</div>
		</div>
	</div>
	<script
		src='//production-assets.codepen.io/assets/common/stopExecutionOnTimeout-b2a7b3fe212eaa732349046d8416e00a9dec26eb7fd347590fbced3ab38af52e.js'></script>
	<script src='https://code.jquery.com/jquery-2.2.4.min.js'></script>
	<script>
		var websocket;
		var myId;
		var roomInfo = {};
		function startWebsocket() {
			var name = prompt("이름을 입력하세요");
			if (name == null || name == "") {
				alert("다시 입력하세요");
				return;
			}

			websocket = new WebSocket("ws://${host}:${websocketPort}/websocket");

			websocket.onerror = function(event) {
				console.log("== error", event);
				alert("== Error : " + event.code + " , " + event.reason);
			};

			websocket.onopen = function(event) {
				console.log("== onopen");
				alert("websocket 연결됨");
				login(name);
			};

			websocket.onclose = function(event) {
				console.log("== websocket closeed");
				alert("websocket 종료");
				initDisplay();
				$("#myId").text("");
				$("#allUsers").empty();
				websocket = null;
			};

			websocket.onmessage = function(event) {
				console.log(event.data);
				var obj = JSON.parse(event.data);
				var action = obj.action;
				if (action == 'LoginConfirmed') {
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					console.log("== myId : " + refId);
					myId = refId;
					$("#myId").text(myId);
					$("#myName").text(refName);
				} else if (action == 'AllUserList') {
					var refId = getFromHeader(obj, "refId");
					var arr = JSON.parse(obj.msg);
					var target = $("#allUsers");
					target.html("");
					$.each(arr, function(idx, elem) {
						append2(target, elem.name + "(" + elem.id + ")"
								+ (myId == elem.id ? " => 나" : ""), elem.id);
					});
				} else if (action == 'FriendsList') {
					var refId = getFromHeader(obj, "refId");
					var arr = JSON.parse(obj.msg);
					var target = $("#friends");
					target.html("");
					$.each(arr, function(idx, elem) {
						append2(target, elem.name + "(" + elem.id + ")"
								+ (myId == elem.id ? " => 나" : ""), elem.id);
					});
				} else if (action == 'RoomList') {
					var arr = JSON.parse(obj.msg);
					var target = $("#contacts ul");
					target.html("");
					$.each(arr, function(idx, elem) {
						append2(target, elem.name, elem.id);
					});
				} else if (action == 'UserList') {
					var arr = JSON.parse(obj.msg);
					var roomId = getFromHeader(obj, "roomId");
					var idx = getIdxFromRoomname(roomId);
					var target = $("#roomUsers_" + idx);
					target.html("");
					$.each(arr, function(idx, elem) {
						append(target, elem.name + "(" + elem.id + ")"
								+ (myId == elem.id ? " => 나" : ""));
					});
				} else if (action == 'EnterToRoom') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var idx = getIdxFromRoomname(roomId);
					if (refId == myId) {
						$("#room_" + idx).css("color", "blue");
						append($("#contents_" + idx), "방으로 들어왔음.");
						
					} else {
						append($("#contents_" + idx), refName + "(" + refId
								+ ")이 방으로 들어왔습니다.");
					}
				
				} else if (action == 'ExitFromRoom') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var idx = getIdxFromRoomname(roomId);
					if (refId == myId) {
						$("#roomUsers_" + idx).empty();
					} else {
						append($("#contents_" + idx), refName + "(" + refId
								+ ")이 방에서 나갔습니다.");
					}
					
				} else if (action == 'SendMsg') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var idx = getIdxFromRoomname(roomId);
					append($("#contents_" + idx), (myId == refId ? "<나> "
							: refName + "(" + refId + ")" + " : ")
							+ obj.msg);
				} else if (action == 'LogIn') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var target = $("#allUsers");
					if (target.find("[hong-etc='" + refId + "']").length == 0) {
						append2(target, refName + "(" + refId + ")", refId);
					}
				} else if (action == 'LogOut') {
					var roomId = getFromHeader(obj, "roomId");
					var refId = getFromHeader(obj, "refId");
					var refName = getFromHeader(obj, "refName");
					var target = $("#allUsers");
					target.find("[hong-etc='" + refId + "']").remove();
				}
			};
		}

		function onMsgKeyUp(event, idx) {
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

		function getFromHeader(obj, key) {
			var headers = obj.headers;
			if (headers != null) {
				return headers[key];
			} else {
				return null;
			}
		}

		function login(name) {
			var obj = new Builder().action("LogIn").header("name", name)
					.finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function disconnect() {
			websocket.close();
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

		function createRoom(idx) {
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
			$("#chat-name").text(roomName);
			var obj = new Builder().action("CreateRoom").header("roomId",
					roomName).finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
		}

		function exitFromRoom(idx) {
			var roomId = $("#chat-name").text();
			var obj = new Builder().action("ExitFromRoom").header("roomId",
					roomId).finish();
			var jsonStr = JSON.stringify(obj);
			websocket.send(jsonStr);
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

		//채팅방 대화를 보내면 최근 대화 목록 마지막에 이어붙인다.
		function append($div, msg, etcVal) {
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
				scrollTop : 100000
			});
		}

		//채팅방 리스트 목록 (target, room name, room Id)
		function append2($div, msg, etcVal) {
			var newElem = $("<li class='contact' id='"+msg +"'><div class='wrap'><span class='contact-status busy'></span><img src='http://emilcarlsson.se/assets/mikeross.png' alt='' /><div class='meta'><p class='name'>"+msg+"</p></div></div></li>")
			
			newElem.appendTo($div);
			$div.animate({
				scrollTop : 100000
			});
		}

		function onClick_room(name) {
			if (confirm(name + " 방에 들어가시겠습니까?")) {
				var obj = new Builder().action("EnterToRoom").header("roomId",
						name).finish();
				var jsonStr = JSON.stringify(obj);
				websocket.send(jsonStr);
			}
		}

		$(".messages").animate({
			scrollTop : $(document).height()
		}, "fast");

		$("#profile-img").click(function() {
			$("#status-options").toggleClass("active");
		});

		$(".expand-button").click(function() {
			$("#profile").toggleClass("expanded");
			$("#contacts").toggleClass("expanded");
		});

		$("#status-options ul li").click(function() {
			$("#profile-img").removeClass();
			$("#status-online").removeClass("active");
			$("#status-away").removeClass("active");
			$("#status-busy").removeClass("active");
			$("#status-offline").removeClass("active");
			$(this).addClass("active");

			if ($("#status-online").hasClass("active")) {
				$("#profile-img").addClass("online");
			} else if ($("#status-away").hasClass("active")) {
				$("#profile-img").addClass("away");
			} else if ($("#status-busy").hasClass("active")) {
				$("#profile-img").addClass("busy");
			} else if ($("#status-offline").hasClass("active")) {
				$("#profile-img").addClass("offline");
			} else {
				$("#profile-img").removeClass();
			}
			;

			$("#status-options").removeClass("active");
		});

		function newMessage() {
			message = $(".message-input input").val();
			if ($.trim(message) == '') {
				return false;
			}
			$('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>'
							+ message + '</p></li>')
					.appendTo($('.messages ul'));
			$('.message-input input').val(null);
			$('.contact.active .preview').html('<span>You: </span>' + message);
			$(".messages").animate({
				scrollTop : $(document).height()
			}, "fast");
		};

		$('.submit').click(function() {
			newMessage();
		});

		$(window).on('keydown', function(e) {
			if (e.which == 13) {
				newMessage();
				return false;
			}
		});
		//# sourceURL=pen.js
	</script>
</body>
</html>