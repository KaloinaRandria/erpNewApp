package mg.working.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mg.working.model.fournisseur.facture.Facture;
import mg.working.model.fournisseur.facture.FactureItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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

    public List<Facture> getFacturesPayees(String sid) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = UriComponentsBuilder
                .fromHttpUrl(erpnextUrl + "/api/resource/Purchase Invoice")
                .queryParam("fields", "[\"name\",\"status\",\"outstanding_amount\",\"supplier\",\"posting_date\",\"grand_total\"]")
                .queryParam("filters", "[[\"docstatus\",\"=\",1]]")
                .build(false)
                .toUriString();

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.get("data");

            List<Facture> factures = new ArrayList<>();
            for (JsonNode node : data) {
                double reste = node.path("outstanding_amount").asDouble(0);
                String statut = node.path("status").asText(null);

//                if (reste == 0 || "Received".equalsIgnoreCase(statut)) {
                    Facture facture = new Facture();
                    facture.setName(node.path("name").asText(null));
                    facture.setSupplier(node.path("supplier").asText(null));
                    facture.setGrandTotal(node.path("grand_total").asDouble(0));
                    facture.setOutstandingAmount(reste);
                    facture.setStatus(statut);

                    factures.add(facture);
//                }
            }

            return factures;
        } else {
            throw new Exception("Échec de récupération des factures : " + response.getStatusCode());
        }
    }

    public void payerFacture(String sid, String nomFacture) throws Exception {
        // Étape 1 : Récupérer les informations de la facture
        String urlFacture = erpnextUrl + "/api/resource/Purchase Invoice/" + nomFacture;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestFacture = new HttpEntity<>(headers);
        ResponseEntity<String> reponseFacture = restTemplate.exchange(urlFacture, HttpMethod.GET, requestFacture, String.class);

        if (reponseFacture.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la récupération de la facture : " + reponseFacture.getStatusCode());
        }

        JsonNode factureData = objectMapper.readTree(reponseFacture.getBody()).get("data");
        String fournisseur = factureData.get("supplier").asText();
        double montantTotal = factureData.get("grand_total").asDouble();
        double montantRestant = factureData.get("outstanding_amount").asDouble();
        String dateDuJour = LocalDate.now().toString();

        // Étape 2 : Créer le JSON du paiement avec les bons comptes
        ObjectNode paiement = objectMapper.createObjectNode();
        paiement.put("doctype", "Payment Entry");
        paiement.put("payment_type", "Pay");
        paiement.put("party_type", "Supplier");
        paiement.put("party", fournisseur);
        paiement.put("posting_date", dateDuJour);
        paiement.put("mode_of_payment", "Cash");
        paiement.put("paid_from", "1110 - Espèces - KAL");     // ✅ compte caisse existant
        paiement.put("paid_to", "2110 - Créditeurs - KAL");            // ✅ à créer si pas encore présent
        paiement.put("paid_amount", montantTotal);
        paiement.put("received_amount", montantTotal);
        paiement.put("reference_no", "AUTO-" + nomFacture);
        paiement.put("reference_date", dateDuJour);
        paiement.put("remarks", "Paiement automatique de la facture " + nomFacture);

        ArrayNode references = objectMapper.createArrayNode();
        ObjectNode reference = objectMapper.createObjectNode();
        reference.put("reference_doctype", "Purchase Invoice");
        reference.put("reference_name", nomFacture);
        reference.put("total_amount", montantTotal);
        reference.put("outstanding_amount", montantRestant);
        reference.put("allocated_amount", montantRestant);
        references.add(reference);
        paiement.set("references", references);

        // Étape 3 : Créer la Payment Entry
        HttpEntity<String> createRequest = new HttpEntity<>(paiement.toString(), headers);
        ResponseEntity<String> creationPaiement = restTemplate.postForEntity(
                erpnextUrl + "/api/resource/Payment Entry",
                createRequest,
                String.class
        );

        if (creationPaiement.getStatusCode() != HttpStatus.OK && creationPaiement.getStatusCode() != HttpStatus.CREATED) {
            throw new Exception("Erreur lors de la création du paiement : " + creationPaiement.getStatusCode()
                    + "\n" + creationPaiement.getBody());
        }

        String nomPaiement = objectMapper.readTree(creationPaiement.getBody()).get("data").get("name").asText();

        // Étape 4 : Récupérer le document complet de paiement pour soumission
        String urlPaiement = erpnextUrl + "/api/resource/Payment Entry/" + nomPaiement;
        ResponseEntity<String> reponsePaiement = restTemplate.exchange(urlPaiement, HttpMethod.GET, requestFacture, String.class);
        JsonNode docComplet = objectMapper.readTree(reponsePaiement.getBody()).get("data");

        // Étape 5 : Soumettre le paiement via frappe.client.submit
        String urlSoumission = erpnextUrl + "/api/method/frappe.client.submit";
        ObjectNode soumissionJson = objectMapper.createObjectNode();
        soumissionJson.put("doc", objectMapper.writeValueAsString(docComplet));

        HttpEntity<String> submitRequest = new HttpEntity<>(soumissionJson.toString(), headers);
        ResponseEntity<String> reponseSoumission = restTemplate.postForEntity(urlSoumission, submitRequest, String.class);

        if (reponseSoumission.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erreur lors de la soumission du paiement : " + reponseSoumission.getStatusCode()
                    + "\n" + reponseSoumission.getBody());
        }

        System.out.println("✅ Paiement soumis avec succès pour la facture : " + nomFacture);
    }


    public Facture getPurchaseInvoiceByName(String sid, String name) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "sid=" + sid);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = erpnextUrl + "/api/resource/Purchase Invoice/" + name;

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody()).get("data");

            Facture invoice = new Facture();
            invoice.setName(root.path("name").asText(null));
            invoice.setSupplier(root.path("supplier").asText(null));
            invoice.setPostingDate(root.path("posting_date").asText(null));
            invoice.setStatus(root.path("status").asText(null));
            invoice.setCurrency(root.path("currency").asText(null));
            invoice.setGrandTotal(root.path("grand_total").asDouble(0));
            invoice.setOutstandingAmount(root.path("outstanding_amount").asDouble(0));

            // Parsing des items
            List<FactureItem> items = new ArrayList<>();
            JsonNode itemsNode = root.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    FactureItem item = new FactureItem();
                    item.setItemCode(itemNode.path("item_code").asText(null));
                    item.setItemName(itemNode.path("item_name").asText(null));
                    item.setDescription(itemNode.path("description").asText(null));
                    item.setQty(itemNode.path("qty").asDouble(0));
                    item.setRate(itemNode.path("rate").asDouble(0));
                    item.setAmount(itemNode.path("amount").asDouble(0));
                    items.add(item);
                }
            }

            invoice.setItems(items);

            return invoice;
        } else {
            throw new Exception("Impossible de charger la facture : " + response.getStatusCode());
        }
    }

}
