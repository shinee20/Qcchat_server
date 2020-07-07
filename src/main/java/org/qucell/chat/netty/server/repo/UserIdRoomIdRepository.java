package org.qucell.chat.netty.server.repo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.qucell.chat.model.room.Room;
import org.springframework.stereotype.Component;

@Component
public class UserIdRoomIdRepository {
	/**
	 * 로그아웃 후에 다시 로그인을 하는 경우 룸의 정보를 그대로 가지고 있어야 한다.
	 */
	
	private static ConcurrentHashMap<String, List<Room>> userIdRoomIdRepository = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<String, List<Room>> getUserIdRoomIdMap() {
		return userIdRoomIdRepository;
	}
	
//	public static void save(String userName) {
//		List<Room> list = userIdRoomIdRepository.get(userName);
//		if (list != null) {
//			String key="id:"+userName+":rooms";
//			for (Room room : list) {
//				redisService.saveValueList(key, room); //정적 메소드에서 비 정적 클래스의 비정적 메소드를 호출할 수 없다. 
//			}
//		}
//	}
}
