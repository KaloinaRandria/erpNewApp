package mg.working.controller.RH;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rh/export")
public class ExportController {

    @GetMapping("/pdf")
    public String ExportSalarySlipByNamePDF(HttpSession session) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) return "redirect:/login";


        return "";
    }
}
