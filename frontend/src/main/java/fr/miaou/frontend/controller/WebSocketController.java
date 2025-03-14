package fr.miaou.frontend.controller;

import fr.miaou.frontend.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class WebSocketController {

    @Autowired
    private ApiService apiService;

//    @MessageMapping("/application")
//    @SendTo("/all/messages")
//    public String handleMessage(String json) {
//        return json;
//    }

    @MessageMapping("/addUser")
    @SendTo("/topic/user")
    public String handleAddUser(String message, SimpMessageHeaderAccessor headerAccessor) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("id", message);
        this.apiService.connection(message);
        return message;
    }
}
