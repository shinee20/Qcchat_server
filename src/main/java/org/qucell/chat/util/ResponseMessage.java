package org.qucell.chat.util;

public class ResponseMessage {
	
	//sign up
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String ALREADY_EXIST_NAME = "이미 존재하는 이름 입니다.";
    public static final String TOO_LONG_PASSWORD ="비밀번호의 길이는 최대 10입니다.";
    
    //login
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String LOGOUT = "로그아웃";
    public static final String WRONG_PASSWORD = "패스워드 틀림";
    
    //auth
    public static final String WRONG_AUTHORIZATION = "권한 없음";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String DB_ERROR = "데이터베이스 에러";
    public static final String DB_UPDATE_IS_ZERO = "데이터베이스 변경 Row가 없음";
    public static final String DB_UPDATE_IS_NOT_ONE = "데이터베이스 변경 Row가 1개가 아님";

    //main view
    public static final String READ_USER_INFO = "회원 정보 조회 성공";
    public static final String READ_ALL_FRIENDS_LIST = "친구 리스트 조회 성공";
    public static final String READ_CHATROOMS = "채팅방 리스트 조회 성공";
    public static final String READ_ALL_USER_LIST = "모든 유저 조회 성공";
    
    //file
    public static final String UPDATED_USER_PIC = "프로필 사진 변경 성공";
    public static final String DELETED_USER_PIC = "프로필 사진 삭제 성공";
    
    //search
    public static final String SEARCH_SUCCESS = "검색 성공";
    public static final String SEARCH_NO_RESULT = "검색 결과가 없습니다.";
    
    //chat room 
    public static final String CREATE_ROOM_SUCCESS="채팅방 생성 성공";
    public static final String ALREADY_EXIST_ROOM_NAME="이미 존재하는 채팅방 이름입니다.";
    public static final String CHAGE_ROOM_PASSWORD_SUCCESS="채팅방 비밀번호 변경 성공";
    public static final String ADD_FRIENDS_SUCCESS="친구 추가 성공";
    public static final String READ_USER_LIST_AT_HOME="채팅방 참여 유저 조회 성공";
    
    //do chat
    public static final String ENTER_ROOM ="방 입장 성공";
    public static final String EXIT_ROOM = "방 퇴장 성공";
    public static final String SEND_MSG ="메시지 전송 성공";
    public static final String SEND_INFO = "정보 메시지 전송 성공";
    
    //util
    public static final String HEALTH_CHECK = "헬스 체크";
    
}
