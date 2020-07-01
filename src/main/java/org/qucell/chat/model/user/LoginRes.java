package org.qucell.chat.model.user;

import org.qucell.chat.service.JwtService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRes {
	private JwtService.TokenRes token;
	private String host;
	private String websocketPort;
	
	public LoginRes(String host, String websocketPort) {
		this.host = host;
		this.websocketPort = websocketPort;
	}
} 
