package mg.working.controller.RH;

import java.util.List;
import java.util.Optional;

import mg.working.model.RH.organisation.Departement;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.model.RH.vivant.Gender;
import mg.working.service.RH.salaire.SalaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import mg.working.model.RH.vivant.Employe;
import mg.working.service.RH.vivant.EmployeService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rh/employe")
public class EmployeController {
    @Autowired
    private EmployeService employeService;

    @Autowired
    private SalaireService salaireService;


    @GetMapping("/list")
    public String listeEmployes(HttpSession session, Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            List<Employe> employes = this.employeService.listerEmployes(sid);
            List<Gender> genders = this.employeService.listerGenres(sid);
            List<Departement> departements = this.employeService.listerDepartements(sid);

            model.addAttribute("employes", employes);
            model.addAttribute("genders", genders);
            model.addAttribute("departements", departements);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des employés : " + e.getMessage());
            return "error/index";
        }

        return "RH/employe/liste-employe";
    }

    @GetMapping("/search")
    public String rechercherEmployes(HttpSession session,
                                     @RequestParam Optional<String> name,
                                     @RequestParam Optional<String> employeeName,
                                     @RequestParam Optional<String> gender,
                                     @RequestParam Optional<String> department,
                                     @RequestParam Optional<String> status,
                                     @RequestParam Optional<String> startDate,
                                     @RequestParam Optional<String> endDate,
                                     Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            List<Employe> employes = employeService.searchEmployes(
                    sid, name, employeeName, gender, department, status, startDate, endDate
            );

            List<Gender> genders = employeService.listerGenres(sid);
            List<Departement> departements = employeService.listerDepartements(sid);

            model.addAttribute("employes", employes);
            model.addAttribute("genders", genders);
            model.addAttribute("departements", departements);

            // Conserver les valeurs du formulaire
            model.addAttribute("nameValue", name.orElse(""));
            model.addAttribute("employeeNameValue", employeeName.orElse(""));
            model.addAttribute("genderValue", gender.orElse(""));
            model.addAttribute("departmentValue", department.orElse(""));
            model.addAttribute("statusValue", status.orElse(""));
            model.addAttribute("startDateValue", startDate.orElse(""));
            model.addAttribute("endDateValue", endDate.orElse(""));

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la recherche des employés : " + e.getMessage());
            return "error/index";
        }

        return "RH/employe/liste-employe";
    }

    @GetMapping("/fiche/{name}")
    public String afficherFicheEmploye(HttpSession session,
                                       @PathVariable("name") String name,
                                       Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";

        try {
            Employe employe = employeService.getEmployeByName(sid, name);
            List<SalarySlip> salarySlips = salaireService.getSalarySlipsByEmployee(sid , name);

            model.addAttribute("employe", employe);
            model.addAttribute("salarySlips", salarySlips);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
            return "error/index";
        }

        return "RH/employe/fiche-employe";
    }


}

