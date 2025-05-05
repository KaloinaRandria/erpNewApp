<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.facture.Facture" %>
<%@ page import="mg.working.service.formatage.Formatutil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List<Facture> factures = (List<Facture>) request.getAttribute("factures");
%>

<!DOCTYPE html>
<html lang="fr">
<%@include file="../../static/head.jsp" %>

<body>
<%@include file="../../static/header.jsp" %>
<%@include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Liste des Factures</h1>
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
                                <th>Montant Total</th>
                                <th>Montant Restant</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (factures != null && !factures.isEmpty()) {
                                    for (Facture f : factures) {
                            %>
                            <tr>
                                <td><%= f.getName() %></td>
                                <td><%= f.getSupplier() %></td>
                                <td><%= Formatutil.formaterMontant(f.getGrandTotal())  %> Ar</td>
                                <td><%= Formatutil.formaterMontant(f.getOutstandingAmount()) %> Ar</td>
                                <td><%= f.getStatus() %></td>
                                <td>
                                    <%
                                        if (f.getOutstandingAmount() > 0) {
                                    %>
                                    <form method="post" action="/erpnext/purchase-invoice/pay" style="display:inline;">
                                        <input type="hidden" name="invoiceName" value="<%= f.getName() %>" />
                                        <button type="submit" class="btn btn-success btn-sm"
                                                onclick="return confirm('Confirmer le paiement de la facture <%= f.getName() %> ?');">
                                            Payer
                                        </button>
                                    </form>
                                    <%
                                    } else {
                                    %>
                                    <span class="text-muted">Déjà payé</span>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="6">Aucune facture trouvée.</td></tr>
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
