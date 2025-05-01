<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.Supplier" %>

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
        <h1>Liste Fournisseurs</h1>
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
                                <th>Nom</th>
                                <th>Groupe</th>
                                <th>Type</th>
                                <th>Pays</th>
                                <th>Langue</th>
                                <th>Email</th>
                                <th>Téléphone</th>
                                <th>Adresse</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (suppliers != null) {
                                    for (Supplier s : suppliers) {
                            %>
                            <tr>
                                <td><%= s.getName() %></td>
                                <td><%= s.getSupplierName() %></td>
                                <td><%= s.getSupplierGroup() %></td>
                                <td><%= s.getSupplierType() %></td>
                                <td><%= s.getCountry() %></td>
                                <td><%= s.getLanguage() %></td>
                                <td><%= s.getEmailId() %></td>
                                <td><%= s.getMobileNo() %></td>
                                <td><%= s.getPrimaryAddress() %></td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr><td colspan="9">Aucun fournisseur trouvé.</td></tr>
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