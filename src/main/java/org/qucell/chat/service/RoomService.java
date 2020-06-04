package org.qucell.chat.service;

import java.io.IOException;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.RoomVO;

public interface RoomService {
	DefaultRes insertChatRoom(int userId, RoomVO vo);
	DefaultRes getAllRooms();
	DefaultRes getUserRooms(int userId);
}
