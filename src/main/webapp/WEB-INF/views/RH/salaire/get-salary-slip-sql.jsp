<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.StatistiqueSalaire" %>
<%@ page import="mg.working.service.formatage.Formatutil" %>
<%@ page import="mg.working.model.RH.salaire.SalarySlip" %>

<%
    // Récupération de la liste passée par le contrôleur
    List<StatistiqueSalaire> statistiqueSalaires = (List<StatistiqueSalaire>) request.getAttribute("stats");
    Integer selectedYear = (Integer) request.getAttribute("selectedYear");

    List<SalarySlip> salarySlips = (List<SalarySlip>) request.getAttribute("salarySlips");
%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../../static/head.jsp" %>

<body>

<%@ include file="../../static/header.jsp" %>
<%@ include file="../../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Statistique Salaire par Mois</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Accueil</a></li>
                <li class="breadcrumb-item active">Salary Slip SQL</li>
            </ol>
        </nav>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">
                <!-- Tableau des statistiques -->
                <% if (salarySlips != null && !salarySlips.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>name</th>
                            <th>Employee</th>
                            <th>Employee Name</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (SalarySlip salarySlip : salarySlips) { %>
                        <tr>

                            <td><%= salarySlip.getName() %></td>
                            <td><%= salarySlip.getEmployee() %></td>
                            <td><%= salarySlip.getEmployeeName() %></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="alert alert-warning mt-3">
                    Aucune donnée statistique disponible pour l'année sélectionnée.
                </div>
                <% } %>

            </div>
        </div>
    </section>

</main>

<%@ include file="../../static/footer.jsp" %>

</body>
</html>
