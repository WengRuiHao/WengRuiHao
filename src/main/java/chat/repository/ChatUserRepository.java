package chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import chat.model.entity.ChatUser;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

}
