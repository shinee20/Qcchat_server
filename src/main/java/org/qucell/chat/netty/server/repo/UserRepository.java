package org.qucell.chat.netty.server.repo;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.qucell.chat.netty.server.common.client.Client;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

	
	private static CopyOnWriteArrayList<Client> userList = new CopyOnWriteArrayList<>();

	public static CopyOnWriteArrayList<Client> getUserList() {
		return userList;
	}
	
	public static Client findLoginedUser(String userName) {
		Optional<Client> optional = userList.stream().filter(cl->userName.equals(cl.getName())).findFirst();

		if (optional.isPresent()) {
			 
			 return optional.get();
		}
		return null;
		
	}
	
}
