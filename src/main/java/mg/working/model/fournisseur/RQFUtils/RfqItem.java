package mg.working.model.fournisseur.RQFUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RfqItem {
    private String itemCode;
    private String itemName;
    private String description;
    private Double qty;
    private String uom;
    private String warehouse;
    private Double rate;
    // Getters / Setters
}
