<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.RequestForQuotation" %>

<%
    List<RequestForQuotation> rfqs = (List<RequestForQuotation>) request.getAttribute("rfqs");
%>

<!DOCTYPE html>
<html lang="en">

<%@ include file="../static/head.jsp" %>

<body>

<!-- ======= Header ======= -->
<%@ include file="../static/header.jsp" %>

<!-- ======= Sidebar ======= -->
<%@ include file="../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Liste des Demandes de Devis (RFQ)</h1>
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
                                <th>Date</th>
                                <th>Statut</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (rfqs != null && !rfqs.isEmpty()) {
                                    for (RequestForQuotation rfq : rfqs) {
                            %>
                            <tr>
                                <td><%= rfq.getName() %></td>
                                <td><%= rfq.getTransactionDate() %></td>
                                <td><%= rfq.getStatus() %></td>
                                <td>
                                    <a href="/erpnext/request-for-quotation/<%= rfq.getName() %>" class="btn btn-sm btn-primary">Détail</a>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="7">Aucune demande de devis trouvée.</td></tr>
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

<%@ include file="../static/footer.jsp" %>

</body>

</html>
