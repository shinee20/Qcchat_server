package org.qucell.chat.service;


import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.LoginRes;
import org.qucell.chat.model.user.LoginVO;


public interface LoginService {
//	public Users login(LoginVO vo);
	public DefaultRes<LoginRes> login(LoginVO vo);
	DefaultRes signUp(LoginVO vo);
}
