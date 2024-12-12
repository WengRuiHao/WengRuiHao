package chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chat.model.dto.ChatDto;
import chat.model.dto.MessageDto;
import chat.model.dto.UserDto;
import chat.model.entity.User;
import chat.response.ApiResponse;
import chat.service.ChatService;
import chat.service.MessageService;
import chat.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/home/chat")
public class ChatController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatService chatService;

	@GetMapping("/{roomId}") // 取得房間的所有聊天紀錄
	public ResponseEntity<ApiResponse<List<MessageDto>>> getChatHistory(@PathVariable String roomId) {
		messageService.getAllMessage(roomId);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", messageService.getAllMessage(roomId)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<UserDto>> getProfile(@AuthenticationPrincipal String username) {
		return ResponseEntity.ok(ApiResponse.success("查詢成功", userService.findByUser(username)));
	}

	@GetMapping("/{roomId}/profile")
	public ResponseEntity<ApiResponse<ChatDto>> getChat(@PathVariable String roomId) {
		return ResponseEntity.ok(ApiResponse.success("查詢成功", chatService.getChat(roomId)));
	}
	
	@DeleteMapping("/{roomId}")
	public ResponseEntity<ApiResponse<ChatDto>> deleteChat(@PathVariable String roomId){
		chatService.deleteChat(roomId);
		return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
	}

	// 處理異常狀況
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleTodoRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
	}

}
