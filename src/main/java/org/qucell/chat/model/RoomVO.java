package org.qucell.chat.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoomVO {
	private int roomId;
	private String roomName;
	private int roomOwner;
	private List<Integer> userList;
	

	public RoomVO(int roomId) {
		this.roomId = roomId;
		userList = new ArrayList<Integer>();
	}
	
	public RoomVO(int roomId, int roomOwner, String roomName) {
		//create room by owner;
		userList = new ArrayList<Integer>();
		userList.add(roomOwner);
		this.roomOwner = roomOwner;
		this.roomName= roomName;
	}
	
	public RoomVO(int roomId, String roomName, List<Integer> userList) {
		//create room by owner;
		this.userList = userList;
		roomOwner = userList.get(0);
		this.roomName= roomName;
	}

}
