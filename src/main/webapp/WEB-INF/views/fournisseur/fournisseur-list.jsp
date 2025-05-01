<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.working.model.fournisseur.Supplier" %>

<html>
<head>
    <title>Liste des Fournisseurs</title>
    <link href="/assets/css/style.css" rel="stylesheet"/>
</head>
<body>

<h1>Liste des Fournisseurs</h1>

<%
    List<Supplier> suppliers = (List<Supplier>) request.getAttribute("suppliers");
%>

<table border="1" cellpadding="5" cellspacing="0">
    <thead>
    <tr>
        <th>Code</th>
        <th>Nom</th>
        <th>Groupe</th>
        <th>Type</th>
        <th>Pays</th>
        <th>Langue</th>
        <th>Email</th>
        <th>Téléphone</th>
        <th>Adresse</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (suppliers != null) {
            for (Supplier s : suppliers) {
    %>
    <tr>
        <td><%= s.getName() %></td>
        <td><%= s.getSupplierName() %></td>
        <td><%= s.getSupplierGroup() %></td>
        <td><%= s.getSupplierType() %></td>
        <td><%= s.getCountry() %></td>
        <td><%= s.getLanguage() %></td>
        <td><%= s.getEmailId() %></td>
        <td><%= s.getMobileNo() %></td>
        <td><%= s.getPrimaryAddress() %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr><td colspan="9">Aucun fournisseur trouvé.</td></tr>
    <%
        }
    %>
    </tbody>
</table>

</body>
</html>
