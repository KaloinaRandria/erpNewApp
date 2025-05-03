package mg.working.model.fournisseur;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForQuotation {
    private String name;
    private String transactionDate;
    private String status;
}
