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
        try {
            ErpNextSessionInfo sessionInfo = authService.loginAndGetSessionInfo(username, password);

            // 🔐 Vérification avant d'accéder à getSid()
            if (sessionInfo != null && sessionInfo.getSid() != null) {
                session.setAttribute("sid", sessionInfo.getSid());
                return "redirect:/accueil";
            } else {
                model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
                return "login/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur de connexion ERPNext : " + e.getMessage());
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // Invalider la session Spring
        if (session != null) {
            session.invalidate();
        }

        // Supprimer le cookie 'sid' (session ERPNext)
        Cookie cookie = new Cookie("sid", "");
        cookie.setPath("/");           // Assurez-vous qu'il correspond bien à celui utilisé à la connexion
        cookie.setMaxAge(0);           // Expire immédiatement
        cookie.setHttpOnly(true);      // Meilleure sécurité
        cookie.setSecure(false);       // À mettre à true si tu es en HTTPS
        response.addCookie(cookie);

        // Rediriger vers la page de login
        return "redirect:/";
    }

}
