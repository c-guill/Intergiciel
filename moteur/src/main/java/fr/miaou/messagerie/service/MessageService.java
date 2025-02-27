package fr.miaou.messagerie.service;


import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAllmessages(){
        return messageRepository.findAll();



    }




}
