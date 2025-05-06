<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.RQFUtils.RequestForQuotationDetail" %>
<%@ page import="mg.working.model.fournisseur.RQFUtils.RfqItem" %>
<%@ page import="mg.working.model.fournisseur.RQFUtils.RfqSupplierInfo" %>


<%
    RequestForQuotationDetail rfq = (RequestForQuotationDetail) request.getAttribute("rfqDetail");
    List<RfqItem> items = rfq.getItems();
    List<RfqSupplierInfo> suppliers = rfq.getSuppliers();
%>

<!DOCTYPE html>
<html lang="fr">

<%@include file="../../static/head.jsp" %>

<body>

<%@include file="../../static/header.jsp" %>
<%@include file="../../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Détail de la Demande de Devis - <%= rfq.getName() %></h1>
    </div>

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">

                        <h2 class="card-title text-primary text-center mb-4">
                            Informations Générales
                        </h2>
                        <p><strong>Nom :</strong> <%= rfq.getName() %></p>
                        <p><strong>Entreprise :</strong> <%= rfq.getCompany() %></p>
                        <p><strong>Date :</strong> <%= rfq.getTransactionDate() %></p>
                        <p><strong>Date prévue :</strong> <%= rfq.getScheduleDate() %></p>
                        <p><strong>Status :</strong> <%= rfq.getStatus() %></p>

                        <hr>

                        <h4 class="mt-4">Fournisseurs concernés</h4>
                        <ul>
                            <% for (RfqSupplierInfo supplier : suppliers) { %>
                            <li><%= supplier.getSupplierName() %> (Statut : <%= supplier.getQuoteStatus() %>)</li>
                            <% } %>
                        </ul>

                        <hr>

                        <h4 class="mt-4">Articles demandés</h4>
                        <table class="table table-bordered table-striped mt-3">
                            <thead class="table-primary">
                            <tr>
                                <th>Code</th>
                                <th>Nom</th>
                                <th>Description</th>
                                <th>Quantité</th>
                                <th>Unité</th>
                                <th>Entrepôt</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (RfqItem item : items) { %>
                            <tr>
                                <td><%= item.getItemCode() %></td>
                                <td><%= item.getItemName() %></td>
                                <td><%= item.getDescription() %></td>
                                <td><%= item.getQty() %></td>
                                <td><%= item.getUom() %></td>
                                <td><%= item.getWarehouse() %></td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>

                        <div class="text-end mt-4">
                            <a href="/erpnext/suppliers" class="btn btn-secondary">Retour à la liste</a>
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
