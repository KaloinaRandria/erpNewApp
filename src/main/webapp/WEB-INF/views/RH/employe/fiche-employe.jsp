<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mg.working.model.RH.vivant.Employe" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.SalarySlip" %>

<%
    Employe employe = (Employe) request.getAttribute("employe");
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
        <h1>Fiche Employé</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Accueil</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/rh/employe/list">Employés</a></li>
                <li class="breadcrumb-item active">Fiche</li>
            </ol>
        </nav>
    </div>

    <section class="section profile">
        <div class="row">
            <div class="col-lg-8 mx-auto">
                <div class="card">
                    <div class="card-body pt-4">

                        <div class="d-flex flex-column align-items-center text-center">
                            <div class="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center"
                                 style="width: 100px; height: 100px; font-size: 36px;">
                                <i class="bi bi-person-fill"></i>
                            </div>
                            <h4 class="mt-3"><%= employe.getEmployee_name() %></h4>
<%--                            <p class="text-muted"><%= employe.getDesignation() %> - <%= employe.getDepartment() %></p>--%>
                        </div>

                        <hr>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Matricule :</div>
                            <div class="col-sm-8"><%= employe.getName() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Genre :</div>
                            <div class="col-sm-8"><%= employe.getGender() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Statut :</div>
                            <div class="col-sm-8"><%= employe.getStatus() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Date d'embauche :</div>
                            <div class="col-sm-8"><%= employe.getDate_of_joining() %></div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-sm-4 fw-bold">Entreprise :</div>
                            <div class="col-sm-8"><%= employe.getCompany() %></div>
                        </div>

<%--                        <div class="row mb-3">--%>
<%--                            <div class="col-sm-4 fw-bold">Agence :</div>--%>
<%--                            <div class="col-sm-8"><%= employe.getBranch() %></div>--%>
<%--                        </div>--%>

<%--                        <div class="row mb-3">--%>
<%--                            <div class="col-sm-4 fw-bold">Téléphone :</div>--%>
<%--                            <div class="col-sm-8"><%= employe.getCell_number() %></div>--%>
<%--                        </div>--%>

<%--                        <div class="row mb-3">--%>
<%--                            <div class="col-sm-4 fw-bold">Email professionnel :</div>--%>
<%--                            <div class="col-sm-8"><%= employe.getCompany_email() %></div>--%>
<%--                        </div>--%>

                        <div class="text-end mt-4">
                            <a href="${pageContext.request.contextPath}/rh/employe/list" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Retour
                            </a>
                        </div>

                    </div>
                </div>
            </div>
            <div class="card mt-4">
                <div class="card-body">
                    <h5 class="card-title">Bulletins de salaire</h5>

                    <% if (salarySlips != null && !salarySlips.isEmpty()) { %>
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover">
                            <thead class="table-light">
                            <tr>
                                <th>Période</th>
                                <th>Date de versement</th>
                                <th>Brut (€)</th>
                                <th>Déductions (€)</th>
                                <th>Net à payer (€)</th>
                                <th>Statut</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (SalarySlip slip : salarySlips) { %>
                            <tr>
                                <td><%= slip.getStartDate() %> au <%= slip.getEndDate() %></td>
                                <td><%= slip.getPostingDate() %></td>
                                <td><%= String.format("%.2f", slip.getGrossPay()) %></td>
                                <td><%= String.format("%.2f", slip.getTotalDeduction()) %></td>
                                <td><%= String.format("%.2f", slip.getNetPay()) %></td>
                                <td><span class="badge bg-info text-dark"><%= slip.getStatus() %></span></td>
                                <td class="d-flex gap-2">
                                    <form action="${pageContext.request.contextPath}/rh/salaire/salary-slip" method="get" class="d-inline">
                                        <input type="hidden" name="name" value="<%= slip.getName() %>">
                                        <button type="submit" class="btn btn-sm btn-outline-primary" title="Aperçu">
                                            <i class="bi bi-eye"></i>
                                        </button>
                                    </form>

                                    <form action="${pageContext.request.contextPath}/rh/export/pdf" method="get" class="d-inline">
                                        <input type="hidden" name="name" value="<%= slip.getName() %>">
                                        <button type="submit" class="btn btn-sm btn-outline-danger" title="Exporter en PDF">
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
                        Aucun bulletin de salaire trouvé pour cet employé.
                    </div>
                    <% } %>
                </div>
            </div>

        </div>
    </section>

</main>

<%@ include file="../../static/footer.jsp" %>

</body>
</html>
