package chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private String username;
	private String nickname;
	private String gender;

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", nickName=" + nickname + ", gender=" + gender + "]";
	}

}
