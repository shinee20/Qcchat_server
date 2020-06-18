package org.qucell.chat.model.room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.netty.server.common.ChannelSendHelper;
import org.qucell.chat.netty.server.common.EmptyRoomMgr;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.netty.server.common.client.ClientAdapter;
import org.qucell.chat.util.JsonUtil;

import lombok.Data;

@Data
public class Room {
	private final String id;
	private final String name;
	private final CopyOnWriteArrayList<Client> clientList = new CopyOnWriteArrayList<>();
	private final ClientAdapter clientAdapter;
	
	public Room(String id, String name, ClientAdapter clientAdapter) {
		this.id = id;
		this.name = name;
		this.clientAdapter = clientAdapter;
	}
	
	public Room enterRoom(Client client) {
		//enter
		Objects.requireNonNull(client);
		
		if (client != null && !clientList.contains(client)) {
			synchronized(this) {
				clientList.add(client);
				client.addRoom(this.id);
			}
			
			JsonMsgRes entity = new JsonMsgRes.Builder(client).setRoomId(this.id).setAction(EventType.EnterToRoom).build();
			ChannelSendHelper.writeAndFlushToClients(clientList, entity);
			sendClientList();
		}
		return this;
	}
	
	public Room exitRoom(Client client) {
		Objects.requireNonNull(client);
		
		if (client != null && this.clientList.contains(client)) {
			//현재 접속된 사용자일 경우
			JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.ExitFromRoom).setRoomId(this.id).build();
			ChannelSendHelper.writeAndFlushToClients(clientList, entity);
		}
		
		synchronized(this) {
			clientList.remove(client);
			client.removeRoom(this.getId());
		}
		
		if (this.clientList.size() == 0) {
			//방에 한 명도 없을 경우
			EmptyRoomMgr.INSTANCE.add(this);
		}
		sendClientList();
		return this;
	}
	
	public Room logout(Client client) {
		//로그아웃시 방에서 빠져나간다?
		
		synchronized(this) {
			if (client != null && this.clientList.contains(client)) {
				clientList.remove(client);
				exitRoom(client);
			}
		}
		return this;
	}
	
	public void sendClientList() {
		List<Map<String, String>> list  = clientList.stream().map(client ->client.toMap()).collect(Collectors.toList());
		String jsonStr = JsonUtil.toJsonStr(list);
		JsonMsgRes entity = new JsonMsgRes.Builder().setAction(EventType.UserList).setHeader("roomId", this.id).setContents(jsonStr).build();
		ChannelSendHelper.writeAndFlushToClients(this.clientList, entity);
	}

	public void sendMsg(Client client, String msg) {
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.SendMsg).setHeader("roomId", this.id).setContents(msg).build();
		ChannelSendHelper.writeAndFlushToClients(this.clientList, entity);
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		return map;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", name=" + name + ", clientList=" + clientList
				+ "]";
	}
	
}
