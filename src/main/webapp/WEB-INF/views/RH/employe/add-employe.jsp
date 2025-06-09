<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.RH.vivant.Gender" %>

<%
    List<Gender> genders = (List<Gender>) request.getAttribute("genders");

    // Champs pré-remplis après validation échouée
    String prenom = request.getParameter("prenom") != null ? request.getParameter("prenom") : "";
    String nom = request.getParameter("nom") != null ? request.getParameter("nom") : "";
    String matricule = request.getParameter("matricule") != null ? request.getParameter("matricule") : "";
    String genre = request.getParameter("genre") != null ? request.getParameter("genre") : "";
    String dateNaissance = request.getParameter("dateNaissance") != null ? request.getParameter("dateNaissance") : "";
    String dateEmbauche = request.getParameter("dateEmbauche") != null ? request.getParameter("dateEmbauche") : "";
    String company = request.getParameter("company") != null ? request.getParameter("company") : "";

%>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../../static/head.jsp" %>

<body>
<%@ include file="../../static/header.jsp" %>
<%@ include file="../../static/sidebar.jsp" %>

<main id="main" class="main">

    <div class="pagetitle">
        <h1>Ajouter un Employé</h1>
    </div>

    <section class="section">
        <div class="card">
            <div class="card-body pt-4">
                <% String error = (String) request.getAttribute("error"); %>
                <% if (error != null) { %>
                <div class="alert alert-danger">
                    <%= error %>
                </div>
                <% } %>
                <form action="${pageContext.request.contextPath}/rh/employe/save" method="post" class="row g-3">

                    <div class="col-md-4">
                        <label for="prenom" class="form-label">Prénom</label>
                        <input type="text" class="form-control" id="prenom" name="prenom" value="<%= prenom %>" required>
                    </div>

                    <div class="col-md-4">
                        <label for="nom" class="form-label">Nom</label>
                        <input type="text" class="form-control" id="nom" name="nom" value="<%= nom %>" required>
                    </div>

                    <div class="col-md-4">
                        <label for="matricule" class="form-label">Matricule</label>
                        <input type="number" class="form-control" id="matricule" name="matricule" value="<%= matricule %>" required>
                    </div>

                    <div class="col-md-3">
                        <label for="genre" class="form-label">Genre</label>
                        <select id="genre" name="genre" class="form-select" required>
                            <option value="">-- Sélectionner --</option>
                            <% for (Gender g : genders) {
                                String selected = g.getName().equalsIgnoreCase(genre) ? "selected" : "";
                            %>
                            <option value="<%= g.getName() %>" <%= selected %>><%= g.getName() %></option>
                            <% } %>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label for="dateNaissance" class="form-label">Date de naissance</label>
                        <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" value="<%= dateNaissance %>" required>
                    </div>

                    <div class="col-md-3">
                        <label for="dateEmbauche" class="form-label">Date d'embauche</label>
                        <input type="date" class="form-control" id="dateEmbauche" name="dateEmbauche" value="<%= dateEmbauche %>" required>
                    </div>

                    <div class="col-12 text-end">
                        <button type="submit" class="btn btn-success">Créer</button>
                        <a href="${pageContext.request.contextPath}/rh/employe/list" class="btn btn-secondary ms-2">Annuler</a>
                    </div>

                </form>

            </div>
        </div>
    </section>

</main>

<%@ include file="../../static/footer.jsp" %>
</body>
</html>
