package fr.miaou.frontend.controller;

import fr.miaou.frontend.model.Message;
import fr.miaou.frontend.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@Controller
public class HomeController {

    private String username;

    @GetMapping("/")
    public String home() {
        return "loginPage";
    }

    @GetMapping("/message")
    public String messagePage(Model model) {
        this.username = "Garfield";
        if (username == null) {
            return "redirect:/";
        }
        this.username = username;
        model.addAttribute("username", username);
        ArrayList<User> contacts = new ArrayList<>();
        contacts.add(new User("John Doe"));
        contacts.add(new User("Gary Smith"));
        contacts.add(new User("Mary Smith"));
        contacts.add(new User("John Smith"));

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message());
        model.addAttribute("contacts", contacts);
        model.addAttribute("messages", messages);
        return "messagePage";
    }

    @PostMapping("/connection")
    @ResponseBody // if returning data directly
    public boolean submitForm(@RequestParam String username) {
        // Process the received data here
        this.username = username;
        return true;
//        return "Name: " + name + ", Hidden Data: " + hiddenData; // Modify as needed
    }
}
