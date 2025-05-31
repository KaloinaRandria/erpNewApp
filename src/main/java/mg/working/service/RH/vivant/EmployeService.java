package mg.working.service.RH.vivant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class EmployeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

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
