package org.qucell.chat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.qucell.chat.model.Users;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class Constant {
	//static data type 
	//singleton
	
	
	//saved user info map as string -web socket
	public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = 
			new ConcurrentHashMap<String, WebSocketServerHandshaker>();
	
	public static Map<String, ChannelHandlerContext> onlineUserMap = 
			new ConcurrentHashMap<String, ChannelHandlerContext>();
	
	public static Map<String, Users> userInfoMap = 
	        new HashMap<String, Users>();

}
