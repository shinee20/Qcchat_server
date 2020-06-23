package org.qucell.chat.netty.server.handler;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * updated 20/06/15
 * @author myseo
 */
@Slf4j
@Component
@Sharable
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
				/**
				* 파이프에 등록되어있는 handler에서가 아닌 새로 생성한 다른 class에서 채널을 참조한다.
				*/
				Client client = Client.from(ctx); //find client 
				if (client != null) {
					log.info("== health check sending : {}, {}", client.getId(), client.getName());
					//broadcast msg
					SendService.writeAndFlushToClient(client, new JsonMsgRes.Builder().setAction(EventType.HealthCheck).build());
				}
			} else {
				super.userEventTriggered(ctx, evt);
			}
		}
	}
	
	
}
