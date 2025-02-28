package fr.miaou.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/") // Redirige vers home.html quand on accède à "/"
    public String home() {
        return "home"; // Spring cherche automatiquement home.html dans /templates/
    }
}
