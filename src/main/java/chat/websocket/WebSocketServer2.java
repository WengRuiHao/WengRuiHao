package chat.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chat.config.SpringConfigurator;
import chat.model.dto.ChatDto;
import chat.service.ChatService;
import chat.util.JwtUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/home", configurator = SpringConfigurator.class)
@Component
public class WebSocketServer2 {

	@Autowired
	private SendMessageMQSender rabbitMqSender;

	@Autowired
	private ChatService chatService;
	
	@Autowired
	private ObjectMapper objectMapper;

	// 儲存聊天室ID與對應的所有sessionId
	private ConcurrentHashMap<String, Set<String>> chatRoom = new ConcurrentHashMap<>();
	// 儲存每個用戶ID與對應的WebSocket session
	private ConcurrentHashMap<String, Session> userSession = new ConcurrentHashMap<>();


	@OnOpen
	public void onOpen(Session session) throws IOException {
		String token = session.getRequestParameterMap().get("token").get(0);
		String username = JwtUtil.getUsernameFromToken(token);
		session.getUserProperties().put("username", username);

		// 記錄該用戶的 WebSocket 連接
		userSession.put(username, session);

		// 查詢用戶擁有的所有聊天室
		Set<String> chatRooms = chatService.findAllChatByUser(username).stream().map(ChatId -> ChatId.toString())
				.collect(Collectors.toSet());
		System.out.println(chatRooms);
		for (String chat : chatRooms) {
			chatRoom.computeIfAbsent(chat, k -> ConcurrentHashMap.newKeySet()).add(username);
			System.out.println("用戶 " + username + " 加入聊天室: " + chat);
			System.out.println("目前聊天室 " + chat + " 中有 " + chatRoom.get(chat).size() + " 人");
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) throws JsonMappingException, JsonProcessingException {
		// 使用 RabbitMqSender 發送消息
		rabbitMqSender.sendMessageToOnlineUserToRabbitMq(message);
	}

	@OnClose
	public void onClose(Session session) {
		String username=(String)session.getUserProperties().get("username");
		userSession.remove(username);
		System.out.println("用戶"+username+"關閉連線");
	}

	@OnError
	public void onError(Session session,Throwable throwable) {
		String username=session.getPathParameters().get("username");
		userSession.remove(username);
		System.err.println("會員 " + username + " 發生錯誤: " + throwable.getMessage());
	}

	public void SendMessage(String roomId, String message) {
		Set<String> members = chatRoom.get(roomId);
		if (members != null) {
			members.forEach(username -> {
				Session session = userSession.get(username);// 查找 session
				if (session != null && session.isOpen()) {
					session.getAsyncRemote().sendText(message);
				}
			});
		}
	}

}
