package org.qucell.chat.service.impl;

import java.util.List;

import org.qucell.chat.dao.RoomDao;
import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.room.RoomVO;
import org.qucell.chat.model.room.Rooms;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.service.JwtService;
import org.qucell.chat.service.RedisService;
import org.qucell.chat.service.RoomService_old;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.qucell.chat.util.auth.AuthAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomServiceImpl_old implements RoomService_old {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RedisService redisService;

	/*
	 * insert new chat room 
	 * 
	 */
	@Transactional
	@Override
	public DefaultRes insertChatRoom(int userId, RoomVO vo){

		try {
			vo.setRoomOwner(userId);
			roomDao.insertRoom(vo); //insert users in room 
			roomDao.insertUsersAtRoom(vo);

			return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_ROOM_SUCCESS);
		}catch(DuplicateKeyException e) { 
			//room name duplicated
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
		catch(Exception e) {
			//ALL ROLLBACK
			log.info("currentTransactionName : {}", TransactionSynchronizationManager.getCurrentTransactionName());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
	}

	@Override
	public DefaultRes getAllRooms(String jwt, String requestURL) {

		/**
		 * parsing request url = http://localhost:10101/
		 */
//		int pos = 0;
//		if (requestURL.startsWith("http://")) {
//			pos = "http://".length();
//		}
//		else  {
//			pos = "https://".length();
//		}
//		
//		int pos2 = requestURL.indexOf(":", pos);
//		if (pos2 < 0) {
//			pos2 = requestURL.length();
//		}
//		
//		int pos3 = requestURL.indexOf("/", pos);
//		if (pos3 < 0) {
//			pos3 = requestURL.length();
//		}
//		
//		pos2 = Math.min(pos2, pos3);
//		
		
		/**
		 * parsing request url
		 */
		
		List<Rooms> roomList;
		if (jwt == null || jwt == "") {
			//there is no token, show all chatting rooms 
			roomList = roomDao.selectAllRooms();
		}
		else {
			//there is token value, show chatting rooms without attending by user join
			final JwtService.Token token = jwtService.decode(jwt);
			int userId = token.getUser_id();

			if (userId < 1) {
				//disabled token
				//return 403
				return AuthAspect.DEFAULT_RES_403;
			}
			roomList = roomDao.selectAllRooms(userId);
		}
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CHATROOMS, roomList);


	}

	@Override
	public DefaultRes getUserRooms(int userId) {

		String key = "id:"+userId+":room";
		List<Rooms> roomList = redisService.getValueList(key);
		if (roomList.isEmpty()) {
			roomList = roomDao.selectUserRooms(userId);
			if (roomList.isEmpty()) {
				return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.SEARCH_NO_RESULT);
			}
			else {
				/*
				 * save at redis cache
				 */
				
				for (Rooms r : roomList) {
					redisService.saveValueList(key, r);
				}
				System.out.println("--------------------redis dao save list--------------------" + redisService.getValueList(key));
				/*
				 * save at redis cache 
				 */
			}
		}
		else
			System.out.println("--------------------redis dao get list--------------------" + redisService.getValueList(key));
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CHATROOMS, roomList);
	}

	@Override
	public DefaultRes editPassword(int userId,RoomVO vo) {
		//오직 방장만이 방의 비밀번호를 변경할 수 있다. 
		//DB와 Redis 모두 정보를 변경해야 한다. 
		
		//DB에서 값을 변경하고 저장한다. 
		vo.setRoomOwner(userId);
		int cnt = roomDao.updateRoomPassword(vo);
		
		System.out.println("@@@@@@@@@update result " + cnt);
		if (cnt == 0) {
			//no authorization
			log.info("no authorization: " + userId);
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_UPDATE_IS_ZERO);
		}
		else if (cnt > 1) {
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_UPDATE_IS_NOT_ONE);
		}
		
		String key = "id:"+userId+":room";
		List<Rooms> roomList = redisService.getValueList(key);
		if (roomList.isEmpty()) {
		}
		else {
			//update
			redisService.deleteValueList(key);
		}
		//변경된 리스트로 저장한다. 
		roomList = roomDao.selectUserRooms(userId);
		/*
		 * save at redis cache
		 */
		
		for (Rooms r : roomList) {
			redisService.saveValueList(key, r);
		}
		System.out.println("--------------------redis dao save list--------------------" + redisService.getValueList(key));
		/*
		 * save at redis cache 
		 */
		return DefaultRes.res(StatusCode.OK, ResponseMessage.CHAGE_ROOM_PASSWORD_SUCCESS);
	}

	/**
	 * add friend[list] at this room 
	 * 
	 */
	@Transactional
	@Override
	public DefaultRes addFriends(int userId, RoomVO vo) {
		try {
			roomDao.insertUsersAtRoom(vo);
			String key = "id:"+vo.getRoomId()+":users";
			
			List<Users> userList = redisService.getValueList(key);
			if (userList.isEmpty()) {
				//create new redis 
				userList = roomDao.getUsersAtRoom(vo.getRoomId());
				
				if (userList.isEmpty()) {
					//there is no one at room -> delete this room automationally
					return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.INTERNAL_SERVER_ERROR);
				}
			}
			else {
				//alternate
				redisService.deleteValueList(key);
			}
			/*
			 * save at redis cache
			 */
			for (Users user : userList) {
				redisService.saveValueList(key, user);
			}	
			log.info("---------redis save userList------" + redisService.getValueList(key));
			/*
			 * save at redis cache
			 */
			return DefaultRes.res(StatusCode.OK, ResponseMessage.ADD_FRIENDS_SUCCESS);
		}
		catch(Exception e) {
			//ALL ROLLBACK
			log.info("currentTransactionName : {}", TransactionSynchronizationManager.getCurrentTransactionName());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
	}

	@Override
	public DefaultRes getUserList(String roomName) {
		//해당 채팅방의 참여 인원 리스트를 가져온다.
		//get roomId with roomName
		int roomId = roomDao.getRoomId(roomName);
		String key = "id:"+roomId+":users";
		
		List<Users> userList = redisService.getValueList(key);
		if (userList.isEmpty()) {
			//create new redis 
			userList = roomDao.getUsersAtRoom(roomId);
			
			if (userList.isEmpty()) {
				//there is no one at room -> delete this room automationally
				return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.INTERNAL_SERVER_ERROR);
			}
			
			/*
			 * save at redis cache
			 */
			for (Users user : userList) {
				redisService.saveValueList(key, user);
			}	
			log.info("---------redis save userList------" + redisService.getValueList(key));
			/*
			 * save at redis cache
			 */
		}
		
		return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_LIST_AT_HOME, userList);
	}
	
}