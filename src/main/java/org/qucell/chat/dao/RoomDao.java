package org.qucell.chat.dao;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.model.RoomVO;
import org.qucell.chat.model.Rooms;

public interface RoomDao {
	void insertRoom(RoomVO vo) throws IOException;
	List<Rooms> selectAllRooms() throws IOException; 
	List<Rooms> selectAllRooms(int userId) throws IOException; 
	List<Rooms> selectUserRooms(int userId) throws IOException;
}
