package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.fournisseur.RQFUtils.RequestForQuotationDetail;
import mg.working.model.fournisseur.RQFUtils.RfqItem;
import mg.working.model.fournisseur.RQFUtils.RfqSupplierInfo;
import mg.working.model.fournisseur.RequestForQuotation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

}
