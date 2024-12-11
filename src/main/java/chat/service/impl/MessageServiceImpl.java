package chat.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import chat.model.dto.ChatDto;
import chat.model.dto.MessageDto;
import chat.model.dto.UserDto;
import chat.model.entity.Chat;
import chat.model.entity.Message;
import chat.model.entity.User;
import chat.repository.ChatRepository;
import chat.repository.MessageRepository;
import chat.repository.UserRepository;
import chat.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void addMessage(MessageDto messageDto) {
		User user = userRepository.findByUsername(messageDto.getSendUser().getUsername()).get();
		Chat chat = chatRepository.findById(messageDto.getReceiveChat().getChatId()).get();
		Message message = new Message();
		message.setMessage(messageDto.getMessage());
		message.setSendUser(user);
		message.setReceiveChat(chat);
//		System.out.println(message.toString());
		messageRepository.save(message);
	}

	@Override
	public List<MessageDto> getAllMessage(String chatId) {
		Pageable pageable = PageRequest.of(0, 30);// 限制數量用
		return messageRepository.findAllByRoomId(Long.parseLong(chatId), pageable).stream()
				.map(message -> new MessageDto(modelMapper.map(message.getSendUser(), UserDto.class),
						message.getMessage(), modelMapper.map(message.getReceiveChat(), ChatDto.class),
						message.getCreateAt(),
						null
						))
				.collect(Collectors.toList());

	}

}
