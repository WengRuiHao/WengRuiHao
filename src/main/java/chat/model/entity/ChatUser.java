package chat.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 中間表的主鍵

	@ManyToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User user; // 使用者

	@ManyToOne
	@JoinColumn(name = "chat_id")
	private Chat chat; // 聊天室
}*/
