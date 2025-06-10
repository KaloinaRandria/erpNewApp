package mg.working.service.RH.salaire;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class SalaryStructureService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    public void createSalaryStructure(
            String sid,
            String name,
            List<Earning> earnings,
            List<Deduction> deductions
    ) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("doctype", "Salary Structure");
        payload.put("name", name);
        payload.put("company", "My Company");
        payload.put("currency", "EUR");
        payload.put("payroll_frequency", "Monthly");  // e.g. "Monthly"
        payload.put("mode_of_payment", "Cash");// e.g. "Cash"
        payload.put("is_active", "Yes");


        // Ajouter earnings
        ArrayNode earningsArray = objectMapper.createArrayNode();
        for (Earning earning : earnings) {
            ObjectNode row = objectMapper.createObjectNode();
            row.put("salary_component", earning.getSalary_component());
            row.put("formula", earning.getFormula());
            earningsArray.add(row);
        }
        payload.set("earnings", earningsArray);

        // Ajouter deductions
        ArrayNode deductionsArray = objectMapper.createArrayNode();
        for (Deduction deduction : deductions) {
            ObjectNode row = objectMapper.createObjectNode();
            row.put("salary_component", deduction.getSalary_component());
            row.put("formula", deduction.getFormula());
            deductionsArray.add(row);
        }
        payload.set("deductions", deductionsArray);

        // Création
        String createUrl = erpnextUrl + "/api/resource/Salary Structure";
        HttpEntity<String> createRequest = new HttpEntity<>(payload.toString(), headers);
        ResponseEntity<String> createResponse = restTemplate.exchange(createUrl, HttpMethod.POST, createRequest, String.class);

        if (!createResponse.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erreur lors de la création du Salary Structure : " + createResponse.getBody());
        }

        String createdName = objectMapper.readTree(createResponse.getBody()).path("data").path("name").asText();

        // Récupération complète pour soumettre
        String getUrl = erpnextUrl + "/api/resource/Salary Structure/" + createdName;
        ResponseEntity<String> getResponse = restTemplate.exchange(getUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        JsonNode fullDoc = objectMapper.readTree(getResponse.getBody()).path("data");

        // Soumission avec frappe.client.submit
        String submitUrl = erpnextUrl + "/api/method/frappe.client.submit";
        ObjectNode submitPayload = objectMapper.createObjectNode();
        submitPayload.put("doc", objectMapper.writeValueAsString(fullDoc));

        HttpEntity<String> submitRequest = new HttpEntity<>(submitPayload.toString(), headers);
        ResponseEntity<String> submitResponse = restTemplate.postForEntity(submitUrl, submitRequest, String.class);

        if (!submitResponse.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erreur lors de la soumission du Salary Structure : " + submitResponse.getBody());
        }

        System.out.println("✅ Salary Structure soumis avec succès : " + createdName);
    }

}
