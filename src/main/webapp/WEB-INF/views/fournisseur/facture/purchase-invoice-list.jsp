<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.facture.Facture" %>

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
                                <td><%= f.getGrandTotal() %> Ar</td>
                                <td><%= f.getOutstandingAmount() %> Ar</td>
                                <td><%= f.getStatus() %></td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="5">Aucune facture trouvée.</td></tr>
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
