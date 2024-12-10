package chat.service;

import chat.model.dto.ChatDto;

public interface ChatService {

	//搜尋聊天室訊息
	ChatDto getChat(String chatId);
}
