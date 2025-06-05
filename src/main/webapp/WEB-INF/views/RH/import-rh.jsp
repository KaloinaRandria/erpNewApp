<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<%@include file="../static/head.jsp" %>

<body>
<%@include file="../static/header.jsp" %>
<%@include file="../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Importation de données CSV</h1>
    </div>

    <section class="section">
        <div class="row">
            <div class="col-md-6">
                <form action="${pageContext.request.contextPath}/rh/import/import-data" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="fichier1" class="form-label">Employés :</label>
                        <input type="file" class="form-control" id="fichier1" name="fichier1" accept=".csv" required>
                    </div>
                    <div class="mb-3">
                        <label for="fichier2" class="form-label">Composants & Structures de salaire :</label>
                        <input type="file" class="form-control" id="fichier2" name="fichier2" accept=".csv" required>
                    </div>
                    <div class="mb-3">
                        <label for="fichier3" class="form-label">Affectation & Fiches de paie :</label>
                        <input type="file" class="form-control" id="fichier3" name="fichier3" accept=".csv" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Importer</button>
                </form>
            </div>
        </div>
    </section>
</main>

<%@include file="../static/footer.jsp" %>
</body>
</html>
