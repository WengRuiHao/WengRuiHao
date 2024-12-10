package chat.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

	@OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
	private List<ChatUser> chatUsers;

	@PrePersist
	public void prePersist() {
		this.createAt = LocalDateTime.now();
	}

	public Chat(String chatname) {
		this.chatname = chatname;
	}

}
