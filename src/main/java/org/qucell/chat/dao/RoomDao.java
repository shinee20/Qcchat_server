package org.qucell.chat.dao;

import java.util.List;

import org.qucell.chat.model.room.RoomDto;
import org.qucell.chat.model.room.RoomVO;
import org.qucell.chat.model.room.Rooms;
import org.qucell.chat.model.user.Users;

public interface RoomDao {
	
	int insertRoom(RoomVO vo);
	void insertUsersAtRoom(RoomVO vo);
	List<Rooms> selectAllRooms(); 
	List<Rooms> selectAllRooms(int userId); 
	List<Rooms> selectUserRooms(int userId);
	int updateRoomPassword(RoomVO vo);
	int getRoomId(String roomName);
	List<Users> getUsersAtRoom(int roomId);
	
	/**
	 * updated 20/06/15
	 * @param room
	 */
	void exitRoom(RoomDto dto);
	void deleteRoom(String roomName);
	Rooms findChatRoomByRoomName(String roomName);
	/**
	 * updated 20/06/15
	 * @param room
	 */
}
