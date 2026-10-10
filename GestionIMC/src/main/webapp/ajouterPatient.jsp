<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%-- Mode modification : "patient" et "id" viennent de la servlet.
     Après une erreur de saisie, on reprend les valeurs envoyées (param.*). --%>
<c:set var="idx"     value="${not empty requestScope.id ? requestScope.id : param.id}"/>
<c:set var="vNom"    value="${not empty requestScope.patient ? requestScope.patient.nom    : param.nom}"/>
<c:set var="vPrenom" value="${not empty requestScope.patient ? requestScope.patient.prenom : param.prenom}"/>
<c:set var="vMasse"  value="${not empty requestScope.patient ? requestScope.patient.masse  : param.masse}"/>
<c:set var="vTaille" value="${not empty requestScope.patient ? requestScope.patient.taille : param.taille}"/>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Patient</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-dark bg-primary mb-4">
        <div class="container">
            <span class="navbar-brand mb-0 h1">Gestion de l'IMC</span>
        </div>
    </nav>

    <div class="container" style="max-width: 680px;">
        <div class="card shadow-sm mb-5">
            <div class="card-header bg-primary text-white">
                <h2 class="h5 mb-0">
                    <c:choose>
                        <c:when test="${not empty idx}">Modifier un patient</c:when>
                        <c:otherwise>Ajouter un patient</c:otherwise>
                    </c:choose>
                </h2>
            </div>

            <div class="card-body">
                <c:if test="${not empty erreur}">
                    <div class="alert alert-danger"><c:out value="${erreur}"/></div>
                </c:if>

                <form method="post" action="${ctx}/patients" class="row g-3">
                    <c:if test="${not empty idx}">
                        <input type="hidden" name="id" value="<c:out value='${idx}'/>">
                    </c:if>

                    <div class="col-md-6">
                        <label for="nom" class="form-label">Nom</label>
                        <input type="text" class="form-control" id="nom" name="nom" required
                               value="<c:out value='${vNom}'/>">
                    </div>

                    <div class="col-md-6">
                        <label for="prenom" class="form-label">Prénom</label>
                        <input type="text" class="form-control" id="prenom" name="prenom" required
                               value="<c:out value='${vPrenom}'/>">
                    </div>

                    <div class="col-md-6">
                        <label for="masse" class="form-label">Masse</label>
                        <div class="input-group">
                            <input type="number" class="form-control" id="masse" name="masse"
                                   step="any" min="0.1" required value="<c:out value='${vMasse}'/>">
                            <span class="input-group-text">kg</span>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label for="taille" class="form-label">Taille</label>
                        <div class="input-group">
                            <input type="number" class="form-control" id="taille" name="taille"
                                   step="any" min="0.1" required value="<c:out value='${vTaille}'/>">
                            <span class="input-group-text">m</span>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label for="imcApercu" class="form-label">IMC (calculé)</label>
                        <input type="text" class="form-control bg-light" id="imcApercu" readonly>
                    </div>

                    <div class="col-md-6">
                        <label for="interpApercu" class="form-label">Interprétation (calculée)</label>
                        <input type="text" class="form-control bg-light" id="interpApercu" readonly>
                    </div>

                    <div class="col-12 d-flex gap-2 mt-4">
                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${ctx}/patients" class="btn btn-outline-secondary">Annuler</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Aperçu en direct (la valeur officielle est recalculée côté serveur par le Bean)
        function apercu() {
            var m = parseFloat(document.getElementById('masse').value);
            var t = parseFloat(document.getElementById('taille').value);
            var imc = document.getElementById('imcApercu');
            var interp = document.getElementById('interpApercu');
            if (m > 0 && t > 0) {
                var v = m / (t * t);
                imc.value = v.toFixed(2).replace('.', ',');
                interp.value = v < 18.5 ? 'Insuffisance pondérale'
                             : v < 25   ? 'Corpulence normale'
                             : v < 30   ? 'Surpoids'
                             : v < 35   ? 'Obésité modérée'
                             : v < 40   ? 'Obésité sévère'
                             : 'Obésité morbide';
            } else {
                imc.value = '';
                interp.value = '';
            }
        }
        document.getElementById('masse').addEventListener('input', apercu);
        document.getElementById('taille').addEventListener('input', apercu);
        apercu();
    </script>

</body>
</html>