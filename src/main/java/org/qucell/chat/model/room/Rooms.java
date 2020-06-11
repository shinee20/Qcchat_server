package org.qucell.chat.model.room;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Rooms {
	/**
	 * infomation of chatrooom
	 */
	private int roomId;
	private String roomName;
	private String roomPw;
	private int roomOwner;
	private Date regDate;
	private Date updateDate;
	
}
