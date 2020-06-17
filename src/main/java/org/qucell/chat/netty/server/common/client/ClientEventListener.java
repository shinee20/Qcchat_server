package org.qucell.chat.netty.server.common.client;

import org.qucell.chat.netty.server.common.EventType;

public interface ClientEventListener {
	void listen(ClientEvent e);
	
	public static class ClientEvent {
		public final Client client;
		public final String roomId;
		public final EventType eventType;
		
		public ClientEvent(Client client, String roomId, EventType eventType) {
			this.client = client;
			this.roomId = roomId;
			this.eventType = eventType;
		}
	}
}
