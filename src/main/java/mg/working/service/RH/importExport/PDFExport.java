package mg.working.service.RH.importExport;

import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.http.HttpServletResponse;
import mg.working.model.RH.salaire.SalarySlip;
import org.springframework.stereotype.Service;

import java.io.OutputStream;


@Service
public class PDFExport {

    public void exporterBulletinSalaire(HttpServletResponse response, SalarySlip slip) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fiche-" + slip.getName().replace("/", "-") + ".pdf");

        // Générer le HTML pour les earnings
        StringBuilder earningsTable = new StringBuilder();
        earningsTable.append("""
        <h3>Revenus</h3>
        <table>
            <thead>
                <tr><th>Composant</th><th>Montant (€)</th><th>Année à ce jour (€)</th></tr>
            </thead>
            <tbody>
    """);
        slip.getEarnings().forEach(e -> earningsTable.append(
                "<tr><td>" + e.getSalary_component() + "</td><td>" + String.format("%.2f", e.getAmount()) + "</td><td>" + String.format("%.2f", e.getYear_to_date()) + "</td></tr>"
        ));
        earningsTable.append("</tbody></table>");

        // Générer le HTML pour les deductions
        StringBuilder deductionsTable = new StringBuilder();
        deductionsTable.append("""
        <h3>Déductions</h3>
        <table>
            <thead>
                <tr><th>Composant</th><th>Montant (€)</th><th>Année à ce jour (€)</th></tr>
            </thead>
            <tbody>
    """);
        slip.getDeductions().forEach(d -> deductionsTable.append(
                "<tr><td>" + d.getSalary_component() + "</td><td>" + String.format("%.2f", d.getAmount()) + "</td><td>" + String.format("%.2f", d.getYear_to_date()) + "</td></tr>"
        ));
        deductionsTable.append("</tbody></table>");

        // HTML principal
        String html = """
<!DOCTYPE html>
<html lang='fr'>
<head>
    <meta charset='UTF-8'>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        h2 { color: #007bff; }
        h3 { margin-top: 30px; color: #333; }
        .section { margin-top: 20px; }
        .row { display: flex; justify-content: space-between; margin-bottom: 10px; }
        .label { font-weight: bold; color: #333; }
        .value { color: #555; }
        table { width: 100%%; border-collapse: collapse; margin-top: 10px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f8f8f8; }
        .total { font-weight: bold; background-color: #e9ffe9; }
    </style>
</head>
<body>
    <h2>Bulletin de Salaire</h2>

    <div class='section'>
        <div class='row'><span class='label'>Nom :</span><span class='value'>%s</span></div>
        <div class='row'><span class='label'>Matricule :</span><span class='value'>%s</span></div>
        <div class='row'><span class='label'>Département :</span><span class='value'>%s</span></div>
        <div class='row'><span class='label'>Poste :</span><span class='value'>%s</span></div>
        <div class='row'><span class='label'>Période :</span><span class='value'>%s au %s</span></div>
        <div class='row'><span class='label'>Date de versement :</span><span class='value'>%s</span></div>
    </div>

    <table>
        <thead>
            <tr><th>Composant</th><th>Montant (€)</th></tr>
        </thead>
        <tbody>
            <tr><td>Salaire brut</td><td>%.2f</td></tr>
            <tr><td>Déductions</td><td>%.2f</td></tr>
            <tr class='total'><td>Net à payer</td><td>%.2f</td></tr>
        </tbody>
    </table>

    <div class='section'>
        <p><span class='label'>Statut :</span> <span class='value'>%s</span></p>
    </div>

    %s
    %s

</body>
</html>
""".formatted(
                slip.getEmployeeName(),
                slip.getEmployee(),
                slip.getDepartment(),
                slip.getDesignation(),
                slip.getStartDate(),
                slip.getEndDate(),
                slip.getPostingDate(),
                slip.getGrossPay(),
                slip.getTotalDeduction(),
                slip.getNetPay(),
                slip.getStatus(),
                earningsTable.toString(),
                deductionsTable.toString()
        );

        try (OutputStream out = response.getOutputStream()) {
            HtmlConverter.convertToPdf(html, out);
        }
    }
}
