package chat.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveChatRequest {
	 private Long chatId;    // 聊天室 ID
	 private String username; // 目標用戶名
}
