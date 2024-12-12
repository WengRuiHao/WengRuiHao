package chat.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long chatId;

	private String chatname;

	@Column(nullable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createAt;

	@ManyToOne
	@JoinColumn(referencedColumnName = "username")
	private User creator;

	@OneToMany(mappedBy = "receiveChat", cascade = CascadeType.ALL)
	private List<Message> messages;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // 級聯保存與更新
	@JsonIgnore
	@JoinTable(
	    name = "chat_users", // 關聯表名稱
	    joinColumns = @JoinColumn(name = "chats_chat_id"), // Chat 表的主鍵列
	    inverseJoinColumns = @JoinColumn(name = "users_user_id") // User 表的主鍵列
	)
	private List<User> users;

	@PrePersist
	public void prePersist() {
		this.createAt = LocalDateTime.now();
	}

	public Chat(String chatname) {
		this.chatname = chatname;
	}

}
