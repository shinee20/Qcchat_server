package org.qucell.chat.netty.server.common.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.room.Room;
import org.qucell.chat.netty.server.common.ChannelSendHelper;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.util.JsonUtil;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/16
 * @author myseo
 */
@Slf4j
@Component
public class ClientAdapter implements ClientEventListener{
	/**
	 * client event listener를 받아서 처리한다.
	 * 관리 대상 : client_rooms, rooms
	 */

	//singleton
	private ClientAdapter() {
//		startMonitorThread(); //create only once;
	}

	//key : client value : rooms
	private final ConcurrentHashMap<Client, List<Room>> CLIENT_TO_ROOMS = new ConcurrentHashMap<>();
//	@Autowired
//	private ClientRoomListRepository clientRoomListRepository;
//	
//	@Autowired
//	private RoomRepository roomRepository;
	
	public static final ClientAdapter INSTANCE = new ClientAdapter();
	private final CopyOnWriteArrayList<Room> ROOMS = new CopyOnWriteArrayList<>();	

	public ClientAdapter login(Client client) {
		//로그인할 때의 이벤트 
		ChannelSendHelper.writeAndFlushToClient(client, new JsonMsgRes.Builder(client).setAction(EventType.LoginConfirmed).build());
		CLIENT_TO_ROOMS.put(client, new ArrayList<>());
		client.getChannel().closeFuture().addListener(listener->logout(client));
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.LogIn).build();
		log.info("room list of client has : {}", CLIENT_TO_ROOMS.keys());
		ChannelSendHelper.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()),  entity);

		sendAllClientListToClient(client);
		return this;
	}

	public ClientAdapter logout(Client client) {
		log.warn("== logout : {}", client);
		if (client == null) return this;
		if (!CLIENT_TO_ROOMS.containsKey(client)) {
			log.warn("== already logout : {}", client);
			return this;
		}

		List<Room> roomsOfClient = CLIENT_TO_ROOMS.get(client);
		CLIENT_TO_ROOMS.remove(client);
		//삭제되어서 룸의 개수가 남아있어야 하는 룸의 개수와 일치하는지 확인한다.
		client.validateRoom(roomsOfClient.stream().map(room->room.getId()).collect(Collectors.toList()));
		/**
		 * 수정해야 할 부분
		 */
		client.removeAllRooms();

		roomsOfClient.stream().forEach(room->{
			room.exitRoom(client);
		});

		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.LogOut).build();
		ChannelSendHelper.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()), entity);

		return this;
	}

	public ClientAdapter createRoom(Client client, String roomId) {
		Objects.requireNonNull(client);
		Objects.requireNonNull(roomId);
		Optional<Room> optional  = ROOMS.stream().filter(room->roomId.equals(room.getId())).findFirst();

		Room newRoom;
		if (optional.isPresent()) {
			//이미 같은 이름의 방이 있다
			log.error("== 동일한 ID의 방이 이미 존재함.");
			newRoom = optional.get();
		}
		else {
			//새로 생성한다
			newRoom = new Room(roomId, roomId, this);
			ROOMS.add(newRoom);
		}

		List<Room> clientRooms = CLIENT_TO_ROOMS.get(client);

		if (clientRooms.isEmpty() || clientRooms.size() == 0) {
			//이 클라이언트가 가진 방이 하나도 없을 경우 -> 새로 생성
			List<Room> newRoomList = new ArrayList<>();
			newRoomList.add(newRoom);
			CLIENT_TO_ROOMS.put(client, newRoomList);
			client.addRoom(roomId);
		} else {
			clientRooms.add(newRoom);
			client.addRoom(roomId);
		}

		newRoom.enterRoom(client);

		//자신이 만든 방을 확인시켜준다.
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setHeader("roomId", roomId).setAction(EventType.CreateRoom).build();
		ChannelSendHelper.writeAndFlushToClient(client, entity);

		// 방이 만들어졌으므로 방 목록을 보냄
		sendAllRoomListToClients();
		return this;
	}
	
	public ClientAdapter sendAllClientListToClient(Client client) {
		//로그인하면 현재 접속해있는 모든 클라이언트 목록을 조회할 수 있다.
		List<Map<String, String>> list = Collections.list(CLIENT_TO_ROOMS.keys()).stream().map(cl->{
			Map<String, String> map = new HashMap<>();
			map.put("id", cl.getId()+"");
			map.put("name", cl.getName());
			return map;
		}).collect(Collectors.toList());

		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.AllUserList).setContents(jsonStr).build();
		ChannelSendHelper.writeAndFlushToClient(client, entity);
		return this;
	}

	public ClientAdapter sendAllRoomListToClient(Client client) {
		//로그인하면 현재 접속해있는 모든 채팅방 목록을 조회할 수 있다.
		List<Map<String, String>> list = getAllRoomList().stream().map(room->room.toMap()).collect(Collectors.toList());
		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.RoomList).setContents(jsonStr).build();
		ChannelSendHelper.writeAndFlushToClient(client, entity);
		return this;
	}
	
	public ClientAdapter sendAllRoomListToClients() {
		List<Map<String, String>> list = getAllRoomList().stream().map(room->room.toMap()).collect(Collectors.toList());
		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder().setAction(EventType.RoomList).setContents(jsonStr).build();
		ChannelSendHelper.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()), entity);
		return this;
	}
	
	public ClientAdapter enterRoom(Client client, String roomId) {
		Optional<Room> optional = ROOMS.stream().filter(room->room.getId().equals(roomId)).findFirst();
		if (optional.isPresent()) {
			Room room  = optional.get();
			room.enterRoom(client);
		} 
		return this;
	}
	
	public ClientAdapter exitRoom(Client client, String roomId) {
		Optional<Room> optional = ROOMS.stream().filter(room->room.getId().equals(roomId)).findFirst();
		if (optional.isPresent()) {
			Room room  = optional.get();
			room.exitRoom(client);
			room.sendClientList();
			//모든 클라이언트들에게 방이 사라졌음을 알린다.
		} 
		return this;
	}
	
	public ClientAdapter invalidateRoom(Room room) {
		Objects.requireNonNull(room);
		ROOMS.remove(room);
		sendAllRoomListToClients();
		return this;
	}
	public List<Room> getAllRoomList() {
		List<Room> list = ROOMS.stream().collect(Collectors.toList());
		return list;
	}
	public List<Room> getRoomList(List<String> roomIds) {
		if (roomIds == null) {
			return Collections.emptyList();
		}

		List<Room> list = ROOMS.stream().filter(room->roomIds.contains(room.getId())).collect(Collectors.toList());
		return list;
	}

	public List<Room> getRoomList(String clientId) {
		return CLIENT_TO_ROOMS.get(clientId);
	}

	public Room getRoomByRoomId(String roomId) {
		//optional => wrapper class 

		Optional<Room> findFirst = ROOMS.stream().filter(room->room.getId().equals(roomId)).findFirst();

		if (findFirst.isPresent()) {
			//get() 메소드를 호출하기 전에 isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한 후 호출하는 것이 좋습니다.
			return findFirst.get();
		}
		else return null;
	}

	public void sendMsgInRoom(Room room, Client client, String msg) {
		room.sendMsg(client, msg);
	}

	public ClientAdapter invalidateClient(Client client) {
		log.error("== invalid client : {}\n{}" , client);

		Objects.requireNonNull(client);
		CLIENT_TO_ROOMS.remove(client);
		return this;
	}


	@Override
	public void listen(ClientEvent e) {
		EventType eventType = e.eventType;
	}

//	private void run(Runnable r) {
//		try {
//			r.run();
//		} catch(Throwable e) {
//			log.error(e.getMessage(), e);
//		}
//	}

	private void startMonitorThread() {
		try {
			new Thread() {
				@Override
				public void run() {
					while(true) {
						try {
							log.info("== client list");
							Collections.list(CLIENT_TO_ROOMS.keys()).stream().forEach(client ->{
								log.info("{}", client);
							});

						}
						catch(Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			}.start();
		} catch(Exception e) {
			log.error("[...]", e);
		}
	}
}
