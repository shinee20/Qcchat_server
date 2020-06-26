package org.qucell.chat.netty.server.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.Message;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.service.SendService;
import org.qucell.chat.util.JsonUtil;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatMessageLogRepository {
	private ConcurrentHashMap<String, List<Message>> chatMessageLogRepository = new ConcurrentHashMap<>();
	
	public ConcurrentHashMap<String, List<Message>> getChatMessageMap() {
		return chatMessageLogRepository;
	}
	
	public void writeAndFlush(Client client, String roomId) {
		List<Message> messageLog = chatMessageLogRepository.get(roomId);
		if (messageLog == null) {
			
		}
		else {
			String jsonStr = JsonUtil.toJsonStr(messageLog.stream().map(msg->msg.toMap()).collect(Collectors.toList()));
			JsonMsgRes entity = new JsonMsgRes.Builder(client).setRoomId(roomId).setAction(EventType.MsgLog).setContents(jsonStr).build();
			SendService.writeAndFlushToClient(client, entity);
		}
	}
	
	public void save(String roomId, String msg, String sender) {
		List<Message> list = chatMessageLogRepository.get(roomId);
		Message message = new Message(msg, sender);
		if (list == null) {
			list = new ArrayList<>();
			list.add(message);
			chatMessageLogRepository.put(roomId, list);
		}
		else {
			list.add(message);
			chatMessageLogRepository.put(roomId, list);

			log.info("chat message Log : {} ", list.toString());
		}
	}
}
