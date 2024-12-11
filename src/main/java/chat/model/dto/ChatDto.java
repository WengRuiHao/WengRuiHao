package chat.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

	private long chatId;
	private String chatname;
	private LocalDateTime createAt;
	private UserDto creator;

	@Override
	public String toString() {
		return "ChatDto [chatId=" + chatId + ", chatname=" + chatname + ", createAt=" + createAt + ", creator="
				+ creator + "]";
	}
}
