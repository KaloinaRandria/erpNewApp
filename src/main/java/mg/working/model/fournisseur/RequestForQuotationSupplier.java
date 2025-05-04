package mg.working.model.fournisseur;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForQuotationSupplier {
    private String name;
    private String parent;
    private String parenttype;
    private String parentfield;
    private String supplier;
    private String supplierName;
    private String contactPerson;
    private String emailId;
    private String addressDisplay;
}
