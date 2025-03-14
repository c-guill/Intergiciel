package fr.miaou.frontend.controller;

import fr.miaou.frontend.model.Contact;
import fr.miaou.frontend.model.Message;
import fr.miaou.frontend.service.ApiService;
import fr.miaou.frontend.service.KafkaDynamicConsumerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    public ApiService apiService;

    @Autowired
    HttpServletResponse response;

    @Autowired
    private KafkaDynamicConsumerService kafkaDynamicConsumerService;

    @GetMapping("/")
    public String home() {
        return "loginPage";
    }

    @GetMapping("/message")
    public String messagePage() {
        return "redirect:/message/0";
    }


    @GetMapping("/message/{id}")
    public String messagePageDiscussion(@PathVariable String id, Model model, @CookieValue(value = "username", defaultValue = "") String username, @CookieValue(value = "id", defaultValue = "-1") String useridstr) {
        if (Objects.equals(username, "") || Objects.equals(useridstr, "")) {
            return "redirect:/";
        }
        try {
            long userid = Long.parseLong(useridstr);
            long targetid = Long.parseLong(id);
            if (userid == -1 && targetid != 0) {
                return "redirect:/";
            }
            this.kafkaDynamicConsumerService.startListening(useridstr);
            Contact garfield = new Contact(userid, username, null, null);
            List<Contact> contacts = this.apiService.getContacts(garfield);
            Optional<Contact> target;
            if (targetid == 0) {
                target = Optional.of(new Contact(-1L, "Général"));
            } else {
                target = contacts.stream().filter(contact -> contact.getId().equals(targetid)).findFirst();
            }
            if (target.isEmpty()) {
                return "redirect:/";
            }
            this.setCookie("targetid",target.get().getId().toString());
            ArrayList<Message> messages = this.apiService.getDiscussion(garfield, target.get());
            model.addAttribute("targetusername", target.get().getUsername());
            model.addAttribute("username", username);
            model.addAttribute("userid", userid);
            model.addAttribute("contacts", contacts);
            model.addAttribute("messages", messages);
            return "messagePage";
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/connection")
    @ResponseBody
    public boolean submitForm(@RequestParam String username) {
        if (username.isEmpty()) {
            this.setCookie("username", "Anonyme");
            this.setCookie("id", "-1");
            return true;
        }
        try {
            Contact contact = this.apiService.getContact(username);
            this.setCookie("username", contact.getUsername());
            this.setCookie("id", contact.getId().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @DeleteMapping("/disconnect")
    @ResponseBody
    public void disconnect() {
        this.setCookie("username", null);
        this.setCookie("id", null);
        this.setCookie("targetid", null);
    }

    private void setCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @PostMapping("sendmessage")
    @ResponseBody
    public ResponseEntity<Message> sendMessage(@RequestParam String message, @CookieValue(value = "id", defaultValue = "") String useridstr, @CookieValue(value = "username", defaultValue = "") String username, @CookieValue(value = "targetid", defaultValue = "") String targetidstr) {
        try {
            long userid = Long.parseLong(useridstr);
            long targetid = Long.parseLong(targetidstr);
            return ResponseEntity.ok(this.apiService.sendMessage(message, new Contact(userid, username), new Contact(targetid, "notneeded")));
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
