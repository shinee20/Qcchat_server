package org.qucell.chat.netty.server.common.repo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.qucell.chat.model.room.Room;
import org.qucell.chat.netty.server.common.client.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientRoomListRepository {
	private final ConcurrentHashMap<Client, List<Room>> clientRoomListMap = new ConcurrentHashMap<>();
	
	public ConcurrentHashMap<Client, List<Room>> getClientRoomListMap() {
		return clientRoomListMap;
	}
}
