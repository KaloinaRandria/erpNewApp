package mg.working.service.RH.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.RH.organisation.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class CompanyService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;


    public List<Company> getCompanyList(String sid) throws Exception {
        // Préparation des en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Définition des champs à récupérer
        String resource = "Company";
        String fieldsParam = "[\"name\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam + "&limit_page_length=2500" ;
        // Construction de la requête
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des employés : " + response.getStatusCode());
        }

        // Traitement de la réponse JSON
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode dataNode = root.get("data");

        List<Company> companyList = new ArrayList<>();
        for (JsonNode companyNode : dataNode) {
            Company company = new Company();
            company.setName(companyNode.get("name").asText());
            companyList.add(company);
        }
        return companyList;
    }
}
