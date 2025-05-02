package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.fournisseur.Supplier;
import mg.working.model.fournisseur.SupplierQuotation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ErpNextSupplierService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    private ObjectMapper objectMapper = new ObjectMapper();


    public List<Supplier> getSuppliers(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = "Supplier";
        String fieldsParam = "[\"*\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body " + response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            List<Supplier> suppliers = new ArrayList<>();
            for (JsonNode node : dataNode) {
                Supplier supplier = new Supplier();
                supplier.setName(node.path("name").asText(null));
                supplier.setSupplierName(node.path("supplier_name").asText(null));
                supplier.setSupplierGroup(node.path("supplier_group").asText(null));
                supplier.setSupplierType(node.path("supplier_type").asText(null));
                supplier.setCountry(node.path("country").asText(null));
                supplier.setLanguage(node.path("language").asText(null));
                supplier.setEmailId(node.path("email_id").asText(null));
                supplier.setMobileNo(node.path("mobile_no").asText(null));
                supplier.setPrimaryAddress(node.path("primary_address").asText(null));

                suppliers.add(supplier);
            }
            return suppliers;
        } else {
            throw new Exception("Échec de la récupération des fournisseurs : " + response.getStatusCode());
        }
    }

    public List<SupplierQuotation> getSupplierQuotations(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = "Supplier Quotation";
        String fieldsParam = "[\"*\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            List<SupplierQuotation> quotations = new ArrayList<>();
            for (JsonNode node : dataNode) {
                SupplierQuotation quotation = new SupplierQuotation();
                quotation.setName(node.path("name").asText(null));
                quotation.setSupplier(node.path("supplier").asText(null));
                quotation.setTransactionDate(node.path("transaction_date").asText(null));
                quotation.setStatus(node.path("status").asText(null));
                quotation.setCurrency(node.path("currency").asText(null));
                quotation.setGrandTotal(node.path("grand_total").asDouble(0));
                quotations.add(quotation);
            }
            return quotations;
        } else {
            throw new Exception("Erreur lors de la récupération des demandes de devis : " + response.getStatusCode());
        }
    }


}


