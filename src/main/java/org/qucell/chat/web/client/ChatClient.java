package org.qucell.chat.web.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.qucell.chat.controller.UserController;
import org.qucell.chat.web.server.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ChatClient implements Runnable{


	private EventLoopGroup group;
	
	private Bootstrap bootstrap;

	private int port;
	private String host;

	private ChannelFuture lastWriteFuture;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		build();
	}

	public void build() {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		try {
			final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

			bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ChatClientInitializer(sslCtx));

			Channel channel = bootstrap.connect(host, port).sync().channel();

			lastWriteFuture = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			while(true) {
				String line = br.readLine();
				if (line == null) break;

				//send the msg to server
				lastWriteFuture = channel.writeAndFlush(line+"\r\n");

				//enter bye -> close connection
				if ("bye".equals(line.toLowerCase())) {
					channel.closeFuture().sync();
					break;
				}
			}

			if (lastWriteFuture != null) lastWriteFuture.sync();
		} catch(Exception e) {
			group.shutdownGracefully();
			e.printStackTrace();
		}

	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}



}
