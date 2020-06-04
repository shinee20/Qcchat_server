package org.qucell.chat.service.impl;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.Users;
import org.qucell.chat.service.JwtService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private JwtService jwtService;

	/**
	 * 
	 * get user info - set caching db
	 */
	@Override
	public DefaultRes getByUserId(int userId) throws IOException{
		// TODO Auto-generated method stub

		Users user = getUser(userId);
		if (user != null) {
			log.info("success search " + user.getUserName());
			return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_INFO, user);
		}
		log.info("no user with " + user.getUserName());
		return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.SEARCH_NO_RESULT);


	}

	// 사용자의 친구 리스트를 찾는다.
	@Override
	public DefaultRes getAllFriendsList(int userId) throws IOException {
		// TODO Auto-generated method stub
		// cache에서 userId를 찾아서 mapper에게 전달해야 한다.

		List<Users> friendsList = userDao.getFriendsList(userId);
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_FRIENDS_LIST_SUCCESS, friendsList);

	}

	public Users getUser(int userId) throws IOException {
		Users user = userDao.getByUserId(userId);
		return user;
	}
	/*
	 * redis test
	 * 
	 * @Cacheable(value = "post-single", key = "#id", unless =
	 * "#result.shares < 500")
	 *
	 */
	//	@Override
	//	@Cacheable(cacheNames="userCache") //if it exists in cache memory, take them.
	//	public DefaultRes getAllUsers() throws IOException {
	//		// TODO Auto-generated method stub
	//		List<Users> users = userDao.getAllUsers();
	//		
	//		if (users.isEmpty()) {
	//			return DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.SEARCH_NO_RESULT );
	//		}
	//		return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, users);
	//	}
	//
	//	//@CacheEvict = remove cache
	//	@Caching(evict = {
	//            @CacheEvict(cacheNames="userCache", allEntries = true)
	//        })
	//	@Override
	//	public void removeCacheUsers() {
	//		// TODO Auto-generated method stub
	//		
	//	}
	/*
	 * redis test
	 */

}
