package org.qucell.chat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.qucell.chat.dao.RoomDao;
import org.qucell.chat.model.room.RoomDto;
import org.qucell.chat.model.room.RoomVO;
import org.qucell.chat.model.room.Rooms;
import org.qucell.chat.model.user.Users;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

	@Inject
	private SqlSession sqlSession;

	private static String namespace="org.qucell.chat.mapper.roomMapper";

	/**
	 * create chat room with user list
	 */
	@Override
	public int insertRoom(RoomVO vo) {
		return sqlSession.insert(namespace +".insertChatRoom", vo);
	}

	public void insertUsersAtRoom(RoomVO vo) {
		List<RoomDto> dto = new ArrayList<>();

		for (int i = 0; i < vo.getUserList().size(); i++) {
			dto.add(new RoomDto(vo.getRoomId(), vo.getUserList().get(i)));
		}
		sqlSession.insert(namespace +".insertUsersAtRoom", dto);

	}
	
	//모든 채팅방 불러오기 
	@Override
	public List<Rooms> selectAllRooms() {
		return sqlSession.selectList(namespace+".selectAllRooms");
	}
	//사용자가 참여한 채팅방을 제외한 모든 채팅방 불러오기
	@Override
	public List<Rooms> selectAllRooms(int userId){
		return sqlSession.selectList(namespace+".selectAllRoomsWithoutAlreadyJoin", userId);
	}

	//사용자가 참여한 채팅방 조회
	@Override
	public List<Rooms> selectUserRooms(int userId){
		return sqlSession.selectList(namespace + ".selectUserRooms",userId );
	}

	//채팅방 비밀번호 변경
	@Override
	public int updateRoomPassword(RoomVO vo) {
		return sqlSession.update(namespace+".updateRoomPw",vo );
		
	}

	@Override
	public int getRoomId(String roomName) {
		return sqlSession.selectOne(namespace+".getRoomId", roomName);
	}

	//채팅방 참여 인원의 정보 조회
	@Override
	public List<Users> getUsersAtRoom(int roomId) {
		return sqlSession.selectList(namespace+".getUsersAtRoom", roomId);
	}

	@Override
	public void deleteRoom(String roomName) {
		sqlSession.delete(namespace+".deleteRoomByRoomName", roomName);
		
	}

	@Override
	public void exitRoom(RoomDto dto) {
		sqlSession.delete(namespace+".deleteUserFromRoom", dto);
	}

	@Override
	public Rooms findChatRoomByRoomName(String roomName) {
		return sqlSession.selectOne(namespace+".selectChatRoomByRoomName",roomName );
	}
	
	
}
