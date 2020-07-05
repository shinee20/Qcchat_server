package org.qucell.chat.service;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.room.Room;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.netty.server.common.client.ClientAdapter;
import org.qucell.chat.netty.server.repo.ChatMessageLogRepository;
import org.qucell.chat.netty.server.repo.ClientIdFriendIdRepository;
import org.qucell.chat.netty.server.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/17
 * @author myseo
 */
@Slf4j
@Service
public class ChatReceiveService {
	
//	private ChatReceiveProcess() {}
//	//singleton
//	public static final ChatReceiveProcess INSTANCE = new ChatReceiveProcess();
	
	@Autowired 
	private ClientIdFriendIdRepository clientIdFriendIdRepository;
	
	@Autowired
	private ChatMessageLogRepository chatMessageLogRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	public void process(Client client, JsonMsgRes entity) {
		EventType eventType = EventType.from(entity);
		ClientAdapter adapter = ClientAdapter.INSTANCE;
		String roomId = entity.extractFromHeader("roomId");
		String userName = entity.extractFromHeader("userName");
		String status = entity.extractFromHeader("status");
		
		switch(eventType) {
		case LogIn:
			adapter.login(client);
			adapter.sendRefreshRoomListToClient(client);
			break;
		case LogOut:
			adapter.logout(client);
			break;
		case FriendsList:
			clientIdFriendIdRepository.writeAndFlush(client);
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
			chatMessageLogRepository.writeAndFlush(client, roomId);
			break;
		case ExitFromRoom:
			adapter.exitRoom(client, roomId);
			break;
		case SendMsg:
			chatMessageLogRepository.save(roomId, entity.msg, client.getName());
			Room room = adapter.getRoomByRoomId(roomId);
			room.sendMsg(client, entity.msg);
			break;
		case MsgLog:
			chatMessageLogRepository.writeAndFlush(client, roomId);
			break;
		case UserRoomList:
			adapter.sendRefreshRoomListToClient(client);
			break;
		case RoomList:
			adapter.sendAllRoomListToClient(client);
			break;
		case FindUserByName:
			
			Users user = userService.getUserByName(userName);
			if (user != null) {
				log.info("find user == {}", user.toString());
			}
			else {
				log.info("find user == null");
			}
			adapter.sendReponseToClient(client, user, EventType.FindUserByName);
			break;
		case AddFriend:
			userService.addUserAsFriend(client.getName(), userName);
			break;
		case ChangeStatus:
			client.setStatus(status);
			break;
		case InviteFriend: 
			/**
			 * 일단 해당 클라이언트가 자신이 초대한 방에 들어가 있는지를 확인해주고
			 * 참여하고 있지 않다면 초대할 수 없다. 
			 * 만약,참여하고 있는 채팅방이라면 친구의 client를 찾아서 room과 연결해줘야 한다. -> 어쩔 수 없이 접속한 사용자만 가능하다. 
			 */
			adapter.inviteFriendToRoom(client, userName, roomId);
			break;
		}
	}
}
