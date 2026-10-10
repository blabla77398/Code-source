<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="fr_FR"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestion de l'IMC</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-dark bg-primary mb-4">
        <div class="container">
            <span class="navbar-brand mb-0 h1">Gestion de l'IMC</span>
        </div>
    </nav>

    <div class="container">
        <h2 class="h4 mb-3">Liste des patients</h2>

        <div class="card shadow-sm">
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Masse (kg)</th>
                            <th>Taille (m)</th>
                            <th>IMC</th>
                            <th>Interprétation</th>
                            <th class="text-center">Opérations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${patients}">
                            <c:choose>
                                <c:when test="${p.imc < 18.5}"><c:set var="couleur" value="bg-info text-dark"/></c:when>
                                <c:when test="${p.imc < 25}"><c:set var="couleur" value="bg-success"/></c:when>
                                <c:when test="${p.imc < 30}"><c:set var="couleur" value="bg-warning text-dark"/></c:when>
                                <c:otherwise><c:set var="couleur" value="bg-danger"/></c:otherwise>
                            </c:choose>
                            <tr>
                                <td><c:out value="${p.nom}"/></td>
                                <td><c:out value="${p.prenom}"/></td>
                                <td><fmt:formatNumber value="${p.masse}" maxFractionDigits="1"/></td>
                                <td><fmt:formatNumber value="${p.taille}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${p.imc}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><span class="badge ${couleur}"><c:out value="${p.interpretation}"/></span></td>
                                <td class="text-center">
                                    <a href="${ctx}/patients?action=supprimer&amp;id=${p.id}"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('Supprimer ce patient ?');">Supprimer</a>
                                    <a href="${ctx}/patients?action=modifier&amp;id=${p.id}"
                                       class="btn btn-warning btn-sm">Modifier</a>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty patients}">
                            <tr>
                                <td colspan="7" class="text-center text-muted py-4">Aucun patient enregistré.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="mt-3 mb-5">
            <a href="${ctx}/patients?action=ajouter" class="btn btn-primary">Ajouter un patient</a>
        </div>
    </div>

</body>
</html>