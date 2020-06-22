package org.qucell.chat.netty.server.handler;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.netty.server.common.AttachHelper;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("loginHandler")
@Sharable
public class LoginHandler {

	private static AtomicInteger idGen = new AtomicInteger();

	private static Hashtable userList = new Hashtable();

	@Autowired
	private LoginService loginService;

	/**
	 * 로그인
	 * @param ctx
	 * @param requestEntity
	 * @return
	 */
	public Client loginProcess(ChannelHandlerContext ctx, JsonMsgRes requestEntity) {
		String name = requestEntity.extractFromHeader("name");
		Objects.requireNonNull(name, "name is required");

		if (isLogin(name)) {
			throw new IllegalStateException("이미 로그인 된 사용자입니다.");
		}
		Users user = loginService.login(new LoginVO(name));
		Client client = new Client(String.valueOf(user.getUserId()), user.getUserName(), ctx.channel());

		log.info("== login ({}) ({})", name, ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());

		//add login user list
		userList.put(name, ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
		AttachHelper.about(ctx).attachUsers(client);
		return client;

	}

	/**
	 * 해당 아이디가 로그인 되어있는지 check
	 * @param name
	 * @return
	 */
	public boolean isLogin(String name) {
		boolean isLogin = false;
		Enumeration e = userList.keys();

		String key = "";
		while(e.hasMoreElements()) {
			key = (String)e.nextElement();

			if (name.equals(key)) {
				isLogin = true;
				break;
			}
		}
		return isLogin;
	}

}
