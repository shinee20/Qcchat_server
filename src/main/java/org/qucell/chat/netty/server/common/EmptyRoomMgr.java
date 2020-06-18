package org.qucell.chat.netty.server.common;

import java.util.ArrayList;
import java.util.List;

import org.qucell.chat.model.room.Room;
import org.qucell.chat.netty.server.common.client.ClientAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * 20.06.18
 * @author myseo
 *
 */
@Slf4j
public class EmptyRoomMgr {

	public static final EmptyRoomMgr INSTANCE = new EmptyRoomMgr();

	//singleton
	private EmptyRoomMgr() {
		new Thread() {
			@Override
			public void run() {
				loop();
			}
		}.start();
	}

	private volatile List<Room> list = new ArrayList<>();

	public synchronized void add(Room room) {
		log.info("=== add empty room {}" , room);
		list.add(room);
	}

	private void loop()
	{
		while(true) {
			try {
				synchronized(this) {
					int size = list.size();
					
					if (size == 0) continue;
					
					for (int i = 0; i < list.size(); i++) {
						//전체 클라이언트의 화면에서 지운다. 
						ClientAdapter.INSTANCE.invalidateRoom(list.get(i));
						list.remove(i);
						
					}
				}
			} catch(Throwable e) {
				log.error(e.toString(), e);
			} finally {
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

