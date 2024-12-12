package chat.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import jakarta.websocket.Session;
import chat.model.dto.MessageDto;
import chat.service.MessageService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SendMessageMQReceiver {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    private WebSocketServer webSocketServer;
	
//	@Autowired
//	private WebSocketServer2 webSocketServer2;

	@RabbitListener(queues = "ChatIdQueue")
	public void receiveMessage(String message) {
		try {
			MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
			// System.out.println(messageDto.toString());

			// 實際的 WebSocket 發送邏輯
			Long roomId=messageDto.getReceiveChat().getChatId();
			System.out.println("訊息已成功收到房間"+roomId+"的 RabbitMQ: " + messageDto.getMessage());
			webSocketServer.SendMessage( message);
//			webSocketServer2.SendMessage(roomId.toString(), message);
		} catch (Exception e) {
			System.err.println("接收訊息處理失敗: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
