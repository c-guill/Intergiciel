package fr.miaou.messagerie.controller;

import fr.miaou.messagerie.engine.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/insa")
public class ProducterController {

    private final Producer producer;

    private final String service=this.getClass().getName();

    @Autowired
    ProducterController(Producer producer) {this.producer = producer;}

    @GetMapping(value = "/isalive", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    private String bem_isAlive()
    {
        String rep = "{\"reponse\":\"42\"}";
        return rep;
    }

    @PostMapping(value = "/kppublish")
    public ResponseEntity<Object> sendMessageToKafkaTopic(@RequestBody() String message)
    {

        this.producer.sendMessage(message);  //envoyer message lu dans le body vers Kafka vers le topic paramètrè sur le service

        return new ResponseEntity<>("{\"reponse\":\"Inserted\"}", HttpStatus.CREATED);  //retourner réussite en cas d'échec c'est le gestionnaire d'exceptions qui prend le relais.
    }

}
