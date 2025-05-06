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

<body>
<%@include file="../../static/header.jsp" %>
<%@include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Détail de la Facture - <%= facture.getName() %></h1>
    </div>

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">

                        <h2 class="card-title text-primary text-center mb-4">
                            Informations Générales
                        </h2>
                        <p><strong>Nom :</strong> <%= facture.getName() %></p>
                        <p><strong>Fournisseur :</strong> <%= facture.getSupplier() %></p>
                        <p><strong>Date :</strong> <%= facture.getPostingDate() %></p>
                        <p><strong>Statut :</strong> <%= facture.getStatus() %></p>
                        <p><strong>Montant Total :</strong> <%= Formatutil.formaterMontant(facture.getGrandTotal()) %> Ar</p>
                        <p><strong>Montant Restant :</strong> <%= Formatutil.formaterMontant(facture.getOutstandingAmount()) %> Ar</p>
                        <p><strong>Devise :</strong> <%= facture.getCurrency() %></p>

                        <hr>

                        <h4 class="mt-4">Articles facturés</h4>
                        <table class="table table-bordered table-striped mt-3">
                            <thead class="table-primary">
                            <tr>
                                <th>Code Article</th>
                                <th>Nom</th>
                                <th>Description</th>
                                <th>Quantité</th>
                                <th>Prix Unitaire</th>
                                <th>Montant</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (FactureItem item : items) { %>
                            <tr>
                                <td><%= item.getItemCode() %></td>
                                <td><%= item.getItemName() %></td>
                                <td><%= item.getDescription() %></td>
                                <td><%= item.getQty() %></td>
                                <td><%= Formatutil.formaterMontant(item.getRate()) %></td>
                                <td><%= Formatutil.formaterMontant(item.getAmount()) %></td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>

                        <div class="mt-4 d-flex justify-content-end">
                            <a href="/erpnext/purchase-invoice" class="btn btn-secondary btn-lg shadow">Retour à la liste</a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<%@include file="../../static/footer.jsp" %>
</body>
</html>
