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

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
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
//            e.printStackTrace();
            model.addAttribute("error", "Erreur : " + e.getMessage());
            return "error/index";
        }

        return "RH/salaire/salary-slip-detail";
    }

    @GetMapping("/salary-month")
    public String getSalaryEmpByMonth(HttpSession session,
                                      @RequestParam(name = "monthYear", required = false) String monthYear,
                                      Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";

        try {
            List<SalarySlip> salarySlips;

            Integer selectedYear = null;
            Integer selectedMonth = null;

            if (monthYear != null && !monthYear.isEmpty()) {
                try {
                    YearMonth ym = YearMonth.parse(monthYear); // format yyyy-MM
                    selectedYear = ym.getYear();
                    selectedMonth = ym.getMonthValue();

                    salarySlips = salaireService.getSalarySlipsByMonth(sid, selectedYear, selectedMonth);
                    model.addAttribute("selectedYear", selectedYear);
                    model.addAttribute("selectedMonth", selectedMonth);
                } catch (DateTimeParseException e) {
                    model.addAttribute("error", "Le format de la date est invalide.");
                    salarySlips = salaireService.getAllSalarySlips(sid);
                }
            } else {
                salarySlips = salaireService.getAllSalarySlips(sid);
            }

            model.addAttribute("salarySlips", salarySlips);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des bulletins : " + e.getMessage());
            return "error/index";
//            e.printStackTrace();
        }

        return "RH/salaire/emp-salary-month";
    }

}
