package mg.working.service.RH.vivant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ArrayNode;
import mg.working.model.RH.organisation.Departement;
import mg.working.model.RH.vivant.Gender;
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

        // Préparation des en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Définition des champs à récupérer
        String resource = "Employee";
        String fieldsParam = "[\"name\",\"employee_name\",\"gender\",\"designation\",\"department\",\"status\",\"date_of_joining\",\"company\",\"branch\",\"cell_number\",\"company_email\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam;
        // Construction de la requête
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des employés : " + response.getStatusCode());
        }

        // Traitement de la réponse JSON
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode dataNode = root.get("data");

        List<Employe> employes = new ArrayList<>();
        for (JsonNode node : dataNode) {
            Employe emp = new Employe();
            emp.setName(node.path("name").asText(null));
            emp.setEmployee_name(node.path("employee_name").asText(null));
            emp.setGender(node.path("gender").asText(null));
            emp.setDesignation(node.path("designation").asText(null));
            emp.setDepartment(node.path("department").asText(null));
            emp.setStatus(node.path("status").asText(null));
            emp.setDate_of_joining(node.path("date_of_joining").asText(null));
            emp.setCompany(node.path("company").asText(null));
            emp.setBranch(node.path("branch").asText(null));
            emp.setCell_number(node.path("cell_number").asText(null));
            emp.setCompany_email(node.path("company_email").asText(null));
            employes.add(emp);
        }

        return employes;
    }


    public List<Employe> searchEmployes(String sid,
                                        Optional<String> name,
                                        Optional<String> employeeName,
                                        Optional<String> gender,
                                        Optional<String> department,
                                        Optional<String> status) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "sid=" + sid);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode filtersArray = mapper.createArrayNode();

        name.ifPresent(val -> {
            if (!val.trim().isEmpty()) {
                ArrayNode filter = filtersArray.addArray();
                filter.add("Employee");
                filter.add("name");
                filter.add("like");
                filter.add("%" + val + "%");
            }
        });

        employeeName.ifPresent(val -> {
            if (!val.trim().isEmpty()) {
                ArrayNode filter = filtersArray.addArray();
                filter.add("Employee");
                filter.add("employee_name");
                filter.add("like");
                filter.add("%" + val + "%");
            }
        });

        gender.ifPresent(val -> {
            if (!val.trim().isEmpty()) {
                ArrayNode filter = filtersArray.addArray();
                filter.add("Employee");
                filter.add("gender");
                filter.add("=");
                filter.add(val);
            }
        });

        department.ifPresent(val -> {
            if (!val.trim().isEmpty()) {
                ArrayNode filter = filtersArray.addArray();
                filter.add("Employee");
                filter.add("department");
                filter.add("=");
                filter.add(val);
            }
        });

        status.ifPresent(val -> {
            if (!val.trim().isEmpty()) {
                ArrayNode filter = filtersArray.addArray();
                filter.add("Employee");
                filter.add("status");
                filter.add("=");
                filter.add(val);
            }
        });


        String filters = mapper.writeValueAsString(filtersArray);
        String fields = "[\"name\",\"employee_name\",\"gender\",\"designation\",\"department\",\"status\",\"date_of_joining\",\"company\",\"branch\",\"cell_number\",\"company_email\"]";

        String url = erpnextUrl + "/api/resource/Employee?fields=" + fields + "&filters=" + filters;

        System.out.println("URL utilisée : " + url);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la recherche des employés : " + response.getStatusCode());
        }

        JsonNode root = mapper.readTree(response.getBody());
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

    public List<Gender> listerGenres(String sid) throws Exception {

        // Préparation des en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construction de l'URL
        String url = erpnextUrl + "/api/resource/Gender";

        // Construction de la requête
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des genres : " + response.getStatusCode());
        }

        // Traitement de la réponse JSON
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode dataNode = root.get("data");

        List<Gender> genders = new ArrayList<>();
        for (JsonNode node : dataNode) {
            Gender gender = new Gender();
            gender.setName(node.path("name").asText(null));
            gender.setOwner(node.path("owner").asText(null));
            gender.setCreation(node.path("creation").asText(null));
            gender.setModified(node.path("modified").asText(null));
            gender.setModified_by(node.path("modified_by").asText(null));
            gender.setDocstatus(node.path("docstatus").asInt(0));
            gender.setIdx(node.path("idx").asInt(0));
            gender.setGender(node.path("gender").asText(null));
            genders.add(gender);
        }

        return genders;
    }

    public List<Departement> listerDepartements(String sid) throws Exception {

        // Préparation des en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construction de l'URL
        String url = erpnextUrl + "/api/resource/Department";

        // Construction de la requête
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des départements : " + response.getStatusCode());
        }

        // Traitement de la réponse JSON
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode dataNode = root.get("data");

        List<Departement> departements = new ArrayList<>();
        for (JsonNode node : dataNode) {
            Departement dept = new Departement();
            dept.setName(node.path("name").asText(null));
            dept.setOwner(node.path("owner").asText(null));
            dept.setCreation(node.path("creation").asText(null));
            dept.setModified(node.path("modified").asText(null));
            dept.setModified_by(node.path("modified_by").asText(null));
            dept.setDocstatus(node.path("docstatus").asInt(0));
            dept.setIdx(node.path("idx").asInt(0));
            dept.setDepartment_name(node.path("department_name").asText(null));
            dept.setParent_department(node.path("parent_department").asText(null));
            dept.setCompany(node.path("company").asText(null));
            dept.setIs_group(node.path("is_group").asInt(0));
            dept.setDisabled(node.path("disabled").asInt(0));
            dept.setLft(node.path("lft").asInt(0));
            dept.setRgt(node.path("rgt").asInt(0));
            dept.setOld_parent(node.path("old_parent").asText(null));
            dept.setPayroll_cost_center(node.path("payroll_cost_center").asText(null));
            dept.setLeave_block_list(node.path("leave_block_list").asText(null));
            departements.add(dept);
        }

        return departements;
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
