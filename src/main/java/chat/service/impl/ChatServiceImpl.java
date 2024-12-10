package chat.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.model.dto.ChatDto;
import chat.model.entity.Chat;
import chat.repository.ChatRepository;
import chat.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ChatDto getChat(String chatId) {
		Chat chat = chatRepository.findById(Long.parseLong(chatId)).get();
		ChatDto chatDto = modelMapper.map(chat, ChatDto.class);
		return chatDto;
	}

}
