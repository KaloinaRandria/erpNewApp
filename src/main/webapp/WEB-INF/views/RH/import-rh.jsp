<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<%@include file="../static/head.jsp" %>

<body>
<%@include file="../static/header.jsp" %>
<%@include file="../static/sidebar.jsp" %>

<main id="main" class="main">
    <div class="pagetitle">
        <h1>Importation De Donnee CSV</h1>
    </div>

    <section class="section">
        <div class="row">
            <div class="col-6">
                <!-- Formulaire d'importation de fichier -->
                <form action="importCommande" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="emp_file" class="form-label">Import Employe :</label>
                        <input type="file" class="form-control" id="emp_file" name="emp_file" accept=".csv" required>
                    </div>
                     <div class="mb-3">
                        <label for="salary_file" class="form-label">Element Salaire :</label>
                        <input type="file" class="form-control" id="salary_file" name="salary_file" accept=".csv" required>
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
