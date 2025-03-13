package fr.miaou.frontend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public String handleMessage(String json) {
        return json;
    }
}
