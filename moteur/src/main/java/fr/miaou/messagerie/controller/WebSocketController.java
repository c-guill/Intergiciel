package fr.miaou.messagerie.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.service.MessageService;
import fr.miaou.messagerie.service.ProducerService;
import fr.miaou.messagerie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProducerService producerService;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Message handleMessage(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            User user = this.userService.getUserById(rootNode.get("senderid").asLong()).orElseThrow();
            Message message = Message.builder()
                    .user(user)
                    .idDestination(rootNode.get("targetid").asLong())
                    .contenu(rootNode.get("message").asText())
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();
            this.producerService.sendMessage(message);
            return message;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}