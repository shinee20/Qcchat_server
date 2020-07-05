package org.qucell.chat.netty.server.handler;

import java.util.Objects;
import java.util.Optional;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.netty.server.common.AttachHelper;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.netty.server.repo.UserRepository;
import org.qucell.chat.service.UserService;
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


	@Autowired
	private UserService userService;
	

	/**
	 * 로그인
	 * @param ctx
	 * @param requestEntity
	 * @return
	 */
	public Client loginProcess(ChannelHandlerContext ctx, JsonMsgRes requestEntity) {
		String auth = requestEntity.extractFromHeader("auth");
		Objects.requireNonNull(auth, "auth is required");

		/**
		 * jwt를 decoding해서 id를 찾아낸다.
		 */
		Users user = userService.getByUserId(auth);
		/**
		 * 중복 로그인 방지
		 */
		if (isLogin(user.getUserName())) {
			throw new IllegalStateException("이미 로그인 된 사용자입니다.");
		}
		Client client = new Client(String.valueOf(user.getUserId()),user.getUserName(), "online", ctx.channel());
		
		//add login user list
		UserRepository.getUserList().add(client);

		log.info("== login, {}, {}", user.getUserName(), client.toString());
		AttachHelper.about(ctx).attachUsers(client);
		return client;

	}

	/**
	 * 해당 아이디가 로그인 되어있는지 check
	 * @param name
	 * @return
	 */
	public boolean isLogin(String name) {
		Optional<Client> optional = UserRepository.getUserList().stream().filter(cl->name.equals(cl.getName())).findFirst();

		if (optional.isPresent()) return true;
		return false;
	}
	
	/**
	 * 현재 로그인되어있는 사용자 리스트에서 제거
	 * @param client
	 */
	public void logout(Client client) {
		Optional<Client> optional = UserRepository.getUserList().stream().filter(cl->client.equals(cl)).findFirst();

		if (optional.isPresent()) {
			UserRepository.getUserList().remove(client);
//			userIdRoomIdRepository.save(client.getName());
		}
	}
}
