package org.qucell.chat.netty.server.handler;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.netty.server.common.AttachHelper;
import org.qucell.chat.netty.server.common.client.Client;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginHandler {
	
//	@Autowired
//	private RedisService redisService;
	
	private static AtomicInteger idGen = new AtomicInteger();
	
	public Client loginProcess(ChannelHandlerContext ctx, JsonMsgRes requestEntity) {
		String name = requestEntity.extractFromHeader("name");
		Objects.requireNonNull(name, "name is required");
		
		/**
		 * 일단 보류
		 */
//		String key = "id:"+name;
//		
//		Users user = (Users)redisService.getValue(key);
//		
//		Client client = new Client(user, ctx.channel());
		/**
		 * 일단 보류
		 */
		
		Client client = new Client(String.valueOf(idGen.incrementAndGet()), name, ctx.channel());
		
		log.info("== login ({}) ({})", name, ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
		
		AttachHelper.about(ctx).attachUsers(client);
		return client;
	}
}
