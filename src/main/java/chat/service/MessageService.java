package chat.service;

import java.util.List;

import chat.model.dto.MessageDto;

public interface MessageService {

	//新增訊息
	void addMessage(MessageDto messageDto);
	
	//取得房間的聊天對話紀錄
	List<MessageDto> getAllMessage(String chatId);
}
