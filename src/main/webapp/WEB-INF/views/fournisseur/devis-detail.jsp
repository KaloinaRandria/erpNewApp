<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mg.working.model.fournisseur.SupplierQuotation" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.SupplierQuotation" %>
<%@ page import="mg.working.model.fournisseur.SupplierQuotationItem" %>


<%
    SupplierQuotation devis = (SupplierQuotation) request.getAttribute("quotation");
    List<SupplierQuotationItem> items = devis.getItems();
%>


<!DOCTYPE html>
<html lang="en">

<%@include file="../static/head.jsp" %>

<body>

<!-- ======= Header ======= -->
<%@include file="../static/header.jsp" %>


<!-- ======= Sidebar ======= -->
<%@include file="../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Détail du Devis - <%= ((SupplierQuotation) request.getAttribute("quotation")).getName() %></h1>
    </div><!-- End Page Title -->

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title text-primary text-center mb-4">
                            Détail du Devis : <strong><%= devis.getName() %></strong>
                        </h2>
                        <p><strong>Fournisseur :</strong> <%= devis.getSupplier() %></p>
                        <p><strong>Date :</strong> <%= devis.getTransactionDate() %></p>
                        <p><strong>Devise :</strong> <%= devis.getCurrency() %></p>
                        <p><strong>Total :</strong> <%= devis.getGrandTotal() %> <%= devis.getCurrency() %></p>

                        <hr>

                        <h4 class="mt-4">Articles demandés</h4>
                        <table class="table table-bordered table-striped mt-3">
                            <thead class="table-primary">
                            <tr>
                                <th>Nom de l'article</th>
                                <th>Description</th>
                                <th>Quantité</th>
                                <th>Prix Unitaire</th>
                                <th>Total</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                for (SupplierQuotationItem item : items) {
                            %>
                            <tr>
                                <td><%= item.getItemCode() %></td>
                                <td><%= item.getDescription() %></td>
                                <td><%= item.getQty() %></td>
                                <td>
                                    <form action="/erpnext/update-item-price" method="post" class="d-flex align-items-center">
                                        <input type="hidden" name="sid" value="<%= session.getAttribute("sid") %>" />
                                        <input type="hidden" name="quotationName" value="<%= devis.getName() %>" />
                                        <input type="hidden" name="itemName" value="<%= item.getName() %>" />
                                        <input type="number" step="0.01" name="newRate" value="<%= item.getRate() %>" class="form-control form-control-sm me-2" required />
                                        <button type="submit" class="btn btn-sm btn-primary">Modifier</button>
                                    </form>
                                </td>

                                <td><%= item.getAmount() %> <%= devis.getCurrency() %></td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>

                        <div class="text-end mt-4">
                            <a href="/erpnext/supplier-quotations" class="btn btn-secondary">Retour à la liste</a>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>

</main>

<!-- ======= Footer ======= -->

<%@include file="../static/footer.jsp" %>

</body>

</html>

