package org.qucell.chat.service;


import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.model.user.Users;


public interface LoginService {
	public Users login(LoginVO vo);
	DefaultRes signUp(LoginVO vo);
	
}
