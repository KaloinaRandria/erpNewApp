<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mg.working.model.RH.salaire.SalarySlip" %>
<%@ page import="mg.working.model.RH.salaire.component.Earning" %>
<%@ page import="mg.working.model.RH.salaire.component.Deduction" %>
<%@ page import="java.util.List" %>

<%
    SalarySlip slip = (SalarySlip) request.getAttribute("salarySlip");
    List<Earning> earnings = slip.getEarnings();
    List<Deduction> deductions = slip.getDeductions();
%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../../static/head.jsp" %>

<body>
<%@ include file="../../static/header.jsp" %>
<%@ include file="../../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Bulletin de Salaire</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Accueil</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/rh/employe/list">Employés</a></li>
                <li class="breadcrumb-item active">Aperçu bulletin</li>
            </ol>
        </nav>
    </div>

    <section class="section">
        <div class="row">
            <div class="col-lg-8 mx-auto">
                <div class="card">
                    <div class="card-body pt-4">

                        <h5 class="card-title">Détails du bulletin</h5>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Nom :</div>
                            <div class="col-sm-8"><%= slip.getEmployeeName() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Période :</div>
                            <div class="col-sm-8"><%= slip.getStartDate() %> au <%= slip.getEndDate() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Date de versement :</div>
                            <div class="col-sm-8"><%= slip.getPostingDate() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Salaire brut (€) :</div>
                            <div class="col-sm-8"><%= String.format("%.2f", slip.getGrossPay()) %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Déductions (€) :</div>
                            <div class="col-sm-8"><%= String.format("%.2f", slip.getTotalDeduction()) %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Net à payer (€) :</div>
                            <div class="col-sm-8"><%= String.format("%.2f", slip.getNetPay()) %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Statut :</div>
                            <div class="col-sm-8"><span class="badge bg-info text-dark"><%= slip.getStatus() %></span></div>
                        </div>

                        <div class="text-end mt-4">
                            <a href="${pageContext.request.contextPath}/rh/employe/fiche/<%= slip.getEmployee() %>" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Retour à la fiche employé
                            </a>
                        </div>

                    </div>
                </div>
            </div>
            <div class="card mt-4">
                <div class="card-body">
                    <h5 class="card-title">Détail du salaire</h5>

                    <div class="row mb-3">
                        <div class="col-md-4"><strong>Structure salariale :</strong></div>
                        <div class="col-md-8"><%= slip.getSalaryStructure() %></div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-4"><strong>Période :</strong></div>
                        <div class="col-md-8"><%= slip.getStartDate() %> au <%= slip.getEndDate() %></div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-4"><strong>Date de versement :</strong></div>
                        <div class="col-md-8"><%= slip.getPostingDate() %></div>
                    </div>

                    <div class="table-responsive mt-4">
                        <table class="table table-bordered table-striped">
                            <thead class="table-light">
                            <tr>
                                <th>Composant</th>
                                <th>Montant (€)</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><strong>Salaire brut</strong></td>
                                <td><%= String.format("%.2f", slip.getGrossPay()) %></td>
                            </tr>
                            <tr>
                                <td><strong>Déductions</strong></td>
                                <td><%= String.format("%.2f", slip.getTotalDeduction()) %></td>
                            </tr>
                            <tr class="table-success">
                                <td><strong>Net à payer</strong></td>
                                <td><strong><%= String.format("%.2f", slip.getNetPay()) %> €</strong></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="card mt-4">
                <div class="card-body">
                    <h5 class="card-title">Détails des composants</h5>
                    <div class="row">
                        <!-- Earnings -->
                        <div class="col-md-6">
                            <h6 class="text-success">Composants du Salaire (Earnings)</h6>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Composant</th>
                                        <th>Montant (€)</th>
                                        <th>Année à ce jour (€)</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% if (earnings != null && !earnings.isEmpty()) {
                                        for (Earning e : earnings) { %>
                                    <tr>
                                        <td><%= e.getSalary_component() %></td>
                                        <td><%= String.format("%.2f", e.getAmount()) %></td>
                                        <td><%= String.format("%.2f", e.getYear_to_date()) %></td>
                                    </tr>
                                    <% }} else { %>
                                    <tr><td colspan="3" class="text-center">Aucun composant trouvé</td></tr>
                                    <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <!-- Deductions -->
                        <div class="col-md-6">
                            <h6 class="text-danger">Déductions</h6>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Composant</th>
                                        <th>Montant (€)</th>
                                        <th>Année à ce jour (€)</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% if (deductions != null && !deductions.isEmpty()) {
                                        for (Deduction d : deductions) { %>
                                    <tr>
                                        <td><%= d.getSalary_component() %></td>
                                        <td><%= String.format("%.2f", d.getAmount()) %></td>
                                        <td><%= String.format("%.2f", d.getYear_to_date()) %></td>
                                    </tr>
                                    <% }} else { %>
                                    <tr><td colspan="3" class="text-center">Aucune déduction trouvée</td></tr>
                                    <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>

</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
