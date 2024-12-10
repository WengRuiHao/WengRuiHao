package chat.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import chat.config.SpringConfigurator;
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

	// 用來統一管理所有連接的 session
	private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> roomSessions = new ConcurrentHashMap<>();
	private Session session;
	private String username;
	private String roomId;

	@OnOpen
	public void onOpen(Session session, @PathParam("roomId") String roomId) throws IOException {
		String token = session.getRequestParameterMap().get("token").get(0);
		this.username = JwtUtil.getUsernameFromToken(token);
		this.roomId = roomId;
		this.session = session;

		// 確保房間存在，並添加用戶 Session
		roomSessions.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>()).put(username, session);
		System.out.println(username + "加入到房間" + roomId);
		System.out.println("目前聊天室 " + roomId + " 中有 " + roomSessions.get(this.roomId).size() + " 人");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws JsonMappingException, JsonProcessingException {
		// 使用 RabbitMqSender 發送消息
		rabbitMqSender.sendMessageToRabbitMq(message);
	}

	@OnClose
	public void onClose() {
		// 當連接關閉時，從 roomSessions 中移除該 session
		roomSessions.get(this.roomId).values().removeIf(s -> s.equals(this.session));
		System.out.println("連接關閉: " + username);
		System.out.println("目前聊天室 " + roomId + " 中剩餘 " + roomSessions.get(this.roomId).size() + " 人");
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// 處理錯誤
		roomSessions.get(this.roomId).values().removeIf(s -> s.equals(this.session));
	}

	public void SendMessage(String message) {
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
