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
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.repo.UserIdRoomIdRepository;
import org.qucell.chat.service.SendService;
import org.qucell.chat.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/16
 * @author myseo
 */
@Slf4j
public class ClientAdapter {
	/**
	 * client event listener를 받아서 처리한다.
	 * 관리 대상 : client_rooms, all rooms list
	 */
	public static final ClientAdapter INSTANCE = new ClientAdapter();
	
	//singleton
	private ClientAdapter() {
	}

	//key : client value : rooms
	private final ConcurrentHashMap<Client, List<Room>> CLIENT_TO_ROOMS = new ConcurrentHashMap<>();
	private final CopyOnWriteArrayList<Room> ROOMS = new CopyOnWriteArrayList<>();	
	/**
	 * 로그인할 때의 이벤트 
	 * @param client
	 * @return
	 */
	public ClientAdapter login(Client client) {
		SendService.writeAndFlushToClient(client, new JsonMsgRes.Builder(client).setAction(EventType.LoginConfirmed).build());
		
		//클라이언트마다 참여하고 있는 방들의 정보를 가지고 있다.
		List<Room> userRoomList = UserIdRoomIdRepository.getUserIdRoomIdMap().get(client.getName());
		if (userRoomList == null) {
			CLIENT_TO_ROOMS.put(client, new ArrayList<>());
		}
		else {
			CLIENT_TO_ROOMS.put(client, userRoomList);
			log.info("client relogin : {} ", userRoomList.toString());
			
			userRoomList.stream().forEach(room->{
				room.enterRoom(client);
			});
		}
		
		client.getChannel().closeFuture().addListener(listener->logout(client));
		
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.LogIn).build();
		
		//클라이언트 리스트를 모든 접속자들에게 broadcast
		SendService.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()), entity);
		return this;
	}

	/**
	 *
	 * 이 방들의 정보를 따로 저장하고 있어야 한다. -> 날라가지 않도록
	 * @param client
	 * @return
	 */
	public ClientAdapter logout(Client client) {
		log.warn("== logout : {}", client);
		if (client == null) return this;
		if (!CLIENT_TO_ROOMS.containsKey(client)) {
			log.warn("== already logout : {}", client);
			return this;
		}

		List<Room> roomsOfClient = CLIENT_TO_ROOMS.get(client);
		CLIENT_TO_ROOMS.remove(client);
		
		/**
		 * 로그아웃 후에 다시 로그인을 하는 경우 룸의 정보를 그대로 가지고 있어야 한다.
		 */
		UserIdRoomIdRepository.getUserIdRoomIdMap().put(client.getName(), roomsOfClient);
		
		//삭제되어서 룸의 개수가 남아있어야 하는 룸의 개수와 일치하는지 확인한다.
//		client.validateRoom(roomsOfClient.stream().map(room->room.getId()).collect(Collectors.toList()));
		client.removeAllRooms();

		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.LogOut).build();
		SendService.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()), entity);

		return this;
	}

	/**
	 * 새로운 룸을 생성하거나, 이미 존재하는 채팅방이라면 참여한다.
	 * @param client
	 * @param roomId
	 * @return
	 */
	public ClientAdapter createRoom(Client client, String roomId) {
		Objects.requireNonNull(client);
		Objects.requireNonNull(roomId);
		//null일 경우 예외를 발생시키지 않도록
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
		SendService.writeAndFlushToClient(client, entity);

		// 방이 만들어졌으므로 방 목록을 보냄
		sendRefreshRoomListToClient(client);
		return this;
	}
	
	/**
	 * 모든 접속자 목록을 화면에 보여준다.
	 * @param client
	 * @return
	 */
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
		SendService.writeAndFlushToClient(client, entity);
		return this;
	}
	
	/**
	 * 모든 채팅방 목록을 화면에 보여지도록 한다.
	 * @param client
	 * @return
	 */
	public ClientAdapter sendAllRoomListToClient(Client client) {
		//로그인하면 현재 접속해있는 모든 채팅방 목록을 조회할 수 있다.
		List<Map<String, String>> list = getAllRoomList().stream().map(room->room.toMap()).collect(Collectors.toList());
		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.RoomList).setContents(jsonStr).build();
		SendService.writeAndFlushToClient(client, entity);
		return this;
	}
	
	/**
	 * 채팅방 목록이 삭제되었을 시에는 모든 접속자들에게 새로운 목록을 보여준다.
	 * @return
	 */
	public ClientAdapter sendAllRoomListToClients() {
		List<Map<String, String>> list = getAllRoomList().stream().map(room->room.toMap()).collect(Collectors.toList());
		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder().setAction(EventType.RoomList).setContents(jsonStr).build();
		SendService.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()), entity);
		return this;
	}
	
	/**
	 * 채팅방이 생성되었을 시에는 생성자에게 새로운 목록을 보여준다.
	 * @return
	 */
	public ClientAdapter sendRefreshRoomListToClient(Client client) {
		List<Map<String, String>> list = getRoomList(client).stream().map(room->room.toMap()).collect(Collectors.toList());
		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder().setAction(EventType.UserRoomList).setContents(jsonStr).build();
		SendService.writeAndFlushToClient(client, entity);
		return this;
	}
	
	/**
	 * 방에 들어간다.
	 * @param client
	 * @param roomId
	 * @return
	 */
	public ClientAdapter enterRoom(Client client, String roomId) {
		Optional<Room> optional = ROOMS.stream().filter(room->room.getId().equals(roomId)).findFirst();
		if (optional.isPresent()) {
			Room room  = optional.get();
			room.enterRoom(client);
		} 
		return this;
	}
	
	/**
	 * 방에서 나온다.
	 * @param client
	 * @param roomId
	 * @return
	 */
	public ClientAdapter exitRoom(Client client, String roomId) {
		Optional<Room> optional = ROOMS.stream().filter(room->room.getId().equals(roomId)).findFirst();
		if (optional.isPresent()) {
			Room room  = optional.get();
			room.exitRoom(client);
			room.sendClientList();
			
			/**
			 * 
			 */
			List<Room> clientRooms = CLIENT_TO_ROOMS.get(client);
			clientRooms.remove(getRoomByRoomId(roomId));
			log.info(clientRooms.toString());
		} 
		return this;
	}
	
	/**
	 * 사람이 한 명도 없거나, 삭제된 채팅방이라면 삭제하고 모든 접속자들에게 알린다.
	 * @param room
	 * @return
	 */
	public ClientAdapter invalidateRoom(Room room) {
		Objects.requireNonNull(room);
		ROOMS.remove(room);
		sendAllRoomListToClients();
		return this;
	}
	
	/**
	 * 모든 채팅방의 목록을 가져온다.
	 * @return
	 */
	public List<Room> getAllRoomList() {
		List<Room> list = ROOMS.stream().collect(Collectors.toList());
		return list;
	}
	/**
	 * 
	 * @param roomIds
	 * @return
	 */
	public List<Room> getRoomList(List<String> roomIds) {
		if (roomIds == null) {
			return Collections.emptyList();
		}

		List<Room> list = ROOMS.stream().filter(room->roomIds.contains(room.getId())).collect(Collectors.toList());
		return list;
	}
	
	/**
	 * 클라이언트가 참여하고 있는 채팅방의 목록을 조회
	 * @param clientId
	 * @return
	 */
	public List<Room> getRoomList(Client client) {
		if (client == null) {
			return Collections.emptyList();
		}
		return CLIENT_TO_ROOMS.get(client);
	}

	/**
	 * 룸이름에 따라 채팅방 정보를 조회한다.
	 * @param roomId
	 * @return
	 */
	public Room getRoomByRoomId(String roomId) {
		//optional => wrapper class 

		Optional<Room> findFirst = ROOMS.stream().filter(room->room.getId().equals(roomId)).findFirst();

		if (findFirst.isPresent()) {
			//get() 메소드를 호출하기 전에 isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한 후 호출하는 것이 좋습니다.
			return findFirst.get();
		}
		else return null;
	}

	/**
	 * 채팅방에 대화를 생성한다.
	 * @param room
	 * @param client
	 * @param msg
	 */
	public void sendMsgInRoom(Room room, Client client, String msg) {
		room.sendMsg(client, msg);
	}

	/**
	 * 이미 로그아웃한 사용자가 가진 채팅방 목록을 지워준다. => 추후 수정
	 * @param client
	 * @return
	 */
	public ClientAdapter invalidateClient(Client client) {
		log.error("== invalid client : {}\n{}" , client);
		/**
		 * 수정해야 할 부분
		 */
		Objects.requireNonNull(client);
		CLIENT_TO_ROOMS.remove(client);
		/**
		 * 수정해야 할 부분
		 */
		return this;
	}


//	private void run(Runnable r) {
//		try {
//			r.run();
//		} catch(Throwable e) {
//			log.error(e.getMessage(), e);
//		}
//	}

//	private void startMonitorThread() {
//		try {
//			new Thread() {
//				@Override
//				public void run() {
//					while(true) {
//						try {
//							log.info("== client list");
//							Collections.list(CLIENT_TO_ROOMS.keys()).stream().forEach(client ->{
//								log.info("{}", client);
//							});
//
//						}
//						catch(Exception e) {
//							log.error(e.getMessage(), e);
//						}
//					}
//				}
//			}.start();
//		} catch(Exception e) {
//			log.error("[...]", e);
//		}
//	}
}
