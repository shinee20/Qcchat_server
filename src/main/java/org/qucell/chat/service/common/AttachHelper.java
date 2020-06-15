package org.qucell.chat.service.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 새로운 채널을 만들어서 붙여준다.
 * @author myseo
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class AttachHelper {

	private Channel channel;
	private AttachHelper(Channel channel) {
		this.channel = channel;
	}
	
	public static AttachHelper about(Channel channel) {
		return new AttachHelper(channel);
	}
	
	public static AttachHelper about(ChannelHandlerContext ctx) {
		return new AttachHelper(ctx.channel());
	}
	
	public AttachHelper attach(String key, Object value) {
		AttributeKey attrKey = AttributeKey.valueOf(key);
		this.channel.attr(attrKey).set(value);
		return this;
	}
	
	private final String USER_INFO = "__userInfo__";
	public AttachHelper attachUsers(Client userInfo){
		attach(USER_INFO, userInfo);
		return this;
	}
	
	public Client getClient() {
		return (Client)get(USER_INFO);
	}
	
	public Object get(String key) {
		AttributeKey attrKey = AttributeKey.valueOf(key);
		
		return this.channel.attr(attrKey).get();
	}
} 
