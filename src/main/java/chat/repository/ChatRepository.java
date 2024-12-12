package chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import chat.model.entity.Chat;
import chat.model.entity.User;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	// 查找某個使用者參與的所有聊天室
    List<Chat> findAllByUsersContaining(User user);
    
}
