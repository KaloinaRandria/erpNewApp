<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.facture.Facture" %>
<%@ page import="mg.working.model.fournisseur.facture.FactureItem" %>
<%@ page import="mg.working.service.formatage.Formatutil" %>

<%
    Facture facture = (Facture) request.getAttribute("factureDetail");
    List<FactureItem> items = facture.getItems();
%>

<!DOCTYPE html>
<html lang="fr">
<%@include file="../../static/head.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>

<body>
<%@include file="../../static/header.jsp" %>
<%@include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Facture n° <%= facture.getName() %></h1>
    </div>

    <!-- BOUTONS EN DEHORS DE LA FACTURE -->
    <div class="mb-3 d-flex justify-content-between">
        <a href="/erpnext/purchase-invoice" class="btn btn-secondary shadow">Retour</a>
        <button onclick="exportPDF()" class="btn btn-success shadow">Exporter en PDF</button>
    </div>

    <section class="section">
        <div class="row justify-content-center">
            <div class="col-lg-10">

                <!-- DIV À EXPORTER EN PDF -->
                <div id="facture-content" class="card shadow">
                    <div class="card-body p-4">

                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h5 class="text-primary">Fournisseur</h5>
                                <p><strong>Nom :</strong> <%= facture.getSupplier() %></p>
                                <p><strong>Date :</strong> <%= facture.getPostingDate() %></p>
                            </div>
                            <div class="col-md-6 text-end">
                                <h5 class="text-primary">Facture</h5>
                                <p><strong>Status :</strong> <%= facture.getStatus() %></p>
                                <p><strong>Devise :</strong> <%= facture.getCurrency() %></p>
                            </div>
                        </div>

                        <hr>

                        <h5 class="mb-3">Détail des Articles</h5>
                        <table class="table table-bordered table-hover">
                            <thead class="table-light text-center">
                            <tr>
                                <th>Code</th>
                                <th>Nom</th>
                                <th>Description</th>
                                <th>Quantité</th>
                                <th>PU</th>
                                <th>Montant</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (FactureItem item : items) { %>
                            <tr>
                                <td><%= item.getItemCode() %></td>
                                <td><%= item.getItemName() %></td>
                                <td><%= item.getDescription() %></td>
                                <td class="text-end"><%= item.getQty() %></td>
                                <td class="text-end"><%= Formatutil.formaterMontant(item.getRate()) %> Ar</td>
                                <td class="text-end"><%= Formatutil.formaterMontant(item.getAmount()) %> Ar</td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>

                        <div class="row mt-4">
                            <div class="col-md-6">
                                <p><strong>Montant Total :</strong> <%= Formatutil.formaterMontant(facture.getGrandTotal()) %> Ar</p>
                                <p><strong>Montant Restant :</strong> <%= Formatutil.formaterMontant(facture.getOutstandingAmount()) %> Ar</p>
                            </div>
                            <div class="col-md-6 text-end">
                                <p class="fw-bold">Signature fournisseur :</p>
                                <div style="height: 80px; border-bottom: 1px solid #999;"></div>
                            </div>
                        </div>

                    </div>
                </div>
                <!-- FIN DE FACTURE À EXPORTER -->

            </div>
        </div>
    </section>
</main>

<%@include file="../../static/footer.jsp" %>

<script>
    function exportPDF() {
        const element = document.getElementById("facture-content");
        const opt = {
            margin: 0.5,
            filename: 'facture_<%= facture.getName() %>.pdf',
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
        };
        html2pdf().set(opt).from(element).save();
    }
</script>
</body>
</html>
