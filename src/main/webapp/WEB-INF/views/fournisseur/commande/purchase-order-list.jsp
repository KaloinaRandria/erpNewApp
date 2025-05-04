<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.commande.PurchaseOrder" %>

<%
    List<PurchaseOrder> commandes = (List<PurchaseOrder>) request.getAttribute("purchaseOrders");
%>

<!DOCTYPE html>
<html lang="en">
<%@include file="../../static/head.jsp" %>

<body>
<%@include file="../../static/header.jsp" %>
<%@include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Liste des Commandes</h1>
    </div>

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body pt-3">
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>Code</th>
                                <th>Fournisseur</th>
                                <th>Date</th>
                                <th>Statut</th>
                                <th>Devise</th>
                                <th>Total</th>
                                <th>Payée</th>
                                <th>Reçue</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (commandes != null) {
                                    for (PurchaseOrder po : commandes) {
                            %>
                            <tr>
                                <td><%= po.getName() %></td>
                                <td><%= po.getSupplier() %></td>
                                <td><%= po.getTransactionDate() %></td>
                                <td><%= po.getStatus() %></td>
                                <td><%= po.getCurrency() %></td>
                                <td><%= po.getGrandTotal() %></td>
                                <td><%= po.isPayee() ? "Oui" : "Non" %></td>
                                <td><%= po.isRecu() ? "Oui" : "Non" %></td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="8">Aucune commande trouvée.</td></tr>
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

<%@include file="../../static/footer.jsp" %>
</body>
</html>
