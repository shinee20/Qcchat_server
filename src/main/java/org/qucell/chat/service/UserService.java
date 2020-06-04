package org.qucell.chat.service;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.Users;

public interface UserService {
	public DefaultRes getByUserId(int userId) throws IOException;
	
	/**
	 * redis test
	 * @return
	 * @throws IOException
	 */
//	public DefaultRes getAllUsers() throws IOException;
//	void removeCacheUsers();
	/**
	 * redis test
	 * @return
	 * @throws IOException
	 */
	
	/**
	 * 
	 * @return json data - friends list
	 * @throws IOException
	 */
	public DefaultRes getAllFriendsList(int userId) throws IOException;
	public Users getUser(int userId) throws IOException;
}
