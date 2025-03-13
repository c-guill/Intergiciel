package fr.miaou.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miaou.frontend.model.Contact;
import fr.miaou.frontend.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    private final String api = "http://localhost:8080/"; // Replace with your API URL


    public Message getMessageFromJson(JsonNode node, Contact user, Contact contact) {
        String date;
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(node.path("date").asText());
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            date = offsetDateTime.format(outputFormatter);
        }catch (Exception e){
            Timestamp timestamp = new Timestamp(node.path("date").asLong());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            date = timestamp.toLocalDateTime().format(formatter);;
        }
        if (contact.getId() == -1) {
            user = new Contact(node.path("user").path("idUser").asLong(),
                    node.path("user").path("nom").asText());
        }
        return Message.builder()
                .sender(node.path("user").path("idUser").asLong() == user.getId() ? user : contact)
                .receiver(node.path("user").path("idUser").asLong() == user.getId() ? contact : user)
                .message(node.path("contenu").asText())
                .date(date).build();
    }


    public ArrayList<Message> getDiscussion(Contact user, Contact contact) {
        String json = restTemplate.getForObject(api + "message/discussion?idUser="+user.getId()+"&idDestination="+contact.getId(), String.class);
        ArrayList<Message> messages = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            for (JsonNode node : rootNode) {
                messages.add(this.getMessageFromJson(node, user, contact));
            }
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
            return new ArrayList<>();
        }
        return messages;
    }

    public List<Contact> getContacts(Contact user) {
        Contact[] contacts = restTemplate.getForObject(api + "/contact?userId="+user.id, Contact[].class);
        if (contacts == null) {
            return new ArrayList<>();
        }
        return List.of(contacts);
    }

    public Contact getContact(String username) {
        username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        String body = "username=" + username;
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Contact> response = restTemplate.exchange(
                this.api + "/contact", HttpMethod.POST,
                requestEntity,
                Contact.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Error: " + response.getStatusCode());
        }
    }

    public Message sendMessage(String message, Contact user, Contact contact) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = "userId=" + user.getId() + "&contenu=" + message + "&idDestination=" + contact.getId();
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                this.api + "/message",
                HttpMethod.POST,
                requestEntity,
                String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            return this.getMessageFromJson(rootNode, user, contact);
        } else {
            throw new RuntimeException("Error: " + response.getStatusCode());
        }
    }

}
