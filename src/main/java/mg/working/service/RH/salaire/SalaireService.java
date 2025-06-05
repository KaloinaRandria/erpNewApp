package mg.working.service.RH.salaire;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;
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

    public List<SalarySlip> getAllSalarySlips(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Salary Slip?fields=[\"name\"]"; // seul le name suffit

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des bulletins : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");

        List<SalarySlip> slips = new ArrayList<>();
        for (JsonNode node : data) {
            String name = node.path("name").asText();
            SalarySlip slip = getSalarySlipByName(sid, name); // ✅ récupère earnings et deductions
            slips.add(slip);
        }

        return slips;
    }

    public List<SalarySlip> getSalarySlipsByEmployee(String sid, String employeeId) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String fields = "[\"name\"]"; // Récupère juste le nom (clé primaire)
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
            String name = node.path("name").asText();
            SalarySlip slip = getSalarySlipByName(sid, name); // Appelle la méthode complète
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

        // Ajouter earnings
        List<Earning> earnings = new ArrayList<>();
        JsonNode earningsNode = data.get("earnings");
        if (earningsNode != null && earningsNode.isArray()) {
            for (JsonNode e : earningsNode) {
                Earning earning = new Earning();
                earning.setSalary_component(e.path("salary_component").asText());
                earning.setAmount(e.path("amount").asDouble(0.0));
                earning.setYear_to_date(e.path("year_to_date").asDouble(0.0));
                earnings.add(earning);
            }
        }
        slip.setEarnings(earnings);

        // Ajouter deductions
        List<Deduction> deductions = new ArrayList<>();
        JsonNode deductionsNode = data.get("deductions");
        if (deductionsNode != null && deductionsNode.isArray()) {
            for (JsonNode d : deductionsNode) {
                Deduction deduction = new Deduction();
                deduction.setSalary_component(d.path("salary_component").asText());
                deduction.setAmount(d.path("amount").asDouble(0.0));
                deduction.setYear_to_date(d.path("year_to_date").asDouble(0.0));
                deductions.add(deduction);
            }
        }
        slip.setDeductions(deductions);

        return slip;
    }

    public List<SalarySlip> getSalarySlipsByMonth(String sid, int year, int month) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        String filters = String.format("[[\"Salary Slip\", \"start_date\", \">=\", \"%s\"], [\"Salary Slip\", \"end_date\", \"<=\", \"%s\"]]",
                startDate, endDate);

        String url = erpnextUrl + "/api/resource/Salary Slip?fields=[\"name\"]&filters=" + filters;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des bulletins de salaire : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");

        List<SalarySlip> slips = new ArrayList<>();
        for (JsonNode node : data) {
            String name = node.path("name").asText();
            SalarySlip slip = getSalarySlipByName(sid, name); // ✅ earnings + deductions inclus
            slips.add(slip);
        }

        return slips;
    }

}
