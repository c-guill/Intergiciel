package fr.miaou.messagerie.service;


import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    // Récupérer un message par son ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Ajouter un nouveau message
    public Message createMessage(Message message) {
        return messageRepository.save(message);
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




