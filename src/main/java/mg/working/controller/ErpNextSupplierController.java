package mg.working.controller;

import jakarta.servlet.http.HttpSession;
import mg.working.model.fournisseur.Supplier;
import mg.working.service.ErpNextSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/erpnext")
public class ErpNextSupplierController {

    @Autowired
    private ErpNextSupplierService supplierService;

    @GetMapping("/suppliers")
    public String getSuppliers(HttpSession session, Model model) throws Exception {
        if (session.getAttribute("sid").toString() == null) {
            return "redirect:/login"; // ou une page d’erreur
        }
        List<Supplier> suppliers = supplierService.getSuppliers(session.getAttribute("sid").toString());
        model.addAttribute("suppliers", suppliers);
        return "fournisseur/fournisseur-list";
    }



}
