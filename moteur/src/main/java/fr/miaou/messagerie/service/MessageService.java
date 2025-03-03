package fr.miaou.messagerie.service;


import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.repository.MessageRepository;
import fr.miaou.messagerie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Récupérer un message par son ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Ajouter un nouveau message
    public Message createMessage(Long userId, String contenu, Long idDestination) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return messageRepository.save(Message.builder()
                .user(user)
                .idDestination(idDestination)
                .contenu(contenu)
                .date(new Timestamp(System.currentTimeMillis()))
                .build());
    }

    // Supprimer un message par son ID
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    // Mettre à jour un message existant
    public Message updateMessage(Message messageDetails) {
        return messageRepository.findById(messageDetails.getIdMessage()).map(message -> {
            message.setContenu(messageDetails.getContenu());
            message.setDate(messageDetails.getDate());
            return messageRepository.save(message);
        }).orElseThrow(() -> new RuntimeException("Message non trouvé"));
    }
}





