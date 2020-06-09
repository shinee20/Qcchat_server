package org.qucell.chat.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.qucell.chat.dao.RoomDao;
import org.qucell.chat.model.RoomDto;
import org.qucell.chat.model.RoomVO;
import org.qucell.chat.model.Rooms;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {
	
	@Inject
	private SqlSession sqlSession;
	
	private static String namespace="org.qucell.chat.mapper.roomMapper";
	
	@Override
	public void insertRoom(RoomVO vo) throws IOException {
		//create tmp vo of room member size;
		List<RoomDto> dto = new ArrayList<>();
		
		for (int i = 0; i < vo.getUserList().size(); i++) {
			dto.add(new RoomDto(vo.getRoomId(), (int)(vo.getUserList().get(i))));
		}
		sqlSession.insert(namespace +".insertRoom", dto);
	}
	
	@Override
	public void insertChatRoom(RoomVO vo) throws IOException {
		sqlSession.insert(namespace +".insertChatRoom", vo);
		
	}

	@Override
	public List<Rooms> selectAllRooms() throws IOException {
		return sqlSession.selectList(namespace+".selectAllRooms");
	}
	@Override
	public List<Rooms> selectAllRooms(int userId) throws IOException {
		return sqlSession.selectList(namespace+".selectAllRoomsWithoutAlreadyJoin", userId);
	}

	@Override
	public List<Rooms> selectUserRooms(int userId) throws IOException {
		return sqlSession.selectList(namespace + ".selectUserRooms",userId );
	}
	
}
