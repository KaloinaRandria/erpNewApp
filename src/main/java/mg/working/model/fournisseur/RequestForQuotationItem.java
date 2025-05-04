package mg.working.model.fournisseur;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForQuotationItem {
    private String name;
    private String itemCode;
    private String itemName;
    private String description;
    private double qty;
    private String uom;
    private String scheduleDate;
    private String warehouse;
    private String materialRequest;
    private String materialRequestItem;
}
