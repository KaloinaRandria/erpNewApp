<!-- ======= Sidebar ======= -->
<aside id="sidebar" class="sidebar">

    <ul class="sidebar-nav" id="sidebar-nav">

        <li class="nav-item">
            <a class="nav-link collapsed" href="/accueil">
                <i class="bi bi-grid"></i>
                <span>Accueil</span>
            </a>
        </li><!-- End Dashboard Nav -->

        

        
  <a class="nav-link collapsed" data-bs-target="#rh-nav" data-bs-toggle="collapse" href="/rh/employe/list">
    <i class="bi bi-menu-button-wide"></i><span>Ressource Humaine</span><i class="bi bi-chevron-down ms-auto"></i>
  </a>
  <ul id="rh-nav" class="nav-content collapse" data-bs-parent="#sidebar-nav">
    <li>
      <a href="${pageContext.request.contextPath}/rh/employe/list">
        <i class="bi bi-circle"></i><span>Liste des Employes</span>
      </a>
    </li>
      <li>
          <a href="${pageContext.request.contextPath}/rh/salaire/salary-month">
              <i class="bi bi-circle"></i><span>Employe Salaire par mois</span>
          </a>
      </li>
      <li>
          <a href="${pageContext.request.contextPath}/rh/salaire/statistique-salaire">
              <i class="bi bi-circle"></i><span>Statistiques</span>
          </a>
      </li>
      <li>
          <a href="${pageContext.request.contextPath}/rh/salaire/salary-slip-form">
              <i class="bi bi-circle"></i><span>Generer Salary Slip</span>
          </a>
      </li>
      <li>
          <a href="${pageContext.request.contextPath}/rh/salaire/update-salaire-base-page">
              <i class="bi bi-circle"></i><span>Update Salaire de Base</span>
          </a>
      </li>
<%--      <li>--%>
<%--          <a href="${pageContext.request.contextPath}/rh/salaire/salary-structure">--%>
<%--              <i class="bi bi-circle"></i><span>Ajouter Salary Structure</span>--%>
<%--          </a>--%>
<%--      </li>--%>
<%--      <li>--%>
<%--          <a href="${pageContext.request.contextPath}/rh/salaire/salary-slip-sql">--%>
<%--              <i class="bi bi-circle"></i><span>Salary Slip SQL</span>--%>
<%--          </a>--%>
<%--      </li>--%>
  </ul>





        <li class="nav-item">
            <a class="nav-link collapsed" href="/erpnext/suppliers">
                <i class="bi bi-grid"></i>
                <span>Fournisseur</span>
            </a>
        </li><!-- End Dashboard Nav -->


        <li class="nav-item">
            <a class="nav-link collapsed" href="/erpnext/purchase-invoice">
                <i class="bi bi-grid"></i>
                <span>Comptabilite</span>
            </a>
        </li><!-- End Dashboard Nav -->

        <li class="nav-item">
            <a class="nav-link collapsed" href="/rh/import/import-page">
                <i class="bi bi-grid"></i>
                <span>Importation Donnees</span>
            </a>
        </li><!-- End Dashboard Nav -->

        <li class="nav-item">
            <a class="nav-link collapsed" href="/erpnext/logout">
                <i class="bi bi-box-arrow-in-right"></i>
                <span>Deconnecter</span>
            </a>
        </li><!-- End Login Page Nav -->

         

    </ul>

</aside><!-- End Sidebar-->