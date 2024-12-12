package chat.websocket;

import java.util.Set;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chat.model.dto.MessageDto;


@Service
public class SendMessageMQSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	//傳送給在線人員的訊息
	public void sendMessageToOnlineUserToRabbitMq(String message) throws JsonMappingException, JsonProcessingException {
		try {
			rabbitTemplate.convertAndSend("SendMessageExchange", "", message);
			System.out.println("訊息已成功發送到 RabbitMQ: " + objectMapper.readValue(message,MessageDto.class).getMessage());
		} catch (Exception e) {
			// 處理訊息發送異常
			System.err.println("訊息發送失敗: " + e.getMessage());
			throw new RuntimeException("Failed to send message to RabbitMQ", e);
		}
	}	
	
	//傳送給不在線人員的訊息
	public void sendMessageToOfflineUserToRabbitMq(String message) throws JsonMappingException, JsonProcessingException {
		try {
			rabbitTemplate.convertAndSend("SendMessageExchange", "", message);
			System.out.println("訊息已成功發送到 RabbitMQ: " + objectMapper.readValue(message,MessageDto.class).getMessage());
		} catch (Exception e) {
			// 處理訊息發送異常
			System.err.println("訊息發送失敗: " + e.getMessage());
			throw new RuntimeException("Failed to send message to RabbitMQ", e);
		}
	}
	
	//傳送不在線人員的名單
	public void sendOfflineUserToRabbitMq(Set<String> OfflineUsers) throws JsonMappingException, JsonProcessingException {
		try {
			rabbitTemplate.convertAndSend("SendOfflineUsersExchange", "OfflineUserList", OfflineUsers);
		} catch (Exception e) {
			// 處理訊息發送異常
			System.err.println("訊息發送失敗: " + e.getMessage());
			throw new RuntimeException("Failed to send message to RabbitMQ", e);
		}
	}
}
