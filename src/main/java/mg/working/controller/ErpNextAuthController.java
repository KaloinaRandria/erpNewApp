package mg.working.controller;

import mg.working.model.Auth.ErpNextSessionInfo;
import mg.working.service.ErpNextAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/erpnext")
public class ErpNextAuthController {

    @Autowired
    private ErpNextAuthService authService;

    @PostMapping("/session-info")
    public String getSessionInfo(@RequestParam(name = "user") String username,
                                 @RequestParam(name = "pwd") String password,
                                 Model model) {
        ErpNextSessionInfo sessionInfo = authService.loginAndGetSessionInfo(username, password);

        if (sessionInfo != null) {
            model.addAttribute("sessionInfo", sessionInfo);
            return "/succes-login";
        } else {
            model.addAttribute("error", "Erreur d'authentification ERPNext.");
            return "login/login";
        }
    }
}
