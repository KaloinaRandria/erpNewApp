<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.RequestForQuotationSupplier" %>

<%
    List<RequestForQuotationSupplier> suppliers = (List<RequestForQuotationSupplier>) request.getAttribute("rfqs");
    String rfqName = (String) request.getAttribute("rfqName");
%>

<!DOCTYPE html>
<html lang="fr">

<%@ include file="../static/head.jsp" %>

<body>

<!-- ======= Header ======= -->
<%@ include file="../static/header.jsp" %>

<!-- ======= Sidebar ======= -->
<%@ include file="../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Fournisseurs pour la RFQ : <%= rfqName %></h1>
    </div>

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Fournisseur</th>
                                <th>Nom Fournisseur</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (suppliers != null && !suppliers.isEmpty()) {
                                    for (RequestForQuotationSupplier s : suppliers) {
                            %>
                            <tr>
                                <td><%= s.getName() %></td>
                                <td><%= s.getSupplier() %></td>
                                <td><%= s.getSupplierName() %></td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="6">Aucun fournisseur trouvé pour cette RFQ.</td></tr>
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                        <a href="/erpnext/request-for-quotation" class="btn btn-secondary mt-3">← Retour à la liste des RFQ</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

</main>

<%@ include file="../static/footer.jsp" %>

</body>

</html>
