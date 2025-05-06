package mg.working.controller;

import jakarta.servlet.http.HttpSession;
import mg.working.model.fournisseur.RQFUtils.RequestForQuotationDetail;
import mg.working.model.fournisseur.RQFUtils.RfqItem;
import mg.working.model.fournisseur.RequestForQuotation;
import mg.working.service.ErpNextRequestForQuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/erpnext/request-for-quotation")
public class ErpNextRFQController {

    @Autowired
    private ErpNextRequestForQuotationService requestForQuotationService;

    @GetMapping("/get/{supplier}")
    public String getRFQBySupplier(
            @PathVariable String supplier,
            HttpSession session,
            Model model) {
        if (session.getAttribute("sid").toString() == null) {
            return "redirect:/login"; // ou une page d’erreur
        }

        try {
            String decodedSupplier = java.net.URLDecoder.decode(supplier, StandardCharsets.UTF_8.toString());
            List<RequestForQuotation> requestForQuotations = this.requestForQuotationService
                    .getRfqsBySupplier(session.getAttribute("sid").toString() , decodedSupplier);
            model.addAttribute("requestForQuotations", requestForQuotations);
            return "/fournisseur/devis/request-for-quotation-list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

    }

    @GetMapping("/detail/{rfqName}")
    public String getRfqDetail(@PathVariable String rfqName, HttpSession session, Model model) {
        if (session.getAttribute("sid") == null) {
            return "redirect:/login";
        }

        try {
            RequestForQuotationDetail detail = requestForQuotationService.getRfqDetailByName(session.getAttribute("sid").toString(), rfqName);
            model.addAttribute("rfqDetail", detail);
            return "/fournisseur/devis/request-for-quotation-detail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/supplier-quotation/save")
    public String createSupplierQuotation(@RequestParam("rfqName") String rfqName,
                                          @RequestParam("supplier") String supplier,
                                          @RequestParam("itemCode") List<String> itemCodes,
                                          @RequestParam("newRate") List<Double> rates,
                                          @RequestParam("qty") List<Double> quantities,
                                          @RequestParam("warehouse") List<String> warehouses,
                                          Model model,
                                          HttpSession session) {
        if (session.getAttribute("sid") == null) {
            return "redirect:/login";
        }

        try {
            // Construction des RfqItem
            List<RfqItem> rfqItems = new ArrayList<>();
            for (int i = 0; i < itemCodes.size(); i++) {
                RfqItem item = new RfqItem();
                item.setItemCode(itemCodes.get(i));
                item.setRate(rates.get(i));
                item.setQty(quantities.get(i));
                item.setWarehouse(warehouses.get(i));
                rfqItems.add(item);
            }

            // Appel du service
            requestForQuotationService.createSupplierQuotation(
                    session.getAttribute("sid").toString(),
                    rfqName,
                    supplier,
                    rfqItems
            );

            return "redirect:/erpnext/suppliers";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


}
