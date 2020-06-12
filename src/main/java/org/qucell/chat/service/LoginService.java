package org.qucell.chat.service;


import java.util.Map;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.LoginVO;

import io.netty.channel.Channel;


public interface LoginService {
	DefaultRes<JwtService.TokenRes> login(LoginVO vo);
	DefaultRes signUp(LoginVO vo);
	
}
