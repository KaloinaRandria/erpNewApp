<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.SalarySlip" %>

<%
    List<SalarySlip> salarySlips = (List<SalarySlip>) request.getAttribute("salarySlips");
    Integer selectedYear = (Integer) request.getAttribute("selectedYear");
    Integer selectedMonth = (Integer) request.getAttribute("selectedMonth");
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

                <!-- Filtre -->
                <form method="get" action="${pageContext.request.contextPath}/rh/salaire/salary-month" class="row g-3 mb-4">
                    <div class="col-md-3">
                        <label for="year" class="form-label">Année</label>
                        <input type="number" min="2000" max="2100" id="year" name="year" class="form-control"
                               value="<%= selectedYear != null ? selectedYear : "" %>" placeholder="Ex: 2025">
                    </div>
                    <div class="col-md-3">
                        <label for="month" class="form-label">Mois</label>
                        <select id="month" name="month" class="form-select">
                            <option value="">-- Tous --</option>
                            <%
                                String[] mois = {"01","02","03","04","05","06","07","08","09","10","11","12"};
                                for (int i = 0; i < mois.length; i++) {
                                    String val = mois[i];
                                    boolean selected = (selectedMonth != null && selectedMonth == (i+1));
                            %>
                            <option value="<%= i+1 %>" <%= selected ? "selected" : "" %>>Mois <%= val %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-3 d-flex align-items-end">
                        <button type="submit" class="btn btn-success">Filtrer</button>
                        <a href="${pageContext.request.contextPath}/rh/salaire/salary-month" class="btn btn-secondary ms-2">Réinitialiser</a>
                    </div>
                </form>

                <!-- Tableau -->
                <% if (salarySlips != null && !salarySlips.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>Employé</th>
                            <th>Département</th>
                            <th>Poste</th>
                            <th>Période</th>
                            <th>Date de versement</th>
                            <th>Brut (€)</th>
                            <th>Déductions (€)</th>
                            <th>Net à payer (€)</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (SalarySlip slip : salarySlips) { %>
                        <tr>
                            <td><%= slip.getEmployeeName() %></td>
                            <td><%= slip.getDepartment() %></td>
                            <td><%= slip.getDesignation() %></td>
                            <td><%= slip.getStartDate() %> au <%= slip.getEndDate() %></td>
                            <td><%= slip.getPostingDate() %></td>
                            <td><%= String.format("%.2f", slip.getGrossPay()) %></td>
                            <td><%= String.format("%.2f", slip.getTotalDeduction()) %></td>
                            <td><strong><%= String.format("%.2f", slip.getNetPay()) %></strong></td>
                            <td><span class="badge bg-info text-dark"><%= slip.getStatus() %></span></td>
                            <td class="d-flex gap-2">
                                <form action="${pageContext.request.contextPath}/rh/salaire/salary-slip" method="get" class="d-inline">
                                    <input type="hidden" name="name" value="<%= slip.getName() %>">
                                    <button type="submit" class="btn btn-sm btn-outline-primary" title="Aperçu">
                                        <i class="bi bi-eye"></i>
                                    </button>
                                </form>
                                <form action="${pageContext.request.contextPath}/rh/salaire/salary-slip/export" method="get" class="d-inline">
                                    <input type="hidden" name="name" value="<%= slip.getName() %>">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Exporter PDF">
                                        <i class="bi bi-file-earmark-pdf-fill"></i>
                                    </button>
                                </form>
                            </td>
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
