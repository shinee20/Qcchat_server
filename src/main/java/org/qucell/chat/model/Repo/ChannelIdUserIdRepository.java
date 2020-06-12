package org.qucell.chat.model.Repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelId;

@Component
public class ChannelIdUserIdRepository {
	
	private final Map<ChannelId, Integer> channelIdUserIdMap = new ConcurrentHashMap<>();
	
	public Map<ChannelId, Integer> getChannelIdUserIdMap() {
		return channelIdUserIdMap;
	}
}
