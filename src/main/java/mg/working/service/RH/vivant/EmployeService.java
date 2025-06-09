package mg.working.service.RH.vivant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ArrayNode;
import mg.working.model.RH.organisation.Departement;
import mg.working.model.RH.salaire.SalarySlip;
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

    public Employe getEmployeByName(String sid, String name) throws Exception {

        // Préparer les en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Définir les champs à récupérer
        String resource = "Employee";
        String fieldsParam = "[\"name\",\"employee_name\",\"gender\",\"designation\",\"department\",\"status\",\"date_of_joining\",\"company\",\"branch\",\"cell_number\",\"company_email\"]";

        // URL pour accéder à un employé spécifique
        String url = erpnextUrl + "/api/resource/" + resource + "/" + name + "?fields=" + fieldsParam + "&limit_page_length=2500" ;

        // Construire la requête
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération de l'employé : " + response.getStatusCode());
        }

        // Traitement de la réponse JSON
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");

        Employe emp = new Employe();
        emp.setName(data.path("name").asText(null));
        emp.setEmployee_name(data.path("employee_name").asText(null));
        emp.setGender(data.path("gender").asText(null));
        emp.setDesignation(data.path("designation").asText(null));
        emp.setDepartment(data.path("department").asText(null));
        emp.setStatus(data.path("status").asText(null));
        emp.setDate_of_joining(data.path("date_of_joining").asText(null));
        emp.setCompany(data.path("company").asText(null));
        emp.setBranch(data.path("branch").asText(null));
        emp.setCell_number(data.path("cell_number").asText(null));
        emp.setCompany_email(data.path("company_email").asText(null));

        return emp;
    }



    public List<Employe> searchEmployes(String sid,
                                        Optional<String> name,
                                        Optional<String> employeeName,
                                        Optional<String> gender,
                                        Optional<String> department,
                                        Optional<String> status,
                                        Optional<String> startDate,
                                        Optional<String> endDate) throws Exception {
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

        // ✅ Ajout des filtres sur la date d'embauche (date_of_joining)
        if (startDate.isPresent() && !startDate.get().trim().isEmpty()) {
            ArrayNode filter = filtersArray.addArray();
            filter.add("Employee");
            filter.add("date_of_joining");
            filter.add(">=");
            filter.add(startDate.get());
        }

        if (endDate.isPresent() && !endDate.get().trim().isEmpty()) {
            ArrayNode filter = filtersArray.addArray();
            filter.add("Employee");
            filter.add("date_of_joining");
            filter.add("<=");
            filter.add(endDate.get());
        }

        String filters = mapper.writeValueAsString(filtersArray);
        String fields = "[\"name\",\"employee_name\",\"gender\",\"designation\",\"department\",\"status\",\"date_of_joining\",\"company\",\"branch\",\"cell_number\",\"company_email\"]";

        String url = erpnextUrl + "/api/resource/Employee?fields=" + fields + "&filters=" + filters + "&limit_page_length=2500";


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

    public void createEmployee(String sid, String prenom, String nom, String employeeNumber, LocalDate dateOfBirth, LocalDate dateOfJoining, String gender) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Préparer le corps JSON
        ObjectNode employeeJson = objectMapper.createObjectNode();
        employeeJson.put("doctype", "Employee");
        employeeJson.put("employee_name", prenom + " " + nom);
        employeeJson.put("first_name", prenom);
        employeeJson.put("last_name", nom);
        employeeJson.put("employee_number", employeeNumber);
        employeeJson.put("company", "My Company");
        employeeJson.put("gender", gender); // "Male" ou "Female"
        employeeJson.put("date_of_birth", dateOfBirth.toString());
        employeeJson.put("date_of_joining", dateOfJoining.toString());
        employeeJson.put("status", "Active");

        // URL de création
        String url = erpnextUrl + "/api/resource/Employee";
        HttpEntity<String> request = new HttpEntity<>(employeeJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("❌ Échec de la création de l'employé : " + response.getBody());
        }

        String empName = objectMapper.readTree(response.getBody()).path("data").path("name").asText();
        System.out.println("✅ Employé créé avec succès : " + empName);
    }

}
