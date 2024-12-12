package chat.service;

import java.util.List;

import chat.model.dto.ChatDto;
import chat.model.dto.ChatroomDto;
import chat.model.dto.UserDto;
import chat.model.entity.User;

public interface ChatService {

	//查詢聊天室有哪些成員
	List<UserDto> findAllUserByChat(String roomId);
	
	//創建聊天室
	void createChat(ChatDto chatDto);
	
	//刪除聊天室
	void deleteChat(String roomId);
	
	//查詢個人所有聊天室
	List<ChatDto>findAllChatByUser(String username);
	
	//增加聊天室人員
	ChatroomDto addUserToChat(Long chatId,String username);
		
	//搜尋聊天室訊息
	ChatDto getChat(String chatId);
}
