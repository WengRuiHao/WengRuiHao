package com.example.chat;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import chat.model.dto.Profile;
import chat.model.entity.User;
import chat.repository.UserRepository;
import chat.service.UserService;
import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class test {

	@Autowired
	private UserRepository userRepository;
	 
	@Autowired
	private ModelMapper modelMapper;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
/*	@Test
	@Transactional
	public void test(){
		User user=userRepository.findByUsername("1").orElseThrow();
		UserDto dto=modelMapper.map(user,UserDto.class);
		System.out.println(dto);
	}*/
}
