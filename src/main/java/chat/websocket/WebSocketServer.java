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

/*@ServerEndpoint(value = "/home/{roomId}", configurator = SpringConfigurator.class)
@Component
public class WebSocketServer {

	@Autowired
	private SendMessageMQSender rabbitMqSender;

	// 用來統一管理所有連接的 session
	private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> roomSessions = new ConcurrentHashMap<>();
	private Session session;

	@OnOpen
	public synchronized void onOpen(Session session, @PathParam("roomId") String roomId) throws IOException {
		String token = session.getRequestParameterMap().get("token").get(0);
		String username = JwtUtil.getUsernameFromToken(token);
		this.session = session;
		session.getPathParameters().put("roomId", roomId);
		session.getPathParameters().put("username", username);
		
		//再連接前，先確認session有沒有在線，在線就移除掉session，在做重新連線
		roomSessions.compute(roomId,(room,sessions)->{
			if(sessions==null) {
				sessions=new ConcurrentHashMap<>();
			}
			sessions.remove(username);
			return sessions;
		});
		
		// 確保房間存在，並添加用戶 Session
		roomSessions.compute(roomId,(room,sessions)->{//使用 `compute` 來確保操作原子性
			if(sessions==null) {
				sessions=new ConcurrentHashMap<>();
			}
			sessions.put(username, session);
			return sessions;
		});
		
		System.out.println(username + "加入到房間" + roomId);
		System.out.println("目前聊天室 " + roomId + " 中有 " + roomSessions.get(roomId).size() + " 人");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws JsonMappingException, JsonProcessingException {
		// 使用 RabbitMqSender 發送消息
		rabbitMqSender.sendMessageToRabbitMq(message);
	}

	@OnClose
	public void onClose() {
//		// 當連接關閉時，從 roomSessions 中移除該 session
//		roomSessions.computeIfPresent(roomId, (room,sessions)->{//使用 `compute` 保證原子性
//			System.out.println(sessions);
//			sessions.remove(username);
//			System.out.println("連接關閉: " + username);
//			System.out.println(sessions);
//			System.out.println("目前聊天室 " + roomId + " 中剩餘 " + sessions.size() + " 人");
//			System.out.println(sessions);
//			return sessions.isEmpty()?null:sessions;// 如果該房間沒有成員，則移除該房間
//		});
	}

	@OnError
	public void onError(Session session, Throwable throwable) {

	}

	public void SendMessage(String message) {
		String roomId=session.getPathParameters().get("roomId");
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

}*/
