package org.qucell.chat.dao.impl;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.qucell.chat.dao.UserDao;
import org.qucell.chat.model.LoginVO;
import org.qucell.chat.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao{

	@Inject
	private SqlSession sqlSession;
	
	private static String namespace="org.qucell.chat.mapper.userMapper";
	
//	@Override
//	public void loadUserInfo() {
//		// TODO Auto-generated method stub
//		//Test -> replace db dao
//		UserVO user1 = new UserVO(1,"member1"); 
//		UserVO user2 = new UserVO(2,"member2"); 
//		UserVO user3 = new UserVO(3,"member3"); 
//		UserVO user4 = new UserVO(4,"member4"); 
//		UserVO user5 = new UserVO(5,"member5"); 
//		UserVO user6 = new UserVO(6,"member6"); 
//		UserVO user7 = new UserVO(7,"member7"); 
//		UserVO user8 = new UserVO(8,"member8"); 
//		UserVO user9 = new UserVO(9,"member9"); 
//		UserVO user10 = new UserVO(10,"member10"); 
//		
//		user1.setFriendList(generateFriendList(1));
//		user2.setFriendList(generateFriendList(2));
//		user3.setFriendList(generateFriendList(3));
//		user4.setFriendList(generateFriendList(4));
//		user5.setFriendList(generateFriendList(5));
//		user6.setFriendList(generateFriendList(6));
//		user7.setFriendList(generateFriendList(7));
//		user8.setFriendList(generateFriendList(8));
//		user9.setFriendList(generateFriendList(9));
//		user10.setFriendList(generateFriendList(10));
//		
//		RoomVO room = new RoomVO(1,"room1",null);
//		
//		List<RoomVO> roomList = new ArrayList<RoomVO>();
//		roomList.add(room);
//		
//		user1.setRoomList(roomList);
//		user2.setRoomList(roomList);
//		user3.setRoomList(roomList);
//		user4.setRoomList(roomList);
//		user5.setRoomList(roomList);
//		user6.setRoomList(roomList);
//		user7.setRoomList(roomList);
//		user8.setRoomList(roomList);
//		user9.setRoomList(roomList);
//		user10.setRoomList(roomList);
//		
//		Constant.userInfoMap.put("user1", user1);
//		Constant.userInfoMap.put("user2", user2);
//		Constant.userInfoMap.put("user3", user3);
//		Constant.userInfoMap.put("user4", user4);
//		Constant.userInfoMap.put("user5", user5);
//		Constant.userInfoMap.put("user6", user6);
//		Constant.userInfoMap.put("user7", user7);
//		Constant.userInfoMap.put("user8", user8);
//		Constant.userInfoMap.put("user9", user9);
//		Constant.userInfoMap.put("user10", user10);
//		
//	}

	@Override
	public Users getByUserName(String userName) throws IOException {
		// TODO Auto-generated method stub
		return (Users)sqlSession.selectOne(namespace+".selectUserByName", userName);
	}

	public List<Users> getFriendsList(int userId) throws IOException{
		return sqlSession.selectList(namespace + ".selectFriendsList",userId);
	}
//    private List<UserVO> generateFriendList(int userId) {
//    	UserVO user1 = new UserVO(1,"member1"); 
//		UserVO user2 = new UserVO(2,"member2"); 
//		UserVO user3 = new UserVO(3,"member3"); 
//		UserVO user4 = new UserVO(4,"member4"); 
//		UserVO user5 = new UserVO(5,"member5"); 
//		UserVO user6 = new UserVO(6,"member6"); 
//		UserVO user7 = new UserVO(7,"member7"); 
//		UserVO user8 = new UserVO(8,"member8"); 
//		UserVO user9 = new UserVO(9,"member9"); 
//		UserVO user10 = new UserVO(10,"member10"); 
//		
//        List<UserVO> friendList = new ArrayList<UserVO>();
//        friendList.add(user1);
//        friendList.add(user2);
//        friendList.add(user3);
//        friendList.add(user4);
//        friendList.add(user5);
//        friendList.add(user6);
//        friendList.add(user7);
//        friendList.add(user8);
//        friendList.add(user9);
//        friendList.add(user10);
//        
//        Iterator<UserVO> iterator = friendList.iterator();
//        while(iterator.hasNext()) {
//            UserVO entry = iterator.next();
//            if (userId==entry.getUserId()) {
//                iterator.remove();
//            }
//        }
//        return friendList;
//    }

	@Override
	public void insertUser(LoginVO user) throws IOException {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace+ ".insertUser", user);
	}

	/*
	 * redis test
	 */
	@Override
	public List<Users> getAllUsers() throws IOException {
		// TODO Auto-generated method stub
		
		return sqlSession.selectList(namespace+".selectUsers");
	}
	/*
	 * redis test
	 */

}
