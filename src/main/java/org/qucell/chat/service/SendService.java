package org.qucell.chat.service;

import java.util.List;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.netty.server.common.client.ClientAdapter;
import org.qucell.chat.netty.server.repo.ChatMessageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * updated 20/06/16
 * @author myseo
 */
@Service
public class SendService {
	
	public static void writeAndFlushToClients(List<Client> clients, JsonMsgRes entity) {
		if (clients == null) {
			return;
		}
		
		String str = entity.toStr();
		TextWebSocketFrame frame = new TextWebSocketFrame(str);
		clients.stream().forEach(client->{
			
			Channel ch = client.getChannel();
			if (ch.isActive()) {
				ch.writeAndFlush(frame.duplicate().retain()); 
				//duplicate ByteBufHolder and increase the reference count by 1
				//bytebuf안에 payload를 저장하는 메세지 객체를 구현할 때 사용한다.
			} else {
				//remove client
				ClientAdapter.INSTANCE.invalidateClient(client);
			}
		});

	}
	public static void writeAndFlushToClient(Client client, JsonMsgRes entity) {
		String str = entity.toStr();
		TextWebSocketFrame frame = new TextWebSocketFrame(str);
		
		Channel channel = client.getChannel();
		if (channel.isActive()) {
			channel.writeAndFlush(frame);
		}
		else {
			ClientAdapter.INSTANCE.invalidateClient(client);
		}
		
	}
}
