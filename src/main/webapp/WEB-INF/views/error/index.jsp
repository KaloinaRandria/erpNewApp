<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.lang.String" %>

<!DOCTYPE html>
<html lang="fr">
<%@ include file="../static/head.jsp" %>
<body class="bg-light d-flex flex-column min-vh-100">

<%@ include file="../static/header.jsp" %>
<%@ include file="../static/sidebar.jsp" %>

<style>
    .center-container {
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: calc(100vh - 100px);
        padding-left: 200px;
    }
</style>

<div class="center-container">
    <div class="card shadow-sm rounded-4 p-5 bg-white">
        <div class="card-body text-center">
            <h1 class="card-title text-danger display-5 mb-4">Une erreur est survenue</h1>

            <%
                String errorMessage = (String) request.getAttribute("error");
                if (errorMessage == null || errorMessage.isEmpty()) {
                    errorMessage = "Erreur inconnue. Veuillez réessayer plus tard.";
                }
            %>

            <p class="lead text-dark"><%= errorMessage %></p>

            <a href="<%= request.getContextPath() %>/accueil" class="btn btn-primary mt-4">Retour à l'accueil</a>
        </div>
    </div>
</div>

</body>
</html>
