package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mg.working.model.fournisseur.RQFUtils.RequestForQuotationDetail;
import mg.working.model.fournisseur.RQFUtils.RfqItem;
import mg.working.model.fournisseur.RQFUtils.RfqSupplierInfo;
import mg.working.model.fournisseur.RequestForQuotation;
import mg.working.model.fournisseur.SupplierQuotation;
import mg.working.model.fournisseur.SupplierQuotationItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErpNextRequestForQuotationService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<RequestForQuotation> getRfqsBySupplier(String sid, String supplier) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/method/my_app.utils.supprimer.get_rfq_by_supplier"
                + "?supplier=" + URLEncoder.encode(supplier, StandardCharsets.UTF_8);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("message");

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

    public RequestForQuotationDetail getRfqDetailByName(String sid, String rfqName) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Request for Quotation/" + URLEncoder.encode(rfqName, StandardCharsets.UTF_8);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            RequestForQuotationDetail detail = new RequestForQuotationDetail();
            detail.setName(data.path("name").asText());
            detail.setCompany(data.path("company").asText());
            detail.setTransactionDate(data.path("transaction_date").asText());
            detail.setScheduleDate(data.path("schedule_date").asText());
            detail.setStatus(data.path("status").asText());


            // Liste des fournisseurs
            List<RfqSupplierInfo> suppliers = new ArrayList<>();
            for (JsonNode node : data.path("suppliers")) {
                RfqSupplierInfo supplier = new RfqSupplierInfo();
                supplier.setSupplier(node.path("supplier").asText());
                supplier.setSupplierName(node.path("supplier_name").asText());
                supplier.setQuoteStatus(node.path("quote_status").asText());
                suppliers.add(supplier);
            }
            detail.setSuppliers(suppliers);

            // Liste des articles
            List<RfqItem> items = new ArrayList<>();
            for (JsonNode node : data.path("items")) {
                RfqItem item = new RfqItem();
                item.setItemCode(node.path("item_code").asText());
                item.setItemName(node.path("item_name").asText());
                item.setDescription(node.path("description").asText());
                item.setQty(node.path("qty").asDouble());
                item.setUom(node.path("uom").asText());
                item.setWarehouse(node.path("warehouse").asText());
                items.add(item);
            }
            detail.setItems(items);

            return detail;
        } else {
            throw new Exception("Erreur lors de la récupération du RFQ : " + response.getStatusCode());
        }
    }

    public void createSupplierQuotation(
            String sid,
            String rfqName,
            String supplier,
            List<RfqItem> items
    ) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectNode quotationJson = objectMapper.createObjectNode();
        quotationJson.put("doctype", "Supplier Quotation");
        quotationJson.put("supplier", supplier);
        quotationJson.put("transaction_date", LocalDate.now().toString());
        quotationJson.put("rfq", rfqName);

        quotationJson.put("currency", "MAD");

        ArrayNode itemsArray = objectMapper.createArrayNode();
        for (RfqItem rfqItem : items) {
            ObjectNode itemJson = objectMapper.createObjectNode();
            itemJson.put("item_code", rfqItem.getItemCode());
            itemJson.put("qty", rfqItem.getQty() != null ? rfqItem.getQty() : 1);
            itemJson.put("rate", rfqItem.getRate() != null ? rfqItem.getRate() : 0);
            itemJson.put("warehouse", rfqItem.getWarehouse());

            if (rfqItem.getDescription() != null) {
                itemJson.put("description", rfqItem.getDescription());
            }

            itemsArray.add(itemJson);
        }

        quotationJson.set("items", itemsArray);

        // Création du Supplier Quotation
        String urlCreate = erpnextUrl + "/api/resource/Supplier Quotation";
        HttpEntity<String> createRequest = new HttpEntity<>(quotationJson.toString(), headers);
        ResponseEntity<String> createResponse = restTemplate.exchange(urlCreate, HttpMethod.POST, createRequest, String.class);

        if (!createResponse.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Échec de la création du devis : " + createResponse.getBody());
        }

        String quotationName = objectMapper.readTree(createResponse.getBody())
                .path("data").path("name").asText();

        // Récupérer le document complet
        String getQuotationUrl = erpnextUrl + "/api/resource/Supplier Quotation/" + quotationName;
        HttpEntity<String> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse = restTemplate.exchange(getQuotationUrl, HttpMethod.GET, getRequest, String.class);

        if (!getResponse.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Échec de la récupération du devis pour soumission : " + getResponse.getBody());
        }

        JsonNode fullDoc = objectMapper.readTree(getResponse.getBody()).get("data");

        // Soumission avec frappe.client.submit
        String submitUrl = erpnextUrl + "/api/method/frappe.client.submit";
        ObjectNode submitPayload = objectMapper.createObjectNode();
        submitPayload.put("doc", objectMapper.writeValueAsString(fullDoc));

        HttpEntity<String> submitRequest = new HttpEntity<>(submitPayload.toString(), headers);
        ResponseEntity<String> submitResponse = restTemplate.postForEntity(submitUrl, submitRequest, String.class);

        if (!submitResponse.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Échec de la soumission du devis : " + submitResponse.getBody());
        }

        System.out.println("✅ Devis fournisseur soumis avec succès : " + quotationName);
    }



}
