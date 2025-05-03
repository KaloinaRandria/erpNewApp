package mg.working.model.fournisseur;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierQuotationItem {
    private String itemCode;
    private String itemName;
    private String description;
    private double qty;
    private double rate;
    private double amount;
}

