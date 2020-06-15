package org.qucell.chat.netty.server.Initializer;

import java.util.concurrent.TimeUnit;

import org.qucell.chat.netty.server.handler.HealthCheckHandler;
import org.qucell.chat.netty.server.handler.NettyServerHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel>{
	private static final StringDecoder STRING_DECODER = new StringDecoder(CharsetUtil.UTF_8);
	private static final StringEncoder STRING_ENCODER = new StringEncoder(CharsetUtil.UTF_8);

	private static final String WEBSOCKET_PATH = "/websocket";
	private final HealthCheckHandler healthCheckHandler = new HealthCheckHandler();

	/**
	 * 채널 파이프라인 설정.
	 * Netty.Server.Config.NettyServerConfiguration 에서 등록한 Bean 을 이용해 사용자의 통신을 처리할 Handler 도 등록.
	 * Netty.Server.Handler.JsonHandler 에서 실제 사용자 요청 처리.
	 *
	 * @param channel
	 * @throws Exception
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		//childChannelHandler

		//event processing 
		ChannelPipeline channelPipeline = ch.pipeline();

		channelPipeline
		.addLast(new HttpServerCodec())
		.addLast(new HttpObjectAggregator(65536))
		.addLast(new WebSocketServerCompressionHandler())
		.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true))
		.addLast(healthCheckHandler)
		.addLast(new IdleStateHandler(HealthCheckHandler.READ_CHECK_INTERVAL, 0, 0, TimeUnit.SECONDS))
		.addLast(new NettyServerHandler());


	}

}
