package org.qucell.chat.netty.server.handler;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.netty.server.common.AttachHelper;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.service.ChatReceiveService;
import org.qucell.chat.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/15
 * @author myseo
 */
@Slf4j
@Component
@Qualifier("nettyServerHandler")
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	@Autowired
	@Qualifier("loginHandler")
	private LoginHandler loginHandler;
	
	@Autowired
	@Qualifier("chatReceiveService")
	private ChatReceiveService chatReceiveService;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		
		if (frame instanceof TextWebSocketFrame) {
			String requestStr = ((TextWebSocketFrame) frame).text();
			//frame => text 
			log.info("==received : {}", requestStr);
			
			JsonMsgRes requestEntity = JsonMsgRes.from(requestStr);
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
			if (client == null) {
				//일단 확인 없이 로그인을 한다. => 추후에는 다른 방식으로 바꿔야 함
				client = loginHandler.loginProcess(ctx, requestEntity);
			}
			
			chatReceiveService.process(client, requestEntity);
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
		loginHandler.logout(client);
		log.warn("== inactive : {}", client);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getMessage(), cause);
		Client client = AttachHelper.about(ctx).getClient();
		JsonMsgRes entity = new JsonMsgRes.Builder(ctx).setAction(EventType.SendInfo).setContents("[error] " + cause.toString()).build();
		SendService.writeAndFlushToClient(client, entity);
	}
	
}
