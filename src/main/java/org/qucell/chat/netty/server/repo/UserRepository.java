package org.qucell.chat.netty.server.repo;

import java.util.concurrent.CopyOnWriteArrayList;

import org.qucell.chat.netty.server.common.client.Client;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

	private final CopyOnWriteArrayList<Client> userList = new CopyOnWriteArrayList<>();

	public CopyOnWriteArrayList<Client> getUserList() {
		return userList;
	}
	
}
