<!DOCTYPE html>
<html lang="en">
<%@include file="../static/head.jsp" %>

<body>

<main>
    <div class="container">

        <section class="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">

                        <div class="d-flex justify-content-center py-4">
                            <a href="#" class="logo d-flex align-items-center w-auto">
                                <span class="d-none d-lg-block">ErpNext New App</span>
                            </a>
                        </div><!-- End Logo -->

                        <div class="card mb-3">

                            <div class="card-body">

                                <div class="pt-4 pb-2">
                                    <h5 class="card-title text-center pb-0 fs-4">Connectez-vous</h5>
                                    <p class="text-center small">Entrez votre nom et mot de passe pour se connecter</p>
                                </div>
                                <% String error = (String) request.getAttribute("error"); %>
                                <% if (error != null) { %>
                                <div class="alert alert-danger">
                                    <%= error %>
                                </div>
                                <% } %>

                                <form class="row g-3 needs-validation" novalidate method="post" action="/erpnext/session-info">

                                    <div class="col-12">
                                        <label for="user" class="form-label">Nom Utilisateur</label>
                                        <div class="input-group has-validation">
                                            <span class="input-group-text" id="inputGroupPrepend">@</span>
                                            <input type="text" name="user" class="form-control" id="user" required>
                                            <div class="invalid-feedback">Entrer le nom d'utilisateur</div>
                                        </div>
                                    </div>

                                    <div class="col-12">
                                    <label for="mdp" class="form-label">Mot de passe</label>
                                        <div class="input-group">
                                            <input type="password" name="pwd" class="form-control" id="mdp" required>
                                            <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                                Afficher
                                            </button>
                                        </div>
                                        <div class="invalid-feedback">Entrer votre mot de passe</div>
                                    </div>

                                    <div class="col-12">
                                        <button class="btn btn-primary w-100" type="submit">Login</button>
                                    </div>
                                </form>

                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </section>

    </div>
</main><!-- End #main -->

<a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

<%@include file="../static/footer.jsp"%>

<script>
    document.getElementById('togglePassword').addEventListener('click', function () {
        const passwordInput = document.getElementById('mdp');
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        this.textContent = type === 'password' ? 'Afficher' : 'Masquer'; // Optionnel : changer l'icône
    });
</script>
</body>

</html>