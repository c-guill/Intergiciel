package fr.miaou.messagerie.repository;

import fr.miaou.messagerie.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
