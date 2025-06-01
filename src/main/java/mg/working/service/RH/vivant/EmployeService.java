package mg.working.service.RH.vivant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mg.working.model.RH.vivant.Employe;

@Service
public class EmployeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    public List<Employe> listerEmployes(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "sid=" + sid);

        String fields = "[\"name\",\"employee_name\",\"gender\",\"designation\",\"department\",\"status\",\"date_of_joining\",\"company\",\"branch\",\"cell_number\",\"company_email\"]";
        String url = erpnextUrl + "/api/resource/Employee?fields=" + URLEncoder.encode(fields, StandardCharsets.UTF_8);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("URL : " + url);
        System.out.println("Emp body : " + response.getBody());

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des employés : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");



        List<Employe> employes = new ArrayList<>();
        for (JsonNode node : data) {
            Employe emp = new Employe();
            emp.setName(node.path("name").asText());
            emp.setEmployee_name(node.path("employee_name").asText());
            emp.setGender(node.path("gender").asText());
            emp.setDesignation(node.path("designation").asText());
            emp.setDepartment(node.path("department").asText());
            emp.setStatus(node.path("status").asText());
            emp.setDate_of_joining(node.path("date_of_joining").asText());
            emp.setCompany(node.path("company").asText());
            emp.setBranch(node.path("branch").asText());
            emp.setCell_number(node.path("cell_number").asText());
            emp.setCompany_email(node.path("company_email").asText());
            employes.add(emp);
        }

        return employes;
    }



    public List<String> importerEmployesDepuisCSV(String sid, String fileName) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<String> employesCrees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String ligne;
            boolean estPremier = true;

            while ((ligne = reader.readLine()) != null) {
                if (estPremier) { // Ignorer l'en-tête
                    estPremier = false;
                    continue;
                }

                String[] parties = ligne.split(",");

                if (parties.length < 5) {
                    throw new IllegalArgumentException("Format CSV incorrect. Chaque ligne doit contenir : nom, prénom, sexe, date_naissance, entreprise");
                }

                String nom = parties[0].trim();
                String prenom = parties[1].trim();
                String genre = parties[2].trim();
                String dateNaissance = parties[3].trim();
                String entreprise = parties[4].trim();

                ObjectNode employeJson = objectMapper.createObjectNode();
                employeJson.put("doctype", "Employee");
                employeJson.put("first_name", prenom);
                employeJson.put("last_name", nom);
                employeJson.put("gender", genre);
                employeJson.put("date_of_birth", dateNaissance);
                employeJson.put("company", entreprise);

                HttpEntity<String> request = new HttpEntity<>(employeJson.toString(), headers);

                ResponseEntity<String> response = restTemplate.postForEntity(
                        erpnextUrl + "/api/resource/Employee",
                        request,
                        String.class
                );

                if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                    employesCrees.add(nom + " " + prenom);
                } else {
                    throw new RuntimeException("Erreur lors de la création de l'employé : " + response.getStatusCode() + "\n" + response.getBody());
                }
            }
        }

        return employesCrees;
    }

}
