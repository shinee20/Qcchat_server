package org.qucell.chat.deprecated;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Rooms_deprecated {
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
