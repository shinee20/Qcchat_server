package org.qucell.chat.model.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Users {
//user dto 
	private int userId;
	private String userName;
	private Date regDate;
	private Date updateDate;
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("id", String.valueOf(userId));
		map.put("name", userName);
		return map;
	}
}
