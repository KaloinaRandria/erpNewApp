package mg.working.controller;

import mg.working.service.ErpNextSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/erpnext")
public class ErpNextSupplierController {

    @Autowired
    private ErpNextSupplierService supplierService;

    @GetMapping("/suppliers")
    @ResponseBody
    public String getSuppliers(@CookieValue(name = "sid", required = true) String sid) {
        return supplierService.getSuppliers(sid);
    }
}
