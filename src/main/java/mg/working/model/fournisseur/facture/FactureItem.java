package mg.working.model.fournisseur.facture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FactureItem {
    private String itemCode;
    private String itemName;
    private String description;
    private double qty;
    private double rate;
    private double amount;
}
