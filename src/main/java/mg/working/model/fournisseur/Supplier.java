package mg.working.model.fournisseur;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private String name;
    private String supplierName;
    private String supplierGroup;
    private String supplierType;
    private String language;
    private String emailId;
    private String mobileNo;
    private String primaryAddress;
    private String country;
}