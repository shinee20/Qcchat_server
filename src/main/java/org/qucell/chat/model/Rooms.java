package org.qucell.chat.model;

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
	private int roomId;
	private String roomName;
	private String roomPw;
	private String userCnt;
	private int roomOwner;
	private Date regDate;
	private Date updateDate;
}
