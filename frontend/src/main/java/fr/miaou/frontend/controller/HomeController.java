package fr.miaou.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private String username;

    @GetMapping("/")
    public String home() {
        return "loginPage";
    }

    @GetMapping("/message")
    public String messagePage(Model model) {
        if (username == null) {
            return "redirect:/";
        }
        this.username = username;
        model.addAttribute("username", username);
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
