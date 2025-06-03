package mg.working.service.RH.salaire;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.RH.salaire.SalarySlip;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaireService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    public List<SalarySlip> getSalarySlipsByEmployee(String sid, String employeeId) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Encoder l'URL avec les bons paramètres
        String fields = "[\"*\"]";
        String filters = "[[\"employee\",\"=\",\"" + employeeId + "\"]]";

        String url = erpnextUrl + "/api/resource/Salary Slip?fields=" + fields + "&filters=" + filters;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des Salary Slips : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode dataArray = root.get("data");

        List<SalarySlip> slips = new ArrayList<>();

        for (JsonNode node : dataArray) {
            SalarySlip slip = new SalarySlip();

            slip.setName(node.path("name").asText(null));
            slip.setEmployee(node.path("employee").asText(null));
            slip.setEmployeeName(node.path("employee_name").asText(null));
            slip.setCompany(node.path("company").asText(null));
            slip.setDepartment(node.path("department").asText(null));
            slip.setDesignation(node.path("designation").asText(null));
            slip.setPostingDate(LocalDate.parse(node.path("posting_date").asText(null)));
            slip.setCurrency(node.path("currency").asText(null));
            slip.setStartDate(LocalDate.parse(node.path("start_date").asText(null)));
            slip.setEndDate(LocalDate.parse(node.path("end_date").asText(null)));
            slip.setGrossPay(node.path("gross_pay").asDouble(0.0));
            slip.setTotalDeduction(node.path("total_deduction").asDouble(0.0));
            slip.setNetPay(node.path("net_pay").asDouble(0.0));
            slip.setStatus(node.path("status").asText(null));
            slip.setSalaryStructure(node.path("salary_structure").asText(null));

            slips.add(slip);
        }

        return slips;
    }

    public SalarySlip getSalarySlipByName(String sid, String name) throws Exception {
        // Préparer les en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Définir les champs à récupérer (ajuste si nécessaire)
        String fieldsParam = "[\"name\",\"employee\",\"employee_name\",\"company\",\"department\",\"designation\",\"posting_date\",\"start_date\",\"end_date\",\"gross_pay\",\"total_deduction\",\"net_pay\",\"status\"]";

        // Construire l'URL de la requête
        String resource = "Salary Slip";
        String url = erpnextUrl + "/api/resource/" + resource + "/" + name + "?fields=" + fieldsParam;
        System.out.println("URL : " + url);


        // Construire la requête HTTP
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération du Salary Slip : " + response.getStatusCode());
        }

        // Lire le corps de la réponse
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");

        // Mapper les données dans un objet SalarySlip
        SalarySlip slip = new SalarySlip();
        slip.setName(data.path("name").asText(null));
        slip.setEmployee(data.path("employee").asText(null));
        slip.setEmployeeName(data.path("employee_name").asText(null));
        slip.setCompany(data.path("company").asText(null));
        slip.setDepartment(data.path("department").asText(null));
        slip.setDesignation(data.path("designation").asText(null));
        slip.setPostingDate(LocalDate.parse(data.path("posting_date").asText(null)));
        slip.setStartDate(LocalDate.parse(data.path("start_date").asText(null)));
        slip.setEndDate(LocalDate.parse(data.path("end_date").asText(null)));
        slip.setGrossPay(data.path("gross_pay").asDouble(0.0));
        slip.setTotalDeduction(data.path("total_deduction").asDouble(0.0));
        slip.setNetPay(data.path("net_pay").asDouble(0.0));
        slip.setStatus(data.path("status").asText(null));
        slip.setSalaryStructure(data.path("salary_structure").asText(null));

        return slip;
    }

}
