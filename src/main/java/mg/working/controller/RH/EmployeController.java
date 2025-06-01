package mg.working.controller.RH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import mg.working.model.RH.vivant.Employe;
import mg.working.service.RH.vivant.EmployeService;

@Controller
@RequestMapping("/rh/employe")
public class EmployeController {
    @Autowired
    private EmployeService employeService;



    @GetMapping("/list")
    public String listeEmployes(HttpSession session, Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            List<Employe> employes = this.employeService.listerEmployes(sid);
            model.addAttribute("employes", employes);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des employés : " + e.getMessage());
        }

        return "RH/employe/liste-employe";
    }
}

