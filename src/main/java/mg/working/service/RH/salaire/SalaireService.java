package mg.working.service.RH.salaire;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.RH.salaire.SalarySlip;
import mg.working.model.RH.salaire.StatistiqueSalaire;
import mg.working.model.RH.salaire.component.Deduction;
import mg.working.model.RH.salaire.component.Earning;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
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

    private static final SimpleDateFormat monthFormatter = new SimpleDateFormat("yyyy-MM");

    public List<SalarySlip> getAllSalarySlips(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Salary Slip?fields=[\"name\"]&limit_page_length=2500"; // seul le name suffit

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

    public List<SalarySlip> getSalarySlipsByEmployee(String sid, String employeeId) throws Exception  {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String fields = "[\"name\"]"; // Récupère juste le nom (clé primaire)
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

    public void insertSalaryStructureAssignment(String sid, String employee, String structure, String fromDate, double base, String company) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String data = "{" +
                "\"doctype\": \"Salary Structure Assignment\"," +
                "\"employee\": \"" + employee + "\"," +
                "\"salary_structure\": \"" + structure + "\"," +
                "\"from_date\": \"" + fromDate + "\"," +
                "\"base\": " + base + "," +
                "\"company\": \"" + company + "\"" +
                "}";

        String url = erpnextUrl + "/api/resource/Salary Structure Assignment";
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            System.out.println("✔ Assignment créé : " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSalarySlip(String sid, String employee, String structure, String startDate, String endDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String company = "Orinasa SA";
        String data = "{" +
                "\"doctype\": \"Salary Slip\"," +
                "\"employee\": \"" + employee + "\"," +
                "\"salary_structure\": \"" + structure + "\"," +
                "\"start_date\": \"" + startDate + "\"," +
                "\"end_date\": \"" + endDate + "\"," +
                "\"payroll_frequency\": \"Monthly\"," +
                "\"company\": \"" + company + "\"" +
                "}";

        String url = erpnextUrl + "/api/resource/Salary Slip";
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );
            System.out.println("✔ Salary Slip créé : " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void genererSalarySlip(String sid , String employe , String startMois , String endMois) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(startMois , formatter);

        String dateDebut = yearMonth.atDay(1).toString();
        String dateFin = yearMonth.atEndOfMonth().toString();

        int year = yearMonth.getYear();
        int month = yearMonth.getMonthValue();

        System.out.println("year : " + year);
        System.out.println("month : " + month);
        SalarySlip salarySlip = null;

        List<SalarySlip> salarySlips = getSalarySlipsByMonth(sid , year , month);
        System.out.println("salarySlips : " + salarySlips.size());
        for (SalarySlip slip : salarySlips) {
            System.out.println("emp" + slip.getEmployeeName());
            if (slip.getEmployeeName().equals(employe)) {
                salarySlip = slip;
                break;
            }
        }

        insertSalarySlip(sid , employe ,salarySlip.getSalaryStructure() , dateDebut , dateFin);
    }
}
