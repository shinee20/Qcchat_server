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
	
}
