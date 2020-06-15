package org.qucell.chat.netty.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.qucell.chat.netty.server.Initializer.NettyChannelInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NettyServer {
	
	@Value("${netty.websocket.port}")
	private int port;
	
	Channel channel;
	
	EventLoopGroup bossGroup ;
	EventLoopGroup workerGroup ;
	
	
	@PostConstruct
	public void postConstruct() {
		new Thread(){
			@Override
			public void run() {
				try {
					startServer();
				}catch(Exception e) {
					log.error("== failed in start websocket", e);
				}
			}
		}.start();
	}
	
	@PreDestroy 
	public void preDestory() {
		stop();
	}
	/**
	 * Netty 서버를 시작합니다.
	 * Netty.Server.NettyServerConfiguration 에서 설정한 ServerBootStrap, port Bean 을 이용해 서버를 시작합니다.
	 * Netty Server 가 이용할 여러 정보들이 NettyServerConfiguration 에 정의되어 있으니 그 곳을 참조합니다.
	 *
	 * @throws Exception
	 */
	public NettyServer startServer() throws Exception {
		log.info("== WebSocketServer start");
		
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)
						.channel(NioServerSocketChannel.class)
						.handler(new LoggingHandler(LogLevel.INFO))
						.childHandler(new NettyChannelInitializer());
		
		channel = serverBootstrap.bind(port).sync().channel();
		return this;
	}
	
	@PreDestroy
	public void stop() {
		channel.close().addListener(f->{
			log.warn("==server close...");
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		});
			
	}
	
	public static void main(String[]args) throws Exception {
		NettyServer main = new NettyServer();
		main.startServer();
	}
}
