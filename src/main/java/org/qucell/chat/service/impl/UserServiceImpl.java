package org.qucell.chat.service.impl;

import java.util.List;
import java.util.Map;

import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.service.MessageService;
import org.qucell.chat.service.RedisService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private MessageService messageService;
	/**
	 * 
	 * get user info - set caching db
	 */
	@Override
	public DefaultRes getByUserId(int userId){
		
		//get User from redis cache memory 
		String key = "id:" + userId;
		Users user = (Users)redisService.getValue(key);
		if (user== null) {
			//GET USER INFO FROM DB
			log.info("redisService does not have user info");
			user = userDao.getByUserId(userId); 
		}
		//get User from redis cache memory 
		if (user != null) {
			log.info("success search " + user.getUserName());
			return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_INFO, user);
		}
		log.info("no user with " + user.getUserName());
		return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.SEARCH_NO_RESULT);


	}

	// 사용자의 친구 리스트를 찾는다.
	@Override
	public DefaultRes getAllFriendsList(int userId) {
		// TODO Auto-generated method stub
		// cache에서 userId를 찾아서 mapper에게 전달해야 한다.
		String key = "id:"+userId+":friends";
		List<Users> friendsList = redisService.getValueList(key);
		if (friendsList.isEmpty()) {
			friendsList= userDao.getFriendsList(userId);
			if (friendsList.isEmpty()) {
				return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.INTERNAL_SERVER_ERROR);
			}
			
			for (Users user : friendsList) {
				redisService.saveValueList(key, user);
			}
		}
		
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_FRIENDS_LIST, friendsList);

	}
	@Override
	public void registUser(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) {
		
		/*
		 * save at redis cache
		 */
		String key="id:"+channel.id()+":userId";
		int userId = (Integer)redisService.getValue(key);
		if (((Integer)userId) == null) {
			userId = Integer.parseInt((String)data.get("userId"));
			redisService.saveValue(key,userId);
		}
		
		key = "id:"+userId + ":channel";
		redisService.saveValue(key, channel);
		/*
		 * save at redis cache
		 */
		messageService.returnMessage(channel, result, method);
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
