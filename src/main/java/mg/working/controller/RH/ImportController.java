package mg.working.controller.RH;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mg.working.service.RH.importExport.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/rh/import")
public class ImportController {

    @Autowired
    ImportService importService;

    @PostMapping("/import-data")
    public String handleImport(
            @RequestParam("fichier1") MultipartFile fichier1,
            @RequestParam("fichier2") MultipartFile fichier2,
            @RequestParam("fichier3") MultipartFile fichier3,
            HttpSession session,
            HttpServletResponse response,
            Model model) {

        try {
            String sid = (String) session.getAttribute("sid");
            if (sid == null) {
                return "redirect:/login";
            }

            if (!fichier1.isEmpty() && !fichier2.isEmpty() && !fichier3.isEmpty()) {
                String employeContent = new String(fichier1.getBytes());
                String salaryComponentAndStructContent = new String(fichier2.getBytes());
                String employeeSalary = new String(fichier3.getBytes());

                importService.sendEmployeesToFrappe(sid, employeContent, salaryComponentAndStructContent, employeeSalary);
            }

            return "/accueil";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "/error";
        }
    }

    @GetMapping("/import-page")
    public String getFormulaireDImport(HttpSession session, HttpServletResponse response, Model model) {
        try {
            String sid = (String) session.getAttribute("sid");
            if (sid == null) {
                return "redirect:/login";
            }

            return "RH/import-rh";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
