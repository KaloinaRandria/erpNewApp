package mg.working.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String goToLoginPage() {
        return "login/login";
    }

    @GetMapping("/accueil")
    public String goToAccueil() {
        return "/accueil";
    }
}
