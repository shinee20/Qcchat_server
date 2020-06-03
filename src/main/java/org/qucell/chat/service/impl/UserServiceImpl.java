package org.qucell.chat.service.impl;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.Users;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public DefaultRes getByUserName(String userName) throws IOException{
		// TODO Auto-generated method stub
		Users user = userDao.getByUserName(userName) ;
		
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_INFO, user);
	}

	/*
	 * redis test
	 */
	@Override
	@Cacheable(cacheNames="userCache")
	public DefaultRes getAllUsers() throws IOException {
		// TODO Auto-generated method stub
		List<Users> users = userDao.getAllUsers();
		
		if (users.isEmpty()) {
			return DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.NOT_FOUND_USER );
		}
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_USERS, users);
	}

	@Caching(evict = {
            @CacheEvict(cacheNames="userCache", allEntries = true)
        })
	@Override
	public void removeCacheUsers() {
		// TODO Auto-generated method stub
		
	}
	/*
	 * redis test
	 */
	
}
