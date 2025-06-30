<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.vivant.Employe" %>
<%@ page import="mg.working.model.RH.salaire.SalaryStructureForm" %>
<%@ page import="mg.working.model.RH.organisation.Company" %>

<%
    List<Employe> employes = (List<Employe>) request.getAttribute("employes");
    List<SalaryStructureForm>  salaryStructureForms = (List<SalaryStructureForm>) request.getAttribute("salaryStructureForms");
%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../../static/head.jsp" %>

<body>
<%@ include file="../../static/header.jsp" %>
<%@ include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Generer Salary Slip</h1>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">
                <form action="${pageContext.request.contextPath}/rh/salaire/generer-salary-slip" method="post">

                    <!-- Nom du Salary Structure -->
                    <div class="mb-3">
                        <label for="name" class="form-label">Employe</label>
                        <select id="employe" name="employe" class="form-select">
                            <% for (Employe employe : employes) {%>
                            <option value="<%=employe.getName()%>"><%=employe.getEmployee_name() %></option>
                            <% } %>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="newSalaire" class="form-label">Ecraser Salaire</label>
                        <input type="checkbox" id="newSalaire" name="newSalaire">
                    </div>

                    <div class="col-md-2">
                        <label for="moyenneSalaire" class="form-label">Moyenne Salaire</label>
                        <input type="checkbox" id="moyenneSalaire" name="moyenneSalaire">
                    </div>

                    <div class="col-md-2">
                        <label for="startDate" class="form-label">Salaire de Base</label>
                        <input type="number" class="form-control" id="salaireBase" name="salaireBase" value="0">
                    </div>

                    <div class="col-md-2">
                        <label for="startDate" class="form-label">Date début</label>
                        <input type="month" class="form-control" id="startDate" name="startDate" value="">
                    </div>

                    <div class="col-md-2">
                        <label for="endDate" class="form-label">Date fin</label>
                        <input type="month" class="form-control" id="endDate" name="endDate" value="">
                    </div>

                    <div class="text-end mt-4">
                        <button type="submit" class="btn btn-primary">generer</button>
                    </div>

                </form>
            </div>
        </div>
    </section>
</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
