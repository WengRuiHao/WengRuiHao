package chat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.model.dto.ChatDto;
import chat.model.dto.ChatroomDto;
import chat.model.dto.UserDto;
import chat.model.entity.Chat;
import chat.model.entity.User;
import chat.repository.ChatRepository;
import chat.repository.UserRepository;
import chat.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatRepository chatRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void createChat(ChatDto chatDto) {
		// 查詢創建者
	    User creator = userRepository.findByUsername(chatDto.getCreator().getUsername())
	            .orElseThrow(() -> new RuntimeException("會員不存在"));
		
		Chat createChat = new Chat();
		createChat.setChatname(chatDto.getChatname());
		createChat.setCreator(creator);
		
		// 添加創建者為聊天室成員
	    List<User> users = new ArrayList<>();
	    users.add(creator);
	    createChat.setUsers(users);
	    
	    // 保存到數據庫
	    chatRepository.save(createChat);
	}

	@Override
	public List<ChatDto> findAllChatByUser(String username) {
	    // 查詢用戶
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("會員不存在，查詢失敗"));
	    
	 // 查詢與該用戶相關的所有聊天室
	    List<Chat> chats = chatRepository.findAllByUsersContaining(user);
	    
		// 將每個 Chat 實體轉換為 ChatroomDto
	    
	    
	    return chats.stream()
	            .map(chat -> modelMapper.map(chat, ChatDto.class)) // 使用 ModelMapper 轉換
	            .collect(Collectors.toList());
	}
	
	@Override
	public ChatroomDto addUserToChat(Long chatId, String username) {
	    
		// 1. 查詢聊天室是否存在
	    Chat chat = chatRepository.findById(chatId)
	                  .orElseThrow(() -> new RuntimeException("聊天室不存在，無法添加用戶"));

	    // 2. 查詢使用者是否存在
	    User user = userRepository.findByUsername(username)
	                  .orElseThrow(() -> new RuntimeException("使用者不存在"));

	    // 3. 檢查用戶是否已經是聊天室成員
	    if (chat.getUsers().contains(user)) {
	        throw new RuntimeException("用戶已經是該聊天室的成員，無需重複添加");
	    }

	    // 4. 將用戶添加到聊天室
	    chat.getUsers().add(user);

	    // 5. 保存更新後的聊天室
	    Chat updatedChat = chatRepository.save(chat);
	    
	    // 6. 將 Chat 實體轉換為 ChatDto並返回
	    return convertToDto(updatedChat);
	}
	
	@Override
	public ChatDto getChat(String chatId) {
		Chat chat = chatRepository.findById(Long.parseLong(chatId)).get();
		ChatDto chatDto = modelMapper.map(chat, ChatDto.class);
		return chatDto;
	}

	//服務層轉換Entity to Dto
		private ChatroomDto convertToDto(Chat chat) {
			// 使用 ModelMapper 進行基本映射
		    ChatroomDto chatroomDto = modelMapper.map(chat, ChatroomDto.class);

		    // 自訂映射規則：將 User 實體轉換為 UserDto
		    List<UserDto> userDtos = chat.getUsers().stream()
		            .map(user -> modelMapper.map(user, UserDto.class))
		            .toList();

		    // 設置到 ChatroomDto 的 users 字段
		    chatroomDto.setUsers(userDtos);
		    
		    //設置ChatId
		    chatroomDto.setChatId(chat.getChatId());

		    return chatroomDto;
		}
}
