package org.qucell.chat.model.room;

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
	
	private int roomId;
	private int userId;
	
}
