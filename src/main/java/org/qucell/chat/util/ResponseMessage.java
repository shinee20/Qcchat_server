package org.qucell.chat.util;

public class ResponseMessage {
	public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

	public static final String DB_ERROR = "데이터베이스 에러";
	public static final String DB_UPDATE_IS_ZERO = "데이터베이스 변경 Row가 없음";
	public static final String DB_UPDATE_IS_NOT_ONE = "데이터베이스 변경 Row가 1개가 아님";
	
	public static final String NOT_FOUND_USER = "유저 결과 없음";
	public static final String READ_USER_INFO = "유저 검색 성공";
	public static final String READ_ALL_USERS = "모든 유저 검색 성공";
			
	public static final String LOGIN = "로그인 성공";
	public static final String LOGOUT = "로그아웃 성공";
	public static final String JOIN = "회원 가입 성공";
}
