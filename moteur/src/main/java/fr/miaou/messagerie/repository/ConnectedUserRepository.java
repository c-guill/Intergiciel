package fr.miaou.messagerie.repository;

import fr.miaou.messagerie.model.ConnectedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectedUserRepository extends JpaRepository<ConnectedUser, Long> {
}

