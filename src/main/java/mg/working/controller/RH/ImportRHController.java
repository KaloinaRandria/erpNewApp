package mg.working.controller.RH;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rh/import")
public class ImportRHController {


    
    @GetMapping("/import-page")
    public String goToImportPage() {
        return "RH/import-rh";    }
}
