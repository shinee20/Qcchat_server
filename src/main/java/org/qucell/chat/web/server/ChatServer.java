package org.qucell.chat.web.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer implements Runnable {


	@Autowired
	private EventLoopGroup bossGroup;
	@Autowired
	private EventLoopGroup workerGroup;

	@Autowired
	private ServerBootstrap serverBootstrap;

	@Value("${websocket.server.port}")
	private int port;
	
	private ChannelFuture serverChannelFuture;


	@Override
	public void run() {
		// TODO Auto-generated method stub
		build();
	}

	public void build()
	{
		try {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			
			serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.option(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChatServerInitializer(sslCtx));
			
			//start server
			serverChannelFuture = serverBootstrap.bind(port).sync();
		}
		catch(Exception e) {
			log.info(e.getMessage());
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			e.printStackTrace();
		}
	}
	
	public void close() {
		serverChannelFuture.channel().close();
		
		Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
		Future<?> workerGroupFuture = workerGroup.shutdownGracefully();
		
		try {
			bossGroupFuture.await();
			workerGroupFuture.await();
			
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
