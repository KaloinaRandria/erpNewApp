package mg.working.model.fournisseur;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierQuotation {
    private String name;
    private String supplier;
    private String transactionDate;
    private String status;
    private String currency;
    private double grandTotal;
}