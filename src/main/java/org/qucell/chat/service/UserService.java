package org.qucell.chat.service;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.Users;

public interface UserService {
	public DefaultRes getByUserName(String userName) throws IOException;
	
	/**
	 * redis test
	 * @return
	 * @throws IOException
	 */
	public DefaultRes getAllUsers() throws IOException;
	void removeCacheUsers();
	/**
	 * redis test
	 * @return
	 * @throws IOException
	 */
}
