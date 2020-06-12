package org.qucell.chat.dao.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.user.LoginVO;
import org.qucell.chat.model.user.Users;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao{

	@Inject
	private SqlSession sqlSession;
	
	private static String namespace="org.qucell.chat.mapper.userMapper";


	@Override
	public Users getByUserId(int userId) {
		return (Users)sqlSession.selectOne(namespace+".selectUserById", userId);
	}

	@Override
	public Users getByUserName(String userName) {
		return (Users)sqlSession.selectOne(namespace+".selectUserByName", userName);
	}

	public List<Users> getFriendsList(int userId){
		return sqlSession.selectList(namespace + ".selectFriendsList",userId);
	}


	@Override
	public void insertUser(LoginVO user){
		sqlSession.insert(namespace+ ".insertUser", user);
	}

//	@Override
//	public void updateUser(Users user) throws IOException {
//		sqlSession.update(namespace+".updateUserInfo",user );
//	}

	/*
	 * redis test
	 */
	@Override
	public List<Users> getAllUsers()  {
		return sqlSession.selectList(namespace+".selectUsers");
	}
	/*
	 * redis test
	 */

}
