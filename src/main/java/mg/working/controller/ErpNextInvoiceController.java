package mg.working.controller;


import jakarta.servlet.http.HttpSession;
import mg.working.model.fournisseur.facture.Facture;
import mg.working.service.ErpNextPurchaseInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/erpnext/purchase-invoice")
public class ErpNextInvoiceController {

    @Autowired
    private ErpNextPurchaseInvoiceService erpNextPurchaseInvoiceService;

    @GetMapping("/detail/{invoiceName}")
    public String getFactureDetails(@PathVariable String invoiceName,
                                    HttpSession session,
                                    Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login"; // redirige si non connecté
        }
        try {
            Facture facture = erpNextPurchaseInvoiceService.getPurchaseInvoiceByName(sid, invoiceName);
            model.addAttribute("factureDetail", facture); // attribut utilisé dans la JSP
            return "/fournisseur/facture/facture-detail"; // chemin vers facture-detail.jsp
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
