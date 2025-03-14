package fr.miaou.messagerie.controller;

import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/connection")
    public void connectUser(@RequestParam Long id) {
        this.userService.connectUser(id);
    }

    @PostMapping("/disconnect")
    public void disconnectUser(@RequestParam Long id) {
        this.userService.disconnectUser(id);
    }
}
