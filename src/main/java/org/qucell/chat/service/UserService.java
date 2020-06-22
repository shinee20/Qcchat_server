package org.qucell.chat.service;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.model.DefaultRes;
import org.qucell.chat.model.user.Users;

public interface UserService {
	public DefaultRes getByUserId(int userId);
	
	/**
	 * 
	 * @return json data - friends list
	 * @throws IOException
	 */
	public List<Users> getAllFriendsList(int userId);
	
}
