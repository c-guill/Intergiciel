package fr.miaou.messagerie.service;


import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.repository.MessageRepository;
import fr.miaou.messagerie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProducerService producerService;

    public Message createMessage(Long userId, String contenu, Long idDestination) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Message message = this.messageRepository.save(messageRepository.save(Message.builder()
                .user(user)
                .idDestination(idDestination)
                .contenu(contenu)
                .date(new Timestamp(System.currentTimeMillis()))
                .build()));
        this.producerService.sendMessage(message);
        return message;
    }

    public List<Message> getMessageByDiscussion(Long idUser, Long idDestination) {
        if (idDestination == -1) {
            return this.messageRepository.findAllByidDestination(idDestination);
        }
        return this.messageRepository.findAllByDiscussion(idUser, idDestination);
    }

    public Optional<Message> getLastMessageByDiscussion(Long idUser, Long idDestination) {
        return Optional.ofNullable(this.messageRepository.findlastMessageByDiscussion(idUser, idDestination));

    }
}





