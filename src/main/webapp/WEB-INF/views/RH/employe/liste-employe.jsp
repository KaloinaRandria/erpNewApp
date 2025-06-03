<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.vivant.Employe" %>
<%@ page import="mg.working.model.RH.vivant.Gender" %>
<%@ page import="mg.working.model.RH.organisation.Departement" %>

<%
    List<Employe> employes = (List<Employe>) request.getAttribute("employes");
    List<Gender> genders =(List<Gender>) request.getAttribute("genders");
    List<Departement> departements = (List<Departement>) request.getAttribute("departements");

    String nameValue = (String) request.getAttribute("nameValue");
    String employeeNameValue = (String) request.getAttribute("employeeNameValue");
    String genderValue = (String) request.getAttribute("genderValue");
    String departmentValue = (String) request.getAttribute("departmentValue");
    String statusValue = (String) request.getAttribute("statusValue");
    String startDateValue = (String) request.getAttribute("startDateValue");
    String endDateValue = (String) request.getAttribute("endDateValue");
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
                        <!-- Formulaire de recherche -->
                        <form method="get" action="${pageContext.request.contextPath}/rh/employe/search" class="row g-3 mt-3 mb-4">

                            <div class="col-md-3">
                                <label for="name" class="form-label">Matricule</label>
                                <input type="text" class="form-control" id="name" name="name"
                                       value="<%= nameValue != null ? nameValue : "" %>" placeholder="Ex: EMP-0001">

                            </div>

                            <div class="col-md-3">
                                <label for="employeeName" class="form-label">Nom complet</label>
                                <input type="text" class="form-control" id="employeeName" name="employeeName"
                                       value="<%= employeeNameValue != null ? employeeNameValue : "" %>" placeholder="Ex: Rakoto Jean">

                            </div>

                            <div class="col-md-2">
                                <label for="gender" class="form-label">Genre</label>
                                <select id="gender" name="gender" class="form-select">
                                    <option value="">-- Tous --</option>
                                    <% for (Gender gender : genders) {
                                        String selected = gender.getName().equals(genderValue) ? "selected" : "";
                                    %>
                                    <option value="<%= gender.getName() %>" <%= selected %>><%= gender.getName() %></option>
                                    <% } %>
                                </select>

                            </div>

                            <div class="col-md-2">
                                <label for="department" class="form-label">Département</label>
                                <select id="department" name="department" class="form-select">
                                    <option value="">-- Tous --</option>
                                    <% for (Departement departement : departements) {
                                        String name = departement.getName();
                                        String selected = (departmentValue != null && departmentValue.equals(name)) ? "selected" : "";
                                    %>
                                    <option value="<%= name %>" <%= selected %>><%= name %></option>
                                    <% } %>
                                </select>
                            </div>



                            <div class="col-md-2">
                                <label for="status" class="form-label">Statut</label>
                                <select id="status" name="status" class="form-select">
                                    <option value="">-- Tous --</option>
                                    <option value="Active" <%= "Active".equals(statusValue) ? "selected" : "" %>>Actif</option>
                                    <option value="Left" <%= "Left".equals(statusValue) ? "selected" : "" %>>Parti</option>
                                    <option value="Suspended" <%= "Suspended".equals(statusValue) ? "selected" : "" %>>Suspendu</option>
                                </select>
                            </div>

                            <div class="col-md-2">
                                <label for="startDate" class="form-label">Date d'embauche (début)</label>
                                <input type="date" class="form-control" id="startDate" name="startDate"
                                       value="<%= startDateValue != null ? startDateValue : "" %>">
                            </div>

                            <div class="col-md-2">
                                <label for="endDate" class="form-label">Date d'embauche (fin)</label>
                                <input type="date" class="form-control" id="endDate" name="endDate"
                                       value="<%= endDateValue != null ? endDateValue : "" %>">
                            </div>

                            <div class="col-md-12 text-end">
                                <button type="submit" class="btn btn-success mt-2">Rechercher</button>
                                <a href="${pageContext.request.contextPath}/rh/employe/list" class="btn btn-secondary mt-2">Réinitialiser</a>
                            </div>

                        </form>
                    </div>
                </div>
                <div class="card">
                    <div class="card-body">

                        <table class="table">
                            <thead>
                            <tr>
                                <th>Matricule</th>
                                <th>Nom</th>
                                <th>Genre</th>
                                <th>Designation</th>
                                <th>Département</th>
                                <th>Statut</th>
                                <th>Date d'embauche</th>
                                <th>Entreprise</th>


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
                                <td>
                                    <a href="${pageContext.request.contextPath}/rh/employe/fiche/<%= emp.getName() %>"
                                       class="btn btn-sm btn-primary d-flex align-items-center gap-1">
                                        <i class="bi bi-person-lines-fill"></i> Fiche
                                    </a>
                                </td>

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
