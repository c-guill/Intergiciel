package fr.miaou.messagerie.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/consumer")
public class ConsumerController { //TODO a déplacer dans frontend

    @Value("${spring.kafka.consumer.topic-name}")
    private String topic;

    @KafkaListener(topics = "${spring.kafka.consumer.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        System.out.println("ICI C BON:"+message);
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
