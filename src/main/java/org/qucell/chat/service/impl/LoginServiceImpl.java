package org.qucell.chat.service.impl;

import java.io.IOException;

import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.LoginVO;
import org.qucell.chat.model.Users;
import org.qucell.chat.service.LoginService;
import org.qucell.chat.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	@Override
	public Users login(String userName) throws IOException {
		// TODO Auto-generated method stub
		
		Users user = userDao.getByUserName(userName);
		
		if (user == null) {
			//notihing 
			//join service
			LoginVO newUser = new LoginVO(userName);
			userDao.insertUser(newUser);
			LOGGER.info("join " + userName);
			
		}
		user = userDao.getByUserName(userName);
		Constant.userInfoMap.put(user.getUserName(), user);
		LOGGER.info("success login " + userName);
		return user;
	}

}
