package chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
	private String username;
	private String password;
	private String gender;
	private String email;

	public Register(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "Register [username=" + username + ", password=" + password + ", gender=" + gender + ", email=" + email
				+ "]";
	}
}
