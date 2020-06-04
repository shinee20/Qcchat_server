package org.qucell.chat.service;

import java.io.IOException;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.LoginVO;


public interface LoginService {
	DefaultRes<JwtService.TokenRes> login(LoginVO vo);
	DefaultRes signUp(LoginVO vo);
	
}
