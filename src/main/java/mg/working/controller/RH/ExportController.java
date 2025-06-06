package mg.working.controller.RH;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.service.RH.importExport.PDFExport;
import mg.working.service.RH.salaire.SalaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rh/export")
public class ExportController {
    @Autowired
    private PDFExport pdfExport;
    @Autowired
    SalaireService salaireService;

    @GetMapping("/pdf")
    public void ExportSalarySlipByNamePDF(HttpSession session ,
                                            @RequestParam("name") String name,
                                            HttpServletResponse response) throws Exception {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            response.sendRedirect("/login");
            return;
        }
        try {
            SalarySlip slip = salaireService.getSalarySlipByName(sid, name);
            pdfExport.exporterBulletinSalaire(response, slip);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
