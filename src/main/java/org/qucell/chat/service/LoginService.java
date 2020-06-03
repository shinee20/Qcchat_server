package org.qucell.chat.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.Users;

public interface LoginService {
	Users login(String userName) throws IOException;
	
	
}
