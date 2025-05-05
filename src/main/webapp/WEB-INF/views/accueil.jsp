<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<%@include file="static/head.jsp"%>
<body class="bg-light d-flex flex-column min-vh-100">
<%@include file="static/header.jsp"%>
<%@include file="static/sidebar.jsp"%>

<style>
    .center-container {
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: calc(100vh - 100px); /* ajuste selon la hauteur de header/sidebar */
        padding-left: 200px; /* espace pour le sidebar */
    }
</style>

<div class="center-container">
    <div class="card shadow-sm rounded-4 p-5">
        <div class="card-body text-center">
            <h1 class="card-title display-4 mb-4 text-primary">Bienvenue sur <strong>ERPNEXT New App</strong></h1>
            <p class="lead">Ce portail vous permet de gérer vos fournisseurs, clients, produits et bien plus encore.</p>
        </div>
    </div>
</div>

</body>
</html>
