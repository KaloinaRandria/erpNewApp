<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.vivant.Employe" %>

<%
    List<Employe> employes = (List<Employe>) request.getAttribute("employes");
%>

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
        <h1>Liste des Employés</h1>
    </div><!-- End Page Title -->

    <section class="section">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Matricule</th>
                                <th>Nom</th>
                                <th>Genre</th>
                                <th>Poste</th>
                                <th>Département</th>
                                <th>Statut</th>
                                <th>Date d'embauche</th>
                                <th>Entreprise</th>
                                <th>Succursale</th>
                                <th>Téléphone</th>
                                <th>Email</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                if (employes != null) {
                                    for (Employe emp : employes) { %>
                            <tr>
                                <td><%= emp.getName() %></td>
                                <td><%= emp.getEmployee_name() %></td>
                                <td><%= emp.getGender() %></td>
                                <td><%= emp.getDesignation() %></td>
                                <td><%= emp.getDepartment() %></td>
                                <td><%= emp.getStatus() %></td>
                                <td><%= emp.getDate_of_joining() %></td>
                                <td><%= emp.getCompany() %></td>
                                <td><%= emp.getBranch() %></td>
                                <td><%= emp.getCell_number() %></td>
                                <td><%= emp.getCompany_email() %></td>
                            </tr>
                            <%  }
                                    } else { %>
                            <tr><td colspan="9">Aucun employe trouvé.</td></tr>
                            <% } %>
                            </tbody>
                        </table>
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
