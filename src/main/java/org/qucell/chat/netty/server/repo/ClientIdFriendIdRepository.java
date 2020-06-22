package org.qucell.chat.netty.server.repo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.qucell.chat.model.JsonMsgRes;
import org.qucell.chat.model.user.Users;
import org.qucell.chat.netty.server.common.ChannelSendHelper;
import org.qucell.chat.netty.server.common.EventType;
import org.qucell.chat.netty.server.common.client.Client;
import org.qucell.chat.service.UserService;
import org.qucell.chat.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientIdFriendIdRepository {
	private final ConcurrentHashMap<Client, List<Users>> clientIdFriendIdRepository = new ConcurrentHashMap<>();
	
	@Autowired
	private UserService userService;
	
	public ConcurrentHashMap<Client, List<Users>> getClientIdFriendIdMap() {
		return clientIdFriendIdRepository;
	}
	
	/**
	 * 친구관계를 가진 접속자들을 보여지도록 한다.
	 * @param client
	 * @return
	 */
	public void writeAndFlush(Client client){
		List<Users> friendsOfClient = clientIdFriendIdRepository.get(client);
		if (friendsOfClient == null) {
			friendsOfClient = userService.getAllFriendsList(Integer.parseInt(client.getId()));
			clientIdFriendIdRepository.put(client, friendsOfClient);
		}
		String jsonStr = JsonUtil.toJsonStr(friendsOfClient.stream().map(user->user.toMap()).collect(Collectors.toList()));
		JsonMsgRes entity = new JsonMsgRes.Builder(client).setAction(EventType.FriendsList).setContents(jsonStr).build();
		ChannelSendHelper.writeAndFlushToClient(client, entity);
	}
}
