package org.qucell.chat.netty.server.common;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.room.Room;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.netty.server.common.client.ClientAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/17
 * @author myseo
 */
@Slf4j
public class ChatReceiveProcess {
	
	
	private ChatReceiveProcess() {}
	//singleton
	public static final ChatReceiveProcess INSTANCE = new ChatReceiveProcess();
	
	public void process(Client client, JsonMsgRes entity) {
		EventType eventType = EventType.from(entity);
		ClientAdapter adapter = ClientAdapter.INSTANCE;
		String roomId = entity.extractFromHeader("roomId");
		
		switch(eventType) {
		case LogIn:
			adapter.login(client);
			adapter.sendAllClientListToClient(client);
			adapter.sendAllRoomListToClient(client);
			break;
		case LogOut:
			adapter.logout(client);
			break;
		case AllUserList:
			//현재 접속해있는 모든 클라이언트 목록을 조회할 수 있다.
			adapter.sendAllClientListToClient(client);
			break;
		case CreateRoom:
			adapter.createRoom(client, roomId);
			break;
		case EnterToRoom:
			adapter.enterRoom(client, roomId);
			break;
		case ExitFromRoom:
			adapter.exitRoom(client, roomId);
			break;
		case SendMsg:
			Room room = adapter.getRoomByRoomId(roomId);
			room.sendMsg(client, entity.msg);
			break;
		}
	}
}
