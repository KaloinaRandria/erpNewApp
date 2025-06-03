package mg.working.controller.RH;

import jakarta.servlet.http.HttpSession;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.service.RH.salaire.SalaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rh/salaire")
public class SalaireController {

    @Autowired
    private SalaireService salaireService;

    @GetMapping("/salary-slip")
    public String getSalarySlipByName(HttpSession session ,
                                      @RequestParam(name = "name") String name ,
                                      Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";

        try {
            SalarySlip salarySlip = salaireService.getSalarySlipByName(sid , name);
            model.addAttribute("salarySlip", salarySlip);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
//            model.addAttribute("error", "Erreur : " + e.getMessage());
        }

        return "RH/salaire/salary-slip-detail";
    }
}
