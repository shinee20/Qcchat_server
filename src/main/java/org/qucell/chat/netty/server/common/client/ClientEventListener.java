package org.qucell.chat.netty.server.common.client;

import org.qucell.chat.model.DefaultRes;

public interface ClientEventListener {
	void listen(ClientEvent e);
	
	public static class ClientEvent {
		public final Client client;
		public final String roomId;
		public final DefaultRes res;
		
		public ClientEvent(Client client, String roomId, DefaultRes res) {
			this.client = client;
			this.roomId = roomId;
			this.res = res;
		}
	}
}
