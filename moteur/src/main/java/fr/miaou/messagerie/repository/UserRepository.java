package fr.miaou.messagerie.repository;

import fr.miaou.messagerie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}