package org.qucell.chat.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.util.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * updated 20/06/16
 * @author myseo
 */
public class JsonMsgRes {
	/**
	 *  {
		  "msg" : "잘 지내고 있냐",
		  "action" : "SendMsg",
		  "headers" : {
		    "roomId" : "room_1234"
		  }
		}
	 */
	public String msg; //->content
	public String action; //->action
	public Map<String, String> headers; //->headers
	
	public ByteBuf formatToByteBuf() throws Exception {
		//string to byte
		String jsonStr = toStr();
		
		ByteBuf result = ByteBufUtil.writeUtf8(PooledByteBufAllocator.DEFAULT, jsonStr);
		return result;
	}
	
	public String extractFromHeader(String key) {
		if (headers == null) {
			return null;
		}
		return headers.get(key);
	}
	
	/**
	 * make json response
	 * @return
	 */
	public String toStr() {
		Objects.requireNonNull(action);
		Map<String, Object> obj = new HashMap<>();
		
		obj.put("action", action);
		obj.put("headers", headers);
		obj.put("msg", msg);
		String jsonStr = JsonUtil.toJsonStr(obj);
		return jsonStr;
	}
	
	public static JsonMsgRes from(String str) {
		JsonMsgRes entity = JsonUtil.toObj(str, JsonMsgRes.class);
		return entity;
	}
	
	//브로드캐스트 하는 메시지와 사용자를 연결해준다.
	public static class Builder {
		private Client client;
		
		public Builder(ChannelHandlerContext ctx) {
			client = Client.from(ctx);
		}
		
		public Builder(Channel channel) {
			client = Client.from(channel);
		}
		
		public Builder(Client client) {
			this.client = client;
		}
		
		public Builder() {
			this.client = null;
		}
		
		private EventType action;
		private String contents;
		private Map<String, String> headers;
		
		public Builder setAction(EventType action) {
			this.action = action;
			return this;
		}
		public Builder setContents(String contents) {
			this.contents = contents;
			return this;
		}
		public Builder setRefId(String refId) {
			setHeader("refId", refId);
			return this;
		}
		public Builder setRefName(String refName) {
			setHeader("refName", refName);
			return this;
		}
		public Builder setRoomId(String roomId) {
			setHeader("roomId", roomId);
			return this;
		}
		
		public Builder setRefIdAndName(Client userInfo) {
			setRefId(userInfo.getId());
			setRefName(userInfo.getName());
			return this;
		}
		
		public Builder setHeader(String key, String value) {
			if (headers == null) {
				headers = new HashMap<>();
			}
			headers.put(key, value);
			return this;
		}
		
		public JsonMsgRes build() {
			JsonMsgRes entity = new JsonMsgRes();
			
			entity.msg = contents;
			entity.action = action.code;
			if (client != null) {
				setRefId(client.getId());
				setRefName(client.getName());
			}
			entity.headers = headers;
			return entity;
		}
	}
}
