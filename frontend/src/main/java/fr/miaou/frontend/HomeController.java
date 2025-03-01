package fr.miaou.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // Redirige vers home.html quand on accède à "/"
    public String home() {
        return "home"; // Spring cherche automatiquement home.html dans /templates/
    }
}
