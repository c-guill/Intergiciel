package fr.miaou.messagerie.controller;

import fr.miaou.messagerie.model.Contact;
import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact")

public class ContactController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Contact> getAllContacts(@RequestParam long userId) {
        return userService.getAllContacts(userId);
    }

    @PostMapping
    public Contact getContact(@RequestParam String username) {
        return this.userService.getcontactByUsername(username);
    }
}
