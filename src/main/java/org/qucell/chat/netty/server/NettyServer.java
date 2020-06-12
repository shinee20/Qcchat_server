package org.qucell.chat.netty.server;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

@Component
public class NettyServer {
	
	@Autowired
	private ServerBootstrap serverBootstrap;	
	
	@Autowired
	private InetSocketAddress port;
	
	private Channel channel;
	
	/**
	 * Netty 서버를 시작합니다.
	 * Netty.Server.NettyServerConfiguration 에서 설정한 ServerBootStrap, port Bean 을 이용해 서버를 시작합니다.
	 * Netty Server 가 이용할 여러 정보들이 NettyServerConfiguration 에 정의되어 있으니 그 곳을 참조합니다.
	 *
	 * @throws Exception
	 */
	public void start() throws Exception {
		channel = serverBootstrap.bind(port).sync().channel().closeFuture().sync().channel();
	}
	
	@PreDestroy
	public void stop() throws Exception {
		channel.close();
		channel.parent().close();	
	}
}
