package mg.working.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mg.working.model.fournisseur.RequestForQuotation;
import mg.working.model.fournisseur.RequestForQuotationSupplier;
import mg.working.model.fournisseur.Supplier;
import mg.working.model.fournisseur.SupplierQuotation;
import mg.working.model.fournisseur.commande.PurchaseOrder;
import mg.working.model.fournisseur.facture.Facture;
import mg.working.service.ErpNextPurchaseInvoiceService;
import mg.working.service.ErpNextPurchaseOrderService;
import mg.working.service.ErpNextSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
@RequestMapping("/erpnext")
public class ErpNextSupplierController {

    @Autowired
    private ErpNextSupplierService supplierService;

    @Autowired
    private ErpNextPurchaseOrderService erpNextPurchaseOrderService;

    @Autowired
    private ErpNextPurchaseInvoiceService erpNextPurchaseInvoiceService;

    @GetMapping("/suppliers")
    public String getSuppliers(HttpSession session, Model model) throws Exception {
        if (session.getAttribute("sid").toString() == null) {
            return "redirect:/login"; // ou une page d’erreur
        }
        List<Supplier> suppliers = supplierService.getSuppliers(session.getAttribute("sid").toString());
        model.addAttribute("suppliers", suppliers);
        return "fournisseur/fournisseur-list";
    }

    @GetMapping("/request-for-quotation")
    public String getRQF(HttpSession session, Model model) throws Exception {
        if (session.getAttribute("sid").toString() == null) {
            return "redirect:/login"; // ou une page d’erreur
        }
        List<RequestForQuotation> rfqs = supplierService.getRequestsForQuotation(session.getAttribute("sid").toString());
        model.addAttribute("rfqs", rfqs);
        return "fournisseur/demande-devis-list";
    }

    @GetMapping("/request-for-quotation/{rfqName}")
    public String getSupplierByRfQ(@PathVariable String rfqName , HttpSession session, Model model) throws Exception {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        List<RequestForQuotationSupplier> rfqs = supplierService.getSuppliersByRfQ(sid,rfqName);
        model.addAttribute("rfqs", rfqs);
        return "fournisseur/devis-supplier-list";
    }

    @GetMapping("/supplier-quotations")
    public String listSupplierQuotations(HttpSession session, Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            List<SupplierQuotation> quotations = supplierService.getSupplierQuotations(sid);
            model.addAttribute("quotations", quotations);
            return "fournisseur/fournisseur-devis-list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/supplier-quotations/{name}")
    public String detailSupplierQuotation(@PathVariable String name, HttpSession session, Model model) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            return "redirect:/login";
        }

        try {
            SupplierQuotation quotation = supplierService.getSupplierQuotationByName(sid, name);
            model.addAttribute("quotation", quotation);
            return "fournisseur/devis-detail"; // JSP à créer
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }



    @PostMapping("/update-item-price")
    public String updateItemPrice(
            @RequestParam String itemName,
            @RequestParam String quotationName,
            @RequestParam String newRate,
            HttpSession session,
            Model model) {

        try {
            String sid = (String) session.getAttribute("sid");
            if (sid == null) {
                model.addAttribute("errorMessage", "Session expirée. Veuillez vous reconnecter.");
                return "redirect:/login"; // ou "redirect:/login" selon ta gestion d’auth
            }

            double rate = Double.parseDouble(newRate);
            supplierService.updateItemRate(sid, itemName, rate);

            model.addAttribute("success", "true");
            model.addAttribute("successMessage", "Taux mis à jour avec succès pour l'élément : " + itemName);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            model.addAttribute("success", "false");
            model.addAttribute("errorMessage", "Taux invalide : " + newRate);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success", "false");
            model.addAttribute("errorMessage", "Erreur lors de la mise à jour du taux : " + e.getMessage());
        }

        return "redirect:/erpnext/supplier-quotations/" + quotationName;
    }

    @GetMapping("/supplier-devis/{supplier}")
    public String listQuotationsBySupplier(
            @PathVariable String supplier,
            Model model , HttpSession session) {

        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            model.addAttribute("errorMessage", "Session expirée. Veuillez vous reconnecter.");
            return "redirect:/login"; // ou "redirect:/login" selon ta gestion d’auth
        }
        try {
            List<SupplierQuotation> quotations = supplierService.getSupplierQuotationsBySupplier(sid, supplier);
            model.addAttribute("quotations1", quotations);
            return "fournisseur/fournisseur-devis-list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/purchase-order/{supplier}")
    public String getAllCommande(@PathVariable String supplier ,Model model , HttpSession session) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            model.addAttribute("errorMessage", "Session expirée. Veuillez vous reconnecter.");
            return "redirect:/login"; // ou "redirect:/login" selon ta gestion d’auth
        }
        try {
            List<PurchaseOrder> purchaseOrders = erpNextPurchaseOrderService.getAllPurchaseOrders(sid , supplier);
            model.addAttribute("purchaseOrders", purchaseOrders);
            return "fournisseur/commande/purchase-order-list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/purchase-invoice")
    public String getAllFacture(Model model , HttpSession session) {
        String sid = (String) session.getAttribute("sid");
        if (sid == null) {
            model.addAttribute("errorMessage", "Session expirée. Veuillez vous reconnecter.");
            return "redirect:/login"; // ou "redirect:/login" selon ta gestion d’auth
        }
        try {
            List<Facture> factures = erpNextPurchaseInvoiceService.getFacturesPayees(sid);
            model.addAttribute("factures", factures);
            return "fournisseur/facture/purchase-invoice-list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/purchase-invoice/pay")
    public String payerFacture(
            @RequestParam String invoiceName,
            HttpSession session,
            Model model) {

        try {
            String sid = (String) session.getAttribute("sid");
            if (sid == null) {
                model.addAttribute("errorMessage", "Session expirée. Veuillez vous reconnecter.");
                return "redirect:/login";
            }

            erpNextPurchaseInvoiceService.payerFacture(sid, invoiceName);

            model.addAttribute("success", "true");
            model.addAttribute("successMessage", "Paiement effectué avec succès pour la facture : " + invoiceName);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success", "false");
            model.addAttribute("errorMessage", "Erreur lors du paiement : " + e.getMessage());
        }

        return "redirect:/erpnext/purchase-invoice";
    }


}
