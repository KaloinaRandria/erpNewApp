package mg.working.service.RH.salaire;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComponentService {

    private final RestTemplate restTemplate  = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    public List<Earning> getEarnings(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Salary Component?filters=[[\"type\", \"=\", \"Earning\"]]&fields=[\"name\"]";

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erreur lors de la récupération des composants Earning : " + response.getBody());
        }

        JsonNode data = objectMapper.readTree(response.getBody()).path("data");
        List<Earning> earnings = new ArrayList<>();
        for (JsonNode node : data) {
            Earning e = new Earning();
            e.setSalary_component(node.path("name").asText());
            earnings.add(e);
        }

        return earnings;
    }

    public List<Deduction> getDeductions(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Salary Component?filters=[[\"type\", \"=\", \"Deduction\"]]&fields=[\"name\"]";

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Erreur lors de la récupération des composants Deduction : " + response.getBody());
        }

        JsonNode data = objectMapper.readTree(response.getBody()).path("data");
        List<Deduction> deductions = new ArrayList<>();
        for (JsonNode node : data) {
            Deduction de = new Deduction();
            de.setSalary_component(node.path("name").asText());
            deductions.add(de);
        }
        return deductions;
    }
}
