package fr.miaou.frontend.controller;

import fr.miaou.frontend.model.Contact;
import fr.miaou.frontend.model.Message;
import fr.miaou.frontend.model.Status;
import fr.miaou.frontend.service.ApiService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @GetMapping("/")
    public String home() {
        return "loginPage";
    }

    @GetMapping("/message")
    public String messagePage() {
        return "redirect:/message/2";
    }

    @GetMapping("/message/{id}")
    public String messagePageDiscussion(@PathVariable String id, Model model, @CookieValue(value = "username", defaultValue = "") String username, @CookieValue(value = "id", defaultValue = "-1") String useridstr) {
        if (Objects.equals(username, "") || Objects.equals(useridstr, "")) {
            return "redirect:/";
        }
        try {
            long userid = Long.parseLong(useridstr);
            long targetid = Long.parseLong(id);
            Contact garfield = new Contact(userid, username, null, null, Status.AVAILABLE);
            List<Contact> contacts = this.apiService.getContacts(garfield);
            Optional<Contact> target = contacts.stream().filter(contact -> contact.getId().equals(targetid)).findFirst();
            if (target.isEmpty()) {
                return "redirect:/";
            }
            this.setCookie("targetid",target.get().getId().toString());
            ArrayList<Message> messages = this.apiService.getDiscussion(garfield, target.get());
            model.addAttribute("targetusername", target.get().getUsername());
            model.addAttribute("username", username);
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
        try {
            Contact contact = this.apiService.getContact(username);
            this.setCookie("username", contact.getUsername());
            this.setCookie("id", contact.getId().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
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

    private void setCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    // Mapped as /app/messages
    @MessageMapping("/application")  // Endpoint for messages sent to /app/notify
    @SendTo("/all/messages")  // Destination for notifications broadcast
    public Message sendMessage(final Message message) throws Exception {
        return message;  // Simple echo of the message sent
    }
}
