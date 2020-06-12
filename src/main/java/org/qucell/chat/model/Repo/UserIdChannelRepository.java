package org.qucell.chat.model.Repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.qucell.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

@Component
public class UserIdChannelRepository {
	
	@Autowired
	private MessageService messageService;
	
	private final Map<String, Channel> userIdChannelMap= new ConcurrentHashMap<>();
	
	public Map<String, Channel> getUserIdChannelMap() {
		return userIdChannelMap;
	}
	
	public void writeAndFlush(String returnMessage) throws Exception {
		
		userIdChannelMap.values().parallelStream().forEach(channel-> {
			if (!channel.isActive()) {
				
				channel.close();
				return;
			}
			channel.writeAndFlush(messageService.returnMessage(returnMessage));
		});
	}
}
