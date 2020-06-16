package org.qucell.chat.netty.server.common.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.room.Room;
import org.qucell.chat.netty.server.common.ChannelSendHelper;
import org.qucell.chat.util.ResponseMessage;
import org.qucell.chat.util.StatusCode;

import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/16
 * @author myseo
 */
@Slf4j
public class ClientAdapter implements ClientEventListener{
	/**
	 * client event listener를 받아서 처리한다.
	 * 관리 대상 : client_rooms, rooms
	 */

	//singleton
	private ClientAdapter() {
		startMonitorThread(); //create only once;
	}
	
	//key : client value : rooms
	private final ConcurrentHashMap<Client, List<Room>> CLIENT_TO_ROOMS = new ConcurrentHashMap<>();
	public static final ClientAdapter INSTANCE = new ClientAdapter();
	private final CopyOnWriteArrayList<Room> ROOMS = new CopyOnWriteArrayList<>();
	
	public ClientAdapter login(Client client) {
		//로그인할 때의 이벤트 
		ChannelSendHelper.writeAndFlushToClient(client, new JsonMsgRes.Builder(client).setAction(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS)).build());
		CLIENT_TO_ROOMS.put(client, new ArrayList<>(2));
		client.getChannel().closeFuture().addListener(listener->logout(client));
//		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS).build());
//		ChannelSendHelper.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()),  entity);

		
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
		
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(DefaultRes.res(StatusCode.OK, ResponseMessage.LOGOUT)).build();
		ChannelSendHelper.writeAndFlushToClients(Collections.list(CLIENT_TO_ROOMS.keys()), entity);
	
		return this;
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
		
	}
	
	private void run(Runnable r) {
		try {
			r.run();
		} catch(Throwable e) {
			log.error(e.getMessage(), e);
		}
	}
	
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
