package org.qucell.chat.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginVO {
	private int userId;
	private String userName;
	private String userPw;
	
	public LoginVO() {}
	public LoginVO(String userName) {
		this.userName =  userName;
	}
	public LoginVO(String userName, String userPw) {

		this.userName =  userName;
		this.userPw = userPw;
	}
	
}
