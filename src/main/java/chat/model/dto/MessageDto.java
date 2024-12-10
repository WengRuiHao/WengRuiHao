package chat.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDto {

	private Long id;
	private UserDto sendUser;
	private String message;
	private ChatDto receiveChat;
	private String type;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createAt;

	@Override
	public String toString() {
		return "MessageDto [sendUser=" + sendUser + ", message=" + message + ", receiveChat=" + receiveChat
				+ ", createAt=" + createAt + "]";
	}

	public MessageDto(UserDto sendUser, String message, ChatDto receiveChat, LocalDateTime createAt,String type) {
		super();
		this.sendUser = sendUser;
		this.message = message;
		this.receiveChat = receiveChat;
		this.createAt = createAt;
		this.type=type;
	}
}
