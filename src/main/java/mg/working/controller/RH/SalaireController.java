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

import java.util.List;

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

    @GetMapping("/salary-month")
    public String getSalaryEmpByMonth(HttpSession session,
                                      @RequestParam(name = "year", required = false) Integer year,
                                      @RequestParam(name = "month", required = false) Integer month,
                                      Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";

        try {
            List<SalarySlip> salarySlips;

            if (year == null || month == null) {
                // Si aucun filtre, on récupère tous les bulletins
                salarySlips = salaireService.getAllSalarySlips(sid);
            } else {
                // Sinon, filtrer par mois/année
                salarySlips = salaireService.getSalarySlipsByMonth(sid, year, month);
                model.addAttribute("selectedYear", year);
                model.addAttribute("selectedMonth", month);
            }

            model.addAttribute("salarySlips", salarySlips);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des bulletins : " + e.getMessage());
            e.printStackTrace();
        }

        return "RH/salaire/emp-salary-month";
    }

}
