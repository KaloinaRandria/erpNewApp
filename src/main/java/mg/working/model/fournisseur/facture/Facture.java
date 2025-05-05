package mg.working.model.fournisseur.facture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
