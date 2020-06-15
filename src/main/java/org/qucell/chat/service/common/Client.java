package org.qucell.chat.service.common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.qucell.chat.model.room.RoomVO;
import org.qucell.chat.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Client {
	
	private LocalDateTime createdTime = LocalDateTime.now();
	private final int id;
	private final String name;
	private final Channel channel;
	private List<String> roomNameList;
	
	@Autowired
	private RoomService roomService;
	
	public Client(int id, String name, Channel channel) {
		
		super();
		this.id = id;
		this.name = name;
		this.channel = channel;
		roomNameList = roomService.getRoomNameList();
		if (roomNameList.isEmpty()) {
			roomNameList = new ArrayList<>();
		}
	}
	
	public LocalDateTime getCreatedTime() {
		
		return createdTime;
	}
	
	public synchronized Client addRoom(String roomName){
		boolean match = roomNameList.stream().anyMatch(r->r.equals(roomName));
		
		if(!match){
			RoomVO vo = new RoomVO(id, roomName);
			roomService.addNewChatRoom(vo);
			roomNameList.add(roomName);
		}
		
		return this;
	}
	
	public synchronized Client removeRoom(String roomName) {
		
		roomService.exitChatRoom(id, roomName);
		roomNameList.removeIf(s->s.equals(roomName));
		
		return this;
	}
	
	public static Client from(ChannelHandlerContext ctx) {
		return AttachHelper.about(ctx).getClient();
	}
	
	public static Client from(Channel channel) {
		return AttachHelper.about(channel).getClient();
	}
	
	public Client attachToChannel(Channel channel) {
		AttachHelper.about(channel).attachUsers(this);
		return this;
	}
	
	public Client validateRoom(List<String> rooms) {
		if (roomNameList.size() != rooms.size()) {
			log.error("=== validateRoom ERROR : client room : {}, cooordiante room : {}", roomNameList, rooms);
		}
		
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name==null)? 0 : name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", channel=" + channel
				+ ", roomNameList=" + roomNameList.toString() + "]";
	}
	
	
}
