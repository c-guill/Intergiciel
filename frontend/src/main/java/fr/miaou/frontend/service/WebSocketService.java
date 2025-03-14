package fr.miaou.frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    public void sendMessage(String message) {
        this.template.convertAndSend("/topic/message", message);
    }

    public void sendMessageBroadcast(String message) {
        this.template.convertAndSend("/topic/broadcast", message);
    }

    public void manageUser(String value) {
        this.template.convertAndSend("/topic/manageuser", value);
    }
}
