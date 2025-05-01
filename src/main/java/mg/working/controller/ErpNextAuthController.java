package mg.working.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
                                 Model model,
                                 HttpSession session) {
        ErpNextSessionInfo sessionInfo = authService.loginAndGetSessionInfo(username, password);

        session.setAttribute("sid", sessionInfo.getSid());

        System.out.println("SID " + sessionInfo.getSid());

        if (sessionInfo != null) {
            model.addAttribute("sessionInfo", sessionInfo);
//            return "redirect:/erpnext/suppliers";
            return "redirect:/accueil";
        } else {
            model.addAttribute("error", "Erreur d'authentification ERPNext.");
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // Invalider la session
        session.invalidate();

        // Supprimer le cookie 'sid'
        Cookie cookie = new Cookie("sid", null);
        cookie.setPath("/"); // Assurez-vous que le path est correct
        cookie.setMaxAge(0); // Supprime immédiatement le cookie
        response.addCookie(cookie);

        // Redirection vers la page de login
        return "redirect:/";
    }

}
