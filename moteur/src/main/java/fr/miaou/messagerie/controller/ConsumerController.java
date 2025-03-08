package fr.miaou.messagerie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/consumer")
public class ConsumerController {

    @Value("${spring.kafka.consumer.topic-name}")
    private String topic;

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(json, Message.class);
            this.messageService.createMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/isalive", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    private String bem_isAlive()
    {
        String rep = "{\"reponse\":\"42\"}";
        return rep;
    }
}
