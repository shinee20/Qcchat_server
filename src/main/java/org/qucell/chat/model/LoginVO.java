package org.qucell.chat.model;

import lombok.Data;

@Data
public class LoginVO {
	private int userId;
	private String userName;
	
	public LoginVO() {}
	public LoginVO(String userName) {
		this.userName =  userName;
	}
	public LoginVO(int userId, String userName) {
		this.userId = userId;
		this.userName =  userName;
	}
	
}
