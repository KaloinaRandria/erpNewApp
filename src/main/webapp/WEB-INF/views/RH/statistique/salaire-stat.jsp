<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.StatistiqueSalaire" %>
<%@ page import="mg.working.model.RH.salaire.component.Deduction" %>
<%@ page import="mg.working.model.RH.salaire.component.Earning" %>

<%
    // La liste et l'année sélectionnée sont fournies par le contrôleur.
    List<StatistiqueSalaire> statistiqueSalaires = (List<StatistiqueSalaire>) request.getAttribute("stats");
    Integer selectedYear = (Integer) request.getAttribute("selectedYear");
%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../../static/head.jsp" %>

<body>

<%@ include file="../../static/header.jsp" %>
<%@ include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Statistiques Salaires par Mois</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Accueil</a></li>
                <li class="breadcrumb-item active">Statistique Salaire</li>
            </ol>
        </nav>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">

                <!-- Filtre par année -->
                <form method="get" action="${pageContext.request.contextPath}/rh/salaire/statistique-salaire"
                      class="row g-3 mb-4 align-items-end">
                    <div class="col-md-2">
                        <label for="year" class="form-label">Année</label>
                        <input type="number"
                               id="year"
                               name="year"
                               class="form-control"
                               min="2000"
                               max="2100"
                               value="<%= (selectedYear != null) ? selectedYear : "" %>"
                        >
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-success">Filtrer</button>
                        <a href="${pageContext.request.contextPath}/rh/salaire/statistique-salaire"
                           class="btn btn-secondary ms-2">Réinitialiser</a>
                    </div>
                </form>

                <!-- Tableau des statistiques -->
                <% if (statistiqueSalaires != null && !statistiqueSalaires.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead class="table-light">
                        <tr>
                            <th>Mois</th>
                            <th>Détail Earning</th>
                            <th>Détail Deduction</th>
                            <th>Total Earning (€)</th>
                            <th>Total Deduction (€)</th>
                            <th>Net à Payer (€)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (StatistiqueSalaire stat : statistiqueSalaires) { %>
                        <tr>
                            <td><strong><%= stat.getMonth() %></strong></td>

                            <!-- Détail Earning -->
                            <td>
                                <% if (stat.getEarnings() != null && !stat.getEarnings().isEmpty()) { %>
                                <ul class="list-unstyled mb-0">
                                    <% for (Earning earning : stat.getEarnings()) { %>
                                    <li>
                                                            <span class="text-primary fw-bold">
                                                                <%= earning.getSalary_component() %>
                                                            </span> :
                                        <span class="badge bg-light text-dark">
                                                                <%= String.format("%.2f", earning.getAmount()) %> €
                                                            </span>
                                    </li>
                                    <% } %>
                                </ul>
                                <% } else { %>
                                <span class="text-muted">-</span>
                                <% } %>
                            </td>

                            <!-- Détail Deduction -->
                            <td>
                                <% if (stat.getDeductions() != null && !stat.getDeductions().isEmpty()) { %>
                                <ul class="list-unstyled mb-0">
                                    <% for (Deduction deduction : stat.getDeductions()) { %>
                                    <li>
                                                            <span class="text-danger fw-bold">
                                                                <%= deduction.getSalary_component() %>
                                                            </span> :
                                        <span class="badge bg-light text-dark">
                                                                <%= String.format("%.2f", deduction.getAmount()) %> €
                                                            </span>
                                    </li>
                                    <% } %>
                                </ul>
                                <% } else { %>
                                <span class="text-muted">-</span>
                                <% } %>
                            </td>

                            <td><%= String.format("%.2f", stat.getGrossTotal()) %></td>
                            <td><%= String.format("%.2f", stat.getDeductionTotal()) %></td>
                            <td><strong><%= String.format("%.2f", stat.getNetTotal()) %></strong></td>
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
