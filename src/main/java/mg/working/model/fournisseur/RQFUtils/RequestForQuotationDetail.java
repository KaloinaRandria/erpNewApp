package mg.working.model.fournisseur.RQFUtils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestForQuotationDetail {
    private String name;
    private String company;
    private String transactionDate;
    private String scheduleDate;
    private String status;
    private List<RfqItem> items;
    private List<RfqSupplierInfo> suppliers;
    // Getters / Setters
}


