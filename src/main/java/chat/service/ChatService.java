package chat.service;

import java.util.List;

import chat.model.dto.ChatDto;
import chat.model.dto.ChatroomDto;

public interface ChatService {

	//創建聊天室
	void createChat(ChatDto chatDto);
	
	//查詢個人所有聊天室
	List<ChatDto>findAllChatByUser(String username);
	
	//增加聊天室人員
	ChatroomDto addUserToChat(Long chatId,String username);
		
	//搜尋聊天室訊息
	ChatDto getChat(String chatId);
}
