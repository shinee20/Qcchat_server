package org.qucell.chat.model.room;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomVO {
	/**
	 * infomation of chatroom accepted from user
	 */
	private int roomId;
	private String roomName;
	private String roomPw;
	private int roomOwner;
	private List<Integer> userList; //include owner also
	
	public RoomVO(int roomId, int roomOwner, String roomName) {
		//create room by owner;
		this.roomOwner = roomOwner;
		this.roomName= roomName;
	}
	public RoomVO(int roomOwner, String roomName, String roomPw) {
		//create room by owner;
		this.roomOwner = roomOwner;
		this.roomName= roomName;
		this.roomPw = roomPw;
	}
	
	public RoomVO(int roomId, String roomName, List<Integer> userList) {
		//create room by owner;
		this.userList = userList;
		this.roomName= roomName;
	}
	public RoomVO(int roomId, String roomName) {
		//create room by owner;
		this.roomName= roomName;
	}
	
	public RoomVO(int roomId, String roomName, String roomPw, List<Integer> userList) {
		//create room by owner;
		this.roomName= roomName;
		this.roomPw = roomPw;
		this.userList = userList;
	}
}
