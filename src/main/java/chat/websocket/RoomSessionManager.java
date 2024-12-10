package chat.websocket;

import jakarta.websocket.Session;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomSessionManager {

    private static final ConcurrentHashMap<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    // 添加 session 到指定房間
    public void addSession(String roomId, Session session) {
	    roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
	}
    // 移除 session 到指定房間
    public void removeSession(String roomId, Session session) {
	    Set<Session> sessions = roomSessions.get(roomId);
	    if (sessions != null) {
	        sessions.remove(session);
	        if (sessions.isEmpty()) {
	            roomSessions.remove(roomId);
	        }
	    }
	}

    // 根據 roomId 取得對應的 session 集合
	public Set<Session> getSessions(String roomId) {
        return roomSessions.getOrDefault(roomId, ConcurrentHashMap.newKeySet());
    }
	
	// 廣播訊息到所有房間
    public void broadcastToAllRooms(String message) {
        // 遍歷所有房間的 session
        for (String roomId : roomSessions.keySet()) {
            Set<Session> sessions = roomSessions.get(roomId);
            for (Session session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(message);
                    } else {
                        removeSession(roomId, session);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 廣播消息到指定房間
    public void broadcastToRoom(String roomId, String message) {
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (Session session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(message);
                    } else {
                        removeSession(roomId, session);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
