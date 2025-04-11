package fr.fisa.webservicedemo.controller;

import fr.fisa.webservicedemo.model.BodyMessage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monservice")
public class MonService {

    @GetMapping("/echo/{nom}")
    public String echo(@PathVariable String nom) {
        return "ECHO: " + nom;
    }

    @PostMapping("/hello")
    public String hello(@RequestBody BodyMessage body) {
        return "Hello " + body.getNom() + "!";
    }
}
