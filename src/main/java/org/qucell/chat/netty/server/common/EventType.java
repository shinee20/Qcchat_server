package org.qucell.chat.netty.server.common;

import java.util.Objects;


import org.qucell.chat.model.JsonMsgRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum EventType {
	LoginConfirmed("LoginConfirmed", "로그인 확인됨"),
	LogIn("LogIn", "로그인"),
	LogOut("LogOut", "로그 아웃"),
	CreateRoom("CreateRoom", "방 새로 만들기"),
	EnterToRoom("EnterToRoom", "방 입장"),
	EnterToDefaultRoom("EnterToDefaultRoom", "기본방 입장"),
	ExitFromRoom("ExitFromRoom", "방 퇴장"),
	RoomList("RoomList", "방 목록"),
	SendMsg("SendMsg", "메시지 전송"),
	SendInfo("SendInfo", "에러 메시지 전송"),
	UserList("UserList", "채팅방 참여 유저 목록"),
	AllUserList("AllUserList", "모든 접속 유저 목록"),
	FriendsList("FriendsList", "친구 목록"),
	HealthCheck("HealthCheck", "헬스 체크"),
	Invalid("Invalid", "잘못된 코드"),
	MsgLog("MsgLog", "채팅 대화 로그"),
	UserRoomList("UserRoomList", "참여 채팅방 목록"),
	AlreadyLogin("AlreadyLogin", "이미 로그인된 사용자"),
	FindUserByName("FindUserByName", "친구 찾기"),
	AddFriend("AddFriend", "친구 추가")
	;
	
	public final String code;
	public final String desc;
	
	private EventType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static EventType from(String c) {
		log.info("EventType code : {}", c );
		if (c == null || c.contentEquals("")) {
			//null
			return Invalid;
		}
		
		for (EventType t : EventType.values()) {
			if (c.equals(t.code)) {
				return t;
			}
		}
		throw new IllegalArgumentException("EventType code가 맞지 않음 ("+c+")");
	}
	
	public static EventType from(JsonMsgRes entity) {
		if (entity == null) return Invalid;
		String action = entity.action;
		
		if (action == null || action.equals("")) {
			return Invalid;
		}
		return from(action);
	}
	
	public static void check(String c) {
		Objects.requireNonNull(c, "EventType의 code는 null이면 안됨");
		
		for (EventType t : EventType.values()) {
			if (c.equals(t.code)) {
				return;
			}
		}
		throw new IllegalArgumentException("EventType code가 맞지 않음 ("+c+")");
	}
}
