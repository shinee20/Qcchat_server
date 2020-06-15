package org.qucell.chat.netty.server.handler;

import org.qucell.chat.service.common.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HealthCheckHandler extends ChannelInboundHandlerAdapter{
	/**
	 * 5분동안 input이 없으면, health check의 목적으로 dummy 데이타를 보냄.
	 */
	public static final int READ_CHECK_INTERVAL = 5*60; //5minutes

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				Client client = Client.from(ctx); //find client 
				if (client == null) {
					log.info("== health check sending : {}, {}", client.getId(), client.getName());
					ChannelSendHelper.writeAndFlushToClient(client, new JsonMsgEntity.Builder().setAction(EventType.HealthCheck).build());
				}
			} else {
				super.userEventTriggered(ctx, evt);
			}
		}
	}
	
	
}
