package org.qucell.chat.web.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@Component
@Sharable
public class ChatServerHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);
	
	private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		channelGroup.add(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		String message = null;
		message = (String)msg;
		
		System.out.println("channel Read of [SERVER] " + message);
		
		Channel incoming = ctx.channel();
		for (Channel channel : channelGroup) {
			if (channel != incoming) {
				//send message
				channel.writeAndFlush("["+ incoming.remoteAddress()+"]" + message + "\n");
				
			}
		}
		if ("bye".equals(message.toLowerCase())) {
			ctx.close();
		}
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("handlerAdded of [SERVER]");
		Channel incoming = ctx.channel();
		for (Channel channel : channelGroup) {
			channel.write("[SERVER]- " + incoming.remoteAddress() + " has joined!\n");
			
		}
		channelGroup.add(incoming);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("handlerRemoved of [SERVER]");
		Channel incoming = ctx.channel();
		
		for (Channel channel : channelGroup) {
			channel.write("[SERVER] - " + incoming.remoteAddress() + "has left!\n");
		}
		channelGroup.remove(incoming);
		
	}

	
}
