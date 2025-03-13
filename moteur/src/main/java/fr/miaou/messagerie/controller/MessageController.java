package fr.miaou.messagerie.controller;

import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public Message createMessage(@RequestParam Long userId, @RequestParam String contenu, @RequestParam Long idDestination) {
        return messageService.createMessage(userId, contenu, idDestination);
    }

    @GetMapping("/discussion")
    public ResponseEntity<List<Message>> getMessagesByDiscussion(@RequestParam Long idUser, @RequestParam Long idDestination) {
        return ResponseEntity.ok(this.messageService.getMessageByDiscussion(idUser, idDestination));
    }
}


