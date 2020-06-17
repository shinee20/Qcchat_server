package org.qucell.chat.netty.server.common.repo;

import java.util.concurrent.CopyOnWriteArrayList;

import org.qucell.chat.model.room.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomRepository {
	private final CopyOnWriteArrayList<Room> roomCOWList = new CopyOnWriteArrayList<>();
	
	public CopyOnWriteArrayList<Room> getRoomCOWList()
	{
		return roomCOWList;
	}
}
