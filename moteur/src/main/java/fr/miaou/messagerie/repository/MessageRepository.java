package fr.miaou.messagerie.repository;

import fr.miaou.messagerie.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.user.idUser = :idUser OR m.user.idUser = :idDestination) AND (m.idDestination = :idUser OR m.idDestination = :idDestination) ORDER BY m.date")
    List<Message> findAllByDiscussion(@Param("idUser") Long idUser,@Param("idDestination") Long idDestination);

    @Query("SELECT m FROM Message m WHERE (m.user.idUser = :idUser OR m.user.idUser = :idDestination) AND (m.idDestination = :idUser OR m.idDestination = :idDestination) ORDER BY m.date DESC  LIMIT 1")
    Message findlastMessageByDiscussion(@Param("idUser") Long idUser,@Param("idDestination") Long idDestination);
}
