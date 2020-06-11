package org.qucell.chat.dao;

import java.util.List;

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
}
