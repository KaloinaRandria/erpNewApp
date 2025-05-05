<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.RequestForQuotation" %>


<!DOCTYPE html>
<html lang="en">

<%@include file="../../static/head.jsp" %>

<body>

<!-- ======= Header ======= -->
<%@include file="../../static/header.jsp" %>


<!-- ======= Sidebar ======= -->
<%@include file="../../static/sidebar.jsp" %>



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
                                <th>Nom</th>
                                <th>Status</th>
                                <th>Date</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                List<RequestForQuotation> requestForQuotations = (List<RequestForQuotation>) request.getAttribute("requestForQuotations");
                                if (requestForQuotations != null) {
                                    for (RequestForQuotation rqf : requestForQuotations) {
                            %>
                            <tr>
                                <td><%= rqf.getName() %></td>
                                <td><%= rqf.getStatus() %></td>
                                <td><%= rqf.getTransactionDate() %></td>
                                <td>
                                    <a href="#" class="btn btn-sm btn-primary">Voir Détail</a>
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
                        <div class="text-end mt-4">
                            <a href="/erpnext/suppliers" class="btn btn-secondary">Retour à la liste</a>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>

</main>

<!-- ======= Footer ======= -->

<%@include file="../../static/footer.jsp" %>

</body>

</html>