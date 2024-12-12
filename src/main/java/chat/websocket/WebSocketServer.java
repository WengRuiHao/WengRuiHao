package chat.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import chat.config.SpringConfigurator;
import chat.service.ChatService;
import chat.util.JwtUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/home/{roomId}", configurator = SpringConfigurator.class)
@Component
public class WebSocketServer {

	@Autowired
	private SendMessageMQSender rabbitMqSender;
	
	@Autowired
	private ChatService chatService;

	// 用來統一管理所有連接的 session
	private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> roomSessions = new ConcurrentHashMap<>();
	private static final Set<String> OnlineUsers = null;
	private Session session;

	@OnOpen
	public synchronized void onOpen(Session session, @PathParam("roomId") String roomId) throws IOException {
		String token = session.getRequestParameterMap().get("token").get(0);
		String username = JwtUtil.getUsernameFromToken(token);
		this.session = session;

		session.getUserProperties().put("roomId", roomId);
		session.getUserProperties().put("username", username);

		// 再連接前，先確認session有沒有在線，在線就移除掉session，在做重新連線
//		roomSessions.compute(roomId, (room, sessions) -> {
//			if (sessions == null) {
//				sessions = new ConcurrentHashMap<>();
//			}
//			sessions.remove(username);
//			return sessions;
//		});

		// 確保房間存在，並添加用戶 Session
		roomSessions.compute(roomId, (room, sessions) -> {// 使用 `compute` 來確保操作原子性
			if (sessions == null) {
				sessions = new ConcurrentHashMap<>();
			}
			sessions.put(username, session);
			return sessions;
		});
		OnlineUsers.add(username);
		
		System.out.println(username + "加入到房間" + roomId);
		System.out.println("目前聊天室 " + roomId + " 中有 " + roomSessions.get(roomId).size() + " 人");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws JsonMappingException, JsonProcessingException {
		// 使用 RabbitMqSender 發送消息
		String roomId=(String) session.getUserProperties().get("roomId");
		Set<String> AllUsers=chatService.findAllUserByChat(roomId).stream()
						  .map(user->user.getUsername())
						  .collect(Collectors.toSet());
		
		Set<String> offlineUsers = new HashSet<>(AllUsers);
		offlineUsers.remove(OnlineUsers);
		if(!offlineUsers.isEmpty()) {
			rabbitMqSender.sendMessageToOfflineUserToRabbitMq(message);
			rabbitMqSender.sendOfflineUserToRabbitMq(offlineUsers);
		}
		rabbitMqSender.sendMessageToOnlineUserToRabbitMq(message);
	}

	@OnClose
	public void onClose(Session session) {
		// 當連接關閉時，從 roomSessions 中移除該 session
		String roomId = (String) session.getUserProperties().get("roomId");
		String username = (String) session.getUserProperties().get("username");
		OnlineUsers.remove(username);
		roomSessions.compute(roomId, (room, sessions) -> {// 使用 `compute` 保證原子性
			// 初次檢查，避免 sessions 為 null
			if (sessions == null) {
				System.out.println("房間 " + roomId + " 不存在，無需關閉連接");
				return null;
			}

			sessions.remove(username);
			System.out.println("連接關閉: " + username);
			System.out.println("目前聊天室 " + roomId + " 中剩餘 " + sessions.size() + " 人");
			return sessions.isEmpty() ? null : sessions;// 如果該房間沒有成員，則移除該房間
		});
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		String roomId = (String) session.getUserProperties().get("roomId");
		String username = (String) session.getUserProperties().get("username");

		OnlineUsers.remove(username);
		if (session != null && username != null) {
			roomSessions.compute(roomId, (room, sessions) -> {
				sessions.remove(username);
				System.err.println("會員 " + username + " 發生錯誤: " + throwable.getMessage());
				System.err.println("連接關閉: " + username);
				return sessions;
			});
		}
	}

	public void SendMessage(String message) {
		String roomId = session.getPathParameters().get("roomId");
		if (roomSessions.containsKey(roomId)) {
			for (Session s : roomSessions.get(roomId).values()) {
				// 非同步發送訊息
				CompletableFuture.runAsync(() -> {
					try {
						s.getBasicRemote().sendText(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}

	}

}
