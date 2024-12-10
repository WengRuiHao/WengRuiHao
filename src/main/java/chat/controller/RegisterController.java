package chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chat.model.dto.Register;
import chat.response.ApiResponse;
import chat.service.UserService;

@RestController
@RequestMapping("/Register")
public class RegisterController {

	@Autowired
	private UserService userService;

	// 獲取所有會員
	@GetMapping
	public ResponseEntity<ApiResponse<List<Register>>> findAllUser() {
		List<Register> registers = userService.findAllUser();
		return ResponseEntity.ok(ApiResponse.success("查詢成功", registers));
	}

	// 新增會員
	@PostMapping
	public ResponseEntity<ApiResponse<Register>> createUser(@RequestBody Register register) {
		System.out.println(register.toString());
		Register addRegister = userService.addUser(register);
		return ResponseEntity.ok(ApiResponse.success("新增成功", addRegister));
	}

	// 處理異常狀況
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleTodoRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
	}
}
