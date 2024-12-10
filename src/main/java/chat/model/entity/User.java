package chat.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId; // 使用者id

	@Column(unique = true, nullable = false)
	private String username; // 使用者帳號

	@Column(nullable = false)
	private String passwordHash; // 使用者Hash密碼

	@Column(nullable = false)
	private String salt; // 隨機鹽

	@Column(nullable = true)
	private String nickName; // 暱稱

	@Column(nullable = false)
	private String gender; // 使用者性別

	@Column(nullable = false)
	private String email; // 電子郵件

	@Column(nullable = true, length = 500)
	private String profileContent;// 個人簡介

	@Column(nullable = true)
	private String role; // 權限

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createAt; // 創建時間

	@OneToMany(mappedBy = "sendUser", cascade = CascadeType.ALL)
	private List<Message> messages;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ChatUser> chatUsers;

	// 在新增記錄時自動設定時間
	@PrePersist
	protected void onCreate() {
		this.createAt = LocalDateTime.now();
	}

	// 註冊會員
	public User(String username, String passwordHash, String salt, String gender, String email) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.salt = salt;
		this.gender = gender;
		this.email = email;
	}

	public User(String username) {
		this.username = username;
	}

}
