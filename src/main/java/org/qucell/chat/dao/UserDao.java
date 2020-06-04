package org.qucell.chat.dao;

import java.io.IOException;
import java.util.List;

import org.qucell.chat.model.LoginVO;
import org.qucell.chat.model.Users;

public interface UserDao {
	
//	void loadUserInfo();
	
	public Users getByUserId(int userId) throws IOException;
	public Users getByUserName(String userName) throws IOException;
	public void insertUser(LoginVO user) throws IOException;
	public List<Users> getFriendsList(int userId) throws IOException;
	
	/*
	 * redis test
	 */
	public List<Users> getAllUsers() throws IOException;
	public void updateUser(Users user) throws IOException;
}
