package chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import chat.model.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
