package org.qucell.chat.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Message {
	/**
	 * 메시지 로그
	 */
	private String text;
	private String sender;
	private String time;
	private String status;
	
	public Message(String text, String sender, String status) {
		this.text = text;
		this.sender = sender;
		this.status = status;
		this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		
		map.put("text", text);
		map.put("sender", sender);
		map.put("time", time);
		map.put("status", status); 
		
		return map;
	}
}
