package org.qucell.chat.service.impl;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.dao.RoomDao;
import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.RoomVO;
import org.qucell.chat.model.Rooms;
import org.qucell.chat.model.Users;
import org.qucell.chat.service.JwtService;
import org.qucell.chat.service.RedisService;
import org.qucell.chat.service.RoomService;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;
import org.qucell.chat.util.auth.AuthAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

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
			roomDao.insertChatRoom(vo); //create new chat room
			roomDao.insertRoom(vo); //insert users in room 
			
			return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATE_ROOM_SUCCESS);
		} catch(IOException e) {
			//ALL ROLLBACK
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
	}

	@Override
	public DefaultRes getAllRooms(String jwt) {
		
		try {
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
			return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, roomList);
		}
		catch(IOException e) {
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
	}

	@Override
	public DefaultRes getUserRooms(int userId) {
		
		try {
			List<Rooms> roomList = roomDao.selectUserRooms(userId);
			/*
			 * save at redis cache
			 */
			
			/*
			 * save at redis cache 
			 */
			return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, roomList);
		}
		catch(IOException e) {
			return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
		}
	}

	@Override
	public DefaultRes editPassword(String jwt, String roomName) {
		//오직 방장만이 방에 비밀번호를 설정하고 수정할 수 있다.
		//redis에 저장되어있는 채팅방 중에 존재한다면 그 방의 정보를 가져오고 
		//없다면 디비에 접근한다.
		
		return null;
	}
}
