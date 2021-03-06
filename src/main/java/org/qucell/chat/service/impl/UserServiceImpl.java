package org.qucell.chat.service.impl;

import java.util.List;

import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.user.UserAndFriend;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.service.JwtService;
import org.qucell.chat.service.RedisService;
import org.qucell.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RedisService redisService;

	@Autowired
	private JwtService jwtService;

	/**
	 * 
	 * get user info - set caching db
	 */
	@Override
	public Users getByUserId(String jwt) {

		/**
		 * jwt decoding
		 */

		if (jwt == null || jwt == "") {
			// there is no token
			return null;
		} else {
			final JwtService.Token token = jwtService.decode(jwt);
			int userId = token.getUser_id();

			if (userId < 1) {
				return null;
			}
			// get User from redis cache memory
			String key = "id:" + userId;
			Users user = (Users) redisService.getValue(key);
			if (user == null) {
				// GET USER INFO FROM DB
				log.info("redisService does not have user info");
				user = userDao.getByUserId(userId);
			}
			// get User from redis cache memory
			if (user != null) {
				log.info("success search " + user.getUserName());
				return user;
			}
			log.info("no user with " + user.getUserName());
			return null;

		}

	}


	@Override
	public Users getUserByName(String userName) {
		log.info("find user by name! {}", userName);
		return userDao.getByUserName(userName);
	}
	// 사용자의 친구 리스트를 찾는다.
	@Override
	public List<Users> getAllFriendsList(int userId) {

		// cache에서 userId를 찾아서 mapper에게 전달해야 한다.
		//		String key = "id:"+userId+":friends";
		//		List<Users> friendsList = redisService.getValueList(key);
		//		if (friendsList.isEmpty()) {
		//			friendsList= userDao.getFriendsList(userId);
		//			log.info(friendsList.toString());
		//			if (friendsList.isEmpty()) {
		//				return null;
		//			}
		//			
		//			for (Users user : friendsList) {
		//				redisService.saveValueList(key, user);
		//			}
		//		}
		List<Users> friendsList = userDao.getFriendsList(userId);
		return friendsList;

	}


	@Override
	public void addUserAsFriend(String userName, String friendName) {
		Users me = getUserByName(userName);
		Users friend = getUserByName(friendName);
		
		userDao.addFriend(new UserAndFriend(me.getUserId(), friend.getUserId()));
		
	}
}
