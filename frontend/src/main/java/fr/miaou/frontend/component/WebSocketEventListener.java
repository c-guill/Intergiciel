package fr.miaou.frontend.component;

import fr.miaou.frontend.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WebSocketEventListener {

    @Autowired
    public ApiService apiService;

    @EventListener
    public void afterConnectionEstablished(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if( headerAccessor.getSessionAttributes() == null) return;
        String id = (String) headerAccessor.getSessionAttributes().get("id");
        if (id == null || id.equals("-1")) return;
        this.apiService.disconnection(id);
    }

}
