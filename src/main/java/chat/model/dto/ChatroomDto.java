package chat.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatroomDto {
	private long chatId;
	private String chatname;
	private LocalDateTime createAt;
	private UserDto creator;
	private List<UserDto>users;

}
