package org.qucell.chat.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoomVO {
	private int id;
	private String roomName;
	private LoginVO owner;
	private List<LoginVO> userList;
	

	public RoomVO(int id) {
		this.id = id;
		userList = new ArrayList<LoginVO>();
	}
	
	public RoomVO(int id, LoginVO owner, String roomName) {
		//create room by owner;
		userList = new ArrayList<LoginVO>();
		userList.add(owner);
		this.owner = owner;
		this.roomName= roomName;
	}
	
	public RoomVO(int id, String roomName, List<LoginVO> userList) {
		//create room by owner;
		this.userList = userList;
		owner = userList.get(0);
		this.roomName= roomName;
	}

}
