package mg.working.model.fournisseur.commande;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mg.working.model.fournisseur.facture.Facture;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
    private String name;
    private String supplier;
    private String transactionDate;
    private String status;
    private double grandTotal;
    private String currency;
    private List<Facture> factures;

    public boolean isPayee() {
        if (factures == null || factures.isEmpty()) {
            return false;
        }

        double totalFactures = 0;

        for (Facture facture : factures) {
            if (facture.getOutstandingAmount() > 0) {
                return false;
            }
            totalFactures += facture.getGrandTotal();
        }

        // Comparaison avec une tolérance pour les arrondis éventuels
        return Math.abs(totalFactures - grandTotal) < 0.01;
    }

    public boolean isRecu() {
        // Vérifie si le statut est différent de "Completed" et "To Bill"
        return "Completed".equalsIgnoreCase(status) || "To Bill".equalsIgnoreCase(status);
    }

}

