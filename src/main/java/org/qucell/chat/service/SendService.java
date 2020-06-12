package org.qucell.chat.service;

import java.util.Map;

import org.qucell.chat.model.Repo.ChannelIdUserIdRepository;
import org.qucell.chat.model.Repo.UserIdChannelRepository;
import org.qucell.chat.model.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.Channel;

@Service
public class SendService {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private ChannelIdUserIdRepository channelIdUserIdRepository;
	
	@Autowired
	private UserIdChannelRepository userIdChannelRepository;
	
	@Autowired
	private RedisService redisService;
	
	
	/**
	 * 모든 접속된 사용자에게 메세지 전송
	 *
	 * @param channel Netty 채널
	 * @param method  data method
	 * @param data    전송받은 데이터
	 * @param result  전송할 데이터
	 * @throws Exception 예외
	 */
	public void send(Channel channel, String method, Map<String, Object> data, Map<String, Object> result) throws Exception {
		
		String key="id:"+channel.id()+":userId";
		int userId = (Integer)redisService.getValue(key);
		key = "id:"+userId;
		Users user = (Users)redisService.getValue(key);
		
		result.put("method", method);
		result.put("userId", userId);
		result.put("userName", user.getUserName());
		result.put("content", data.get("content"));
		
		String resultMessage = objectMapper.writeValueAsString(result);
		
		userIdChannelRepository.writeAndFlush(resultMessage);
	}
}
