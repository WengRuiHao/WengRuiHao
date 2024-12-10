package chat.websocket;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Service
public class SendMessageMQSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMessageToRabbitMq(String message) throws JsonMappingException, JsonProcessingException {
		try {
			rabbitTemplate.convertAndSend("SendMessageExchange", "SendMessage", message);
			System.out.println("訊息已成功發送到 RabbitMQ: " + message);
		} catch (Exception e) {
			// 處理訊息發送異常
			System.err.println("訊息發送失敗: " + e.getMessage());
			throw new RuntimeException("Failed to send message to RabbitMQ", e);
		}
	}	
}
