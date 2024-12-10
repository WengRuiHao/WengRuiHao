package chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import chat.model.dto.Profile;
import chat.model.dto.Password;
import chat.response.ApiResponse;
import chat.service.UserService;
import chat.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private UserService userService;

	@GetMapping() // 查詢個人資訊
	public ResponseEntity<ApiResponse<String>> gettoken(@AuthenticationPrincipal String username) {
		return ResponseEntity.ok(ApiResponse.success("查詢成功", JwtUtil.generateToken(username)));
	}

	@GetMapping("/profile") // 查詢個人資訊
	public ResponseEntity<ApiResponse<Profile>> getUsername(@AuthenticationPrincipal String username) {
		Profile profile = userService.getUser(username);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", profile));
	}

	@PutMapping("/profile") // 修改個人資料，指定路徑為 /home/profile
	public ResponseEntity<ApiResponse<Profile>> updateUser(@RequestBody Profile profile) {
		Profile updateProfile = userService.updateProfile(profile);
		return ResponseEntity.ok(ApiResponse.success("修改成功", updateProfile));
	}

	@PutMapping("/profile/updatePassword") // 修改個人密碼
	public ResponseEntity<ApiResponse<Boolean>> UpdatePasswordController(@AuthenticationPrincipal String username,
			@RequestBody Password Password) {
		userService.updatePassword(username, Password);
		return ResponseEntity.ok(ApiResponse.success("修改成功", true));
	}

	// 處理異常狀況
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleTodoRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
	}
}
