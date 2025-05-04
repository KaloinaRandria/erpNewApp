package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mg.working.model.fournisseur.facture.Facture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErpNextPurchaseInvoiceService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${erpnext.url}")
    private String erpnextUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Facture> getFacturesByCommande(String sid, String purchaseOrder) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/method/my_app.utils.supprimer.get_facture_by_commande"
                + "?purchase_order=" + URLEncoder.encode(purchaseOrder, StandardCharsets.UTF_8);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        System.out.println("Body: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode dataNode = root.get("message");

            List<Facture> factures = new ArrayList<>();
            for (JsonNode node : dataNode) {
                Facture facture = new Facture();
                facture.setName(node.path("name").asText(null));
                facture.setSupplier(node.path("supplier").asText(null));
                facture.setGrandTotal(node.path("grand_total").asDouble(0));
                facture.setOutstandingAmount(node.path("outstanding_amount").asDouble(0));
                facture.setStatus(node.path("status").asText(null));

                factures.add(facture);
            }
            return factures;
        } else {
            throw new Exception("Erreur lors de la récupération des factures : " + response.getStatusCode());
        }
    }
}
