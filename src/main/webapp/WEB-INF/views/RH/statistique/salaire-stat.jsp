<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.StatistiqueSalaire" %>
<%@ page import="mg.working.model.RH.salaire.component.Deduction" %>
<%@ page import="mg.working.model.RH.salaire.component.Earning" %>
<%@ page import="java.util.Locale" %>

<%
    // Récupération de la liste passée par le contrôleur
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
        <h1>Statistique Salaire par Mois</h1>
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
                <form method="get"
                      action="${pageContext.request.contextPath}/rh/salaire/statistique-salaire"
                      class="row g-3 mb-4 align-items-end">
                    <div class="col-md-2">
                        <label for="year" class="form-label">Année</label>
                        <input type="number"
                               id="year"
                               name="year"
                               class="form-control"
                               min="2000"
                               max="2100"
                               value="<%= (selectedYear != null) ? selectedYear : "" %>">
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
                            <td>
                                <a href="${pageContext.request.contextPath}/rh/salaire/statistique-salaire/<%= stat.getMonth() %>">
                                    <strong><%= String.format("%.2f", stat.getNetTotal()) %></strong>
                                </a>
                            </td>

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

                <!-- Espace pour le graphique Chart.js -->
                <% if (statistiqueSalaires != null && !statistiqueSalaires.isEmpty()) { %>
                <div class="mt-5">
                    <h2 class="text-center mb-4">Évolution mensuelle des totaux</h2>
                    <canvas id="salaryChart" height="400"></canvas>
                </div>
                <% } %>

            </div>
        </div>
    </section>

</main>

<%@ include file="../../static/footer.jsp" %>

<!-- Charger Chart.js (version 4.x) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>

<%-- Construire les tableaux JS à partir de stat.getMonth(), stat.getGrossTotal(), etc. --%>
<script>
    <% if (statistiqueSalaires != null && !statistiqueSalaires.isEmpty()) { %>
    // Récupérer les données depuis le back-end
    const labels = [
        <% for (int i = 0; i < statistiqueSalaires.size(); i++) {
               String mois = statistiqueSalaires.get(i).getMonth(); %>
        '<%= mois %>'<%= (i < statistiqueSalaires.size() - 1 ? "," : "") %>
        <% } %>
    ];

    const grossData = [
        <% for (int i = 0; i < statistiqueSalaires.size(); i++) {
               double val = statistiqueSalaires.get(i).getGrossTotal(); %>
        <%= String.format(Locale.US, "%.2f", val) %><%= (i < statistiqueSalaires.size() - 1 ? "," : "") %>
        <% } %>
    ];

    const deductionData = [
        <% for (int i = 0; i < statistiqueSalaires.size(); i++) {
               double val = statistiqueSalaires.get(i).getDeductionTotal(); %>
        <%= String.format(Locale.US, "%.2f", val) %><%= (i < statistiqueSalaires.size() - 1 ? "," : "") %>
        <% } %>
    ];

    const netData = [
        <% for (int i = 0; i < statistiqueSalaires.size(); i++) {
               double val = statistiqueSalaires.get(i).getNetTotal(); %>
        <%= String.format(Locale.US, "%.2f", val) %><%= (i < statistiqueSalaires.size() - 1 ? "," : "") %>
        <% } %>
    ];

    // Configurer Chart.js
    const ctx = document.getElementById('salaryChart').getContext('2d');
    const salaryChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: "Total Earnings",
                    data: grossData,
                    borderColor: "blue",
                    borderWidth: 2,
                    tension: 0.2
                },
                {
                    label: "Total Deductions",
                    data: deductionData,
                    borderColor: "black",
                    borderWidth: 2,
                    borderDash: [5, 5],
                    tension: 0.2
                },
                {
                    label: "Net Pay",
                    data: netData,
                    borderColor: "#2c3e50",
                    borderWidth: 2,
                    tension: 0.2
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Évolution mensuelle des totaux de salaire'
                },
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Mois'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Montant (€)'
                    },
                    ticks: {
                        stepSize: 500000 // Ajustez selon vos besoins
                    }
                }
            }
        }
    });
    <% } %>
</script>
</body>
</html>
