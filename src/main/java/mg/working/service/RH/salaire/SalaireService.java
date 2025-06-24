package mg.working.service.RH.salaire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.model.RH.salaire.StatistiqueSalaire;
import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SalaireService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    @Autowired
    DataSource dataSource;

    private static final SimpleDateFormat monthFormatter = new SimpleDateFormat("yyyy-MM");

    public List<SalarySlip> getAllSalarySlips(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Salary Slip?fields=[\"name\",\"docstatus\"]&limit_page_length=2500"; // seul le name suffit

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des bulletins : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");

        List<SalarySlip> slips = new ArrayList<>();
        for (JsonNode node : data) {
            if (node.path("docstatus").asInt() != 2) {
                String name = node.path("name").asText();
                SalarySlip slip = getSalarySlipByName(sid, name); // ✅ récupère earnings et deductions
                slips.add(slip);
            }
        }

        return slips;
    }

    public List<SalarySlip> getSalarySlipsByEmployee(String sid, String employeeId) throws Exception  {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String fields = "[\"name\",\"docstatus\"]"; // Récupère juste le nom (clé primaire)
        String filters = "[[\"employee\",\"=\",\"" + employeeId + "\"]]";
        String url = erpnextUrl + "/api/resource/Salary Slip?fields=" + fields + "&filters=" + filters + "&limit_page_length=2500";

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des Salary Slips : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode dataArray = root.get("data");

        List<SalarySlip> slips = new ArrayList<>();

        for (JsonNode node : dataArray) {
            if (node.path("docstatus").asInt() != 2) {
                String name = node.path("name").asText();
                SalarySlip slip = getSalarySlipByName(sid, name); // ✅ récupère earnings et deductions
                slips.add(slip);
            }
        }

        return slips;
    }

    public SalarySlip getSalarySlipByName(String sid, String name) throws Exception {
        // Préparer les en-têtes HTTP avec le cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Définir les champs à récupérer (ajuste si nécessaire)
        String fieldsParam = "[\"name\",\"employee\",\"employee_name\",\"company\",\"department\",\"designation\",\"posting_date\",\"start_date\",\"end_date\",\"gross_pay\",\"total_deduction\",\"net_pay\",\"status\"]&limit_page_length=2500";

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

        String filters = String.format("[[\"Salary Slip\", \"start_date\", \">=\", \"%s\"], [\"Salary Slip\", \"end_date\", \"<=\", \"%s\"]]&limit_page_length=2500",
                startDate, endDate);

        String url = erpnextUrl + "/api/resource/Salary Slip?fields=[\"name\",\"docstatus\"]&filters=" + filters;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération des bulletins de salaire : " + response.getStatusCode());
        }

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode data = root.get("data");

        List<SalarySlip> slips = new ArrayList<>();
        for (JsonNode node : data) {
            if (node.path("docstatus").asInt() != 2) {
                String name = node.path("name").asText();
                SalarySlip slip = getSalarySlipByName(sid, name); // ✅ récupère earnings et deductions
                slips.add(slip);
            }
        }

        return slips;
    }

    public List<StatistiqueSalaire> groupSalarySlipsByMonth(List<SalarySlip> slips) {
        Map<String, StatistiqueSalaire> groupedStats = new HashMap<>();

        for (SalarySlip slip : slips) {
            String month = monthFormatter.format(slip.getStartDateString());

            StatistiqueSalaire stat;

            // Vérifie si le mois existe déjà
            if (groupedStats.containsKey(month)) {
                stat = groupedStats.get(month);
            } else {
                stat = new StatistiqueSalaire();
                stat.setMonth(month);
                stat.setEarnings(new ArrayList<>());
                stat.setDeductions(new ArrayList<>());
                stat.setGrossTotal(0);
                stat.setNetTotal(0);
                stat.setDeductionTotal(0);
                groupedStats.put(month, stat);
            }

            // === Gérer les earnings ===
            List<Earning> existingEarnings = stat.getEarnings();
            for (Earning e : slip.getEarnings()) {
                boolean found = false;
                for (Earning ex : existingEarnings) {
                    if (ex.getSalary_component().equals(e.getSalary_component())) {
                        ex.setAmount(ex.getAmount() + e.getAmount());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Earning newEarning = new Earning();
                    newEarning.setSalary_component(e.getSalary_component());
                    newEarning.setAmount(e.getAmount());
                    existingEarnings.add(newEarning);
                }
            }

            // === Gérer les deductions ===
            List<Deduction> existingDeductions = stat.getDeductions();
            for (Deduction d : slip.getDeductions()) {
                boolean found = false;
                for (Deduction ex : existingDeductions) {
                    if (ex.getSalary_component().equals(d.getSalary_component())) {
                        ex.setAmount(ex.getAmount() + d.getAmount());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Deduction newDeduction = new Deduction();
                    newDeduction.setSalary_component(d.getSalary_component());
                    newDeduction.setAmount(d.getAmount());
                    existingDeductions.add(newDeduction);
                }
            }

            // === Ajouter les totaux ===
            stat.setGrossTotal(stat.getGrossTotal() + slip.getGrossPay());
            stat.setNetTotal(stat.getNetTotal() + slip.getNetPay());
            stat.setDeductionTotal(stat.getDeductionTotal() + slip.getTotalDeduction());
        }

        List<StatistiqueSalaire> result = new ArrayList<>(groupedStats.values());
        result.sort(Comparator.comparing(StatistiqueSalaire::getMonth));

        return result;
    }

    public void insertSalarySlip(String sid, String employee, String structure, String startDate, String endDate , String ssa) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String company = "Orinasa SA";

        String data = "{" +
                "\"doctype\": \"Salary Slip\"," +
                "\"employee\": \"" + employee + "\"," +
                "\"salary_structure\": \"" + structure + "\"," +
                "\"salary_structure_assignment\": \"" + ssa + "\"," +
                "\"start_date\": \"" + startDate + "\"," +
                "\"end_date\": \"" + endDate + "\"," +
                "\"payroll_frequency\": \"Monthly\"," +
                "\"company\": \"" + company + "\"" +
                "}";

        String createUrl = erpnextUrl + "/api/resource/Salary Slip";
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        try {
            // 1. Créer le Salary Slip
            ResponseEntity<String> response = restTemplate.exchange(
                    createUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            System.out.println("✔ Salary Slip créé : " + response.getBody());

            // Après création :
            String body = response.getBody();
            JSONObject json = new JSONObject(body);
            JSONObject fullDoc = json.getJSONObject("data");

// Soumettre le Salary Slip
            String submitUrl = erpnextUrl + "/api/method/frappe.client.submit";

// Important : Frapper veut un champ "doc" contenant un JSON complet de l'objet
            JSONObject payload = new JSONObject();
            payload.put("doc", fullDoc);

            HttpEntity<String> submitRequest = new HttpEntity<>(payload.toString(), headers);

            ResponseEntity<String> submitResponse = restTemplate.exchange(
                    submitUrl,
                    HttpMethod.POST,
                    submitRequest,
                    String.class
            );

            System.out.println("✔ Salary Slip soumis : " + submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String insertSalaryStructureAssignment(String sid, String employee, String salaryStructure, String startDate, double base) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String company = "Orinasa SA";

        // Construction du JSON pour l'insertion
        String data = "{" +
                "\"doctype\": \"Salary Structure Assignment\"," +
                "\"employee\": \"" + employee + "\"," +
                "\"salary_structure\": \"" + salaryStructure + "\"," +
                "\"from_date\": \"" + startDate + "\"," +
                "\"base\": " + base + "," +
                "\"company\": \"" + company + "\"" +
                "}";

        String createUrl = erpnextUrl + "/api/resource/Salary Structure Assignment";
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        try {
            // Étape 1 : Création
            ResponseEntity<String> response = restTemplate.exchange(
                    createUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            System.out.println("✔ Salary Structure Assignment créé : " + response.getBody());

            // Étape 2 : Soumission du document (optionnel)
            String body = response.getBody();
            JSONObject json = new JSONObject(body);
            JSONObject fullDoc = json.getJSONObject("data");

            String submitUrl = erpnextUrl + "/api/method/frappe.client.submit";

            JSONObject payload = new JSONObject();
            payload.put("doc", fullDoc);

            HttpEntity<String> submitRequest = new HttpEntity<>(payload.toString(), headers);

            ResponseEntity<String> submitResponse = restTemplate.exchange(
                    submitUrl,
                    HttpMethod.POST,
                    submitRequest,
                    String.class
            );

            if (submitResponse.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(submitResponse.getBody());
                return root.path("data").path("name").asText();
            }

            System.out.println("✔ Salary Structure Assignment soumis : " + submitResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void generateSalarySlips(String sid, String employeeId, String salaryStructure, String startMonth, String endMonth) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate start = YearMonth.parse(startMonth, monthFormatter).atDay(1);
        LocalDate end = YearMonth.parse(endMonth, monthFormatter).atEndOfMonth();

        LocalDate current = start;
        while (!current.isAfter(end)) {
            YearMonth ym = YearMonth.from(current);
            LocalDate startDate = ym.atDay(1);
            LocalDate endDate = ym.atEndOfMonth();

            String sDate = startDate.format(dateFormatter);
            String eDate = endDate.format(dateFormatter);

            try {
                String ssa = this.getClosestSalaryAssignementId(sid , employeeId , startDate.toString());
                System.out.println("➡ Tentative de création Salary Slip pour " + ym.getMonth() + " " + ym.getYear());
                insertSalarySlip(sid, employeeId, salaryStructure, sDate, eDate, ssa);
            } catch (Exception e) {
                if (e.getMessage().contains("already exists") || e.getMessage().contains("Duplicate")) {
                    System.out.println("⚠ Salary Slip déjà existant pour " + ym.getMonth() + " " + ym.getYear() + ", ignoré.");
                } else {
                    System.out.println("❌ Erreur inconnue pour " + ym.getMonth() + ": " + e.getMessage());
                }
            }

            current = current.plusMonths(1);
        }
    }

    public void cancelSalarySlip(String sid , String salarySlipName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestData = "{" +
                "\"doctype\": \"Salary Slip\"," +
                "\"name\": \"" + salarySlipName + "\"" +
                "}";
        String url = erpnextUrl + "/api/method/frappe.desk.form.save.cancel";
        HttpEntity<String> request = new HttpEntity<>(requestData, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
              url,
              HttpMethod.POST,
              request,
              String.class
            );

            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelSalaryAssignment(String sid , String salaryAssignmentName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestData = "{" +
                "\"doctype\": \"Salary Structure Assignment\"," +
                "\"name\": \"" + salaryAssignmentName + "\"" +
                "}";
        String url = erpnextUrl + "/api/method/frappe.desk.form.save.cancel";
        HttpEntity<String> request = new HttpEntity<>(requestData, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SalarySlip> getSalaryFiltre(String sid ,
                                            String componentName ,
                                            double componentMin ,
                                            double componentMax ,
                                            List<SalarySlip> salarySlips) throws Exception {
        List<SalarySlip> toReturn = new ArrayList<>();

        if (salarySlips == null) {
            salarySlips = this.getAllSalarySlips(sid);
        }

        for (SalarySlip salarySlip : salarySlips) {
            for (Deduction deduction : salarySlip.getDeductions()) {
                if (deduction.getSalary_component().equals(componentName)
                && deduction.getAmount() >= componentMin
                && deduction.getAmount() <= componentMax) {
                    toReturn.add(salarySlip);

                }
            }

            for (Earning earning : salarySlip.getEarnings()) {
                if (earning.getSalary_component().equals(componentName)
                && earning.getAmount() >= componentMin
                && earning.getAmount() <= componentMax) {
                    toReturn.add(salarySlip);
                }
            }
        }
        return toReturn;
    }

    public String getClosestSalaryAssignementId(String sid , String employeId , String targetDate) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String filters = String.format("[[\"employee\", \"=\" , \"%s\"], [\"from_date\" , \"<=\" , \"%s\"]]" , employeId, targetDate);

        String url = UriComponentsBuilder.fromHttpUrl(erpnextUrl + "/api/resource/Salary Structure Assignment")
                .queryParam("fields","[\"name\" , \"from_date\"]")
                .queryParam("filters", filters)
                .queryParam("limit_page_length","1000")
                .queryParam("order_by","from_date desc")
                .build(false)
                .toUriString();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            if (data.isArray() && data.size() > 0) {
                JsonNode firstMatch = data.get(0);
                return firstMatch.get("name").asText(null);
            } else {
                System.out.println("Aucun Salary Assignment trouve pour " + employeId);
                return null;
            }
        } else {
            throw new Exception("Echec de la recuperation de la Salary Assignment");
        }

    }

    public void updateSalary(String sid , String component , double componentMin , double componentMax , double baseMin , double baseMax , double pourcentage) throws Exception {
        List<SalarySlip> salarySlips = getSalaryFiltre(sid , component , componentMin , componentMax , null);
        salarySlips = getSalaryFiltre(sid , "Salaire Base" , baseMin , baseMax , salarySlips);

        for (SalarySlip salarySlip : salarySlips) {
            cancelSalarySlip(sid , salarySlip.getName());
            String structureAssignment = getClosestSalaryAssignementId(sid , salarySlip.getEmployee(), salarySlip.getStartDate().toString());
            cancelSalaryAssignment(sid , structureAssignment);
            double base = 0;
            for (Earning earning : salarySlip.getEarnings()) {
                if (earning.getSalary_component().equals("Salaire Base")) {
                    base = earning.getAmount();
                    System.out.println(base);
                }
            }
            String ssa = insertSalaryStructureAssignment(sid , salarySlip.getEmployee(), salarySlip.getSalaryStructure(), salarySlip.getStartDate().toString(), base + (base * pourcentage / 100));
            insertSalarySlip(sid , salarySlip.getEmployee() , salarySlip.getSalaryStructure() , salarySlip.getStartDate().toString() , salarySlip.getEndDate().toString() , ssa);
        }
    }

    public List<SalarySlip> getAllSalarySlipBySQL(Connection connection) throws SQLException {
        List<SalarySlip> salarySlips = new ArrayList<>();
        boolean check = false;
        try {
            if (connection == null) {
                connection = dataSource.getConnection();
                check = true;
            }
            String sql = "SELECT * FROM `tabSalary Slip`";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SalarySlip salarySlip = new SalarySlip();
                salarySlip.setName(resultSet.getString("name"));
                salarySlip.setEmployee(resultSet.getString("employee"));
                salarySlip.setEmployeeName(resultSet.getString("employee_name"));

                salarySlips.add(salarySlip);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (check) {
                connection.close();
            }
        }
        return salarySlips;
    }


}
