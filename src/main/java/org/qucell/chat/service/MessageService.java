package org.qucell.chat.service;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Service
public class MessageService {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${netty.server.type}")
	private String transferType;

	@Autowired
	private SendService sendService;
	
	@Autowired
	private RoomService roomService;

	@Autowired
	private UserService userService;
	/**
	 * 메세지 분기
	 *
	 * @param channel Netty 채널
	 * @param data    전송받은 데이터
	 * @param result  전송할 데이터
	 * @throws Exception
	 */
	public void execute(Channel channel, Map<String, Object> data, Map<String, Object> result) throws Exception {

		String method = (String) data.getOrDefault("method", "");

		switch(method) {
		case "register":
			//register user in channel 
			userService.registUser(channel, method, data, result);
			break;
		case "send" :
			//send message 1:1 chat room
			sendService.send(channel, method, data, result);
			break;

		case "enter_room":
			roomService.enter(channel, method, data, result);
			break;
		case "send_room" :
			//send message broad cast in all of chat room user
			roomService.send(channel, method, data, result);
			break;

		default:
			returnMessage(channel, result,  new Exception("메세지 구분이 정확하지 않습니다."), "1005");
			return;
		}
	}

	/**
	 * 예외 발생 메세지 전송
	 *
	 * @param channel   Netty 채널
	 * @param result    전송할 데이터
	 * @param throwable 예외
	 * @param status    상태코드
	 * @throws Exception 예외
	 */
	public void returnMessage(Channel channel, Map<String, Object> result, Throwable throwable, String status) throws Exception {

		result.put("status", status);
		result.put("message", ExceptionUtils.getStackTrace(throwable));

		channel.writeAndFlush(returnMessage(result));
	}

	/**
	 * 메세지 전송
	 *
	 * @param channel Netty 채널
	 * @param result  전송할 데이터
	 * @param method  data method
	 * @throws Exception 예외
	 */

	public void returnMessage(Channel channel, Map<String, Object> result, String method) {
		result.put("status", 0);
		result.put("method", method);

		try {
			channel.writeAndFlush(returnMessage(result));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	Object returnMessage(Map<String,Object> result) throws Exception {
		switch(transferType) {
		case "websocket" :
			return new TextWebSocketFrame(objectMapper.writeValueAsString(result));

		case "tcp":
		default:
			return objectMapper.writeValueAsString(result) + System.lineSeparator();
		}
	}

	public Object returnMessage(String message) {
		switch(transferType) {
		case "websocket" :
			return new TextWebSocketFrame(message);

		case "tcp":
		default:
			return message + System.lineSeparator();
		}
	}
}
