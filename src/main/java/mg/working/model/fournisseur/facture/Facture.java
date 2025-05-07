package mg.working.model.fournisseur.facture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Facture {
    private String name;
    private String supplier;
    private double grandTotal;
    private double outstandingAmount;
    private String status;
    private String postingDate;
    private String currency;
    private List<FactureItem> items;
}
