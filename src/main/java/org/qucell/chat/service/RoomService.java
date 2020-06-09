package org.qucell.chat.service;


import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.RoomVO;

public interface RoomService {
	DefaultRes insertChatRoom(int userId, RoomVO vo);
	DefaultRes getAllRooms(String jwt);
	DefaultRes getUserRooms(int userId);
	DefaultRes editPassword(String jwt, String roomName);
}
