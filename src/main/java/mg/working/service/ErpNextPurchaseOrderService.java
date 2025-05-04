package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.fournisseur.commande.PurchaseOrder;
import mg.working.model.fournisseur.facture.Facture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErpNextPurchaseOrderService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ErpNextPurchaseInvoiceService erpNextPurchaseInvoiceService;

    public List<PurchaseOrder> getAllPurchaseOrders(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String resource = "Purchase Order";
        String fieldsParam = "[\"name\", \"supplier\", \"transaction_date\", \"status\", \"grand_total\", \"currency\"]";
        String url = erpnextUrl + "/api/resource/" + resource + "?fields=" + fieldsParam;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("data");

            List<PurchaseOrder> orders = new ArrayList<>();
            for (JsonNode node : dataNode) {
                PurchaseOrder order = new PurchaseOrder();
                order.setName(node.path("name").asText(null));
                order.setSupplier(node.path("supplier").asText(null));
                order.setTransactionDate(node.path("transaction_date").asText(null));
                order.setStatus(node.path("status").asText(null));
                order.setGrandTotal(node.path("grand_total").asDouble(0));
                order.setCurrency(node.path("currency").asText(null));
                List<Facture> factures = this.erpNextPurchaseInvoiceService.getFacturesByCommande(sid,order.getName());
                order.setFactures(factures);
                orders.add(order);
            }
            return orders;
        } else {
            throw new Exception("Erreur lors de la récupération des bons de commande : " + response.getStatusCode());
        }
    }

}
