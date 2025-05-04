<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.Supplier" %>
<%@ page import="mg.working.model.fournisseur.SupplierQuotation" %>


<%
    List<Supplier> suppliers = (List<Supplier>) request.getAttribute("suppliers");
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
        <h1>Liste Demande Devis</h1>
    </div><!-- End Page Title -->

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">

                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Code</th>
                                    <th>Fournisseur</th>
                                    <th>Date</th>
                                    <th>Statut</th>
                                    <th>Devise</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                            <%
                                List<SupplierQuotation> quotations = (List<SupplierQuotation>) request.getAttribute("quotations");
                                if (quotations != null) {
                                    for (SupplierQuotation q : quotations) {
                            %>
                            <tr>
                                <td><%= q.getName() %></td>
                                <td><%= q.getSupplier() %></td>
                                <td><%= q.getTransactionDate() %></td>
                                <td><%= q.getStatus() %></td>
                                <td><%= q.getCurrency() %></td>
                                <td><%= q.getGrandTotal() %></td>
                                <td>
                                    <a href="/erpnext/supplier-quotations/<%= q.getName() %>" class="btn btn-sm btn-primary">Détail</a>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="6">Aucune demande de devis trouvée.</td></tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
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