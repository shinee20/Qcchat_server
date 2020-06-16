package org.qucell.chat.netty.server.common.client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qucell.chat.model.user.Users;
import org.qucell.chat.netty.server.common.AttachHelper;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/15
 * @author myseo
 */
@Slf4j
@Data
public class Client {
	/**
	 * 채널(==socket)의 클라이언트 정보에 관한 것들을 처리한다.
	 */
	private LocalDateTime createdTime = LocalDateTime.now();
	private Users user;
	private final Channel channel;
	private List<String> roomList = new ArrayList<>();
	
	public Client(Users user, Channel channel) {
		
		super();
		this.user = user;
		this.channel = channel;
	}
	
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	
	public synchronized Client addRoom(String roomId){
		boolean match = roomList.stream().anyMatch(r->r.equals(roomId));
		
		if(!match){
			roomList.add(roomId);
		}
		
		return this;
	}
	
	public synchronized Client removeRoom(String roomId) {
		roomList.removeIf(s->s.equals(roomId));
		return this;
	}
	public synchronized Client removeAllRooms(){
		this.roomList.clear();
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
		if (roomList.size() != rooms.size()) {
			log.error("=== validateRoom ERROR : client room : {}, cooordiante room : {}", roomList, rooms);
		}
		
		return this;
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("id", user.getUserId()+"");
		map.put("name", user.getUserName());
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
		Client other = (Client) obj;
		if (user.getUserName() == null) {
			if (other.user.getUserName() != null)
				return false;
		} else if (!user.getUserName().equals(other.user.getUserName()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user.getUserName()==null)? 0 : user.getUserName().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Client [id=" + user.getUserId() + ", name=" + user.getUserName() + ", channel=" + channel
				+ ", roomNameList=" + roomList.toString() + "]";
	}
	
	
}
