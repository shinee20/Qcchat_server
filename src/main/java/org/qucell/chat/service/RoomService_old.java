package org.qucell.chat.service;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.room.RoomVO;


public interface RoomService_old {
	
	DefaultRes insertChatRoom(int userId, RoomVO vo);
	DefaultRes getAllRooms(String jwt, String requestURL);
	DefaultRes getUserRooms(int userId);
	DefaultRes editPassword(int userId, RoomVO vo);
	DefaultRes addFriends(int userId,RoomVO vo);
	DefaultRes getUserList(String roomName);
	
}
