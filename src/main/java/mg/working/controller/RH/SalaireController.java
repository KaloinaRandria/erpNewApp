package mg.working.controller.RH;

import jakarta.servlet.http.HttpSession;
import mg.working.model.RH.organisation.Company;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.model.RH.salaire.SalaryStructureForm;
import mg.working.model.RH.salaire.StatistiqueSalaire;
import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;
import mg.working.model.RH.vivant.Employe;
import mg.working.service.RH.salaire.ComponentService;
import mg.working.service.RH.salaire.SalaireService;
import mg.working.service.RH.salaire.SalaryStructureService;
import mg.working.service.RH.util.CompanyService;
import mg.working.service.RH.vivant.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rh/salaire")
public class SalaireController {

    @Autowired
    private SalaireService salaireService;

    @Autowired
    private SalaryStructureService salaryStructureService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private EmployeService employeService;


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

    @GetMapping("/statistique-salaire/{monthYear}")
    public String getSalarySlipsByMonthNoFilter(HttpSession session,
                                                @PathVariable(name = "monthYear") String monthYear,
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
        }
        return "RH/statistique/salaire-stat-details";
    }

    @GetMapping("/statistique-salaire")
    public String afficherStatistiquesSalaire(
            HttpSession session,
            @RequestParam(name = "year", required = false) Integer year,
            Model model) {

        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            // Si aucun 'year' fourni, on utilise l'année courante
            int selectedYear = (year != null) ? year : LocalDate.now().getYear();
            model.addAttribute("selectedYear", selectedYear);

            // Récupérer tous les bulletins
            List<SalarySlip> allSlips = salaireService.getAllSalarySlips(sid);

            // Filtrer pour ne conserver que ceux de l'année sélectionnée
            List<SalarySlip> slipsFiltered = allSlips.stream()
                    .filter(slip -> slip.getStartDate().getYear() == selectedYear)
                    .collect(Collectors.toList());

            // Regroupement par mois
            List<StatistiqueSalaire> stats = salaireService.groupSalarySlipsByMonth(slipsFiltered);

            model.addAttribute("stats", stats);
            return "RH/statistique/salaire-stat";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des statistiques : " + e.getMessage());
            return "error/index";
        }
    }

    @PostMapping("/salary-structure/save")
    public String createSalaryStructure(
            HttpSession session,
            @ModelAttribute SalaryStructureForm salaryStructureForm,
            Model model
    ) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";

        try {
            List<Earning> earnings = salaryStructureForm.getEarnings()
                    .stream()
                    .filter(e -> (e.getAmount() != null && e.getAmount() > 0) || (e.getFormula() != null && !e.getFormula().isEmpty()))
                    .collect(Collectors.toList());

            List<Deduction> deductions = salaryStructureForm.getDeductions()
                    .stream()
                    .filter(d -> (d.getAmount() != null && d.getAmount() > 0) || (d.getFormula() != null && !d.getFormula().isEmpty()))
                    .collect(Collectors.toList());

            salaryStructureService.createSalaryStructure(
                    sid,
                    salaryStructureForm.getName(),
                    earnings,
                    deductions
            );

            model.addAttribute("success", "Structure de salaire créée et soumise avec succès !");
            return "redirect:/accueil";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
            return "error/index";
        }
    }


    @GetMapping("/salary-structure")
    public String goToSalaryStructureForm(HttpSession session , Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";

        try {
            model.addAttribute("earningsComponents", componentService.getEarnings(sid));
            model.addAttribute("deductionsComponents",componentService.getDeductions(sid));
            return "RH/salaire/salary-structure-form";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des composants : " + e.getMessage());
            e.printStackTrace();
            return "error/index";
        }
    }

    @GetMapping("/salary-slip-form")
    public String goToSalarySlipForm(HttpSession session , Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            List<Employe> employes = employeService.listerEmployes(sid);
            List<SalaryStructureForm> salaryStructureForms = salaryStructureService.listSalaryStructure(sid);
            List<Company> companies = employeService.getCompanyList(sid);
            model.addAttribute("employes", employes);
            model.addAttribute("salaryStructureForms", salaryStructureForms);
            model.addAttribute("companies", companies);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur : " + e.getMessage());
        }

        return "RH/salaire/salary-slip-form";
    }

    @PostMapping("/generer-salary-slip")
    public String genererSalarySlip(HttpSession session, Model model,
                                    @RequestParam(name = "employe") String employee,
                                    @RequestParam(name = "startDate") String startDate,
                                    @RequestParam(name = "endDate") String endDate) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            salaireService.genererSalarySlip(sid , employee , startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur : " + e.getMessage());
            return "error/index";
        }

        return "redirect:/accueil";
    }
}
