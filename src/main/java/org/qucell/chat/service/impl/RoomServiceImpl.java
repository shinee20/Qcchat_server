package org.qucell.chat.service.impl;

import java.util.List;

import org.qucell.chat.dao.RoomDao;
import org.qucell.chat.model.room.RoomDto;
import org.qucell.chat.model.room.RoomVO;
import org.qucell.chat.model.room.Rooms;
import org.qucell.chat.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomDao roomDao;
	/**
	 * 현재 존재하는 모든 채팅방 이름을 불러온다.
	 */
	@Override
	public List<String> getRoomNameList() {
		
		List<Rooms> roomList;
		
		roomList = roomDao.selectAllRooms();
		
		List<String> roomNameList = Lists.transform(roomList, new Function<Rooms, String> (){
			@Override
			public String apply(Rooms rooms) {
				return rooms.getRoomName();
			}
		});
		
		return roomNameList;
	}

	/**
	 * 새로운 채팅방을 추가한다.
	 */
	@Transactional
	@Override
	public void addNewChatRoom(RoomVO vo) {
		
		try {
			roomDao.insertRoom(vo);
			roomDao.insertUsersAtRoom(vo);
			
		} catch(DuplicateKeyException e) { 
			//room name duplicated
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback
		}
		catch(Exception e) {
			//ALL ROLLBACK
			log.info("currentTransactionName : {}", TransactionSynchronizationManager.getCurrentTransactionName());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //Rollback			
		}
	}
	
	/**
	 * 사용자가 채팅방을 나온다. 
	 * 만약 채팅방에 인원이 0이면 자동으로 삭제하게 만들어야 한다.
	 * 만약, 방장이 방을 지우더라도 방은 계속 유지한다.
	 */
	@Override
	public void exitChatRoom(int userId, String roomName) {
		
		Rooms room = roomDao.findChatRoomByRoomName(roomName);
		RoomDto dto = new RoomDto(room.getRoomId(),userId);
		roomDao.exitRoom(dto);
	}
	
	
}
