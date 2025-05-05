package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

}
