<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.salaire.component.Earning" %>
<%@ page import="mg.working.model.RH.salaire.component.Deduction" %>

<%
    List<Earning> earningsComponents = (List<Earning>) request.getAttribute("earningsComponents");
    List<Deduction> deductionsComponents = (List<Deduction>) request.getAttribute("deductionsComponents");
%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../../static/head.jsp" %>

<body>
<%@ include file="../../static/header.jsp" %>
<%@ include file="../../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Créer une Structure de Salaire</h1>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">
                <form action="${pageContext.request.contextPath}/rh/salaire/salary-structure/save" method="post">

                    <!-- Nom du Salary Structure -->
                    <div class="mb-3">
                        <label for="name" class="form-label">Nom de la Structure</label>
                        <input type="text" id="name" name="name" class="form-control" required placeholder="Ex : Structure Stage" />
                    </div>

                    <hr />

                    <!-- Liste des Earnings -->
                    <h5>Composants de Revenu (Earnings)</h5>
                    <%
                        int i = 0;
                        for (Earning comp : earningsComponents) {
                    %>
                    <div class="row mb-2 align-items-center">
                        <div class="col-md-4">
                            <input type="hidden" name="earnings[<%= i %>].salary_component" value="<%= comp.getSalary_component() %>" />
                            <label><%= comp.getSalary_component() %></label>
                        </div>
<%--                        <div class="col-md-3">--%>
<%--                            <input type="number" step="0.01" min="0" class="form-control" name="earnings[<%= i %>].amount" value="0.00"--%>
<%--                                   placeholder="Montant (€)">--%>
<%--                        </div>--%>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="earnings[<%= i %>].formula"
                                   placeholder="formule">
                        </div>

                    </div>
                    <%
                            i++;
                        }
                    %>

                    <hr />

                    <!-- Liste des Deductions -->
                    <h5>Composants de Déduction (Deductions)</h5>
                    <%
                        int j = 0;
                        for (Deduction comp : deductionsComponents) {
                    %>
                    <div class="row mb-2 align-items-center">
                        <div class="col-md-4">
                            <input type="hidden" name="deductions[<%= j %>].salary_component" value="<%= comp.getSalary_component() %>" />
                            <label><%= comp.getSalary_component() %></label>
                        </div>
<%--                        <div class="col-md-3">--%>
<%--                            <input type="number" step="0.01" min="0" class="form-control" name="deductions[<%= j %>].amount" value="0.00"--%>
<%--                                   placeholder="Montant (€)">--%>
<%--                        </div>--%>
                        <div class="col-md-5">
                            <input type="text" class="form-control" name="deductions[<%= j %>].formula"
                                   placeholder="formule">
                        </div>
                    </div>
                    <%
                            j++;
                        }
                    %>

                    <div class="text-end mt-4">
                        <button type="submit" class="btn btn-primary">Valider</button>
                        <a href="${pageContext.request.contextPath}/rh/salaire/structures" class="btn btn-secondary">Annuler</a>
                    </div>

                </form>
            </div>
        </div>
    </section>
</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
