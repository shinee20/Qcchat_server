package org.qucell.chat.service;

import org.qucell.chat.model.DefaultRes;

public interface UserService {
	public DefaultRes getByUserId(int userId);
	
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
	public DefaultRes getAllFriendsList(int userId);
	
}
