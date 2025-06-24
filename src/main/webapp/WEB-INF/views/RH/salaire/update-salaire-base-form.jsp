<%@ page import="mg.working.model.RH.salaire.component.SalaryComponent" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<SalaryComponent> salaryComponents = (List<SalaryComponent>) request.getAttribute("salaryComponents");
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
                <form action="${pageContext.request.contextPath}/rh/salaire/update" method="post">

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

                    <div class="col-md-2">
                        <label for="salaireMin" class="form-label">Salaire Min</label>
                        <input type="number" class="form-control" id="salaireMin" name="salaireMin">
                    </div>
                    <div class="col-md-2">
                        <label for="salaireMax" class="form-label">Salaire Max</label>
                        <input type="number" class="form-control" id="salaireMax" name="salaireMax">
                    </div>


                    <div class="col-md-2">
                        <label for="pourcentage" class="form-label">Pourcentage</label>
                        <input type="number" class="form-control" id="pourcentage" name="pourcentage">
                    </div>


                    <div class="text-end mt-4">
                        <button type="submit" class="btn btn-primary">Mettre a jour</button>
                    </div>

                </form>
            </div>
        </div>
    </section>
</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
