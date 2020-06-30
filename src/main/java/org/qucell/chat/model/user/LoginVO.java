package org.qucell.chat.model.user;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginVO {
	private int userId;
	private String userName;
	private String userPw;
	private String host;
	private String websocketPort;
	
	public LoginVO() {}
	public LoginVO(String userName) {
		this.userName =  userName;
	}
	public LoginVO(String userName, String userPw) {
		this.userName =  userName;
		this.userPw = userPw;
	}
	public LoginVO(String userName, String host, String websocketPort) {
		this.userName =  userName;
		this.host = host;
		this.websocketPort = websocketPort;
	}
	
}
