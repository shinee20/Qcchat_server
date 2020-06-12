package org.qucell.chat.netty.server.config;

import java.net.InetSocketAddress;

import org.qucell.chat.netty.server.Initializer.NettyChannelInitializer;
import org.qucell.chat.netty.server.handler.JsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Configuration
public class NettyConfig {
	@Value("${netty.server.type}")
	private String transferType;

	@Value("${netty.server.port}")
	private int port;

	@Value("${netty.server.host}")
	private String host;

	@Value("${netty.server.thread.count.boss}")
	private int threadCountBoss;

	@Value("${netty.server.thread.count.worker}")
	private int threadCountWorker;

	@Value("${netty.server.log.level.bootstrap}")
	private String logLevelBootstrap;


	@Autowired
	private NettyChannelInitializer nettyChannelInitializer;

	/**
	 * Netty Server 의 Boss Thread 설정.
	 * 추후 http, udp 등의 설정이 필요할 경우 case 을 추가하여 설정을 변경할 수 있다.
	 *
	 * @return
	 */
	@Bean(destroyMethod="shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		switch(transferType) {
			case "tcp":
			default:
				return new NioEventLoopGroup(threadCountBoss);
		}
	}
	
	/**
	 * Netty Server 의 Worker Thread 설정.
	 * 추후 http, udp 등의 설정이 필요할 경우 case 을 추가하여 설정을 변경할 수 있다.
	 *
	 * @return
	 */
	@Bean(destroyMethod="shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		switch(transferType) {
			case "tcp":
			default:
				return new NioEventLoopGroup(threadCountWorker);
		}
	}
	
	/**
	 * transfer port
	 */
	@Bean
	public InetSocketAddress port() {
		return new InetSocketAddress(port);
	}
	
	/**
	 * netty serverbootstrap 
	 * netty는 부트스트랩이 두 종류가 있다. 
			1. serverbootstrap
			2. bootstrap
	 */
	@Bean 
	public ServerBootstrap serverBootstreap() {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap
			.group(bossGroup(), workerGroup())
			.handler(new LoggingHandler(LogLevel.valueOf(logLevelBootstrap)))
			.childHandler(nettyChannelInitializer);
		
		switch(transferType) {
			case "websocket":
			case "tcp":
			default :
				serverBootstrap.channel(NioServerSocketChannel.class);
		}
		
		return serverBootstrap;
	}
	
	/**
	 * Handler Bean 을 등록한다.
	 * Netty.Server.Initializer.NettyChannelInitializer 에서 이용할 Handler 을 등록해둔다.
	 *
	 * @return
	 */
	@Bean 
	public ChannelInboundHandlerAdapter handler() {
		//event processing handler
		return new JsonHandler();
	}
}
