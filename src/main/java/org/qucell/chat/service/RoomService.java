package org.qucell.chat.service;



import java.util.Map;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.room.RoomVO;

import io.netty.channel.Channel;

public interface RoomService {
	
	DefaultRes insertChatRoom(int userId, RoomVO vo);
	DefaultRes getAllRooms(String jwt);
	DefaultRes getUserRooms(int userId);
	DefaultRes editPassword(int userId, RoomVO vo);
	DefaultRes addFriends(int userId,RoomVO vo);
	DefaultRes getUserList(String roomName);
	void enter(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) throws Exception;
	void exit(Channel channel, String method,  Map<String, Object> result) throws Exception;
	void send(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) throws Exception;
	
}
