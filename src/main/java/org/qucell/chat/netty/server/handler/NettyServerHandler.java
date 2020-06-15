package org.qucell.chat.netty.server.handler;

import org.qucell.chat.service.common.AttachHelper;
import org.qucell.chat.service.common.Client;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		
		if (frame instanceof TextWebSocketFrame) {
			String requestStr = ((TextWebSocketFrame) frame).text();
			//frame => text 
			log.info("==received : {}", requestStr);
			
			JsonMsgEntity requestEntity = JsonMsgEntity.from(requestStr);
			/**
			 * {
			 * 	"msg" : 
			 * 	"action": "send"
			 * 	"headers":{
			 * 		"roomId":"room1"
			 * 	}
			 * }
			 */
			Client client = Client.from(ctx);
			if (client == null && !EventType.LogIn.code.equals(requestEntity.action)) {
				throw new IllegalStateException("login부터 시작해야 함.");
			}
			ChatRcvMainProcessor.INSTANCE.process(client, requestEntity);
		} 
		else {
			String msg = "unsupported frame type: " + frame.getClass().getName();
			throw new UnsupportedOperationException(msg);
		}
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//remove users in channel 
		//userService
		Client client = AttachHelper.about(ctx).getClient();
		log.warn("== inactive : {}", client);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getMessage(), cause);
		Client client = AttachHelper.about(ctx).getClient();
		JsonMsgEntity entity = new JsonMsgEntity.Builder(ctx).setAction(EventType.SendInfo).setContents("[error] " + cause.toString()).build();
		ChannelSendHelper.writeAndFlushToClient(client, entity);
	}
	
	
}
