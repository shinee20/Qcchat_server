//package org.qucell.chat.deprecated;
//
//import java.util.List;
//
//import org.qucell.chat.model.user.Users;
//
//public interface RoomDao_deprecated {
//	
//	int insertRoom(RoomVO_deprecated vo);
//	void insertUsersAtRoom(RoomVO_deprecated vo);
//	List<Rooms_deprecated> selectAllRooms(); 
//	List<Rooms_deprecated> selectAllRooms(int userId); 
//	List<Rooms_deprecated> selectUserRooms(int userId);
//	int updateRoomPassword(RoomVO_deprecated vo);
//	int getRoomId(String roomName);
//	List<Users> getUsersAtRoom(int roomId);
//	
//	/**
//	 * updated 20/06/15
//	 * @author myseo
//	 */
//	void exitRoom(RoomDto_deprecated dto);
//	void deleteRoom(String roomName);
//	Rooms_deprecated findChatRoomByRoomName(String roomName);
//	/**
//	 * updated 20/06/15
//	 * @author myseo
//	 */
//}
