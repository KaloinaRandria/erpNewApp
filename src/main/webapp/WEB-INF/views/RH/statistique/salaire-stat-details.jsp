<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.SalarySlip" %>
<%@ page import="mg.working.service.RH.util.DateUtils" %>
<%@ page import="mg.working.model.RH.salaire.component.Deduction" %>
<%@ page import="mg.working.model.RH.salaire.component.Earning" %>

<%
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
        <h1>Bulletins de salaire par mois</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Accueil</a></li>
                <li class="breadcrumb-item active">Salaire mensuel</li>
            </ol>
        </nav>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">

                <!-- Tableau -->
                <% if (salarySlips != null && !salarySlips.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>Employé</th>
                            <th>Période</th>
                            <th>Earning</th>
                            <th>Total Earning (€)</th>
                            <th>Deduction</th>
                            <th>Total Déductions (€)</th>
                            <th>Net à payer (€)</th>
                            <th>Statut</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (SalarySlip slip : salarySlips) { %>
                        <tr>
                            <td><%= slip.getEmployeeName() %></td>
                            <td><%= DateUtils.getMoisFrancais()[slip.getStartDate().getMonthValue()] %> <%= slip.getStartDate().getYear() %></td>
                            <td>
                                <% if (slip.getEarnings() != null && !slip.getEarnings().isEmpty()) { %>
                                <ul class="list-unstyled mb-0">
                                    <% for (Earning earning : slip.getEarnings()) { %>
                                    <li>
                                        <span class="text-primary fw-bold"><%= earning.getSalary_component() %></span> :
                                        <span class="badge bg-light text-dark"><%= String.format("%.2f", earning.getAmount()) %> €</span>
                                    </li>
                                    <% } %>
                                </ul>
                                <% } else { %>
                                <span class="text-muted">-</span>
                                <% } %>
                            </td>
                            <td><%= String.format("%.2f", slip.getGrossPay()) %></td>
                            <td>
                                <% if (slip.getDeductions() != null && !slip.getDeductions().isEmpty()) { %>
                                <ul class="list-unstyled mb-0">
                                    <% for (Deduction deduction : slip.getDeductions()) { %>
                                    <li>
                                        <span class="text-danger fw-bold"><%= deduction.getSalary_component() %></span> :
                                        <span class="badge bg-light text-dark"><%= String.format("%.2f", deduction.getAmount()) %> €</span>
                                    </li>
                                    <% } %>
                                </ul>
                                <% } else { %>
                                <span class="text-muted">-</span>
                                <% } %>
                            </td>


                            <td><%= String.format("%.2f", slip.getTotalDeduction()) %></td>
                            <td><strong><%= String.format("%.2f", slip.getNetPay()) %></strong></td>
                            <td><span class="badge bg-info text-dark"><%= slip.getStatus() %></span></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="alert alert-warning mt-3">
                    Aucun bulletin trouvé pour les critères sélectionnés.
                </div>
                <% } %>

            </div>
        </div>
    </section>

</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
