package mg.working.service.RH.importExport;

import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.http.HttpServletResponse;
import mg.working.model.RH.salaire.SalarySlip;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
public class PDFExport {

    public void exporterBulletinSalaire(HttpServletResponse response, SalarySlip slip) throws Exception {
        // Configuration de la réponse HTTP pour le PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fiche-" + slip.getName().replace("/", "-") + ".pdf");

        // Construction du contenu HTML à transformer en PDF (inspiré du fichier fourni)
        String html = """
    <!DOCTYPE html>
    <html lang='fr'>
    <head>
        <meta charset='UTF-8'>
        <style>
            body { font-family: Arial, sans-serif; margin: 30px; }
            h2 { color: #007bff; }
            .section { margin-top: 20px; }
            .row { display: flex; justify-content: space-between; margin-bottom: 10px; }
            .label { font-weight: bold; color: #333; }
            .value { color: #555; }
            table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
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
                slip.getStatus()
        );

        try (OutputStream out = response.getOutputStream()) {
            HtmlConverter.convertToPdf(html, out);

        }
    }
}
