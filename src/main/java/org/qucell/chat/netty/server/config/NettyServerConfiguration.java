package org.qucell.chat.netty.server.config;

import org.qucell.chat.netty.server.Initializer.NettyChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerConfiguration {
	
	@Value("${server.port}")
	private int port;
	
	@Autowired
	private NettyChannelInitializer nettyChannelInitializer;
	
	
}
