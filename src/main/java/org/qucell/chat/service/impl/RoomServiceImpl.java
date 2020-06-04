package org.qucell.chat.service.impl;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.dao.RoomDao;
import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.RoomVO;
import org.qucell.chat.model.Rooms;
import org.qucell.chat.model.Users;
import org.qucell.chat.service.RoomService;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public class RoomServiceImpl implements RoomService{

	@Autowired
	private UserService userService;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private UserDao userDao;

	/*
	 * insert new chat room 
	 * 
	 */
	@Transactional
	@Override
	public DefaultRes insertChatRoom(int userId, RoomVO vo){
		// TODO Auto-generated method stub
		//get user_id with userName
		try {
			vo.setRoomOwner(userId);
			roomDao.insertRoom(vo); //create new chat room
			Users user = userService.getUser(userId); //get User from cache memory
			//update user info (room_id)
			user.setRoomId(vo.getRoomId());
			userDao.updateUser(user);

			return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_ROOM_SUCCESS);
		} catch(IOException e) {
			//ALL ROLLBACK
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

		}
	}

	@Override
	public DefaultRes getAllRooms() {
		// TODO Auto-generated method stub
		try {
			List<Rooms> roomList = roomDao.selectAllRooms();
			return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, roomList);
		}
		catch(IOException e) {
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
		
	}

	@Override
	public DefaultRes getUserRooms(int userId) {
		// TODO Auto-generated method stub
		return null;
	}


}
