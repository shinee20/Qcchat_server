package org.qucell.chat.dao;

import java.util.List;

import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.model.user.Users;

public interface UserDao {
	
	public Users getByUserId(int userId);
	public Users getByUserName(String userName);
	public void insertUser(LoginVO user);
	public List<Users> getFriendsList(int userId);
	
	/*
	 * redis test
	 */
	public List<Users> getAllUsers();
//	public void updateUser(Users user) throws IOException;

	/*
	 * redis test
	 */
}
