package org.qucell.chat.netty.server.Initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

@Component
@Qualifier("nettyChannelInitializer")
public class NettyChannelInitializer extends ChannelInitializer<Channel>{
	private static final StringDecoder STRING_DECODER = new StringDecoder(CharsetUtil.UTF_8);
	private static final StringEncoder STRING_ENCODER = new StringEncoder(CharsetUtil.UTF_8);
	
	@Value("${netty.server.type}")
	private String transferType;
	
	@Value("${netty.server.log.level.bootstrap}")
	private String logLevelBootstrap;

	@Autowired
	@Qualifier("websocketHandler")
	private ChannelInboundHandlerAdapter websocketHandler;
	
	@Autowired
	@Qualifier("jsonHandler")
	private ChannelInboundHandlerAdapter jsonHandler;

	
	/**
	 * 채널 파이프라인 설정.
	 * Netty.Server.Config.NettyServerConfiguration 에서 등록한 Bean 을 이용해 사용자의 통신을 처리할 Handler 도 등록.
	 * Netty.Server.Handler.JsonHandler 에서 실제 사용자 요청 처리.
	 *
	 * @param channel
	 * @throws Exception
	 */
	@Override
	protected void initChannel(Channel ch) throws Exception {
		//childChannelHandler
		
		//event processing 
		ChannelPipeline channelPipeline = ch.pipeline();
		
		switch(transferType) {
			case "websocket":
				channelPipeline
					.addLast(new HttpServerCodec())
					.addLast(new HttpObjectAggregator(65536))
					.addLast(new WebSocketServerCompressionHandler())
					.addLast(new LoggingHandler(LogLevel.valueOf(logLevelBootstrap)))
					.addLast(websocketHandler);
				break;
			case "tcp":
			default:
				channelPipeline
					.addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
					.addLast(STRING_DECODER)
					.addLast(STRING_ENCODER)
					.addLast(new LoggingHandler(LogLevel.valueOf(logLevelBootstrap)))
					.addLast(jsonHandler);
				
		}
	}
	
}
