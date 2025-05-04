package mg.working.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.fournisseur.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    public List<RequestForQuotation> getRequestsForQuotation(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = "Request for Quotation";
        String fieldsParam = "[\"name\", \"transaction_date\", \"status\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body RFQ " + response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            List<RequestForQuotation> rfqs = new ArrayList<>();
            for (JsonNode node : dataNode) {
                RequestForQuotation rfq = new RequestForQuotation();
                rfq.setName(node.path("name").asText(null));
                rfq.setTransactionDate(node.path("transaction_date").asText(null));
                rfq.setStatus(node.path("status").asText(null));
                rfqs.add(rfq);
            }
            return rfqs;
        } else {
            throw new Exception("Erreur lors de la récupération des RFQ : " + response.getStatusCode());
        }
    }


    public List<RequestForQuotationSupplier> getSuppliersByRfQ(String sid, String rfqName) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Request for Quotation/" + rfqName;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body RFQ Supplier " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody()).get("data");
            JsonNode suppliersNode = root.get("suppliers");

            List<RequestForQuotationSupplier> suppliers = new ArrayList<>();

            if (suppliersNode != null && suppliersNode.isArray()) {
                for (JsonNode node : suppliersNode) {
                    RequestForQuotationSupplier supplier = new RequestForQuotationSupplier();
                    supplier.setSupplier(node.path("supplier").asText(null));
                    supplier.setSupplierName(node.path("supplier_name").asText(null));
                    supplier.setContactPerson(node.path("contact_person").asText(null));
                    supplier.setEmailId(node.path("email_id").asText(null));
                    supplier.setName(node.path("name").asText(null));
                    supplier.setParent(node.path("parent").asText(null));
                    supplier.setParenttype(node.path("parent_type").asText(null));
                    supplier.setParentfield(node.path("parent_field").asText(null));
                    suppliers.add(supplier);
                }
            }

            return suppliers;
        } else {
            throw new Exception("Impossible de récupérer la RFQ : " + rfqName);
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


    public SupplierQuotation getSupplierQuotationByName(String sid, String name) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Supplier Quotation/" + name;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody()).get("data");

            SupplierQuotation quotation = new SupplierQuotation();
            quotation.setName(root.path("name").asText(null));
            quotation.setSupplier(root.path("supplier").asText(null));
            quotation.setTransactionDate(root.path("transaction_date").asText(null));
            quotation.setStatus(root.path("status").asText(null));
            quotation.setCurrency(root.path("currency").asText(null));
            quotation.setGrandTotal(root.path("grand_total").asDouble(0));

            // Parsing des items
            List<SupplierQuotationItem> items = new ArrayList<>();
            JsonNode itemsNode = root.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    SupplierQuotationItem item = new SupplierQuotationItem();
                    item.setName(itemNode.path("name").asText(null));
                    item.setItemCode(itemNode.path("item_code").asText(null));
                    item.setItemName(itemNode.path("item_name").asText(null));
                    item.setDescription(itemNode.path("description").asText(null));
                    item.setQty(itemNode.path("qty").asDouble(0));
                    item.setRate(itemNode.path("rate").asDouble(0));
                    item.setAmount(itemNode.path("amount").asDouble(0));
                    items.add(item);
                }
            }

            quotation.setItems(items);

            return quotation;
        } else {
            throw new Exception("Impossible de charger le devis : " + response.getStatusCode());
        }
    }

    public void updateItemRate(String sid, String itemId, double newRate) throws Exception {
        // URL pour accéder à un Supplier Quotation Item spécifique
        String url = erpnextUrl + "/api/resource/Supplier Quotation Item/" + itemId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "sid=" + sid);

        // Corps de la requête contenant uniquement le champ à modifier
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("rate", newRate);

        // Sérialisation en JSON
        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Affichage de debug
        System.out.println(">>> URL appelée : " + url);
        System.out.println(">>> Corps de la requête : " + jsonBody);
        System.out.println(">>> Réponse ERPNext : " + response.getBody());

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Échec de la mise à jour du taux de l'article : " + itemId);
        }
    }


}


