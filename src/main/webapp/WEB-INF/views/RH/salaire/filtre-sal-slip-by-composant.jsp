<%@ page import="mg.working.model.RH.salaire.component.SalaryComponent" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.SalarySlip" %>
<%@ page import="mg.working.service.RH.util.DateUtils" %>
<%@ page import="mg.working.model.RH.salaire.component.Earning" %>
<%@ page import="mg.working.service.formatage.Formatutil" %>
<%@ page import="mg.working.model.RH.salaire.component.Deduction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<SalaryComponent> salaryComponents = (List<SalaryComponent>) request.getAttribute("salaryComponents");
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
        <h1>Update Salaire de Base Multiple</h1>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">
                <form action="${pageContext.request.contextPath}/rh/salaire/filtre-salaire-component" method="post">

                    <!-- Nom du Salary Structure -->
                    <div class="mb-3">
                        <label for="component" class="form-label">Salary Component</label>
                        <select id="component" name="component" class="form-select">
                            <% for (SalaryComponent salaryComponent : salaryComponents) {%>
                            <option><%=salaryComponent.getSalary_component()%></option>
                            <% } %>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="componentMin" class="form-label">Component Min</label>
                        <input type="number" class="form-control" id="componentMin" name="componentMin">
                    </div>

                    <div class="col-md-2">
                        <label for="componentMax" class="form-label">Component Max</label>
                        <input type="number" class="form-control" id="componentMax" name="componentMax">
                    </div>

                    <div class="text-end mt-4">
                        <button type="submit" class="btn btn-primary">Filtrer</button>
                    </div>

                </form>
            </div>
        </div>
    </section>
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
                            <span class="badge bg-light text-dark"><%= Formatutil.formaterMontant(earning.getAmount()) %> €</span>
                        </li>
                        <% } %>
                    </ul>
                    <% } else { %>
                    <span class="text-muted">-</span>
                    <% } %>
                </td>
                <td><%=  Formatutil.formaterMontant(slip.getGrossPay()) %></td>
                <td>
                    <% if (slip.getDeductions() != null && !slip.getDeductions().isEmpty()) { %>
                    <ul class="list-unstyled mb-0">
                        <% for (Deduction deduction : slip.getDeductions()) { %>
                        <li>
                            <span class="text-danger fw-bold"><%= deduction.getSalary_component() %></span> :
                            <span class="badge bg-light text-dark"><%= Formatutil.formaterMontant(deduction.getAmount()) %> €</span>
                        </li>
                        <% } %>
                    </ul>
                    <% } else { %>
                    <span class="text-muted">-</span>
                    <% } %>
                </td>


                <td><%= Formatutil.formaterMontant(slip.getTotalDeduction()) %></td>
                <td><strong><%= Formatutil.formaterMontant(slip.getNetPay()) %></strong></td>
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
</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
