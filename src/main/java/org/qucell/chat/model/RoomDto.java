package org.qucell.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class RoomDto {
	/**
	 * object of using dto
	 */
	private int userId;
	private int roomId;
	
}
