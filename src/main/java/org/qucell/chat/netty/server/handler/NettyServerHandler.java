package org.qucell.chat.netty.server.handler;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.netty.server.common.AttachHelper;
import org.qucell.chat.netty.server.common.ChannelSendHelper;
import org.qucell.chat.netty.server.common.ChatReceiveProcess;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;

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
public class NettyServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private static LoginHandler loginHandler = new LoginHandler();
	
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
			/**
			* 파이프에 등록되어있는 handler에서가 아닌 새로 생성한 다른 class에서 채널을 참조한다.
			*/
			Client client = Client.from(ctx);
			if (client == null) {
				//redis에서 찾아서 로그인시켜준다. 
				client = loginHandler.loginProcess(ctx, requestEntity);
			}
			
			ChatReceiveProcess.INSTANCE.process(client, requestEntity);
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
		JsonMsgRes entity = new JsonMsgRes.Builder(ctx).setAction(EventType.SendInfo).setContents("[error] " + cause.toString()).build();
		ChannelSendHelper.writeAndFlushToClient(client, entity);
	}
	
}
